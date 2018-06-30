package tracker.DAO.DAOServices;

import javax.decorator.Decorator;
import javax.inject.Named;
import java.io.Serializable;


public interface GenericDao<T, PK extends Serializable> {
    T create(T t);
    T read(final Class<T> type, PK id);
    T update(T t);
    void delete(T t);
}
