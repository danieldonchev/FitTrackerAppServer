package tracker.DAO.Daos;

import tracker.Utils.DBConstants;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.io.Serializable;
import java.util.UUID;

@Default
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
    public T readWithUserId(Class<T> type, PK id, UUID userID) {
        TypedQuery<T> query = this.entityManager.createQuery("Select t from" + type.getSimpleName() + " t where " + DBConstants.userID + "=:arg1", type);
        query.setParameter("arg1", userID);
        return query.getSingleResult();
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

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
