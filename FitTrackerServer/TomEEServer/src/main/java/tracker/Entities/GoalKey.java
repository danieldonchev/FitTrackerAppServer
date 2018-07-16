package tracker.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GoalKey implements Serializable {

    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "userID", nullable = false)
    private String userID;

    public GoalKey(){}

    public GoalKey(String id, String userID){
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

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        result = 31 * result + userID.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(!(obj instanceof GoalKey)){
            return false;
        }
        GoalKey key = (GoalKey) obj;

        return key.id.equals(this.id) &&
                key.userID.equals(this.userID);
    }
}
