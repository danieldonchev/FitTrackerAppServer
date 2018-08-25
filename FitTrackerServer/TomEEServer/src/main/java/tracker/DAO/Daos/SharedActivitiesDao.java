package tracker.DAO.Daos;

import com.tracker.shared.Entities.SportActivityMap;
import org.json.JSONObject;
import tracker.Entities.SportActivity;

import javax.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.UUID;

public interface SharedActivitiesDao extends GenericDao<SportActivity, UUID> {

    ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds);
    SportActivityMap getSharedSportActivityMap(UUID activityID, UUID userID);
}
