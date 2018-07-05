package tracker.DAO.DAOServices;

import tracker.DAO.Daos.SyncDao;
import tracker.Entities.Users.GenericUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class SynchronizationService {

    private SyncDao dao;

    @Inject
    public SynchronizationService(SyncDao dao) {
        this.dao = dao;
    }

    public List<Object> getMissingEntities(GenericUser user, String tableName, Class clazz){
        return dao.getMissingEntities(user, tableName, clazz);
    }
}
