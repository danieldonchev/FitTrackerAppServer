package tracker.DAO.DaoServices;

import tracker.Entities.User;
import tracker.Entities.UserTokens;

import java.util.Optional;

public interface UserService {
    UserTokens insertUser(User user);
    UserTokens insertOAuthUser(User user);
    Optional<UserTokens> loginUser(User user);
    String getAccessTokenFromRefreshToken(String refreshToken);
    boolean changePassword(String email, String code, String newPassword);
    boolean sendPasswordCodeToUser(String email);
}
