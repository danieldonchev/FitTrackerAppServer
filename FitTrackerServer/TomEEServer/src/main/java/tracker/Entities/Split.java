package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = DBConstants.TABLE_SPORT_ACTIVITY_SPLITS)
public class Split {
    @EmbeddedId
    private SplitKey splitKey;
    @Column(name = DBConstants.userID)
    private UUID userID;
    private long duration;
    private double distance;

    public Split() {
    }

    public Split(int id, UUID sportActivityId, UUID userID, long duration, double distance) {
        this.splitKey = new SplitKey(id, sportActivityId);
        this.userID = userID;
        this.duration = duration;
        this.distance = distance;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public SplitKey getSplitKey() {
        return splitKey;
    }

    public void setSplitKey(SplitKey splitKey) {
        this.splitKey = splitKey;
    }
}
