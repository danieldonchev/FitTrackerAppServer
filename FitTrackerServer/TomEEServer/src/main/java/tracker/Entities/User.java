package tracker.Entities;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import java.util.UUID;

@XmlRootElement
@Entity
@Table(name = "users")
public class User implements Principal {

    @Id
    private String id;
    private String name;
    @Column(unique = true)
    private String email;
    @Nullable
    private String password;
    @Transient @Nullable
    private String accessToken;
    @Column(name = "password_last_modified")
    private long lastPassChange;

    public User(){
        this.id = UUID.randomUUID().toString();
    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @XmlElement
    @Override
    public String getName() {
        return null;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
