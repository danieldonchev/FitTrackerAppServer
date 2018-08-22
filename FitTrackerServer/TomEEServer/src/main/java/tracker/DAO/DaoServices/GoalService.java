package tracker.DAO.DaoServices;

import tracker.Entities.Goal;

import java.util.List;

public interface GoalService {
    Goal insertGoal(Goal goal);
    Goal updateGoal(Goal goal);
    void deleteGoal(String id, String userID);
    List<Goal> getGoals(String userID);
}
