package tracker.Entities;

import java.util.Objects;
import java.util.UUID;

public class UserTokens {

    private UUID id;
    private String email;
    private String refreshToken;
    private String accessToken;
    private boolean isUserNew;

    public UserTokens() {
    }

    public UserTokens(UUID id, String email, String refreshToken, String accessToken, boolean isUserNew) {
        this.id = id;
        this.email = email;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.isUserNew = isUserNew;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTokens that = (UserTokens) o;
        return isUserNew == that.isUserNew &&
                Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(refreshToken, that.refreshToken) &&
                Objects.equals(accessToken, that.accessToken);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, email, refreshToken, accessToken, isUserNew);
    }
}
