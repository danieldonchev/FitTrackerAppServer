package tracker.Entities;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import tracker.Utils.DBConstants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = DBConstants.TABLE_SPORT_ACTIVITIES)
public class SportActivity {

    @EmbeddedId
    private SportActivityKey sportActivityKey;
    private String activity = "Walking";
    private double distance = 0;
    private long duration = 0;
    private int calories = 0;
    private long steps = 0;
    private long startTimestamp = 0;
    private long endTimestamp = 0;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = DBConstants.splits_sport_activity_id),
            @JoinColumn(name = DBConstants.userID)
    })

    private List<Split> splits;
    @Column(columnDefinition = "LINESTRING") private LineString polyline;
    @Column(columnDefinition = "MULTIPOINT") private MultiPoint markers;
    @Column(name = DBConstants.last_modified) private long lastModified;
    @Column(name = DBConstants.last_sync) private long lastSync;
    private int deleted = 0;

    public SportActivity() {
    }

    public SportActivity(UUID id, UUID userID, String activity, double distance, long steps, long startTimestamp, long endTimestamp, ArrayList<Split> splits, LineString polyline, MultiPoint markers, long lastModified, long lastSync) {
        this.sportActivityKey = new SportActivityKey(id, userID);
        this.activity = activity;
        this.distance = distance;
        this.steps = steps;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.splits = splits;
        this.polyline = polyline;
        this.markers = markers;
        this.lastModified = lastModified;
        this.lastSync = lastSync;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public void setSplits(ArrayList<Split> splits) {
        this.splits = splits;
    }

    public LineString getPolyline() {
        return polyline;
    }

    public void setPolyline(LineString polyline) {
        this.polyline = polyline;
    }

    public MultiPoint getMarkers() {
        return markers;
    }

    public void setMarkers(MultiPoint markers) {
        this.markers = markers;
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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public SportActivityKey getSportActivityKey() {
        return sportActivityKey;
    }

    public void setSportActivityKey(SportActivityKey sportActivityKey) {
        this.sportActivityKey = sportActivityKey;
    }
}
