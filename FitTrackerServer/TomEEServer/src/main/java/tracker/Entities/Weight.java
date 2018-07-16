package tracker.Entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "weights")
public class Weight {

    @EmbeddedId
    private WeightKey weightKey;
    private double weight;
    @Column(name = "last_modified")
    private long lastModified;
    @Column(name = "last_sync")
    private long lastSync;

    public Weight() {
    }

    public Weight(String userID, long date, double weight, long lastModified, long lastSync) {
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
