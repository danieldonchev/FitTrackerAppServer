package tracker.utils;

public class API {

    /*

    ----AUTHENTICATION-----
    AuthenticationRest path - /auth/
    Register account - /auth/register/ - POST @see tracker.authentication.AuthenticationRest#register()
    Login account - /auth/login/ - POST
    Google login - /auth/googlelogin/ - POST
    Facebook login - /auth/fblogin/ - POST
    Forgotten password - /auth/forgotten-password/ - POST
    Access token - /auth/access-token/ - POST
    Change password - /auth/password/ - POST


   ----SPORT ACTIVITY----
   SportActivity - /sport-activity/
   SportActivity - /sport-activity/ - POST

    ----GOAL----
    Goal - /interceptors/

    ----WEIGHT----
    Weight - /weight/

    ----SETTINGS----
    SettingsRest - /settings/

    ----SYNCHRONIZATION----
    SynchronizationRest - /sync/
    Sync check - /sync/shoud-sync/
    Missing Sport activities - /sync/sport-activities/
    Deleted Sport activities - /sync/deleted-activities/
    Missing goals - /sync/goals/
    Deleted goals - /sync/deleted-goals/
    Weight - /sync/weights/
    SettingsRest - /sync/settings/
    Sync times - /sync/sync-times

    ----SHARED SPORT ACTIVITIES----
    Shared sport activities - /shared-sport-activities/
    Sport activities - /shared-sport-activities/activities/
    Sport activities map - /shared-sport-activites/map/

     */

    // AuthenticationRest
    public static final String authentication = "auth";
    public static final String register = "register";
    public static final String login = "login";
    public static final String googleLogin = "googlelogin";
    public static final String facebookLogin = "fblogin";
    public static final String forgottenPassword = "forgotten-password";
    public static final String accessToken = "access-token";
    public static final String changePassword = "password";

    // Sport Activity
    public static final String sportActivity = "sport-activity";

    // Goal
    public static final String goal = "interceptors";

    // Weight
    public static final String weight = "weight";

    // SettingsRest
    public static final String settings = "settings";

    // SynchronizationRest
    public static final String sync = "sync";
    public static final String syncCheck = "should-sync";
    public static final String missingSportActivities = "sport-activities";
    public static final String deletedSportActivities = "deleted-activities";
    public static final String missingGoals = "goals";
    public static final String deletedGoals = "deleted-goals";
    public static final String weights = "weights";
    public static final String syncTimes = "sync-times";

    // Shared sport activities
    public static final String sharedSportActivities = "shared-sport-activities";
    public static final String sportActivities = "activities";
    public static final String sportActivityMap = "map";

}
