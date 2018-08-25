package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = DBConstants.TABLE_PASSWORD_TOKENS)
public class UserPasswordToken {

    @Id
    private String email;
    private String token;

    public UserPasswordToken() {
    }

    public UserPasswordToken(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
