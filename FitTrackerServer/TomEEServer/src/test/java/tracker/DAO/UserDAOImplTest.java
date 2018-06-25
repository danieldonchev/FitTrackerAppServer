package tracker.DAO;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.internal.util.reflection.Fields;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class UserDAOImplTest {

    @Mock
    DataSource mockDatasource;
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStatement;
    @Mock
    ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
//        when(mockDatasource.getConnection()).thenReturn(mockConnection);
//        when(mockDatasource.getConnection(anyString(), anyString())).thenReturn(mockConnection);
//        doNothing().when(mockConnection).commit();
//        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
//        doNothing().when(mockPreparedStatement).setString(anyInt(), anyString());
//        when(mockPreparedStatement.execute()).thenReturn(Boolean.TRUE);
//        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    }

    @AfterEach
    public void tearDown(){}

    @Test
    public void insertUserWithNoExceptions() throws SQLException {


    }

}
