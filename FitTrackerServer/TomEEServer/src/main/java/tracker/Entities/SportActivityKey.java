package tracker.Entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class SportActivityKey implements Serializable {
    private UUID id;
    private UUID userID;

    public SportActivityKey() {
    }

    public SportActivityKey(UUID id, UUID userID) {
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
