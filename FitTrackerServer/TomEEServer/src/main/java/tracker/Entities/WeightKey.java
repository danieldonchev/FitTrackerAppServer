package tracker.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class WeightKey implements Serializable {

    @Column(name = "userID")
    private String userId;
    private long date;

    public WeightKey() {
    }

    public WeightKey(String userId, long date) {
        this.userId = userId;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
