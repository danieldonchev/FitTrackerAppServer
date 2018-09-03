package tracker.goal.dao;

import tracker.utils.dao.GenericDao;
import tracker.goal.Goal;

import java.util.List;
import java.util.UUID;

public interface GoalDao extends GenericDao<Goal, UUID> {
    List<Goal> getAll(UUID userID);
}
