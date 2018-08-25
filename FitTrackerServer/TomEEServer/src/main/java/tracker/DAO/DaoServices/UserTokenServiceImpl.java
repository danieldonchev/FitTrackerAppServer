package tracker.DAO.DaoServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import tracker.DAO.Daos.GenericDao;
import tracker.Entities.UserRefreshToken;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.ws.rs.NotAuthorizedException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static tracker.Authenticate.TokenAuthenticator.refreshTokenSecret;

@Stateless
public class UserTokenServiceImpl implements UserTokenService{

    private GenericDao<UserRefreshToken, UUID> dao;

    @Inject
    public UserTokenServiceImpl(GenericDao<UserRefreshToken, UUID> dao){
        this.dao = dao;
    }

    /*
        Inserts user with refresh token into refresh_tokens.
        @param UserRefreshToken
        @return     The inserted entity.
     */
    @Override
    public UserRefreshToken insertRefreshToken(UserRefreshToken userRefreshToken){
        dao.create(userRefreshToken);
        return userRefreshToken;
    }

    /*
        Returns UserRefreshToken entity from database by user ID.
        @param id User id.
        @return     The found token. If not found returns null.
     */
    @Override
    public UserRefreshToken getRefreshToken(UUID id){
        return dao.read(UserRefreshToken.class, id);
    }

    /*
        Checks a refresh token to see if its a valid token in the database. If the last password change is more
        recent than the refresh token issued time the token is not a valid one.
        @param userID Users ID.
        @param refreshToken The token to be evaluated.
        @param lastPassChange The last time the user changed his password.
        @return     True if its valid else false.
     */
    @Override
    public boolean isRefreshTokenValid(UUID userID, String refreshToken, long lastPassChange){
        boolean isInDB = isRefreshTokenInDb(refreshToken, userID.toString());

        if(isInDB){
            Jws<Claims> jwsClaims = null;
            try {
                jwsClaims = Jwts.parser().setSigningKey(refreshTokenSecret.getBytes("UTF-8")).parseClaimsJws(refreshToken);
                Claims claims = jwsClaims.getBody();
                if (System.currentTimeMillis() > claims.getExpiration().getTime()) {
                    throw new NotAuthorizedException("Token not authorized");
                } else {
                    long tokenIssuedAt = claims.getIssuedAt().getTime();
                    if (lastPassChange > tokenIssuedAt) {
                        throw new NotAuthorizedException("Token not authorized");
                    } else {
                        return true;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            throw new NotAuthorizedException("Token not authorized");
        }

        return false;
    }

    /*
        Helper method to check if the token is in the database.
     */
    private boolean isRefreshTokenInDb(String refreshToken, String userID){
        Query query = dao.getEntityManager().createQuery("select r from UserRefreshToken r where id=:arg1 and " +
                "refreshToken=:arg2");
        query.setParameter("arg1", userID);
        query.setParameter("arg2", refreshToken);
        UserRefreshToken userTokens = (UserRefreshToken) query.getSingleResult();
        if(userTokens.getRefreshToken().equals(refreshToken)){
            return true;
        }
        return false;
    }

}
