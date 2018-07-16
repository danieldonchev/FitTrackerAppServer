package tracker.DAO.DaoServicesTest;

import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tracker.DAO.DAOServices.GoalService;
import tracker.DAO.DAOServices.GoalServiceImpl;
import tracker.DAO.Daos.GenericDAOImpl;
import tracker.DAO.Daos.GenericDao;
import tracker.Entities.Goal;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.when;

//@Classes(cdi = true, value = {GoalServiceImpl.class, GenericDAOImpl.class})
@RunWith(ApplicationComposer.class)
public class GoalServiceTest {

    @InjectMocks
    private GoalServiceImpl service;

    @Mock
    private GenericDAOImpl<Goal, String> dao;

    @Mock
    private EntityManager entityManager;

    private Goal goal;

    @Module
    public PersistenceUnit persistence() {
        PersistenceUnit unit = new PersistenceUnit("trackerApp");
        unit.setJtaDataSource("traker.db");
        unit.getClazz().add(GoalServiceImpl.class.getName());
        unit.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
        return unit;
    }


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.reset(entityManager);
        goal = new Goal();
        when(dao.create(goal)).thenReturn(goal);
    }

    @Test
    public void insertGoalTest(){
        Goal testGoal = service.insertGoal(goal);

        Assert.assertEquals(testGoal, goal);
    }
}
