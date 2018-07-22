package tracker.DAO.DaoServices;

import tracker.DAO.Daos.GenericDao;
import tracker.Entities.SportActivity;
import tracker.Entities.SportActivityKey;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SportActivityService  {

    private GenericDao<SportActivity, SportActivityKey> dao;

    @Inject
    public SportActivityService(GenericDao<SportActivity, SportActivityKey> dao){
        this.dao = dao;
    }

    public SportActivity create(SportActivity sportActivity){
        dao.create(sportActivity);
        return sportActivity;
    }

    public SportActivity update(SportActivity sportActivity){
        dao.update(sportActivity);
        return sportActivity;
    }

    public SportActivity read(String id, String userID){
        return dao.read(SportActivity.class, new SportActivityKey(id, userID));
    }

    public void delete(String id, String userID){
        SportActivity sportActivity = new SportActivity();
        sportActivity.setSportActivityKey(new SportActivityKey(id, userID));
        sportActivity.setDeleted(0);
        dao.update(sportActivity);
    }
}
