package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class SplitKey implements Serializable {

    private int id;
    @Column(name = DBConstants.splits_sport_activity_id)
    private UUID sportActivityId;

    public SplitKey() {
    }

    public SplitKey(int id, UUID sportActivityId) {
        this.id = id;
        this.sportActivityId = sportActivityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getSportActivityId() {
        return sportActivityId;
    }

    public void setSportActivityId(UUID sportActivityId) {
        this.sportActivityId = sportActivityId;
    }
}
