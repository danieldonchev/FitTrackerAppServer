package tracker.DAO.Daos;

import tracker.Entities.Goal;
import tracker.Entities.GoalKey;

import java.util.List;

public interface GoalDao extends GenericDao<Goal, GoalKey>{
    List<Goal> getAll(String id);
}
