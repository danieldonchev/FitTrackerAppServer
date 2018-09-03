package tracker.weight;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class WeightKey implements Serializable {

    private UUID userID;
    private long date;

    public WeightKey() {
    }

    public WeightKey(UUID userId, long date) {
        this.userID = userId;
        this.date = date;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + userID.hashCode();
        result = 31 * result + Long.hashCode(date);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof WeightKey)){
            return false;
        }
        WeightKey weightKey = (WeightKey) obj;

        return weightKey.getUserID().equals(this.userID) &&
                weightKey.getDate() == this.date ;
    }
}
