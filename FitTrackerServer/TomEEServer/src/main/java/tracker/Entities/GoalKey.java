package tracker.Entities;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class GoalKey implements Serializable {

    @Id
    private UUID id;
    private UUID userID;

    public GoalKey() {
    }

    public GoalKey(UUID id, UUID userID) {
        this.id = id;
        this.userID = userID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
