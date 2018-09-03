package tracker.sync;

import tracker.authentication.users.UserPrincipal;

import java.util.List;

public interface SynchronizationService {

    List<Object> getMissingEntities(UserPrincipal user, String tableName, Class clazz);
    List<?> insertEntities(List<?> list);
    List<String> getDeletedEntitiesId(UserPrincipal user, String tableName);
    List<String> deleteEntities(UserPrincipal user, String tableName, List<String> ids);
    ModifiedTimes getTimes(UserPrincipal user);
}
