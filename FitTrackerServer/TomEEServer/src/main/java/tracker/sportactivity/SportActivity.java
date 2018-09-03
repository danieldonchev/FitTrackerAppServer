package tracker.sportactivity;

import tracker.utils.DBConstants;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = DBConstants.TABLE_SPORT_ACTIVITIES)
public class SportActivity {

    @Id
    private UUID id;
    private UUID userID;
    private String activity = "Walking";
    private double distance = 0;
    private long duration = 0;
    private int calories = 0;
    private long steps = 0;
    private long startTimestamp = 0;
    private long endTimestamp = 0;
    private String data;
    @Column(name = DBConstants.last_modified) private long lastModified;
    @Column(name = DBConstants.last_sync) private long lastSync;
    private int deleted = 0;

    public SportActivity() {
    }

    public SportActivity(UUID id, UUID userID, String activity, double distance, long steps, long startTimestamp, long endTimestamp, long lastModified, long lastSync) {
        this.id = id;
        this.userID = userID;
        this.activity = activity;
        this.distance = distance;
        this.steps = steps;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.lastModified = lastModified;
        this.lastSync = lastSync;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportActivity that = (SportActivity) o;
        return Double.compare(that.distance, distance) == 0 &&
                duration == that.duration &&
                calories == that.calories &&
                steps == that.steps &&
                startTimestamp == that.startTimestamp &&
                endTimestamp == that.endTimestamp &&
                lastModified == that.lastModified &&
                lastSync == that.lastSync &&
                deleted == that.deleted &&
                Objects.equals(id, that.id) &&
                Objects.equals(userID, that.userID) &&
                Objects.equals(activity, that.activity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userID, activity, distance, duration, calories, steps, startTimestamp, endTimestamp, lastModified, lastSync, deleted);
    }
}
