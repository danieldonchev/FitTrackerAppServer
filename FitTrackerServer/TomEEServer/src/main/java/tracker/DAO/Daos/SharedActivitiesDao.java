package tracker.DAO.Daos;

import com.tracker.shared.Entities.SportActivityMap;
import org.json.JSONObject;
import tracker.Entities.SportActivity;
import tracker.Entities.SportActivityKey;

import java.util.ArrayList;

public interface SharedActivitiesDao extends GenericDao<SportActivity, SportActivityKey> {

    public ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds);
    public SportActivityMap getSharedSportActivityMap(String activityID, String userID);
}
