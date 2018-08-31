package tracker.goal;

import tracker.goal.Goal;

import java.util.List;
import java.util.UUID;

public interface GoalService {
    Goal insertGoal(Goal goal);
    Goal updateGoal(Goal goal);
    void deleteGoal(UUID id, UUID userID);
    Goal getGoal(UUID id, UUID userID);
    List<Goal> getGoals(UUID userID);
}
