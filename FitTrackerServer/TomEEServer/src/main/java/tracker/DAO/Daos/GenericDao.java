package tracker.DAO.Daos;

import javax.decorator.Decorator;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;


public interface GenericDao<T, PK extends Serializable> {
    T create(T t);
    T read(final Class<T> type, PK id);
    T update(T t);
    void delete(T t);
    EntityManager getEntityManager();
}
