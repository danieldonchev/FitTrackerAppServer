package tracker.rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tracker.DAO.DAOFactory;
import tracker.DAO.DatabaseConnection;

import javax.ws.rs.core.SecurityContext;
import java.sql.Connection;


public class GoalsTest {

    @InjectMocks
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection mockConnection;

    @Mock
    private SecurityContext context;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }


    public void insertGoal(){

    }

}
