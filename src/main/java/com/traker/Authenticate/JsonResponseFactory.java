package main.java.com.traker.Authenticate;

import main.java.com.traker.DAO.UserDAOImpl;
import main.java.com.traker.Users.GenericUser;
import main.java.com.traker.Users.User;
import org.json.JSONObject;

public class JsonResponseFactory
{
    private static final String LOGIN_STRING = "login";
    private static final String AUTH_TOKEN_REFRESH = "refresh_token";
    private static final String AUTH_TOKEN_ACCESS = "access_token";
    private static final String MESSAGE_STRING = "message";
    private static final String SUCCESS_MESSAGE = "success";
    private static final String FAIL_MESSAGE = "fail";


    public static JSONObject authSuccessfulJson(User user)
    {
        if(user != null)
        {
            return  new JSONObject()
                    .put(LOGIN_STRING, SUCCESS_MESSAGE)
                    .put(AUTH_TOKEN_REFRESH, Authenticator.getRefreshToken(user))
                    .put(AUTH_TOKEN_ACCESS, Authenticator.getRegisterAccessToken(user.getId().toString(), user.getEmail(), user.getDevice()))
                    .put("id", user.getId())
                    .put("new_user", ((GenericUser)user).isNew());
        }
        else return problem();
    }

    public static JSONObject localAuthSuccessfulJson(User user)
    {
        return  new JSONObject()
                .put(LOGIN_STRING, SUCCESS_MESSAGE)
                .put(UserDAOImpl.UserConstants.USER_COLUMN_NAME, user.getName())
                .put(AUTH_TOKEN_REFRESH, Authenticator.getRefreshToken(user))
                .put(AUTH_TOKEN_ACCESS, Authenticator.getRegisterAccessToken(user.getId().toString(), user.getEmail(), user.getDevice()))
                .put("id", user.getId())
                .put("new_user", false);
    }

    public static JSONObject authFailPasswordIncorrectJson()
    {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Invalid email or password");
    }

    public static JSONObject accountAlreadyExistsJson()
    {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Account already exists");
    }

    public static JSONObject robotFoundJson(){
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "You might be a robot");
    }

    public static JSONObject accountNotExistJson()
    {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Account does not exist");
    }

    public static JSONObject problem()
    {
        return new JSONObject()
                .put(LOGIN_STRING, "fail")
                .put(MESSAGE_STRING, "Problem occured");
    }
}
