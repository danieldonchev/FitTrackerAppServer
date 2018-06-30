package tracker.DAO.DAOServices;

import tracker.Entities.Goal;
import tracker.Entities.GoalKey;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GoalServiceImpl implements GoalService{

    private GenericDao<Goal, String> dao;

    @Inject
    public GoalServiceImpl(GenericDao<Goal, String> dao){
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
    public void deleteGoal(String id, String userID){
        Goal goal = new Goal();
        goal.setGoalKey(new GoalKey(id, userID));
        goal.setDeleted(1);
        dao.update(goal);
    }
}
