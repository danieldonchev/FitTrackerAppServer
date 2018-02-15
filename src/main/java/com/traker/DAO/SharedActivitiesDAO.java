package main.java.com.traker.DAO;

import com.traker.shared.SportActivity;
import com.traker.shared.SportActivityMap;
import com.traker.shared.SportActivityWithOwner;
import org.json.JSONObject;

import java.util.ArrayList;

public interface SharedActivitiesDAO {
    ArrayList<SportActivityWithOwner> getSharedSportActivities(JSONObject bounds);
    SportActivityMap getSharedSportActivityMap(String activityID, String userID);
}
