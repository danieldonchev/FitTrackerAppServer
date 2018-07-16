package tracker.DAO.PreviousDAOSystem;



import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Named
public class DAOFactory implements DAOFactoryEntities {

    public DAOFactory(){ }

    public Connection getConnection() throws SQLException, NamingException {

        DataSource dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/MySQL_Database");
        DatabaseConnection databaseConnection = new DatabaseConnection();
        return databaseConnection.getConnection(dataSource);
    }

    @Override
    public UserDAOImpl getUserDAO() {
        return new UserDAOImpl();
    }

    @Override
    public SportActivityDAOImpl getSportActivityDAO() {
        return new SportActivityDAOImpl();
    }

    @Override
    public GoalDAOImpl getGoalDAO() {
        return new GoalDAOImpl();
    }

    @Override
    public SharedActivitiesDAOImpl getSharedActivitiesDAO() {
        return new SharedActivitiesDAOImpl();
    }

}