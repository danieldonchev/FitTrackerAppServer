package tracker.Entities;

public class UserTokens {

    private String id;
    private String email;
    private String refreshToken;
    private String accessToken;
    private boolean isUserNew;

    public UserTokens() {
    }

    public UserTokens(String id, String email, String refreshToken, String accessToken, boolean isUserNew) {
        this.id = id;
        this.email = email;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.isUserNew = isUserNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isUserNew() {
        return isUserNew;
    }

    public void setUserNew(boolean userNew) {
        isUserNew = userNew;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
