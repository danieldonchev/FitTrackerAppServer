package tracker.DAO.DAOServices;

import com.sun.org.apache.xpath.internal.operations.Mod;
import tracker.DAO.Daos.SyncDao;
import tracker.Entities.ModifiedTimes;
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

    public List<?> insertEntities(List<?> list){
        for(Object object : list){
            dao.create(object);
        }
        return list;
    }

    public List<String> getDeletedEntitiesId(GenericUser user, String tableName){
        return dao.getDeletedEntities(user, tableName);
    }

    public List<String> deleteEntities(GenericUser user, String tableName, List<String> ids){
        dao.deleteEntities(user, tableName, ids);
        return ids;
    }

    public ModifiedTimes getTimes(GenericUser user){
        ModifiedTimes times = (ModifiedTimes) dao.read(ModifiedTimes.class, user.getId());
        return times;
    }
}
