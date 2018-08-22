package tracker.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_sync")
public class ModifiedTimes {

    @Id
    private String id;
    @Column(name = "last_modified")
    private long lastModified;
    @Column(name = "last_modified_activities")
    private long lastModifiedActivities;
    @Column(name = "last_modified_settings")
    private long lastModifiedSettings;
    @Column(name = "last_modified_goals")
    private long lastModifiedGoals;
    @Column(name = "last_modified_weights")
    private long lastModifiedWeights;

    public ModifiedTimes() {
    }

    public ModifiedTimes(String id, long lastModified, long lastModifiedActivities, long lastModifiedSettings, long lastModifiedGoals, long lastModifiedWeights) {
        this.id = id;
        this.lastModified = lastModified;
        this.lastModifiedActivities = lastModifiedActivities;
        this.lastModifiedSettings = lastModifiedSettings;
        this.lastModifiedGoals = lastModifiedGoals;
        this.lastModifiedWeights = lastModifiedWeights;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLastModifiedActivities() {
        return lastModifiedActivities;
    }

    public void setLastModifiedActivities(long lastModifiedActivities) {
        this.lastModifiedActivities = lastModifiedActivities;
    }

    public long getLastModifiedSettings() {
        return lastModifiedSettings;
    }

    public void setLastModifiedSettings(long lastModifiedSettings) {
        this.lastModifiedSettings = lastModifiedSettings;
    }

    public long getLastModifiedGoals() {
        return lastModifiedGoals;
    }

    public void setLastModifiedGoals(long lastModifiedGoals) {
        this.lastModifiedGoals = lastModifiedGoals;
    }

    public long getLastModifiedWeights() {
        return lastModifiedWeights;
    }

    public void setLastModifiedWeights(long lastModifiedWeights) {
        this.lastModifiedWeights = lastModifiedWeights;
    }
}
