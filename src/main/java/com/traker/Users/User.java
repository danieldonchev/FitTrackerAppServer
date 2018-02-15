package main.java.com.traker.Users;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import java.util.UUID;

@XmlRootElement
public abstract class User implements Principal
{
    protected String email;
    protected String name;
    protected String device;
    protected String androidId;
    protected UUID id;

    public User()
    {
        this.id = UUID.randomUUID();
    }

    protected User(UUID id)
    {
        this.id = id;
    }

    protected User(UUID id, String email, String name)
    {
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

    public UUID getId() {
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
