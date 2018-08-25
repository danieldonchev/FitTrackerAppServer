package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class WeightKey implements Serializable {

    @Column(name = DBConstants.userID)
    private UUID userId;
    private long date;

    public WeightKey() {
    }

    public WeightKey(UUID userId, long date) {
        this.userId = userId;
        this.date = date;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
