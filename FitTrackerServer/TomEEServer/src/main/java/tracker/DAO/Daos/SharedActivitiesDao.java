package tracker.DAO.Daos;

import com.tracker.shared.Entities.SportActivityMap;
import org.json.JSONObject;
import tracker.Entities.SportActivity;
import tracker.Entities.SportActivityKey;

import javax.enterprise.inject.Alternative;
import java.util.ArrayList;

public interface SharedActivitiesDao extends GenericDao<SportActivity, SportActivityKey> {

    ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds);
    SportActivityMap getSharedSportActivityMap(String activityID, String userID);
}
