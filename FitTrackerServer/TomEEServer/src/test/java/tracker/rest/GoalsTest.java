//package tracker.rest;
//
//import com.tracker.shared.entities.GoalWeb;
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
//import tracker.interceptors.GoalService;
//import tracker.authentication.GenericUser;
//import tracker.interceptors.Goal;
//
//import javax.naming.NamingException;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.SecurityContext;
//import java.sql.SQLException;
//import java.util.UUID;
//
//import static org.mockito.Mockito.when;
//
//
//@Classes(cdi = true, value = {GoalWeb.class})
//@RunWith(ApplicationComposer.class)
//public class GoalsTest {
//
//    @InjectMocks
//    public GoalRest goals;
//
//    private Goal interceptors;
//    private GenericUser user;
//
//    @Mock
//    public GoalService service;
//
//    @Mock
//    public SecurityContext context;
//
//    //@Mock
//    //private InputStream is;
//
//
//    @Before
//    public void setUp() throws SQLException, NamingException {
//        this.user = new GenericUser(UUID.randomUUID().toString(), "didone7@abv.bg");
//        this.interceptors = new Goal(
//                UUID.randomUUID().toString(),
//                user.getId(),
//                3,
//                50,
//                60,
//                70,
//                80,
//                123151,
//                23423,
//                123,
//                412);
//
//        MockitoAnnotations.initMocks(this);
//        when(context.getUserPrincipal()).thenReturn(new GenericUser(UUID.randomUUID().toString(), "asd@asd.com"));
//        when(this.service.insertGoal(interceptors)).thenReturn(interceptors);
//    }
//
//    @Test
//    public void insertGoal(){
//
//        Response response  = goals.insertGoal(interceptors);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data", GoalWeb.class.getSimpleName());
//        jsonObject.put("id", interceptors.getGoalKey().getId());
//
//        Assert.assertEquals(response.getStatus(), 200);
//        Assert.assertEquals(jsonObject.toString(), response.getEntity().toString());
//    }
//
//    @Test
//    public void insertGoalFail(){
////        interceptors = null;
////        Response response  = goals.insertGoal(interceptors);
////
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("data", GoalWeb.class.getSimpleName());
////        jsonObject.put("id", interceptors.getGoalKey().getId());
////
////        Assert.assertEquals(response.getStatus(), 200);
////        Assert.assertEquals(jsonObject.toString(), response.getEntity().toString());
//    }
//}
