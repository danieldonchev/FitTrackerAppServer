package tracker.authentication.users;

import tracker.utils.DBConstants;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import java.util.UUID;

@XmlRootElement
@Entity
@Table(name = DBConstants.TABLE_USERS)
public class User {

    @Id
    private UUID id;
    private String name;
    @Column(unique = true)
    private String email;
    @Nullable
    private String password;
    @Transient @Nullable
    private String accessToken;
    @Column(name = DBConstants.user_password_last_modified)
    private long lastPassChange;

    public User(){
        this.id = UUID.randomUUID();
    }

    public User(UUID id, String email){
        this.id = id;
        this.email = email;
    }

    public User(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @XmlElement
    public String getName() {
        return null;
    }

    @XmlElement
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    @XmlElement
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getLastPassChange() {
        return lastPassChange;
    }

    public void setLastPassChange(long lastPassChange) {
        this.lastPassChange = lastPassChange;
    }
}
