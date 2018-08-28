package tracker.Entities;

import tracker.Utils.DBConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SplitKey implements Serializable {

    private int id;
    @ManyToOne
    @JoinColumn(name = DBConstants.splits_sport_activity_id)
    private SportActivity sportActivity;

    public SplitKey() {
    }

    public SplitKey(int id, SportActivity sportActivity) {
        this.id = id;
        this.sportActivity = sportActivity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SportActivity getSportActivity() {
        return sportActivity;
    }

    public void setSportActivity(SportActivity sportActivity) {
        this.sportActivity = sportActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SplitKey splitKey = (SplitKey) o;
        return id == splitKey.id &&
                Objects.equals(sportActivity, splitKey.sportActivity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sportActivity);
    }
}
