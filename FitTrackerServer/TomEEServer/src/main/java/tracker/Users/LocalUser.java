package tracker.Users;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

@Entity
public class LocalUser extends User {
    private String password;

    public LocalUser() {}

    public LocalUser(String id, String email, String name, String password) {
        super(id, email, name);
        this.password = password;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
