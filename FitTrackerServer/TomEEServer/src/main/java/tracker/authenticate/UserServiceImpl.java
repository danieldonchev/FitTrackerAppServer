package tracker.authenticate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import tracker.authenticate.dao.UserDao;
import tracker.authenticate.dao.UserDaoQualifier;
import tracker.authenticate.utils.PasswordGenerator;
import tracker.authenticate.utils.PasswordValidator;
import tracker.authenticate.utils.TokenAuthenticator;
import tracker.authenticate.utils.TokenFactory;
import tracker.utils.MailSender;


import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.NotAuthorizedException;
import java.util.Optional;
import java.util.UUID;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserServiceImpl implements UserService{

    @Resource
    private UserTransaction userTransaction;
    private UserDao dao;
    private UserTokenService userTokenService;
    private UserPasswordTokenService userPasswordTokenService;

    @Inject
    public UserServiceImpl(@UserDaoQualifier UserDao dao, UserTokenService userTokenService, UserPasswordTokenService passwordTokenService){
        this.dao = dao;
        this.userTokenService = userTokenService;
        this.userPasswordTokenService = passwordTokenService;
    }

    /*
        Inserts a user into the database.
        Inserts user's refresh token to the database.
        Returns a UserTokens with user id, refresh token and access token.

        @param user User with data from the client
        @return UserTokens
     */
    @Override
    public UserTokens insertUser(User user){
        boolean userNew = true;

        try {
            userTransaction.begin();
            PasswordGenerator generator = new PasswordGenerator();
            user.setPassword(generator.generatePasswordHash(user.getPassword()));
            dao.create(user);
            userTransaction.commit();
        } catch (Exception e){
           userNew = catchUserExistsException(e);
        }
        UserTokens userTokens = new UserTokens();
        if(userNew){
            userTokens  = getUserTokens(user);
        }

        userTokens.setId(user.getId());
        userTokens.setEmail(user.getEmail());
        userTokens.setUserNew(userNew);

        return userTokens;
    }

    /*
        Inserts an OAuth user from Google or Facebook. If PersistenceException is thrown the user already
        exists in the database - not a new user.
        @param user User credentials
        @return     UserTokens object with user details and refresh, access token
     */
    @Override
    public UserTokens insertOAuthUser(User user){
        boolean userNew = true;
        Optional<User> existingUser = Optional.empty();


        try {
            userTransaction.begin();
            existingUser = dao.findUser(user.getEmail());
            if(!existingUser.isPresent()){
                dao.create(user);
            }
            userTransaction.commit();
        } catch (Exception e) {
            userNew = catchUserExistsException(e);
        }
        UserTokens userTokens = null;
        if(existingUser.isPresent()){
            userTokens = getUserTokens(existingUser.get());
            userTokens.setId(existingUser.get().getId());
        } else {
            try {
                userTransaction.begin();
                dao.create(user);
                userTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            userTokens = getUserTokens(user);
            userTokens.setId(user.getId());
        }
        userTokens.setEmail(user.getEmail());
        userTokens.setUserNew(userNew);

        return userTokens;
    }

    /*
        User for logging a local user with user email and password.
        @param user User credentials.
        @return     UserTokens with new refresh and access token.
     */
    @Override
    @Nullable
    public Optional<UserTokens> loginUser(User user) {
        Optional<User> optionalUser = dao.findUser(user.getEmail());
        if (optionalUser.isPresent()) {
            User userFromDB = optionalUser.get();
            PasswordValidator validator = new PasswordValidator();
            if (validator.validatePassword(user.getPassword(), userFromDB.getPassword())) {
                TokenFactory tokenFactory = new TokenFactory();
                String refreshToken = tokenFactory.getRefreshToken(user.getEmail(), userFromDB.getId().toString());
                UserTokens userTokens = new UserTokens(userFromDB.getId(),
                        user.getEmail(),
                        refreshToken,
                        tokenFactory.getRegisterAccessToken(userFromDB.getId().toString(), user.getEmail()),
                        false);
                insertRefreshToken(new UserRefreshToken(userFromDB.getId(), refreshToken));
                return Optional.of(userTokens);
            }
        }
        return Optional.empty();
    }

    /*
        Creates user refresh and access token. Inserts the new created refresh token into refresh_tokens table.
        @param User
        @return UserTokens
     */
    private UserTokens getUserTokens(User user){
        UserTokens userTokens = new UserTokens();
        TokenFactory tokenFactory = new TokenFactory();
        String refreshToken = tokenFactory.getRefreshToken(user.getEmail(), user.getId().toString());
        String accessToken = tokenFactory.getRegisterAccessToken(user.getId().toString(), user.getEmail());
        UserRefreshToken userRefreshToken = new UserRefreshToken(user.getId(), refreshToken);
        insertRefreshToken(userRefreshToken);

        userTokens.setRefreshToken(refreshToken);
        userTokens.setAccessToken(accessToken);

        return userTokens;
    }

    /*
        Getting access tokens from active refresh tokens.
        @param refreshToken User active refresh token.
        @return     The newly created access token.
     */
    @Override
    public String getAccessTokenFromRefreshToken(String refreshToken){
        TokenAuthenticator authenticator = new TokenAuthenticator();
        TokenFactory tokenFactory = new TokenFactory();
        Jws<Claims> claimsJws = authenticator.validateRefreshJwt(refreshToken);
        String userID = (String) claimsJws.getBody().get("userID");
        String email = (String) claimsJws.getBody().get("email");
        boolean isTokenValid = userTokenService.isRefreshTokenValid(UUID.fromString(userID), refreshToken, dao.getLastPasswordChange(UUID.fromString(userID)));
        if(isTokenValid){
            return tokenFactory.getAccessTokenFromRefreshToken(userID, email);
        } else{
            throw new NotAuthorizedException("Token is not valid");
        }
    }

    /*
        Changing the users password.
        @param email User email.
        @param code The code from the users email.
        @return boolean Returns if password has changed correctly.
     */
    @Override
    public boolean changePassword(String email, String code, String newPassword){
        UserPasswordToken passwordToken = new UserPasswordToken(email, code);
        try {
            userPasswordTokenService.find(passwordToken);
            String password = new PasswordGenerator().generatePasswordHash(newPassword);
            dao.changePassword(email, password);
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /*
        Sends password to user email. If the user doesn't exist returns false.
     */
    @Override
    public boolean sendPasswordCodeToUser(String email){

        try {
            Optional<User> user = dao.findUser(email);
        } catch (NoResultException e) {
            return false;
        }

        MailSender sender = new MailSender();
        String code = PasswordGenerator.getRandomPasswordToken(8);
        UserPasswordToken userPasswordToken = new UserPasswordToken(email, code);
        createUserPasswordToken(userPasswordToken);

        sender.sendMail(email, code);

        return true;
    }


    private UserPasswordToken createUserPasswordToken(UserPasswordToken passwordToken){
        userPasswordTokenService.create(passwordToken);
        return passwordToken;
    }

    private UserRefreshToken insertRefreshToken(UserRefreshToken user){
        userTokenService.insertRefreshToken(user);
        return user;
    }

    /*
        Helper method to avoid repeating code for exception catch.
        @return     True if user is new, false if user exists.
     */
    private boolean catchUserExistsException(Exception e){

        try {
            if(userTransaction.getStatus() == Status.STATUS_ACTIVE){
                userTransaction.rollback();
            }
            Throwable t = e.getCause();
            while(t != null) {
                t = t.getCause();
                if (t instanceof org.hibernate.exception.ConstraintViolationException) {
                    return false;
                }
            }
        } catch (SystemException e1) {
            e1.printStackTrace();
        }
        return true;
    }
}
