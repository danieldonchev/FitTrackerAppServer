package tracker.DAO;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    public Connection getConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }
}
