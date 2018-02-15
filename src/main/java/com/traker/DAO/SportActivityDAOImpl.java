package main.java.com.traker.DAO;

import com.google.gson.Gson;
import com.traker.shared.LatLng;
import com.traker.shared.Split;
import com.traker.shared.SportActivity;
import com.traker.shared.SportActivityMap;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKBReader;
import main.java.com.traker.SQLBuilderHelper.SQLBuilder;
import org.json.JSONArray;

import javax.naming.NamingException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class SportActivityDAOImpl implements SportActivityDAO
{
    @Override
    public int insertSportActivity(SportActivity sportActivity, String userID, long timeStamp)
    {
        String[] columns = {Constants.SPORT_ACTIVITY_ID,
                            Constants.SPORT_ACTIVITY_USERID,
                            Constants.SPORT_ACTIVITY_ACTIVITY,
                            Constants.SPORT_ACTIVITY_START_TIMESTMAP,
                            Constants.SPORT_ACTIVITY_END_TIMESTAMP,
                            Constants.SPORT_ACTIVITY_DISTANCE,
                            Constants.SPORT_ACTIVITY_DURATION,
                            Constants.SPORT_ACTIVITY_STEPS,
                            Constants.SPORT_ACTIVITY_CALORIES,
                            Constants.SPORT_ACTIVITY_POLYLINE,
                            Constants.SPORT_ACTIVITY_MARKERS,
                            Constants.SPORT_ACTIVITY_TYPE,
                            Constants.SPORT_ACTIVITY_LAST_MODIFIED,
                            Constants.SPORT_ACTIVITY_LAST_SYNC};

        String[] values = {"?", "?", "?", "?", "?", "?", "?", "?", "?", "ST_LineStringFromText(?)", "ST_GeomFromText(?)", "?","?", "?"};

        SQLBuilder builder = new SQLBuilder();
        String statement = builder.table(Constants.SPORT_ACTIVITY_TABLE)
                                .insert(columns, values)
                                .updateOnDuplicate(true)
                                .build();

        StringBuilder markerPointsBuilder = new StringBuilder();
        if(sportActivity.getSportActivityMap().getMarkers().size() > 0)
        {
            markerPointsBuilder.append("MULTIPOINT(");
            markerPointsBuilder = stringConcatinator(markerPointsBuilder, sportActivity.getSportActivityMap().getMarkers().iterator());
            markerPointsBuilder.append(")");
        }

        StringBuilder polylineBuilder = new StringBuilder();
        if(sportActivity.getSportActivityMap().getPolyline().size() == 1){
            polylineBuilder.append("LINESTRING(");
            polylineBuilder = stringConcatinator(polylineBuilder, sportActivity.getSportActivityMap().getPolyline().iterator());
            polylineBuilder.append(",");
            polylineBuilder = stringConcatinator(polylineBuilder, sportActivity.getSportActivityMap().getPolyline().iterator());
            polylineBuilder.append(")");
        } else if(sportActivity.getSportActivityMap().getPolyline().size() > 1)
        {
            polylineBuilder.append("LINESTRING(");
            polylineBuilder = stringConcatinator(polylineBuilder, sportActivity.getSportActivityMap().getPolyline().iterator());
            polylineBuilder.append(")");
        }

        try(Connection connection = DAOFactory.getConnection())
        {
            connection.setAutoCommit(false);
            int parameterIndex = 1;
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(parameterIndex++, sportActivity.getId().toString());
                preparedStatement.setString(parameterIndex++, userID);
                preparedStatement.setString(parameterIndex++, sportActivity.getWorkout());
                preparedStatement.setLong(parameterIndex++, sportActivity.getStartTimestamp());
                preparedStatement.setLong(parameterIndex++, sportActivity.getEndTimestamp());
                preparedStatement.setDouble(parameterIndex++, sportActivity.getDistance());
                preparedStatement.setLong(parameterIndex++, sportActivity.getDuration());
                preparedStatement.setLong(parameterIndex++, sportActivity.getSteps());
                preparedStatement.setInt(parameterIndex++, sportActivity.getCalories());
                if(sportActivity.getSportActivityMap().getPolyline().size() > 0)
                {
                    preparedStatement.setString(parameterIndex++, polylineBuilder.toString());
                }
                else
                {
                    preparedStatement.setString(parameterIndex++, null);
                }
                if(sportActivity.getSportActivityMap().getMarkers().size() > 0)
                {
                    preparedStatement.setString(parameterIndex++, markerPointsBuilder.toString());
                }
                else
                {
                    preparedStatement.setString(parameterIndex++, null);
                }
                preparedStatement.setInt(parameterIndex++, sportActivity.getType());
                preparedStatement.setLong(parameterIndex++, sportActivity.getLastModified());
                preparedStatement.setLong(parameterIndex++, timeStamp);
                preparedStatement.execute();
                int updateCount = preparedStatement.getUpdateCount();
                if(updateCount == 1){
                    inserActivitySplits(connection, sportActivity.getSplits(), sportActivity.getId().toString(), userID);
                }
                connection.commit();
                connection.setAutoCommit(true);
            }
        }
        catch (SQLException | NamingException ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public SportActivity getSportActivity(String id, String userID) {
        SportActivity sportActivity = null;
        SQLBuilder builder = new SQLBuilder();
        String where = Constants.SPORT_ACTIVITY_ID + "=?" + " AND " + Constants.SPORT_ACTIVITY_USERID + "=?";
        String statement = builder.table(Constants.SPORT_ACTIVITY_TABLE)
                .select("*")
                .where(where)
                .build();

        try(Connection connection = DAOFactory.getConnection())
        {
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, userID);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next())
                {
                    getSportActivityFromResultSet(connection, userID, rs);
                }
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sportActivity;
    }

    @Override
    public ArrayList<SportActivity> getActivities(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit) {
        ArrayList<SportActivity> sportActivities = new ArrayList<>();
        SQLBuilder builder = new SQLBuilder();
        String statement = builder.table(Constants.SPORT_ACTIVITY_TABLE)
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
                        sportActivities.add(getSportActivityFromResultSet(connection, userID, rs));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }

        return sportActivities;
    }

    @Override
    public boolean deleteSportActivity(String userID, String id, long syncTimestamp) {
        String where = Constants.SPORT_ACTIVITY_ID + "=? AND " + Constants.SPORT_ACTIVITY_USERID + "=?";
        String[] columns = {Constants.SPORT_ACTIVITY_DELETED,
                            Constants.SPORT_ACTIVITY_LAST_SYNC};
        String[] values = {"1", "?"};
        String statement = new SQLBuilder()
                            .table(Constants.SPORT_ACTIVITY_TABLE)
                            .where(where)
                            .update(columns, values)
                            .build();

        try(Connection connection = DAOFactory.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int paramIndex = 1;
                preparedStatement.setLong(paramIndex++, syncTimestamp);
                preparedStatement.setString(paramIndex++, id);
                preparedStatement.setString(paramIndex, userID);

                preparedStatement.execute();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        } catch (NamingException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public int updateSportActivity(String userID, SportActivity sportActivity, long timestamp) {
        String where = Constants.SPORT_ACTIVITY_ID + "=? AND " + Constants.SPORT_ACTIVITY_USERID + "=?";
        SQLBuilder builder = new SQLBuilder();
        builder.table(Constants.SPORT_ACTIVITY_TABLE);
        builder.where(where);

        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        if (sportActivity.getDistance() != 0) {
            columns.add(Constants.SPORT_ACTIVITY_DISTANCE);
        }
        if (sportActivity.getDuration() != 0) {
            columns.add(Constants.SPORT_ACTIVITY_DURATION);
        }
        if (sportActivity.getEndTimestamp() != 0) {
            columns.add(Constants.SPORT_ACTIVITY_END_TIMESTAMP);
        }
        if (sportActivity.getStartTimestamp() != 0) {
            columns.add(Constants.SPORT_ACTIVITY_START_TIMESTMAP);
        }
        if (sportActivity.getSteps() != 0) {
            columns.add(Constants.SPORT_ACTIVITY_STEPS);
        }
        if (sportActivity.getCalories() != 0) {
            columns.add(Constants.SPORT_ACTIVITY_CALORIES);
        }
        if(sportActivity.getWorkout() != null){
            columns.add(Constants.SPORT_ACTIVITY_ACTIVITY);
        }

        columns.add(Constants.SPORT_ACTIVITY_LAST_MODIFIED);
        columns.add(Constants.SPORT_ACTIVITY_LAST_SYNC);

        int columnsSize = columns.size();
        for (int i = 0; i < columnsSize; i++) {
            values.add("?");
        }

        builder.update(columns.toArray(new String[0]), values.toArray(new String[0]));
        String statement = builder.build();

        try (Connection connection = DAOFactory.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                int parameterIndex = 1;
                if (sportActivity.getDistance() != 0) {
                    preparedStatement.setDouble(parameterIndex++, sportActivity.getDistance());
                }
                if (sportActivity.getDuration() != 0) {
                    preparedStatement.setDouble(parameterIndex++, sportActivity.getDuration());
                }
                if (sportActivity.getStartTimestamp() != 0) {
                    preparedStatement.setLong(parameterIndex++, sportActivity.getStartTimestamp());
                }
                if (sportActivity.getEndTimestamp() != 0) {
                    preparedStatement.setLong(parameterIndex++, sportActivity.getEndTimestamp());
                }
                if (sportActivity.getCalories() != 0) {
                    preparedStatement.setInt(parameterIndex++, sportActivity.getCalories());
                }
                if (sportActivity.getSteps() != 0) {
                    preparedStatement.setLong(parameterIndex++, sportActivity.getSteps());
                }
                if(sportActivity.getWorkout() != null){
                    preparedStatement.setString(parameterIndex++, sportActivity.getWorkout());
                }
                preparedStatement.setLong(parameterIndex++, sportActivity.getLastModified());
                preparedStatement.setLong(parameterIndex++, timestamp);
                preparedStatement.setString(parameterIndex++, sportActivity.getId().toString());
                preparedStatement.setString(parameterIndex++, userID);

                preparedStatement.execute();
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        } catch (NamingException ex) {
            ex.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public JSONArray getDeletedSportActivities(String userID) {
        JSONArray jsonArray = new JSONArray();
        SQLBuilder builder = new SQLBuilder();
        String where = Constants.SPORT_ACTIVITY_USERID + "=? AND " + Constants.SPORT_ACTIVITY_DELETED + "=1";
        String statement = builder
                .table(Constants.SPORT_ACTIVITY_TABLE)
                .select(Constants.SPORT_ACTIVITY_ID)
                .where(where)
                .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, userID);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    jsonArray.put(rs.getString(Constants.SPORT_ACTIVITY_ID));
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }


        return jsonArray;
    }

    private int inserActivitySplits(Connection connection, ArrayList<Split> splits, String activityID, String userID)
    {
        String[] columns = {SplitConstants.SPLIT_ID, SplitConstants.SPLIT_SPORT_ACTIVITY_ID, SplitConstants.SPLIT_USER_ID, SplitConstants.SPLIT_DISTANCE, SplitConstants.SPLIT_DURATION};
        String[] values = {"?", "?", "?", "?", "?"};
        String statement = new SQLBuilder()
                .table(SplitConstants.SPLIT_TABLE)
                .insert(columns, values)
                .build();
        try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
            for(Split split : splits)
            {
                    preparedStatement.clearParameters();
                    preparedStatement.setInt(1, split.getId());
                    preparedStatement.setString(2, activityID);
                    preparedStatement.setString(3, userID);
                    preparedStatement.setDouble(4, split.getDistance());
                    preparedStatement.setLong(5, split.getDuration());
                    preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }

        return 0;
    }

    private ArrayList<Split> getActivitySplits(Connection connection, String activityID, String userID)
    {
        ArrayList<Split> splits = new ArrayList<>();
        String[] projection = {SplitConstants.SPLIT_ID,
                                SplitConstants.SPLIT_DISTANCE,
                                SplitConstants.SPLIT_DURATION};
        String where = SplitConstants.SPLIT_SPORT_ACTIVITY_ID + "=? AND " + SplitConstants.SPLIT_USER_ID + "=?";
        String statement = new SQLBuilder()
                            .table(SplitConstants.SPLIT_TABLE)
                            .where(where)
                            .select(projection)
                            .build();

        try(PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, activityID);
            preparedStatement.setString(2, userID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Split split = new Split(rs.getInt(SplitConstants.SPLIT_ID));
                split.setDistance(rs.getDouble(SplitConstants.SPLIT_DISTANCE));
                split.setDuration(rs.getLong(SplitConstants.SPLIT_DURATION));
                splits.add(split);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }


        return splits;
    }

    private SportActivity getSportActivityFromResultSet(Connection connection, String userID, ResultSet rs){
        ArrayList<LatLng> markers = null;
        ArrayList<LatLng> polyline = null;
        try {
            Geometry geometry = getGeometryFromInputStream(rs.getBinaryStream(Constants.SPORT_ACTIVITY_MARKERS));
            markers = new ArrayList<>();
            if(geometry != null){
                for(Coordinate coordinate : geometry.getCoordinates()){
                    LatLng latLng = new LatLng(coordinate.x, coordinate.y);
                    markers.add(latLng);
                }
            }

            geometry = getGeometryFromInputStream(rs.getBinaryStream(Constants.SPORT_ACTIVITY_POLYLINE));
            polyline = new ArrayList<>();
            if(geometry != null){
                for(Coordinate coordinate : geometry.getCoordinates()){
                    LatLng latLng = new LatLng(coordinate.x, coordinate.y);
                    polyline.add(latLng);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SportActivityMap sportActivityMap = new SportActivityMap();
        sportActivityMap.setMarkers(markers);
        sportActivityMap.setPolyline(polyline);

        SportActivity sportActivity = null;
        try {
            sportActivity = new SportActivity(UUID.fromString(rs.getString(Constants.SPORT_ACTIVITY_ID)),
                    rs.getString(Constants.SPORT_ACTIVITY_ACTIVITY),
                    rs.getLong(Constants.SPORT_ACTIVITY_DURATION),
                    rs.getDouble(Constants.SPORT_ACTIVITY_DISTANCE),
                    rs.getInt(Constants.SPORT_ACTIVITY_STEPS),
                    rs.getInt(Constants.SPORT_ACTIVITY_CALORIES),
                    sportActivityMap,
                    rs.getLong(Constants.SPORT_ACTIVITY_START_TIMESTMAP),
                    rs.getLong(Constants.SPORT_ACTIVITY_END_TIMESTAMP),
                    rs.getInt(Constants.SPORT_ACTIVITY_TYPE),
                    rs.getLong(Constants.SPORT_ACTIVITY_LAST_MODIFIED),
                    getActivitySplits(connection, rs.getString(Constants.SPORT_ACTIVITY_ID), userID));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sportActivity;
    }

    private StringBuilder stringConcatinator(StringBuilder stringBuilder, Iterator<LatLng> iterator)
    {
        for (Iterator<LatLng> latLngIterator = iterator; latLngIterator.hasNext(); ) {
            LatLng latLng = latLngIterator.next();
            stringBuilder.append(latLng.latitude);
            stringBuilder.append(" ");
            stringBuilder.append(latLng.longitude);
            if (!latLngIterator.hasNext()) {
                break;
            } else {
                stringBuilder.append(",");
            }
        }

        return stringBuilder;
    }

    public static Geometry getGeometryFromInputStream(InputStream inputStream) throws Exception {

        Geometry dbGeometry = null;

        if (inputStream != null) {

            //convert the stream to a byte[] array
            //so it can be passed to the WKBReader
            byte[] buffer = new byte[255];

            int bytesRead = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] geometryAsBytes = baos.toByteArray();

            if (geometryAsBytes.length < 5) {
                throw new Exception("Invalid geometry inputStream - less than five bytes");
            }

            //first four bytes of the geometry are the SRID,
            //followed by the actual WKB.  Determine the SRID
            //here
            byte[] sridBytes = new byte[4];
            System.arraycopy(geometryAsBytes, 0, sridBytes, 0, 4);
            boolean bigEndian = (geometryAsBytes[4] == 0x00);

            int srid = 0;
            if (bigEndian) {
                for (int i = 0; i < sridBytes.length; i++) {
                    srid = (srid << 8) + (sridBytes[i] & 0xff);
                }
            } else {
                for (int i = 0; i < sridBytes.length; i++) {
                    srid += (sridBytes[i] & 0xff) << (8 * i);
                }
            }

            //use the JTS WKBReader for WKB parsing
            WKBReader wkbReader = new WKBReader();

            //copy the byte array, removing the first four
            //SRID bytes
            byte[] wkb = new byte[geometryAsBytes.length - 4];
            System.arraycopy(geometryAsBytes, 4, wkb, 0, wkb.length);
            dbGeometry = wkbReader.read(wkb);
            dbGeometry.setSRID(srid);
        }

        return dbGeometry;
    }

    public final class Constants
    {
        public static final String SPORT_ACTIVITY_TABLE = "user_sport_activity";
        public static final String SPORT_ACTIVITY_ID = "id";
        public static final String SPORT_ACTIVITY_USERID = "userID";
        public static final String SPORT_ACTIVITY_ACTIVITY = "activity";
        public static final String SPORT_ACTIVITY_START_TIMESTMAP = "startTimestamp";
        public static final String SPORT_ACTIVITY_END_TIMESTAMP = "endTimestamp";
        public static final String SPORT_ACTIVITY_DISTANCE = "distance";
        public static final String SPORT_ACTIVITY_DURATION = "duration";
        public static final String SPORT_ACTIVITY_STEPS = "steps";
        public static final String SPORT_ACTIVITY_CALORIES = "calories";
        public static final String SPORT_ACTIVITY_POLYLINE = "polyline";
        public static final String SPORT_ACTIVITY_MARKERS = "markers";
        public static final String SPORT_ACTIVITY_TYPE = "type";
        public static final String SPORT_ACTIVITY_LAST_MODIFIED = "last_modified";
        public static final String SPORT_ACTIVITY_LAST_SYNC = "last_sync";
        public static final String SPORT_ACTIVITY_DELETED = "deleted";
    }

    public final class SplitConstants
    {
        public static final String SPLIT_TABLE = "sport_activity_splits";
        public static final String SPLIT_ID = "id";
        public static final String SPLIT_SPORT_ACTIVITY_ID = "sport_activity_id";
        public static final String SPLIT_USER_ID = "user_id";
        public static final String SPLIT_DURATION = "duration";
        public static final String SPLIT_DISTANCE = "distance";
    }
}
