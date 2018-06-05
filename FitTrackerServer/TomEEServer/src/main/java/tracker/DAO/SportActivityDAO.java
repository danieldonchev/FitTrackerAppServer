package tracker.DAO;

import com.tracker.shared.SportActivity;
import org.json.JSONArray;

import java.util.ArrayList;

public interface SportActivityDAO
{
    int insertSportActivity(SportActivity sportActivity, String userID, long syncTimestamp);
    SportActivity getSportActivity(String id, String userID);
    ArrayList<SportActivity> getActivities(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit);
    boolean deleteSportActivity(String userID, String id, long syncTimestamp);
    int updateSportActivity(String userID, SportActivity sportActivity, long timestamp);
    JSONArray getDeletedSportActivities(String userID);
}
