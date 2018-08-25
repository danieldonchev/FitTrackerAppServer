package tracker.DAO.DaoServices;

import tracker.Entities.Goal;

import java.util.List;
import java.util.UUID;

public interface GoalService {
    Goal insertGoal(Goal goal);
    Goal updateGoal(Goal goal);
    void deleteGoal(UUID id, String userID);
    List<Goal> getGoals(String userID);
}
