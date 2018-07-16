package tracker.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table (name = "user_settings")
@XmlRootElement
public class Details {

    @Id
    private String id;
    private String settings;
    @Column(name = "last_modified")
    private long lastModified;
    @Column(name = "last_sync")
    private long lastSync;

    public Details() {
    }

    public Details(String id, String settings, long lastModified, long lastSync) {
        this.id = id;
        this.settings = settings;
        this.lastModified = lastModified;
        this.lastSync = lastSync;
    }

    public String getId() {
        return id;
    }

    public void setId(String userID) {
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
}
