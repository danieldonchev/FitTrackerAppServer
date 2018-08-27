package tracker.DAO.Daos;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.UUID;

public interface GenericDao<T, PK extends Serializable> {
    T create(T t);
    T read(final Class<T> type, PK id);
    T readWithUserId(final Class<T> type, PK id, UUID userID);
    T update(T t);
    void delete(T t);
    EntityManager getEntityManager();
}
