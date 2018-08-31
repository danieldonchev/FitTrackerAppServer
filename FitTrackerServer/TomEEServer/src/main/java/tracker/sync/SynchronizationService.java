package tracker.sync;

import tracker.authenticate.GenericUser;
import tracker.sync.ModifiedTimes;

import java.util.List;

public interface SynchronizationService {

    List<Object> getMissingEntities(GenericUser user, String tableName, Class clazz);
    List<?> insertEntities(List<?> list);
    List<String> getDeletedEntitiesId(GenericUser user, String tableName);
    List<String> deleteEntities(GenericUser user, String tableName, List<String> ids);
    ModifiedTimes getTimes(GenericUser user);
}
