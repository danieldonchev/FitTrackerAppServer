package tracker.authenticate;

import java.util.UUID;

public interface UserTokenService {
    UserRefreshToken insertRefreshToken(UserRefreshToken userRefreshToken);
    UserRefreshToken getRefreshToken(UUID id);
    boolean isRefreshTokenValid(UUID userID, String refreshToken, long lastPassChange);

}
