package main.java.com.traker.DAO;


import main.java.com.traker.Authenticate.Password;
import main.java.com.traker.SQLBuilderHelper.SQLBuilder;
import main.java.com.traker.Users.*;
import org.json.JSONObject;

import javax.naming.NamingException;
import java.sql.*;
import java.util.UUID;

public class UserDAOImpl implements UserDAO
{
    public static final int MYSQL_DUPLICATE_PK = 1062;
    public static final int COMMUNICATION_ERROR = 4004;

    @Override
    public GenericUser insertUser(User user)
    {
        String statement = "";
        statement += "INSERT INTO ";
        statement += UserConstants.USER_TABLE + " (" +
        UserConstants.USER_COLUMN_NAME+ ", " +
                UserConstants.USER_COLUMN_EMAIL + ", " +
                UserConstants.USER_COLUMN_ID;

            if(user instanceof LocalUser)
            {
                statement +=  ", " + UserConstants.USER_COLUMN_PASSWORD + ") VALUES (?, ?, ?, ?)";
            }
            else if(user instanceof ExternalUser)
            {
                statement +=") VALUES (?, ?, ?); ";
            }

        try (Connection connection = DAOFactory.getConnection())
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getId().toString());
                if(user instanceof LocalUser)
                {
                    preparedStatement.setString(4, Password.generatePasswordHash(((LocalUser) user).getPassword()));
                }
                preparedStatement.execute();
            }
        }
        catch (SQLIntegrityConstraintViolationException e)
        {
            GenericUser genericUser = new GenericUser(findUserID(user.getEmail()), user.getEmail());
            genericUser.setNew(false);
            return genericUser;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        catch (NamingException ex)
        {
            ex.printStackTrace();
        }

        if(user instanceof ExternalUser)
        {
            return new GenericUser(user.getId(), user.getEmail());
        }
        else
        {
            GenericUser genericUser = new GenericUser(user.getId(), user.getEmail());
            genericUser.setNew(true);
            return genericUser;
        }
    }

    @Override
    public boolean deleteUser(User user) {
        return false;
    }

    @Override
    public int changePassword(JSONObject data) {

        SQLBuilder builder = new SQLBuilder();

        String[] projection = {PasswordTokenConstants.TOKEN,
                                PasswordTokenConstants.EMAIL};
        String where = PasswordTokenConstants.EMAIL + "=? AND " +
                        PasswordTokenConstants.TOKEN + "=?";

        String statement = builder.table(PasswordTokenConstants.TABLE)
                                    .select(projection)
                                    .where(where)
                                    .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int paramIndex = 1;
                String email = data.getString("email");
                preparedStatement.setString(paramIndex++, email);
                preparedStatement.setString(paramIndex++, data.getString("code"));

                ResultSet rs = preparedStatement.executeQuery();
                if(rs.first()){
                    String newPassword = data.getString("password");
                    changePassword(email, newPassword);
                    return 0;
                } else {
                    return -1;
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }

        return -1;
    }

    @Override
    public LocalUser findUser(String email)
    {
        LocalUser user = null;
        PreparedStatement sqlStatement = null;
        try
        {
            String statement = "SELECT "  +
                    UserConstants.USER_COLUMN_PASSWORD +", " +
                    UserConstants.USER_COLUMN_NAME + ", " +
                    UserConstants.USER_COLUMN_ID +
                    " FROM " + UserConstants.USER_TABLE +
                    " WHERE " + UserConstants.USER_COLUMN_EMAIL + "= ?";
            sqlStatement = DAOFactory.getConnection().prepareStatement(statement);
            sqlStatement.setString(1, email);
            ResultSet rs = sqlStatement.executeQuery();

            if(!rs.next())
            {
                return null;
            }
            else
            {
                user = new LocalUser(UUID.fromString(rs.getString(UserConstants.USER_COLUMN_ID)),
                                    email,
                                    rs.getString(UserConstants.USER_COLUMN_NAME),
                                    rs.getString(UserConstants.USER_COLUMN_PASSWORD));
            }
        }
       catch (NamingException | SQLException ex)
       {
           ex.printStackTrace();
       }
        finally {
            if(sqlStatement != null)
            {
                try
                {
                    sqlStatement.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }


    @Override
    public int updateUserDetails(User user)
    {
        PreparedStatement preparedStatement;
        //String statement = "UPDATE " + Constants.USER_DETAILS_TABLE + "SET " + Us
        return 0;
    }

    private UUID findUserID(String email)
    {
        String query = "SELECT " + UserConstants.USER_COLUMN_ID + " FROM " + UserConstants.USER_TABLE + " WHERE " + UserConstants.USER_COLUMN_EMAIL + "=?";

        try(PreparedStatement preparedStatement = DAOFactory.getConnection().prepareStatement(query))
        {
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.next())
            {
                return null;
            }
            else
            {
                return UUID.fromString(rs.getString(UserConstants.USER_COLUMN_ID));
            }
        } catch (SQLException | NamingException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public int saveUserToken(String email, String token) {
        SQLBuilder builder = new SQLBuilder();
        String[] columns = {PasswordTokenConstants.EMAIL,
                            PasswordTokenConstants.TOKEN};
        String[] values = new String[columns.length];
        for(int i = 0; i < columns.length; i++){
            values[i] = "?";
        }

        String statement = builder.table(PasswordTokenConstants.TABLE)
                            .insert(columns, values)
                            .updateOnDuplicate(true)
                            .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int parameterIndex = 1;
                preparedStatement.setString(parameterIndex++, email);
                preparedStatement.setString(parameterIndex++, token);

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
    public String getUserToken(String email) {
        return null;
    }

    private void changePassword(String email, String password){
        SQLBuilder builder = new SQLBuilder();

        String[] columns = {UserConstants.USER_COLUMN_PASSWORD};
        String[] values = {"?"};

        String where = UserConstants.USER_COLUMN_EMAIL + "=?";

        String statement = builder.table(UserConstants.USER_TABLE)
                                    .update(columns, values)
                                    .where(where)
                                    .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int parameterIndex = 1;
                preparedStatement.setString(parameterIndex++, Password.generatePasswordHash(password));
                preparedStatement.setString(parameterIndex++, email);

                preparedStatement.execute();
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public long getLastPasswordChange(String id) {
        SQLBuilder builder = new SQLBuilder();
        String[] projection = {UserConstants.USER_COLUMN_PASSWORD_LAST_MODIFIED};
        String where = UserConstants.USER_COLUMN_ID + "=?";

        String statement = builder.table(UserConstants.USER_TABLE)
                                .select(projection)
                                .where(where)
                                .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.first()){
                    return rs.getLong(UserConstants.USER_COLUMN_PASSWORD_LAST_MODIFIED);
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }

        return -1;
    }

    @Override
    public int insertRefreshToken(User user, String token){
        SQLBuilder builder = new SQLBuilder();

        String[] columns = {RefreshTokenConstants.ID,
                            RefreshTokenConstants.REFRESH_TOKEN,
                            RefreshTokenConstants.DEVICE,
                            RefreshTokenConstants.ANDROID_ID};
        String[] values = new String[columns.length];
        for(int i = 0; i < columns.length; i++){
            values[i] = "?";
        }

        String statement = builder.table(RefreshTokenConstants.TABLE)
                                    .insert(columns, values)
                                    .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int paramIndex = 1;
                preparedStatement.setString(paramIndex++, user.getId().toString());
                preparedStatement.setString(paramIndex++, token);
                preparedStatement.setString(paramIndex++, user.getDevice());
                preparedStatement.setString(paramIndex, user.getAndroidId());
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
    public boolean isRefreshTokenValid(String userID, String device, String androidId,String token){
        SQLBuilder builder = new SQLBuilder();

        String[] projection = {RefreshTokenConstants.ID,
                                RefreshTokenConstants.REFRESH_TOKEN,
                                RefreshTokenConstants.DEVICE,
                                RefreshTokenConstants.ANDROID_ID};

        String where = RefreshTokenConstants.ID + "=? AND " + RefreshTokenConstants.REFRESH_TOKEN + "=?";

        String statement = builder.table(RefreshTokenConstants.TABLE)
                                    .where(where)
                                    .select(projection)
                                    .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int paramIndex = 1;
                preparedStatement.setString(paramIndex++, userID);
                preparedStatement.setString(paramIndex++, token);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.first()){
                    if(rs.getString(RefreshTokenConstants.ANDROID_ID).equals(androidId) &&
                            rs.getString(RefreshTokenConstants.DEVICE).equals(device)){
                        return true;
                    }
                } else{
                    return false;
                }
            }

        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public final class UserConstants
    {
        public static final String USER_TABLE = "users";
        public static final String USER_COLUMN_ID = "id";
        public static final String USER_COLUMN_EMAIL = "email";
        public static final String USER_COLUMN_PASSWORD = "passhash";
        public static final String USER_COLUMN_NAME = "name";
        public static final String USER_COLUMN_PASSWORD_LAST_MODIFIED = "password_last_modified";
    }

    public final class PasswordTokenConstants{
        public static final String TABLE = "user_password_tokens";
        public static final String EMAIL = "email";
        public static final String TOKEN = "token";
    }

    public final class RefreshTokenConstants{
        public static final String TABLE = "refresh_tokens";
        public static final String ID = "id";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String DEVICE = "device";
        public static final String ANDROID_ID = "android_id";
    }
}
