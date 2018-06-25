package tracker.DAO;

import com.tracker.shared.Entities.GoalWeb;
import org.json.JSONArray;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface GoalDAO {
    GoalWeb insertGoal(GoalWeb goalWeb, String userID, long timestamp) throws SQLException, NamingException;

    void deleteGoal(String userID, String id, long timestamp) throws SQLException, NamingException;

    GoalWeb updateGoal(GoalWeb goalWeb, String userID, long timestamp) throws SQLException, NamingException;

    ArrayList<GoalWeb> getGoals(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit);

    JSONArray getDeletedGoals(String userID);
}
