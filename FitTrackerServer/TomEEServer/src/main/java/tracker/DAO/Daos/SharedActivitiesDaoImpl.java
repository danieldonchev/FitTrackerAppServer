package tracker.DAO.Daos;

import com.tracker.shared.Entities.SportActivityMap;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBReader;
import org.json.JSONObject;
import tracker.Entities.SportActivity;
import tracker.Entities.SportActivityKey;

import javax.enterprise.inject.Alternative;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Alternative
public class SharedActivitiesDaoImpl extends GenericDAOImpl<SportActivity, SportActivityKey> implements SharedActivitiesDao {

    /*
    TO DO:
    Finish Tuples. Not working properly
     */
    @Override
    public ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds){
        ArrayList<SportActivity> sportActivities = new ArrayList<>();
        String polygon = "Polygon((" + bounds.getDouble("topLeftLat") + " " + bounds.getDouble("topLeftLong") + "," +
                bounds.getDouble("topRightLat") + " " + bounds.getDouble("topRightLong") + "," +
                bounds.getDouble("bottomRightLat") + " " + bounds.getDouble("bottomRightLong") + "," +
                bounds.getDouble("bottomLeftLat") + " " + bounds.getDouble("bottomLeftLong") + "," +
                bounds.getDouble("topLeftLat") + " " + bounds.getDouble("topLeftLong") + "))";

        List<Tuple> results = getEntityManager().createNativeQuery("SELECT * from user_sport_activity" +
                "inner join users on user_sport_activity.userID = users.id" +
                " where deleted = 0 and polyline is not null AND st_within(ST_PointN(polyline, 1)," +
                "ST_GeomFromText(:arg1))")
                .setParameter("arg1", polygon)
                .getResultList();

        Query query = getEntityManager().createNativeQuery("SELECT * from user_sport_activity where deleted = 0 AND st_within(ST_PointN(polyline, 1)," +
                "ST_GeomFromText(:arg1))");
        query.setParameter("arg1", polygon);
        sportActivities = (ArrayList<SportActivity>) query.getResultList();

        return sportActivities;
    }

    /*
    TO DO:
     public SportActivityMap getSharedSportActivityMap(){}
     */

    /*
    Old method, has to be refactored to use jpa
     */
    @Override
    public SportActivityMap getSharedSportActivityMap(String activityID, String userID) {
        SportActivityMap map = new SportActivityMap();

//        String[] projection = {Constants.SPORT_ACTIVITY_POLYLINE,
//                Constants.SPORT_ACTIVITY_MARKERS};
//        String where = Constants.SPORT_ACTIVITY_ID + "=? AND " +
//                Constants.SPORT_ACTIVITY_USERID + "=? AND " +
//                Constants.SPORT_ACTIVITY_DELETED + "=0";
//
//        SQLBuilder builder = new SQLBuilder();
//        String statement = builder.table(Constants.SPORT_ACTIVITY_TABLE)
//                .select(projection)
//                .where(where)
//                .build();
//
//        DAOFactory daoFactory = new DAOFactory();
//        try (Connection connection = daoFactory.getConnection()) {
//            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
//                int paramIndex = 1;
//                preparedStatement.setString(paramIndex++, activityID);
//                preparedStatement.setString(paramIndex++, userID);
//
//                ResultSet rs = preparedStatement.executeQuery();
//                while (rs.next()) {
//                    ArrayList<LatLng> markers = null;
//                    ArrayList<LatLng> polyline = null;
//                    try {
//                        Geometry geometry = getGeometryFromInputStream(rs.getBinaryStream(Constants.SPORT_ACTIVITY_MARKERS));
//                        markers = new ArrayList<>();
//                        if (geometry != null) {
//                            for (Coordinate coordinate : geometry.getCoordinates()) {
//                                LatLng latLng = new LatLng(coordinate.x, coordinate.y);
//                                markers.add(latLng);
//                            }
//                        }
//
//                        geometry = getGeometryFromInputStream(rs.getBinaryStream(Constants.SPORT_ACTIVITY_POLYLINE));
//                        polyline = new ArrayList<>();
//                        if (geometry != null) {
//                            for (Coordinate coordinate : geometry.getCoordinates()) {
//                                LatLng latLng = new LatLng(coordinate.x, coordinate.y);
//                                polyline.add(latLng);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                    map.setMarkers(markers);
//                    map.setPolyline(polyline);
//
//                }
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } catch (NamingException ex) {
//            ex.printStackTrace();
//        }
//

        return map;
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

    public final class Constants {
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
}
