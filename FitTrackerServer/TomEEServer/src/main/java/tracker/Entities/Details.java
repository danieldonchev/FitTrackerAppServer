package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = DBConstants.TABLE_SETTINGS)
@XmlRootElement
public class Details {

    @Id
    private UUID id;
    private String settings;
    @Column(name = DBConstants.last_modified)
    private long lastModified;
    @Column(name = DBConstants.last_sync)
    private long lastSync;

    public Details() {
    }

    public Details(UUID id, String settings, long lastModified, long lastSync) {
        this.id = id;
        this.settings = settings;
        this.lastModified = lastModified;
        this.lastSync = lastSync;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID userID) {
        this.id = userID;
    }

    @XmlElement
    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    @XmlElement
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Details details = (Details) o;
        return lastModified == details.lastModified &&
                lastSync == details.lastSync &&
                Objects.equals(id, details.id) &&
                Objects.equals(settings, details.settings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, settings, lastModified, lastSync);
    }
}
