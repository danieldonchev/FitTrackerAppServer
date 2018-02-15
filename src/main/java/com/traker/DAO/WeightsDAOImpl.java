package main.java.com.traker.DAO;


import com.traker.shared.Weight;
import main.java.com.traker.SQLBuilderHelper.SQLBuilder;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WeightsDAOImpl implements WeightDAO {
    @Override
    public int insertWeight(Weight weight, String userID, long timestamp) {

        SQLBuilder builder = new SQLBuilder();

        String[] columns = {Constants.DATE,
                            Constants.USER_ID,
                            Constants.WEIGHT,
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
                int parameterIndex = 1;
                preparedStatement.setLong(parameterIndex++, weight.date);
                preparedStatement.setString(parameterIndex++, userID);
                preparedStatement.setDouble(parameterIndex++, weight.weight);
                preparedStatement.setLong(parameterIndex++, weight.lastModified);
                preparedStatement.setLong(parameterIndex++, timestamp);

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
    public int updateWeight(Weight weight, String userID, long timestamp) {
        return 0;
    }

    @Override
    public ArrayList<Weight> getWeights(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit) {
        ArrayList<Weight> weights = new ArrayList<>();
        String[] projection = {Constants.WEIGHT,
                                Constants.DATE,
                                Constants.LAST_MODIFIED};

        SQLBuilder builder = new SQLBuilder();

        String statement = builder.table(Constants.TABLE)
                                    .select(projection)
                                    .where(where)
                                    .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int parameterIndex = 1;
                parameterIndex = DatabaseUtils.setPreparedStatement(preparedStatement, parameterIndex, selectionArgs);
                preparedStatement.setString(parameterIndex++, userID);

                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    Weight weight = new Weight(rs.getDouble(Constants.WEIGHT),
                                                rs.getLong(Constants.DATE),
                                                rs.getLong(Constants.LAST_MODIFIED));
                    weights.add(weight);
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }

        return weights;
    }

    public final class Constants{
        public static final String TABLE = "weights";
        public static final String DATE = "date";
        public static final String USER_ID = "userID";
        public static final String WEIGHT = "weight";
        public static final String LAST_MODIFIED = "last_modified";
        public static final String LAST_SYNC = "last_sync";
    }
}
