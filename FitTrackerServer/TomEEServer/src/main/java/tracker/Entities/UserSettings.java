package tracker.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "settings")
    private String settings;
    @Column(name = "last_modified")
    private long lastModified;
    @Column(name = "last_sync")
    private long lastSync;

    public UserSettings() {
    }

    public UserSettings(String id, String settings) {
        this.id = id;
        this.settings = settings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
