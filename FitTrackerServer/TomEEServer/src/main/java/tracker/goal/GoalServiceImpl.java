package tracker.goal;

import tracker.goal.dao.GoalDao;
import tracker.goal.dao.GoalDaoQualifier;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Stateless
public class GoalServiceImpl implements GoalService{

    private GoalDao dao;

    public GoalServiceImpl() {
    }

    @Inject
    public GoalServiceImpl(@GoalDaoQualifier GoalDao dao){
        this.dao = dao;
    }

    @Override
    public Goal insertGoal(Goal goal){
        dao.create(goal);
        return goal;
    }

    @Override
    public Goal updateGoal(Goal goal){
        dao.update(goal);
        return goal;
    }

    @Override
    public Goal getGoal(UUID id, UUID userID) {
        return dao.readWithUserId(Goal.class, id, userID);
    }

    @Override
    public void deleteGoal(UUID id, UUID userID){
        Goal goal = new Goal();
        goal.setId(id);
        goal.setUserID(userID);
        goal.setDeleted(1);
        dao.update(goal);
    }

    @Override
    public List<Goal> getGoals(UUID userID){
        return dao.getAll(userID);
    }
}
