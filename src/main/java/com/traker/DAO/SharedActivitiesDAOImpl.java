package main.java.com.traker.DAO;

import com.traker.shared.LatLng;
import com.traker.shared.SportActivity;
import com.traker.shared.SportActivityMap;
import com.traker.shared.SportActivityWithOwner;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import main.java.com.traker.SQLBuilderHelper.SQLBuilder;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static main.java.com.traker.DAO.SportActivityDAOImpl.getGeometryFromInputStream;

public class SharedActivitiesDAOImpl implements SharedActivitiesDAO{
    @Override
    public ArrayList<SportActivityWithOwner> getSharedSportActivities(JSONObject bounds) {
        ArrayList<SportActivityWithOwner> sportActivities = new ArrayList<>();

        String statement = "SELECT " +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_ID + " as sportID," +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_ACTIVITY + "," +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_DISTANCE + "," +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_DURATION + "," +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_START_TIMESTMAP + "," +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_END_TIMESTAMP + "," +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_STEPS + "," +
                "ST_PointN(" + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." +  SportActivityDAOImpl.Constants.SPORT_ACTIVITY_POLYLINE + ",1) as polyline," +
                UserDAOImpl.UserConstants.USER_TABLE + "." + UserDAOImpl.UserConstants.USER_COLUMN_NAME + "," +
                UserDAOImpl.UserConstants.USER_TABLE + "." + UserDAOImpl.UserConstants.USER_COLUMN_ID +
                " FROM " + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + " inner join " + UserDAOImpl.UserConstants.USER_TABLE +
                " on " + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE + "." + SportActivityDAOImpl.Constants.SPORT_ACTIVITY_USERID +
                "  = " + UserDAOImpl.UserConstants.USER_TABLE + "." + UserDAOImpl.UserConstants.USER_COLUMN_ID +
                " where deleted=0 and polyline is not null and st_within(ST_PointN(polyline,1), ST_GeomFromText(?));";
        String polygon = "Polygon((" + bounds.getDouble("topLeftLat") + " " + bounds.getDouble("topLeftLong") + "," +
                bounds.getDouble("topRightLat") + " " + bounds.getDouble("topRightLong") + "," +
                bounds.getDouble("bottomRightLat") + " " + bounds.getDouble("bottomRightLong") + "," +
                bounds.getDouble("bottomLeftLat") + " " + bounds.getDouble("bottomLeftLong") + "," +
                bounds.getDouble("topLeftLat") + " " + bounds.getDouble("topLeftLong") + "))";

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int i = 1;
                preparedStatement.setString(i++, polygon);

                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    SportActivityWithOwner sportActivity = new SportActivityWithOwner();
                    sportActivity.setActivityID(rs.getString("sportID"));
                    sportActivity.setUserID(rs.getString(UserDAOImpl.UserConstants.USER_COLUMN_ID));
                    sportActivity.setDistance(rs.getDouble(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_DISTANCE));
                    sportActivity.setDuration(rs.getLong(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_DURATION));
                    sportActivity.setStartTimestamp(rs.getLong(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_START_TIMESTMAP));
                    sportActivity.setEndTimestamp(rs.getLong(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_END_TIMESTAMP));
                    sportActivity.setSteps(rs.getLong(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_STEPS));
                    sportActivity.setName(rs.getString(UserDAOImpl.UserConstants.USER_COLUMN_NAME));
                    sportActivity.setWorkout(rs.getString(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_ACTIVITY));

                    File imgPath = new File("D:/server_pics/" + rs.getString(UserDAOImpl.UserConstants.USER_COLUMN_ID) + ".png");
                    try {
                        sportActivity.setProfilePic(Files.readAllBytes(imgPath.toPath()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Geometry geometry = getGeometryFromInputStream(rs.getBinaryStream(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_POLYLINE));
                        if(geometry != null){
                            for(Coordinate coordinate : geometry.getCoordinates()){
                                sportActivity.setLatLng(new LatLng(coordinate.x, coordinate.y));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sportActivities.add(sportActivity);
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){

        }

        return sportActivities;
    }

    @Override
    public SportActivityMap getSharedSportActivityMap(String activityID, String userID) {
        SportActivityMap map = new SportActivityMap();

        String[] projection = {SportActivityDAOImpl.Constants.SPORT_ACTIVITY_POLYLINE,
                                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_MARKERS};
        String where = SportActivityDAOImpl.Constants.SPORT_ACTIVITY_ID + "=? AND " +
                        SportActivityDAOImpl.Constants.SPORT_ACTIVITY_USERID + "=? AND " +
                        SportActivityDAOImpl.Constants.SPORT_ACTIVITY_DELETED + "=0";

        SQLBuilder builder = new SQLBuilder();
        String statement = builder.table(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_TABLE)
                .select(projection)
                .where(where)
                .build();

        try(Connection connection = DAOFactory.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                int paramIndex = 1;
                preparedStatement.setString(paramIndex++, activityID);
                preparedStatement.setString(paramIndex++, userID);

                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    ArrayList<LatLng> markers = null;
                    ArrayList<LatLng> polyline = null;
                    try {
                        Geometry geometry = getGeometryFromInputStream(rs.getBinaryStream(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_MARKERS));
                        markers = new ArrayList<>();
                        if(geometry != null){
                            for(Coordinate coordinate : geometry.getCoordinates()){
                                LatLng latLng = new LatLng(coordinate.x, coordinate.y);
                                markers.add(latLng);
                            }
                        }

                        geometry = getGeometryFromInputStream(rs.getBinaryStream(SportActivityDAOImpl.Constants.SPORT_ACTIVITY_POLYLINE));
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


                    map.setMarkers(markers);
                    map.setPolyline(polyline);

                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        } catch (NamingException ex){
            ex.printStackTrace();
        }


        return map;
    }


}
