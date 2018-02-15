package main.java.com.traker.DAO;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DAOFactory implements DAOFactoryEntities
{
    public static Connection getConnection() throws SQLException, NamingException
    {
        DataSource dataSource = (DataSource) new InitialContext().lookup("jdbc/__msqlPool");
        return dataSource.getConnection();
    }

    @Override
    public UserDAOImpl getUserDAO()
    {
        return new UserDAOImpl();
    }

    @Override
    public UserDetailsDAOImpl getUserDetailsDAO() {
        return new UserDetailsDAOImpl();
    }

    @Override
    public SportActivityDAOImpl getSportActivityDAO() {
        return new SportActivityDAOImpl();
    }

    @Override
    public UserSyncDAOImpl getUserSyncDAO() {
        return new UserSyncDAOImpl();
    }

    @Override
    public GoalDAOImpl getGoalDAO() {
        return new GoalDAOImpl();
    }

    @Override
    public SharedActivitiesDAOImpl getSharedActivitiesDAO() {
        return new SharedActivitiesDAOImpl();
    }

    @Override
    public WeightsDAOImpl getWeightsDAO() {
        return new WeightsDAOImpl();
    }
}
