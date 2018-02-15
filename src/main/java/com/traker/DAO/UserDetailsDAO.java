package main.java.com.traker.DAO;

import main.java.com.traker.Users.UserDetails;
import org.json.JSONObject;

import java.util.UUID;


public interface UserDetailsDAO
{
    int update(String id, JSONObject data, long lastModified, long timestamp);
    JSONObject getUserSettings(String userID, String where, Object[] selectionArgs);
    JSONObject getUserSettings(String userID);
}
