package tracker.Authenticate;

import io.jsonwebtoken.*;

import javax.ws.rs.NotAuthorizedException;
import java.io.UnsupportedEncodingException;

public class TokenAuthenticator {
    public static final String refreshTokenSecret = "refresh_token_secret";
    public static final String accessTokenSecret = "access_token_secret";

    public Jws<Claims> validateJwt(String jwt) throws NotAuthorizedException {
        try {
            return Jwts.parser().setSigningKey(accessTokenSecret.getBytes("UTF-8")).parseClaimsJws(jwt);
        } catch (UnsupportedEncodingException | SignatureException e) {
            throw new NotAuthorizedException("Token not authorized");
        }
    }

    public Jws<Claims> validateRefreshJwt(String jwt) throws NotAuthorizedException {
        try {
            return Jwts.parser().setSigningKey(refreshTokenSecret.getBytes("UTF-8")).parseClaimsJws(jwt);
        } catch (UnsupportedEncodingException | SignatureException e) {
            throw new NotAuthorizedException("Token not authorized");
        }
    }
}
