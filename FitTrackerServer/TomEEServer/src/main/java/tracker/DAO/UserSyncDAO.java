package tracker.DAO;

import org.json.JSONObject;

import java.sql.Connection;

public interface UserSyncDAO {
    long getLastModifiedTime(String id);

    JSONObject getLastModifiedTimes(String id);

    int setLastModifiedTime(Connection connection, String id, long timeStamp);

}
