package tracker.sharedsportactivities;

import org.json.JSONObject;
import tracker.sportactivity.SportActivity;

import java.util.ArrayList;
import java.util.UUID;

public interface SharedActivitiesService {

    ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds);
    //SportActivityMap getSportActivityMap(UUID activityID, UUID userID);
}
