package main.java.com.traker.DAO;

import main.java.com.traker.SQLBuilderHelper.SQLBuilder;
import org.json.JSONObject;

import javax.naming.NamingException;
import java.sql.*;

public class UserSyncDAOImpl implements UserSyncDAO {

    @Override
    public long getLastModifiedTime(String id) {
        SQLBuilder builder = new SQLBuilder();
        builder.table(Constants.TABLE);
        builder.where(Constants.ID + "=?");
        builder.select(Constants.LAST_SYNC);
        String statement = builder.build();

        try(Connection connection = DAOFactory.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next())
                {
                    long test = rs.getLong(Constants.LAST_SYNC);
                    return rs.getLong(Constants.LAST_SYNC);
                }
            }
        }catch (NamingException ex){
            ex.printStackTrace();
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return -1;
    }

    @Override
    public JSONObject getLastModifiedTimes(String id) {
        JSONObject jsonObject = new JSONObject();
        String[] projection = new String[]{Constants.LAST_MODIFIED_ACTIVITIES,
                                            Constants.LAST_MODIFIED_SETTINGS,
                                            Constants.LAST_MODIFIED_GOALS,
                                            Constants.LAST_MODIFIED_WEIGHTS};
        String where = Constants.ID + "=?";
        String statement = new SQLBuilder()
                .table(Constants.TABLE)
                .select(projection)
                .where(where)
                .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next()){
                    jsonObject.put(Constants.LAST_MODIFIED_ACTIVITIES, rs.getLong(1));
                    jsonObject.put(Constants.LAST_MODIFIED_SETTINGS, rs.getLong(2));
                    jsonObject.put(Constants.LAST_MODIFIED_GOALS, rs.getLong(3));
                    jsonObject.put(Constants.LAST_MODIFIED_WEIGHTS, rs.getLong(4));
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public int setLastModifiedTime(Connection connection, String id, long timeStamp) {

        String[] columns = {Constants.LAST_SYNC};
        String[] values = {"?"};
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.table(Constants.TABLE);
        sqlBuilder.where(Constants.ID + "=?");
        sqlBuilder.update(columns, values);

        String statement = sqlBuilder.build();

            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setLong(1, timeStamp);
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
            }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public final class Constants {
        public static final String TABLE = "user_sync";
        public static final String ID = "id";
        public static final String LAST_SYNC = "last_modified";
        public static final String LAST_MODIFIED_ACTIVITIES = "last_modified_activities";
        public static final String LAST_MODIFIED_SETTINGS = "last_modified_settings";
        public static final String LAST_MODIFIED_GOALS = "last_modified_goals";
        public static final String LAST_MODIFIED_WEIGHTS = "last_modified_weights";
    }
}
