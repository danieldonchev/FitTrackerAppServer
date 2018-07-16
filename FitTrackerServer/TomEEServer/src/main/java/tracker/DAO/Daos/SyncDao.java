package tracker.DAO.Daos;

import tracker.Entities.SportActivity;
import tracker.Entities.SportActivityKey;
import tracker.Entities.User;
import tracker.Entities.Users.GenericUser;

import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Alternative
public class SyncDao extends GenericDAOImpl {

    private EntityManager entityManager;

    public SyncDao(){
        this.entityManager = getEntityManager();
    }

    public <T> List<T> getMissingEntities(GenericUser user, String table, Class<T> clazz){
        Query query = getEntityManager().createNativeQuery("SELECT * from " + table + " s WHERE last_sync > :arg1 AND " +
                "userID = :arg2 and deleted = 0", clazz);
        query.setParameter("arg1", user.getClientSyncTimestamp());
        query.setParameter("arg2", user.getId());
        List list =   query.getResultList();
        return list;
    }

    public List<String> getDeletedEntities(GenericUser user, String table){
        Query query = getEntityManager().createNativeQuery("SELECT id from " + table + " s WHERE last_sync > :arg1 AND " +
                "userID = :arg2 and deleted = 1");
        query.setParameter("arg1", user.getClientSyncTimestamp());
        query.setParameter("arg2", user.getId());
        List list =   query.getResultList();
        return list;
    }

    public List<String> deleteEntities(GenericUser user, String table, List<String> ids){
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
