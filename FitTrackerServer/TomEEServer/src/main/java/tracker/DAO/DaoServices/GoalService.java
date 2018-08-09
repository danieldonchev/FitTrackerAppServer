package tracker.DAO.DaoServices;

import tracker.Entities.Goal;

public interface GoalService {
    Goal insertGoal(Goal goal);
    Goal updateGoal(Goal goal);
    void deleteGoal(String id, String userID);
}