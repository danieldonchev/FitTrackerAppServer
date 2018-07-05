package tracker.Entities;

import javax.persistence.*;

@Entity
@Table(name = "sport_activity_splits")
public class Split {
    @EmbeddedId
    private SplitKey splitKey;
    @Column(name = "user_id") private String userID;
    private long duration;
    private double distance;

    public Split() {
    }

    public Split(int id, String sportActivityId, String userID, long duration, double distance) {
        this.splitKey = new SplitKey(id, sportActivityId);
        this.userID = userID;
        this.duration = duration;
        this.distance = distance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
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
