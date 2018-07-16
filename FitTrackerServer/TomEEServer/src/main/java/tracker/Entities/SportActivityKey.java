package tracker.Entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SportActivityKey implements Serializable {
    private String id;
    private String userID;

    public SportActivityKey() {
    }

    public SportActivityKey(String id, String userID) {
        this.id = id;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
