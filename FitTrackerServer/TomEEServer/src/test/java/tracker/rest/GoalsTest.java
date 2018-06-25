//package tracker.rest;
//
//import com.tracker.shared.Entities.GoalFlat;
//import org.apache.openejb.junit.ApplicationComposer;
//import org.apache.openejb.testing.Classes;
//import org.json.JSONObject;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import tracker.DAO.DAOFactory;
//import tracker.DAO.GoalDAOImpl;
//import tracker.Users.GenericUser;
//
//import javax.naming.NamingException;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.SecurityContext;
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.sql.SQLException;
//import java.util.UUID;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//
//@Classes(cdi = true, value = {GoalsFlat.class, DAOFactory.class})
//@RunWith(ApplicationComposer.class)
//public class GoalsTest {
////
////    @InjectMocks
////    public GoalsFlat goals;
////
////    GoalFlat goal = new GoalFlat(UUID.randomUUID(),
////            1,
////            554.42d,
////            180l,
////            50l,
////            300l,
////            1l,
////            2l,
////            1l);
////
////    @Mock
////    public DAOFactory daoFactory;
////
////    @Mock
////    public SecurityContext context;
////
////    //@Mock
////    //private InputStream is;
////
////
////    @Before
////    public void setUp() throws SQLException, NamingException {
////        MockitoAnnotations.initMocks(this);
////        when(context.getUserPrincipal()).thenReturn(new GenericUser(UUID.randomUUID(), "asd@asd.com"));
////        GoalDAOImpl goalDAO = mock(GoalDAOImpl.class);
////        when(daoFactory.getGoalDAO()).thenReturn(goalDAO);
////    }
////
////    @Test
////    public void insertGoal(){
//////        byte[] array = goal.serialize();
//////
//////
//////        InputStream is = new ByteArrayInputStream(array);
//////
//////        Response response  = goals.insertGoal(is, context);
//////
//////        GenericUser user = (GenericUser) context.getUserPrincipal();
//////
//////        JSONObject jsonObject = new JSONObject();
//////        jsonObject.put("data", GoalFlat.class.getSimpleName());
//////        jsonObject.put("id", goal.getId());
//////
//////        Assert.assertEquals(response.getStatus(), 200);
//////        Assert.assertEquals(jsonObject.toString(), response.getEntity().toString());
////    }
//
//}
