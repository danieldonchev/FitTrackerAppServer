package tracker.Entities;

import org.hibernate.annotations.ColumnTransformer;
import tracker.Utils.DBConstants;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = DBConstants.TABLE_REFRESH_TOKENS)
public class UserRefreshToken {

    @Id
    @ColumnTransformer(read = "uuid_from_bin(id)", write = "uuid_to_bin(?)")
    @Column(columnDefinition = "BINARY(16)")
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
