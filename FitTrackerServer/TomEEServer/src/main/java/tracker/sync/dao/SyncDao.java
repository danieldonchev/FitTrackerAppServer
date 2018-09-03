package tracker.sync.dao;

import tracker.authentication.users.UserPrincipal;
import tracker.utils.dao.GenericDao;

import java.util.List;

public interface SyncDao extends GenericDao {

    <T> List<T> getMissingEntities(UserPrincipal user, String table, Class<T> clazz);
    List<String> getDeletedEntities(UserPrincipal user, String table);
    List<String> deleteEntities(UserPrincipal user, String table, List<String> ids);
}
