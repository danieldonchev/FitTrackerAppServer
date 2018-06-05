package tracker;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Settings {

    private String settings;
    private long lastModified;

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
}
