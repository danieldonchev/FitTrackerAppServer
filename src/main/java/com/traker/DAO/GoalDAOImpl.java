package main.java.com.traker.DAO;


import com.traker.shared.Goal;
import main.java.com.traker.SQLBuilderHelper.SQLBuilder;
import org.json.JSONArray;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class GoalDAOImpl implements GoalDAO {
    @Override
    public int insertGoal(Goal goal, String userID, long timestamp) {
        SQLBuilder builder = new SQLBuilder();
        String[] columns = {Constants.ID,
        Constants.USERID,
        Constants.TYPE,
        Constants.DISTANCE,
        Constants.DURATION,
        Constants.CALORIES,
        Constants.STEPS,
        Constants.FROM_DATE,
        Constants.TO_DATE,
        Constants.LAST_MODIFIED,
        Constants.LAST_SYNC};

        String[] values = new String[columns.length];
        for(int i = 0; i < columns.length; i++){
            values[i] = "?";
        }

        String statement = builder.table(Constants.TABLE)
                                    .insert(columns, values)
                                    .updateOnDuplicate(true)
                                    .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int i = 1;
                preparedStatement.setString(i++, goal.getId().toString());
                preparedStatement.setString(i++, userID);
                preparedStatement.setInt(i++, goal.getType());
                preparedStatement.setDouble(i++, goal.getDistance());
                preparedStatement.setLong(i++, goal.getDuration());
                preparedStatement.setLong(i++, goal.getCalories());
                preparedStatement.setLong(i++, goal.getSteps());
                preparedStatement.setLong(i++, goal.getFromDate());
                preparedStatement.setLong(i++, goal.getToDate());
                preparedStatement.setLong(i++, goal.getLastModified());
                preparedStatement.setLong(i++, timestamp);
                preparedStatement.execute();
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean deleteGoal(String userID, String id, long timestamp) {

        SQLBuilder builder = new SQLBuilder();
        String where = Constants.ID + "=? AND " + Constants.USERID + "=?";
        String[] columns = {Constants.DELETED, Constants.LAST_SYNC};
        String[] values = {"1", "?"};
        String statement = builder.table(Constants.TABLE)
                .update(columns, values)
                .where(where)
                .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int i = 1;
                preparedStatement.setLong(i++, timestamp);
                preparedStatement.setString(i++, id);
                preparedStatement.setString(i++, userID);
                return preparedStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int updateGoal(Goal goal, String userID, long timestamp) {
        String where = Constants.ID + "=? AND " + Constants.USERID + "=?";
        SQLBuilder builder = new SQLBuilder();

        String[] columns = {Constants.TYPE,
                Constants.DISTANCE,
                Constants.DURATION,
                Constants.CALORIES,
                Constants.STEPS,
                Constants.FROM_DATE,
                Constants.TO_DATE,
                Constants.LAST_MODIFIED,
                Constants.LAST_SYNC};
        String[] values = new String[columns.length];
        for(int i = 0; i < columns.length; i++){
            values[i] = "?";
        }

        String statement = builder.table(Constants.TABLE)
                .where(where)
                .update(columns, values)
                .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int i = 1;
                preparedStatement.setInt(i++, goal.getType());
                preparedStatement.setDouble(i++, goal.getDistance());
                preparedStatement.setLong(i++, goal.getDuration());
                preparedStatement.setLong(i++, goal.getCalories());
                preparedStatement.setLong(i++, goal.getSteps());
                preparedStatement.setLong(i++, goal.getFromDate());
                preparedStatement.setLong(i++, goal.getToDate());
                preparedStatement.setLong(i++, goal.getLastModified());
                preparedStatement.setLong(i++, timestamp);
                preparedStatement.setString(i++, goal.getId().toString());
                preparedStatement.setString(i++, userID);
                preparedStatement.execute();
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }

        return 0;
    }

    @Override
    public ArrayList<Goal> getGoals(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit) {
        ArrayList<Goal> goals = new ArrayList<>();

        SQLBuilder builder = new SQLBuilder();
        String statement = builder.table(Constants.TABLE)
                .select("*")
                .where(where)
                .build();

        try (Connection connection = DAOFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int parameterIndex = 1;
                parameterIndex = DatabaseUtils.setPreparedStatement(preparedStatement, parameterIndex, selectionArgs);
                preparedStatement.setString(parameterIndex, userID);

                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Goal goal = new Goal(UUID.fromString(rs.getString(Constants.ID)),
                            rs.getInt(Constants.TYPE),
                            rs.getDouble(Constants.DISTANCE),
                            rs.getLong(Constants.DURATION),
                            rs.getLong(Constants.CALORIES),
                            rs.getLong(Constants.STEPS),
                            rs.getLong(Constants.FROM_DATE),
                            rs.getLong(Constants.TO_DATE),
                            rs.getLong(Constants.LAST_MODIFIED));
                    goals.add(goal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return goals;
    }

    @Override
    public JSONArray getDeletedGoals(String userID) {
        JSONArray jsonArray = new JSONArray();
        SQLBuilder builder = new SQLBuilder();
        String where = Constants.USERID + "=? AND " + Constants.DELETED + "=1";
        String statement = builder
                .table(Constants.TABLE)
                .select(Constants.ID)
                .where(where)
                .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, userID);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    jsonArray.put(rs.getString(Constants.ID));
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }


        return jsonArray;
    }

    public final class Constants{
        public static final String TABLE = "goals";
        public static final String ID = "id";
        public static final String USERID = "userID";
        public static final String TYPE = "type";
        public static final String DISTANCE = "distance";
        public static final String DURATION = "duration";
        public static final String CALORIES = "calories";
        public static final String STEPS = "steps";
        public static final String FROM_DATE = "from_date";
        public static final String TO_DATE = "to_date";
        public static final String LAST_MODIFIED = "last_modified";
        public static final String LAST_SYNC = "last_sync";
        public static final String DELETED = "deleted";

    }
}
