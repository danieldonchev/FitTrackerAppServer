package tracker.Utils;

public class DBConstants {

    public final static String id = "id";
    public final static String userID = "userID";
    public final static String email = "email";
    public final static String last_modified = "last_modified";
    public final static String last_sync = "last_sync";
    public final static String deleted = "deleted";
    public final static String distance = "distance";
    public final static String duration = "duration";
    public final static String steps = "steps";
    public final static String calories = "calories";
    public final static String type = "type";

    // Table weight
    public final static String TABLE_WEIGHT = "weights";
    public final static String date_column = "date";
    public final static String weight_column = "weight";

    // Table users
    public final static String TABLE_USERS = "users";
    public final static String user_name = "name";
    public final static String user_password = "password";
    public final static String user_password_last_modified = "password_last_modified";

    // Table sync
    public final static String TABLE_SYNC = "sync";
    public final static String sync_last_modified = "last_modified";
    public final static String sync_activities = "last_modified_activities";
    public final static String sync_settings = "last_modified_settings";
    public final static String sync_goals = "last_modified_goals";
    public final static String sync_weights = "last_modified_weights";

    // Table sport_activity_splits
    public final static String TABLE_SPORT_ACTIVITY_SPLITS = "sport_activity_splits";
    public final static String splits_sport_activity_id = "sport_activity_id";

    // Table sport_activities
    public final static String TABLE_SPORT_ACTIVITIES = "sport_activities";
    public final static String sport_activity_activity = "activity";
    public final static String sport_activity_polyline = "polyline";
    public final static String sport_activity_markers = "markers";
    public final static String sport_activity_start_timestamp = "start_timestamp";
    public final static String sport_activity_end_timestamp = "end_timestamp";

    // Table settings
    public final static String TABLE_SETTINGS = "settings";
    public final static String settings_settings = "settings";

    // Table refresh_tokens
    public final static String TABLE_REFRESH_TOKENS = "refresh_tokens";
    public final static String refresh_token = "refresh_token";
    public final static String device = "device";
    public final static String android_id = "android_id";

    // Table password_tokens
    public final static String TABLE_PASSWORD_TOKENS = "password_tokens";
    public final static String token = "token";

    // Table goals
    public final static String TABLE_GOALS = "goals";
    public final static String goals_from_date = "from_date";
    public final static String goals_to_date = "to_date";


}
