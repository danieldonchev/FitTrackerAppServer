package tracker.Users;

import javax.xml.bind.annotation.XmlElement;


public class OAuthUser extends User {
    private String accessToken;

    @XmlElement
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
