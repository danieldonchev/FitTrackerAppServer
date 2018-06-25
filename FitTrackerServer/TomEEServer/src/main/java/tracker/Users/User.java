package tracker.Users;

import javax.enterprise.inject.Alternative;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import java.util.UUID;

@MappedSuperclass
@XmlRootElement
public abstract class User implements Principal {
    protected String email;
    protected String name;
    protected String device;
    protected String androidId;
    @Id
    protected String id;

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    protected User(String id) {
        this.id = id;
    }

    protected User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
}
