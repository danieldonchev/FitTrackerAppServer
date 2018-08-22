package tracker.DAO.DaoServices;

import tracker.Entities.GenericUser;
import tracker.Entities.ModifiedTimes;

import java.util.List;

public interface SynchronizationService {

    List<Object> getMissingEntities(GenericUser user, String tableName, Class clazz);
    List<?> insertEntities(List<?> list);
    List<String> getDeletedEntitiesId(GenericUser user, String tableName);
    List<String> deleteEntities(GenericUser user, String tableName, List<String> ids);
    ModifiedTimes getTimes(GenericUser user);
}
