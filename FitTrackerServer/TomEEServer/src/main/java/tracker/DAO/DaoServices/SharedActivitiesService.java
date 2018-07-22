package tracker.DAO.DaoServices;

import com.tracker.shared.Entities.SportActivityMap;
import org.json.JSONObject;
import tracker.DAO.Daos.SharedActivitiesDao;
import tracker.Entities.SportActivity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class SharedActivitiesService {

    public SharedActivitiesDao dao;

    public SharedActivitiesService() {
    }

    @Inject
    public SharedActivitiesService(SharedActivitiesDao dao) {
        this.dao = dao;
    }

    public ArrayList<SportActivity> getSharedSportActivities(JSONObject bounds){
        return dao.getSharedSportActivities(bounds);
    }

    public SportActivityMap getSportActivityMap(String activityID, String userID){
        return dao.getSharedSportActivityMap(activityID, userID);
    }
}
