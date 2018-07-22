package tracker.DAO;

import com.tracker.shared.SportActivityMap;
import com.tracker.shared.SportActivityWithOwner;
import org.json.JSONObject;

import java.util.ArrayList;

public interface SharedActivitiesDAO {
    ArrayList<SportActivityWithOwner> getSharedSportActivities(JSONObject bounds);
    SportActivityMap getSharedSportActivityMap(String activityID, String userID);
}
