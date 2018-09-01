package tracker.sync;

import tracker.authentication.users.UserPrincipal;
import tracker.sync.dao.SyncDao;
import tracker.sync.dao.SyncDaoQualifier;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class SynchronizationServiceImpl implements SynchronizationService{

    private SyncDao dao;

    @Inject
    public SynchronizationServiceImpl(@SyncDaoQualifier SyncDao dao) {
        this.dao = dao;
    }

    public List<Object> getMissingEntities(UserPrincipal user, String tableName, Class clazz){
        return dao.getMissingEntities(user, tableName, clazz);
    }

    public List<?> insertEntities(List<?> list){
        for(Object object : list){
            dao.create(object);
        }
        return list;
    }

    public List<String> getDeletedEntitiesId(UserPrincipal user, String tableName){
        return dao.getDeletedEntities(user, tableName);
    }

    public List<String> deleteEntities(UserPrincipal user, String tableName, List<String> ids){
        dao.deleteEntities(user, tableName, ids);
        return ids;
    }

    public ModifiedTimes getTimes(UserPrincipal user){
        ModifiedTimes times = (ModifiedTimes) dao.read(ModifiedTimes.class, user.getId());
        return times;
    }
}
