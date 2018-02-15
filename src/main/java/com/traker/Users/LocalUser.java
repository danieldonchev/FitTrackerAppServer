package main.java.com.traker.Users;

import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

public class LocalUser extends User
{
    private String password;
    private String responseToken;

    public LocalUser(){}

    public LocalUser(UUID id, String email, String name, String password)
    {
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

    @XmlElement
    public String getResponseToken() {
        return responseToken;
    }

    public void setResponseToken(String responseToken) {
        this.responseToken = responseToken;
    }
}
