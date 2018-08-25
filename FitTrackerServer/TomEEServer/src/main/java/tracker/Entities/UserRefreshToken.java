package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.*;
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
}
