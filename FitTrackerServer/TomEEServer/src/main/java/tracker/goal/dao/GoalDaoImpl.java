package tracker.goal.dao;

import tracker.utils.dao.GenericDAOImpl;
import tracker.goal.Goal;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@GoalDaoQualifier
public class GoalDaoImpl extends GenericDAOImpl<Goal, UUID> implements GoalDao{

    /*


     */
    @Override
    public List<Goal> getAll(UUID userID) {
         TypedQuery<Goal> query = getEntityManager().createQuery("select g from Goal g where g.userID = :arg1", Goal.class);
        query.setParameter("arg1", userID);
        List<Goal> list = query.getResultList();

        return list;
    }
}
