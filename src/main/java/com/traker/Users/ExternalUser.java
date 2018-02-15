package main.java.com.traker.Users;

import javax.xml.bind.annotation.XmlElement;

public class ExternalUser extends User
{
    private String accessToken;

    @XmlElement
    public String getAccessToken() {
        return accessToken;
    }

    public void setOuterToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
