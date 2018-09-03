package tracker.authentication.utils;

import io.jsonwebtoken.*;

import javax.ws.rs.NotAuthorizedException;
import java.util.Date;

import static tracker.authentication.utils.TokenAuthenticator.accessTokenSecret;
import static tracker.authentication.utils.TokenAuthenticator.refreshTokenSecret;

public class TokenFactory {

    public static final String SUBJECT = "user";
    public static final String ISSUER = "tracker.com";
    public static final String EMAIL = "email";
    public static final String DEVICE = "device";
    public static final String USERID = "userID";


    public String getRegisterAccessToken(String userID, String email) {

        String jwt = Jwts.builder()
                .setSubject(SUBJECT)
                .setIssuer(ISSUER)
                .claim(EMAIL, email)
                .claim(USERID, userID)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 720000 * 1000))
                .signWith(SignatureAlgorithm.HS256, accessTokenSecret.getBytes())
                .compact();

        return jwt;
    }

    public String getRefreshToken(String email, String userID) {
        return Jwts.builder()
                .setSubject(SUBJECT)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400 * 90 * 1000L))
                .claim(EMAIL, email)
                .claim(USERID, userID)
                .signWith(SignatureAlgorithm.HS256, refreshTokenSecret.getBytes())
                .compact();
    }

    public String getAccessTokenFromRefreshToken(String userID, String email) throws NotAuthorizedException {
        String jwt;
        try {
                jwt = Jwts.builder()
                        .setSubject(SUBJECT)
                        .setIssuer(ISSUER)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 720000 * 1000))
                        .claim(EMAIL, email)
                        .claim(USERID, userID)
                        .signWith(SignatureAlgorithm.HS256, accessTokenSecret.getBytes())
                        .compact();
        } catch (SignatureException e) {
            throw new NotAuthorizedException("Token not authorized");
        }

        return jwt;
    }
}
