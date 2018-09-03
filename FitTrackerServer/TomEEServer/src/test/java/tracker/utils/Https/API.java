package tracker.utils.Https;

public final class API {
    //base url address
//    private static final String baseURLHttps = "https://95.42.100.225:8181/";
//    private static final String baseURLHttp = "http://95.42.100.225:8080/";
    private static final String baseURLHttps = "https://192.168.100.44:8181/api/";
    private static final String baseURLHttp = "http://192.168.100.44:8080/api/";
//
//    private static final String baseURLHttps = "https://192.168.43.96:8181/";
//    private static final String baseURLHttp = "http://192.168.43.96:8080/";

    //base resources
    private static final String baseAuth = baseURLHttps + "auth/";
    private static final String baseSync = baseURLHttps + "sync/";
    private static final String baseGoal = baseURLHttps + "goal/";
    private static final String basePeopleActivites = baseURLHttp + "people-activities/";
    private static final String baseWeight = baseURLHttps + "weights/";

    //user authentication
    public static final String googleLogin = baseAuth + "google-login/";
    public static final String fbLogin = baseAuth + "fb-login/";
    public static final String localLogin = baseAuth + "login/";
    public static final String register = baseAuth + "register/";
    public static final String captcha = baseURLHttp + "auth/" + "captcha/";
    public static final String passwordToken = baseAuth + "forgotten-password/";
    public static final String changePassword = baseAuth + "change-password/";
    public static final String accessToken = baseAuth + "access-token/";

    //sport activity resources
    public static final String sportActivity = baseURLHttps + "sport-activity/";

   // public static final String profilePic = baseUser + "profile-pic";


    // settings


    //goals
    public static final String goal = baseGoal;

    //weights
    public static final String weight = baseWeight + "weight/";

    //people activities
    public static final String peopleActivities = basePeopleActivites + "activities/";
    public static final String sharedMap = basePeopleActivites + "map/";

    //settings
    public static final String settings = baseURLHttps + "settings/" + "setting/";

    //synchronization
    public static final String syncCheck = "should-sync";
    public static final String missingActivities = baseSync +"missing-sport-activities"; // GET METHOD
    public static final String deletedActivities = baseSync +"deleted-activities";
    public static final String missingGoals = baseSync +"missing-goals";
    public static final String deletedGoals = baseSync +"deleted-goals";
    public static final String weights = baseSync +"weights";
    public static final String syncSettings = baseSync +"settings";
    public static final String getSyncTimes = baseSync +"sync-times";

}



