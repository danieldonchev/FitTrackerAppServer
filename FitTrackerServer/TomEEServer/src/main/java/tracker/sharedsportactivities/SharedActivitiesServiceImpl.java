package tracker.sharedsportactivities;

import com.tracker.shared.entities.SportActivityMap;
import org.json.JSONObject;
import tracker.sharedsportactivities.dao.SharedActivitiesDao;
import tracker.sharedsportactivities.dao.SharedActivitiesDaoQualifier;
import tracker.sportactivity.SportActivity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

@Stateless
public class SharedActivitiesServiceImpl implements SharedActivitiesService {

    public SharedActivitiesDao dao;

    public SharedActivitiesServiceImpl() {
    }

    @Inject
    public SharedActivitiesServiceImpl(@SharedActivitiesDaoQualifier SharedActivitiesDao dao) {
        this.dao = dao;
    }

    public ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds){
        return dao.getSharedSportActivities(bounds);
    }

    public SportActivityMap getSportActivityMap(UUID activityID, UUID userID){
        return dao.getSharedSportActivityMap(activityID, userID);
    }
}
