package tracker.DAO.Daos;

import tracker.Entities.Goal;

import java.util.List;
import java.util.UUID;

public interface GoalDao extends GenericDao<Goal, UUID>{
    List<Goal> getAll(String id);
}
