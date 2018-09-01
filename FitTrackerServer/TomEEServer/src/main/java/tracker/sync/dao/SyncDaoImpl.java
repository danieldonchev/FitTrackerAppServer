package tracker.sync.dao;

import tracker.authentication.users.UserPrincipal;
import tracker.utils.dao.GenericDAOImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@SyncDaoQualifier
public class SyncDaoImpl extends GenericDAOImpl implements SyncDao{

    private EntityManager entityManager;

    public SyncDaoImpl(){
        this.entityManager = getEntityManager();
    }

    public <T> List<T> getMissingEntities(UserPrincipal user, String table, Class<T> clazz){
        Query query = getEntityManager().createNativeQuery("SELECT * from " + table + " s WHERE last_sync > :arg1 AND " +
                "userID = :arg2 and deleted = 0", clazz);
        query.setParameter("arg1", user.getClientSyncTimestamp());
        query.setParameter("arg2", user.getId());
        List list =   query.getResultList();
        return list;
    }

    public List<String> getDeletedEntities(UserPrincipal user, String table){
        Query query = getEntityManager().createNativeQuery("SELECT id from " + table + " s WHERE last_sync > :arg1 AND " +
                "userID = :arg2 and deleted = 1");
        query.setParameter("arg1", user.getClientSyncTimestamp());
        query.setParameter("arg2", user.getId());
        List list =   query.getResultList();
        return list;
    }

    public List<String> deleteEntities(UserPrincipal user, String table, List<String> ids){
        for(String id : ids){
            Query query = getEntityManager().createNativeQuery("UPDATE " + table + " SET deleted = 1 WHERE " +
                    "id = :arg1 AND userID = :arg2");
            query.setParameter("arg1", id);
            query.setParameter("arg2", user.getId());
            query.executeUpdate();
        }

        return ids;
    }


}
