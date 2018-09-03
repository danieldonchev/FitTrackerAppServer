package tracker.authentication;

import tracker.utils.DBConstants;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = DBConstants.TABLE_REFRESH_TOKENS)
public class UserRefreshToken {

    @Id
    private UUID id;
    @Column(name = DBConstants.refresh_token)
    private String refreshToken;

    public UserRefreshToken() {
    }

    public UserRefreshToken(UUID id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRefreshToken that = (UserRefreshToken) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, refreshToken);
    }
}
