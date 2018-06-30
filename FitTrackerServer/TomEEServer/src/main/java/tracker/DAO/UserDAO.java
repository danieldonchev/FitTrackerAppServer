package tracker.DAO;

import org.json.JSONObject;
import tracker.Entities.Users.LocalUser;
import tracker.Entities.User;

public interface UserDAO {
    Object insertUser(User user);

    boolean deleteUser(User user);

    LocalUser findUser(String email);

    int updateUserDetails(User user);

    int saveUserToken(String email, String token);

    String getUserToken(String email);

    int changePassword(JSONObject data);

    long getLastPasswordChange(String id);

    int insertRefreshToken(User user, String token);

    boolean isRefreshTokenValid(String userID, String refreshToken);
}
