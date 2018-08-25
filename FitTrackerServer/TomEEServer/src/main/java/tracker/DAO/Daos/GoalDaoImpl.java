package tracker.DAO.Daos;

import tracker.Entities.Goal;
import tracker.Qualifiers.GoalDaoQualifier;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@GoalDaoQualifier
public class GoalDaoImpl extends GenericDAOImpl<Goal, UUID> implements GoalDao{

    @Override
    public List<Goal> getAll(String id) {
         TypedQuery<Goal> query = getEntityManager().createQuery("select g from Goal g where g.userID = :arg1", Goal.class);
        query.setParameter("arg1", id);
        List<Goal> list = query.getResultList();

        return list;
    }
}
