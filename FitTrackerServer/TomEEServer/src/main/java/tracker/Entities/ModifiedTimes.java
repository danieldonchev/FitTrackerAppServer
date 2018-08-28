package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = DBConstants.TABLE_SYNC)
public class ModifiedTimes {

    @Id
    private UUID id;
    @Column(name = DBConstants.last_modified)
    private long lastModified;
    @Column(name = DBConstants.sync_activities)
    private long lastModifiedActivities;
    @Column(name = DBConstants.sync_settings)
    private long lastModifiedSettings;
    @Column(name = DBConstants.sync_goals)
    private long lastModifiedGoals;
    @Column(name = DBConstants.sync_weights)
    private long lastModifiedWeights;

    public ModifiedTimes() {
    }

    public ModifiedTimes(UUID id, long lastModified, long lastModifiedActivities, long lastModifiedSettings, long lastModifiedGoals, long lastModifiedWeights) {
        this.id = id;
        this.lastModified = lastModified;
        this.lastModifiedActivities = lastModifiedActivities;
        this.lastModifiedSettings = lastModifiedSettings;
        this.lastModifiedGoals = lastModifiedGoals;
        this.lastModifiedWeights = lastModifiedWeights;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedTimes times = (ModifiedTimes) o;
        return lastModified == times.lastModified &&
                lastModifiedActivities == times.lastModifiedActivities &&
                lastModifiedSettings == times.lastModifiedSettings &&
                lastModifiedGoals == times.lastModifiedGoals &&
                lastModifiedWeights == times.lastModifiedWeights &&
                Objects.equals(id, times.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, lastModified, lastModifiedActivities, lastModifiedSettings, lastModifiedGoals, lastModifiedWeights);
    }
}
