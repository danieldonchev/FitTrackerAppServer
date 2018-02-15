package main.java.com.traker.DAO;


public interface DAOFactoryEntities
{
    UserDAOImpl getUserDAO();
    UserDetailsDAOImpl getUserDetailsDAO();
    SportActivityDAOImpl getSportActivityDAO();
    UserSyncDAOImpl getUserSyncDAO();
    GoalDAOImpl getGoalDAO();
    SharedActivitiesDAOImpl getSharedActivitiesDAO();
    WeightsDAOImpl getWeightsDAO();
}
