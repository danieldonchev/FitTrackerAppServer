package tracker.Entities;

import javax.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class UserRefreshToken {

    @Id
    private String id;
    @Column(name = "refresh_token")
    private String refreshToken;

    public UserRefreshToken() {
    }

    public UserRefreshToken(String id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
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
}
