package tracker.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
public class SplitKey implements Serializable {

    private int id;
    @Column(name = "sport_activity_id") private String sportActivityId;

    public SplitKey() {
    }

    public SplitKey(int id, String sportActivityId) {
        this.id = id;
        this.sportActivityId = sportActivityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSportActivityId() {
        return sportActivityId;
    }

    public void setSportActivityId(String sportActivityId) {
        this.sportActivityId = sportActivityId;
    }
}
