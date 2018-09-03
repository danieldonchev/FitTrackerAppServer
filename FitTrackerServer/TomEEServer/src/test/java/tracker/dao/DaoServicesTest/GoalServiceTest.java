//package tracker.dao.DaoServicesTest;
//
//import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
//import org.apache.openejb.junit.ApplicationComposer;
//import org.apache.openejb.testing.Module;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import tracker.interceptors.GoalServiceImpl;
//import tracker.utils.dao.GenericDAOImpl;
//import tracker.interceptors.Goal;
//
//import javax.persistence.EntityManager;
//
//import static org.mockito.Mockito.when;
//
////@Classes(cdi = true, value = {GoalServiceImpl.class, GenericDAOImpl.class})
//@RunWith(ApplicationComposer.class)
//public class GoalServiceTest {
//
//    @InjectMocks
//    private GoalServiceImpl service;
//
//    @Mock
//    private GenericDAOImpl<Goal, String> dao;
//
//    @Mock
//    private EntityManager entityManager;
//
//    private Goal interceptors;
//
//    @Module
//    public PersistenceUnit persistence() {
//        PersistenceUnit unit = new PersistenceUnit("trackerApp");
//        unit.setJtaDataSource("traker.db");
//        unit.getClazz().add(GoalServiceImpl.class.getName());
//        unit.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
//        return unit;
//    }
//
//
//    @Before
//    public void setUp(){
//        MockitoAnnotations.initMocks(this);
//        Mockito.reset(entityManager);
//        interceptors = new Goal();
//        when(dao.create(interceptors)).thenReturn(interceptors);
//    }
//
//    @Test
//    public void test(){
//        Assert.assertEquals(5, 5);
//    }
//}
