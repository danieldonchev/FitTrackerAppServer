package tracker.sharedsportactivities.dao;

import com.tracker.shared.entities.SportActivityMap;
import org.json.JSONObject;
import tracker.utils.dao.GenericDao;
import tracker.sportactivity.SportActivity;

import java.util.ArrayList;
import java.util.UUID;

public interface SharedActivitiesDao extends GenericDao<SportActivity, UUID> {

    ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds);
    SportActivityMap getSharedSportActivityMap(UUID activityID, UUID userID);
}
