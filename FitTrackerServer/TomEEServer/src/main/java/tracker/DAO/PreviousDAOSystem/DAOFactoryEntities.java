package tracker.DAO.PreviousDAOSystem;


public interface DAOFactoryEntities {
    UserDAOImpl getUserDAO();

    SportActivityDAOImpl getSportActivityDAO();

    GoalDAOImpl getGoalDAO();

    SharedActivitiesDAOImpl getSharedActivitiesDAO();
}
