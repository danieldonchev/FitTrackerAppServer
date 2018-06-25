package tracker.DAO;

import org.json.JSONObject;
import tracker.SQLBuilderHelper.SQLBuilder;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class UserDetailsDAOImpl implements UserDetailsDAO {

    @Override
    public int update(String id, JSONObject data, long lastModified, long timestamp) {
        try {

            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE " + Constants.TABLE + " SET " + Constants.SETTINGS + " = JSON_SET(" + Constants.SETTINGS + ", ");

            Iterator<String> iterator = data.keys();

            int size = data.keySet().size();
            int counter = 0;

            while (iterator.hasNext()) {
                builder.append("\'$." + iterator.next() + "\'");
                builder.append(", ");
                builder.append("?");

                if (counter == size - 1) {
                    break;
                }
                builder.append(", ");
                counter++;
            }
            builder.append(")");
            builder.append(", ");
            builder.append(Constants.COLUMN_LAST_MODIFIED + "=?,");
            builder.append(Constants.COLUMN_LAST_SYNC + "=?");
            builder.append(" WHERE " + Constants.COLUMN_ID + " = ?");


            DAOFactory daoFactory = new DAOFactory();
            try (Connection connection = daoFactory.getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(builder.toString())) {
                    Iterator<String> it = data.keys();
                    int preparedStatementIndex = 1;
                    while (it.hasNext()) {
                        String key = it.next();

                        if (key.equals(Constants.COLUMN_SEX)) {
                            preparedStatement.setString(preparedStatementIndex, data.getString(key));
                        } else if (key.equals(Constants.COLUMN_BIRTHDAY)) {
                            preparedStatement.setLong(preparedStatementIndex, data.getLong(key));
                        } else if (key.equals(Constants.COLUMN_HEIGHT)) {
                            preparedStatement.setInt(preparedStatementIndex, data.getInt(key));
                        } else if (key.equals(Constants.COLUMN_WEIGHT)) {
                            preparedStatement.setDouble(preparedStatementIndex, data.getDouble(key));
                        } else {
                            if (data.get(key) instanceof Boolean) {
                                if (data.getBoolean(key)) {
                                    preparedStatement.setString(preparedStatementIndex, "true");
                                } else {
                                    preparedStatement.setString(preparedStatementIndex, "false");
                                }
                            } else if ((data.get(key) instanceof String)) {
                                preparedStatement.setString(preparedStatementIndex, data.getString(key));
                            } else if ((data.get(key) instanceof Float)) {
                                preparedStatement.setFloat(preparedStatementIndex, (Float) data.get(key));
                            } else if ((data.get(key) instanceof Integer)) {
                                preparedStatement.setInt(preparedStatementIndex, (Integer) data.get(key));
                            } else if ((data.get(key) instanceof Double)) {
                                preparedStatement.setDouble(preparedStatementIndex, (Double) data.get(key));
                            }
                        }

                        preparedStatementIndex++;
                    }
                    preparedStatement.setLong(preparedStatementIndex++, lastModified);
                    preparedStatement.setLong(preparedStatementIndex++, timestamp);
                    preparedStatement.setString(preparedStatementIndex, id.toString());
                    preparedStatement.execute();
                }
            }

        } catch (SQLException | NamingException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    @Override
    public JSONObject getUserSettings(String userID, String where, Object[] selectionArgs) {
        JSONObject settings = null;
        String[] projection = {Constants.SETTINGS};

        String statement = new SQLBuilder()
                .table(Constants.TABLE)
                .where(where)
                .select(projection)
                .build();

        DAOFactory daoFactory = new DAOFactory();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int parameterIndex = 1;
                parameterIndex = DatabaseUtils.setPreparedStatement(preparedStatement, parameterIndex, selectionArgs);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String string = rs.getString(Constants.SETTINGS);
                    settings = new JSONObject(string);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }

        return settings;
    }

    @Override
    public JSONObject getUserSettings(String userID) {
        JSONObject settings = null;
        String[] projection = {Constants.SETTINGS};

        String where = Constants.COLUMN_ID + "=?";

        String statement = new SQLBuilder()
                .table(Constants.TABLE)
                .where(where)
                .select(projection)
                .build();

        DAOFactory daoFactory = new DAOFactory();
        try (Connection connection = daoFactory.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int parameterIndex = 1;
                preparedStatement.setString(parameterIndex++, userID);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String string = rs.getString(Constants.SETTINGS);
                    settings = new JSONObject(string);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }

        return settings;
    }

    public final class Constants {
        public static final String SETTINGS = "settings";
        public static final String TABLE = "user_settings";
        public static final String COLUMN_SETTINGS = "settings";

        public static final String COLUMN_ID = "userID";
        public static final String COLUMN_SEX = "gender";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_BIRTHDAY = "birthday";

        public static final String COLUMN_AUDIO_ON = "audio_on";
        public static final String COLUMN_AUDIO_DISTANCE = "audio_distance";
        public static final String COLUMN_AUDIO_DURATION = "audio_duration";
        public static final String COLUMN_AUDIO_REPEAT_TIME = "audio_repeat_time";
        public static final String COLUMN_AUDIO_PACE = "audio_pace";
        public static final String COLUMN_AUDIO_AVG_PACE = "audio_avg_pace";
        public static final String COLUMN_AUDIO_SPEED = "audio_speed";
        public static final String COLUMN_AUDIO_AVG_SPEED = "audio_avg_speed";
        public static final String COLUMN_AUDIO_STEPS = "audio_steps";
        public static final String COLUMN_AUDIO_CALORIES = "audio_calories";

        public static final String COLUMN_SPLIT = "split";

        public static final String COLUMN_LAST_MODIFIED = "last_modified";
        public static final String COLUMN_LAST_SYNC = "last_sync";
    }
}
