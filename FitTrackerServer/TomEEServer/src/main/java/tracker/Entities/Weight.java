package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = DBConstants.TABLE_WEIGHT)
public class Weight {

    @EmbeddedId
    private WeightKey weightKey;
    private double weight;
    @Column(name = DBConstants.last_modified)
    private long lastModified;
    @Column(name = DBConstants.last_sync)
    private long lastSync;

    public Weight() {
    }

    public Weight(UUID userID, long date, double weight, long lastModified, long lastSync) {
        this.weightKey = new WeightKey(userID, date);
        this.weight = weight;
        this.lastModified = lastModified;
        this.lastSync = lastSync;
    }

    public WeightKey getWeightKey() {
        return weightKey;
    }

    public void setWeightKey(WeightKey weightKey) {
        this.weightKey = weightKey;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
}
