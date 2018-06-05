package tracker.DAO;

import com.tracker.shared.Goal;
import org.json.JSONArray;

import java.util.ArrayList;

public interface GoalDAO {
    int insertGoal(Goal goal, String userID, long timestamp);
    boolean deleteGoal(String userID, String id, long timestamp);
    int updateGoal(Goal goal, String userID, long timestamp);
    ArrayList<Goal> getGoals(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit);
    JSONArray getDeletedGoals(String userID);
}
