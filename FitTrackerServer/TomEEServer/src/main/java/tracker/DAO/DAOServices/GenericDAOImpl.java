package tracker.DAO.DAOServices;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class GenericDAOImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

    @PersistenceContext(unitName = "trackerApp")
    private EntityManager entityManager;

    public GenericDAOImpl(){ }

    @Override
    public T create(T t) {
        this.entityManager.persist(t);
        return t;
    }

    @Override
    public T read(final Class<T> type, PK id) {
        return this.entityManager.find(type, id);
    }

    @Override
    public void delete(T t) {
        t = this.entityManager.merge(t);
        this.entityManager.remove(t);
    }

    @Override
    public T update(T t) {
        return this.entityManager.merge(t);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
