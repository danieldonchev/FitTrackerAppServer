package tracker.Authenticate;

import org.json.JSONObject;
import tracker.DAO.UserDAOImpl;
import tracker.Users.GenericUser;
import tracker.Users.User;

public class JsonResponseFactory {
    private static final String LOGIN_STRING = "login";
    private static final String AUTH_TOKEN_REFRESH = "refresh_token";
    private static final String AUTH_TOKEN_ACCESS = "access_token";
    private static final String MESSAGE_STRING = "message";
    private static final String SUCCESS_MESSAGE = "success";

    public JSONObject authSuccessfulJson(User user) {
        if (user != null) {
            TokenFactory tokenFactory = new TokenFactory();
            return new JSONObject()
                    .put(LOGIN_STRING, SUCCESS_MESSAGE)
                    .put(AUTH_TOKEN_REFRESH, tokenFactory.getRefreshToken(user))
                    .put(AUTH_TOKEN_ACCESS, tokenFactory.getRegisterAccessToken(user.getId().toString(), user.getEmail(), user.getDevice()))
                    .put("id", user.getId())
                    .put("new_user", ((GenericUser) user).isNew());
        } else return problem();
    }

    public JSONObject localAuthSuccessfulJson(User user) {
        TokenFactory tokenFactory = new TokenFactory();
        return new JSONObject()
                .put(LOGIN_STRING, SUCCESS_MESSAGE)
                .put(UserDAOImpl.UserConstants.USER_COLUMN_NAME, user.getName())
                .put(AUTH_TOKEN_REFRESH, tokenFactory.getRefreshToken(user))
                .put(AUTH_TOKEN_ACCESS, tokenFactory.getRegisterAccessToken(user.getId().toString(), user.getEmail(), user.getDevice()))
                .put("id", user.getId())
                .put("new_user", false);
    }

    public JSONObject authFailPasswordIncorrectJson() {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Invalid email or password");
    }

    public JSONObject accountAlreadyExistsJson() {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Account already exists");
    }

    public JSONObject robotFoundJson() {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "You might be a robot");
    }

    public JSONObject accountNotExistJson() {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Account does not exist");
    }

    public JSONObject problem() {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Problem occured");
    }
}
