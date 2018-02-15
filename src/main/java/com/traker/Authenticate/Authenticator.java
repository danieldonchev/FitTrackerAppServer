package main.java.com.traker.Authenticate;


import io.jsonwebtoken.*;
import main.java.com.traker.DAO.DAOFactory;
import main.java.com.traker.DAO.UserDAO;
import main.java.com.traker.DAO.UserDAOImpl;
import main.java.com.traker.Users.User;

import javax.ws.rs.NotAuthorizedException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class Authenticator
{
    private static final String refreshTokenSecret = "refresh_token_secret";
    private static final String accessTokenSecret = "access_token_secret";
    private static final String SUBJECT = "user";
    private static final String ISSUER = "tracker.com";
    private static final String EMAIL = "email";
    private static final String DEVICE = "device";
    private static final String USERID  = "userID";

    public static String getRegisterAccessToken(String userID, String email, String device)
    {

        String jwt = Jwts.builder()
                .setSubject(SUBJECT)
                .setIssuer(ISSUER)
                .claim(EMAIL, email)
                .claim(DEVICE, device)
                .claim(USERID, userID)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7200 * 1000))
                .signWith(SignatureAlgorithm.HS256, accessTokenSecret.getBytes())
                .compact();

        return jwt;
    }

    public static String getRefreshToken(User user){

        String jwt = Jwts.builder()
                .setSubject(SUBJECT)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400 * 90 * 1000L))
                .claim(EMAIL, user.getEmail())
                .claim(DEVICE, user.getDevice())
                .claim(USERID, user.getId())
                .signWith(SignatureAlgorithm.HS256, refreshTokenSecret.getBytes())
                .compact();

        DAOFactory daoFactory = new DAOFactory();
        UserDAO userDAO = daoFactory.getUserDAO();
        userDAO.insertRefreshToken(user, jwt);

        return jwt;
    }

    public static String getAccessToken(String userID, String androidID, String device, String refreshToken) throws NotAuthorizedException{
        String jwt;

        try {
            Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(refreshTokenSecret.getBytes("UTF-8")).parseClaimsJws(refreshToken);
            Claims claims = jwsClaims.getBody();
            if(System.currentTimeMillis() > claims.getExpiration().getTime()){
                throw new NotAuthorizedException("Token not authorized");
            } else {
                String id = (String) claims.get(UserDAOImpl.UserConstants.USER_COLUMN_ID);
                long tokenIssuedAt = claims.getIssuedAt().getTime();

                DAOFactory daoFactory = new DAOFactory();
                UserDAO userDAO = daoFactory.getUserDAO();
                long lastPassChange = userDAO.getLastPasswordChange(id);

                if(lastPassChange > tokenIssuedAt){
                    throw new NotAuthorizedException("Token not authorized");
                } else {
                   if(userDAO.isRefreshTokenValid(userID, device, androidID, refreshToken)){
                       jwt = Jwts.builder()
                               .setSubject(SUBJECT)
                               .setIssuer(ISSUER)
                               .setIssuedAt(new Date(System.currentTimeMillis()))
                               .setExpiration(new Date(System.currentTimeMillis() + 7200 * 1000))
                               .claim(EMAIL, claims.get(UserDAOImpl.UserConstants.USER_COLUMN_EMAIL))
                               .claim(DEVICE, claims.get(DEVICE))
                               .claim(USERID, claims.get(USERID))
                               .signWith(SignatureAlgorithm.HS256, accessTokenSecret.getBytes())
                               .compact();
                   } else {
                       throw new NotAuthorizedException("Token not authorized");
                    }


                }
            }
        } catch (UnsupportedEncodingException | SignatureException e) {
            throw new NotAuthorizedException("Token not authorized");
        }

        return jwt;
    }

    public static Jws<Claims> validateJwt(String jwt)  throws NotAuthorizedException
    {
        try
        {
            return Jwts.parser().setSigningKey(accessTokenSecret.getBytes("UTF-8")).parseClaimsJws(jwt);
        }
        catch (UnsupportedEncodingException | SignatureException e)
        {
            throw new NotAuthorizedException("Token not authorized");
        }
    }

    public static Jws<Claims> validateRefreshJwt(String jwt)  throws NotAuthorizedException
    {
        try
        {
            return Jwts.parser().setSigningKey(refreshTokenSecret.getBytes("UTF-8")).parseClaimsJws(jwt);
        }
        catch (UnsupportedEncodingException | SignatureException e)
        {
            throw new NotAuthorizedException("Token not authorized");
        }
    }
}
