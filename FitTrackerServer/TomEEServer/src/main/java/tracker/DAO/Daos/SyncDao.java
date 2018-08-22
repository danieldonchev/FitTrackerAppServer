package tracker.DAO.Daos;

import tracker.Entities.GenericUser;

import java.util.List;

public interface SyncDao extends GenericDao{

    <T> List<T> getMissingEntities(GenericUser user, String table, Class<T> clazz);
    List<String> getDeletedEntities(GenericUser user, String table);
    List<String> deleteEntities(GenericUser user, String table, List<String> ids);
}
