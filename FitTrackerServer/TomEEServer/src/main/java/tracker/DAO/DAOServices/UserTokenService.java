package tracker.DAO.DAOServices;

import tracker.Entities.UserRefreshToken;

public interface UserTokenService {
    UserRefreshToken insertRefreshToken(UserRefreshToken userRefreshToken);
    UserRefreshToken getRefreshToken(String id);
    boolean isRefreshTokenValid(String userID, String refreshToken, long lastPassChange);

}
