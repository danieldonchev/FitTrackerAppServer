package tracker.Entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
//@DynamicUpdate
@Table(name = "goals")
public class Goal {

    @Id
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID id;
    private String userID;
    private int type;
    private double distance;
    private long duration;
    private long calories;
    private long steps;
    @Column(name = "from_date") private long fromDate;
    @Column(name = "to_date") private long toDate;
    @Column(name = "last_modified") private long lastModified;
    @Column(name = "last_sync") private long lastSync;
    private int deleted = 0;

    public Goal(){ }

    public Goal(UUID id, String userID, int type, double distance, long duration, long calories, long steps, long fromDate, long toDate, long lastModified, long lastSync){
        this.id = id;
        this.userID = userID;
        this.type = type;
        this.distance = distance;
        this.duration = duration;
        this.calories = calories;
        this.steps = steps;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.lastModified = lastModified;
        this.lastSync = lastSync;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCalories() {
        return calories;
    }

    public void setCalories(long calories) {
        this.calories = calories;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
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

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Goal)){
            return false;
        }
        Goal goal = (Goal) obj;

        return goal.getId().equals(this.id) &&
                goal.getUserID().equals(this.userID) &&
                goal.getType() == this.type &&
                goal.getDistance() == this.distance &&
                goal.getDuration() == this.duration &&
                goal.getCalories() == this.calories &&
                goal.getSteps() == this.steps &&
                goal.getFromDate() == this.fromDate &&
                goal.getToDate() == this.toDate &&
                goal.getLastModified() == this.lastModified &&
                goal.getLastSync() == this.lastSync;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        result = 31 * result + userID.hashCode();
        result = 31 * result + type;
        result = 31 * result + Double.hashCode(distance);
        result = 31 * result + Long.hashCode(duration);
        result = 31 * result + Long.hashCode(calories);
        result = 31 * result + Long.hashCode(steps);
        result = 31 * result + Long.hashCode(fromDate);
        result = 31 * result + Long.hashCode(toDate);
        result = 31 * result + Long.hashCode(lastModified);
        result = 31 * result + Long.hashCode(lastSync);

        return result;
    }
}
