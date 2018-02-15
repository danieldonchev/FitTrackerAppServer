package main.java.com.traker.DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Daniel on 23.5.2017 г..
 */
public class DatabaseUtils {

    public static int setPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, Object[] selectionArgs){
        try {
            for (Object object : selectionArgs) {
                if(object instanceof String){
                    preparedStatement.setString(parameterIndex, (String) object);
                } else if(object instanceof Long){
                    preparedStatement.setLong(parameterIndex, (Long) object);
                } else if(object instanceof Integer){
                    preparedStatement.setInt(parameterIndex, (Integer) object);
                } else if(object instanceof Float){
                    preparedStatement.setFloat(parameterIndex, (Float) object);
                } else if(object instanceof Double){
                    preparedStatement.setDouble(parameterIndex, (Double) object);
                } else if(object instanceof Boolean){
                    preparedStatement.setBoolean(parameterIndex, (Boolean) object);
                }

                parameterIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parameterIndex;
    }
}
