package tracker.DAO.DaoServices;

import tracker.DAO.Daos.GoalDao;
import tracker.Entities.Goal;
import tracker.Qualifiers.GoalDaoQualifier;

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
    public void deleteGoal(UUID id, String userID){
        Goal goal = new Goal();
        goal.setId(id);
        goal.setUserID(userID);
        goal.setDeleted(1);
        dao.update(goal);
    }

    @Override
    public List<Goal> getGoals(String userID){
        return dao.getAll(userID);
    }
}
