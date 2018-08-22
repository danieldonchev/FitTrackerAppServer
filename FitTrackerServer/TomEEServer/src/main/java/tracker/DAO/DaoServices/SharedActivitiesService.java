package tracker.DAO.DaoServices;

import com.tracker.shared.Entities.SportActivityMap;
import org.json.JSONObject;
import tracker.Entities.SportActivity;

import java.util.ArrayList;

public interface SharedActivitiesService {

    ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds);
    SportActivityMap getSportActivityMap(String activityID, String userID);
}
