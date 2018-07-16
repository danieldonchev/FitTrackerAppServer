package tracker.DAO.PreviousDAOSystem;

import com.tracker.shared.Entities.SportActivityWeb;
import org.json.JSONArray;

import java.util.ArrayList;

public interface SportActivityDAO {
    int insertSportActivity(SportActivityWeb sportActivityWeb, String userID, long syncTimestamp);

    SportActivityWeb getSportActivity(String id, String userID);

    ArrayList<SportActivityWeb> getActivities(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit);

    boolean deleteSportActivity(String userID, String id, long syncTimestamp);

    int updateSportActivity(String userID, SportActivityWeb sportActivityWeb, long timestamp);

    JSONArray getDeletedSportActivities(String userID);
}
