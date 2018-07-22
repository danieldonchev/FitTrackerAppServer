package tracker.DAO;

import org.json.JSONObject;


public interface UserDetailsDAO
{
    int update(String id, JSONObject data, long lastModified, long timestamp);
    JSONObject getUserSettings(String userID, String where, Object[] selectionArgs);
    JSONObject getUserSettings(String userID);
}
