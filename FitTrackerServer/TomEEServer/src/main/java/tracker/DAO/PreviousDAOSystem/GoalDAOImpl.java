package tracker.DAO.PreviousDAOSystem;

import com.tracker.shared.Entities.GoalWeb;
import org.json.JSONArray;
import tracker.SQLBuilderHelper.SQLBuilder;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class GoalDAOImpl implements GoalDAO {
    @Override
    public GoalWeb insertGoal(GoalWeb goalWeb, String userID, long timestamp) throws SQLException, NamingException {
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
        for (int i = 0; i < columns.length; i++) {
            values[i] = "?";
        }

        String statement = builder.table(Constants.TABLE)
                .insert(columns, values)
                .updateOnDuplicate(true)
                .build();

        DAOFactory daoFactory = new DAOFactory();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int i = 1;
                preparedStatement.setString(i++, goalWeb.getId().toString());
                preparedStatement.setString(i++, userID);
                preparedStatement.setInt(i++, goalWeb.getType());
                preparedStatement.setDouble(i++, goalWeb.getDistance());
                preparedStatement.setLong(i++, goalWeb.getDuration());
                preparedStatement.setLong(i++, goalWeb.getCalories());
                preparedStatement.setLong(i++, goalWeb.getSteps());
                preparedStatement.setLong(i++, goalWeb.getFromDate());
                preparedStatement.setLong(i++, goalWeb.getToDate());
                preparedStatement.setLong(i++, goalWeb.getLastModified());
                preparedStatement.setLong(i++, timestamp);
                preparedStatement.execute();
            }
        }

        return goalWeb;
    }

    @Override
    public void deleteGoal(String userID, String id, long timestamp) throws SQLException, NamingException {

        SQLBuilder builder = new SQLBuilder();
        String where = Constants.ID + "=? AND " + Constants.USERID + "=?";
        String[] columns = {Constants.DELETED, Constants.LAST_SYNC};
        String[] values = {"1", "?"};
        String statement = builder.table(Constants.TABLE)
                .update(columns, values)
                .where(where)
                .build();

        DAOFactory daoFactory = new DAOFactory();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int i = 1;
                preparedStatement.setLong(i++, timestamp);
                preparedStatement.setString(i++, id);
                preparedStatement.setString(i++, userID);
                preparedStatement.execute();
            }
        }
    }

    @Override
    public GoalWeb updateGoal(GoalWeb goalWeb, String userID, long timestamp) throws SQLException, NamingException {
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
        for (int i = 0; i < columns.length; i++) {
            values[i] = "?";
        }

        String statement = builder.table(Constants.TABLE)
                .where(where)
                .update(columns, values)
                .build();

        DAOFactory daoFactory = new DAOFactory();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int i = 1;
                preparedStatement.setInt(i++, goalWeb.getType());
                preparedStatement.setDouble(i++, goalWeb.getDistance());
                preparedStatement.setLong(i++, goalWeb.getDuration());
                preparedStatement.setLong(i++, goalWeb.getCalories());
                preparedStatement.setLong(i++, goalWeb.getSteps());
                preparedStatement.setLong(i++, goalWeb.getFromDate());
                preparedStatement.setLong(i++, goalWeb.getToDate());
                preparedStatement.setLong(i++, goalWeb.getLastModified());
                preparedStatement.setLong(i++, timestamp);
                preparedStatement.setString(i++, goalWeb.getId().toString());
                preparedStatement.setString(i++, userID);
                preparedStatement.execute();
            }
        }

        return goalWeb;
    }

    @Override
    public ArrayList<GoalWeb> getGoals(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit) {
        ArrayList<GoalWeb> goalWebs = new ArrayList<>();

        SQLBuilder builder = new SQLBuilder();
        String statement = builder.table(Constants.TABLE)
                .select("*")
                .where(where)
                .build();

        DAOFactory daoFactory = new DAOFactory();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int parameterIndex = 1;
                parameterIndex = DatabaseUtils.setPreparedStatement(preparedStatement, parameterIndex, selectionArgs);
                preparedStatement.setString(parameterIndex, userID);

                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    GoalWeb goalWeb = new GoalWeb(rs.getString(Constants.ID),
                            rs.getInt(Constants.TYPE),
                            rs.getDouble(Constants.DISTANCE),
                            rs.getLong(Constants.DURATION),
                            rs.getLong(Constants.CALORIES),
                            rs.getLong(Constants.STEPS),
                            rs.getLong(Constants.FROM_DATE),
                            rs.getLong(Constants.TO_DATE),
                            rs.getLong(Constants.LAST_MODIFIED));
                    goalWebs.add(goalWeb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return goalWebs;
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

        DAOFactory daoFactory = new DAOFactory();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, userID);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    jsonArray.put(rs.getString(Constants.ID));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }


        return jsonArray;
    }

    public final class Constants {
        public static final String TABLE = "goals";
        public static final String ID = "id";
        public static final String USERID = "userID";
        public static final String TYPE = "type";
        public static final String DISTANCE = "distance";
        public static final String DURATION = "duration";
        public static final String CALORIES = "calories";
        public static final String STEPS = "steps";
        public static final String FROM_DATE = "fromDate";
        public static final String TO_DATE = "toDate";
        public static final String LAST_MODIFIED = "lastModified";
        public static final String LAST_SYNC = "last_sync";
        public static final String DELETED = "deleted";
    }
}
