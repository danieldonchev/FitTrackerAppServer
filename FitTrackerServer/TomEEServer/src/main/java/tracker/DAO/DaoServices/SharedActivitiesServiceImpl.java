package tracker.DAO.DaoServices;

import com.tracker.shared.Entities.SportActivityMap;
import org.json.JSONObject;
import tracker.DAO.Daos.SharedActivitiesDao;
import tracker.Entities.SportActivity;
import tracker.Qualifiers.SharedActivitiesDaoQualifier;

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
