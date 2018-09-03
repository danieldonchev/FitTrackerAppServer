//package tracker.InterceptorsTest;
//
//import com.tracker.shared.entities.GoalWeb;
//import org.apache.cxf.jaxrs.impl.ReaderInterceptorContextImpl;
//import org.apache.cxf.message.MessageImpl;
//import org.apache.openejb.junit.ApplicationComposer;
//import org.apache.openejb.testing.Classes;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import tracker.interceptors.Goal;
//import tracker.interceptors.interceptors.GoalReader;
//
//import javax.ws.rs.core.SecurityContext;
//import javax.ws.rs.ext.ReaderInterceptorContext;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.UUID;
//
//@Classes(cdi = true, value = {GoalReader.class})
//@RunWith(ApplicationComposer.class)
//public class GoalReaderInterceptorTest {
//
//    @InjectMocks
//    private GoalReader interceptor;
//    private ReaderInterceptorContext interceptorContext;
//    private GoalWeb goalWeb;
//
//    @Mock
//    private SecurityContext context;
//
//    @Before
//    public void setUp(){
////        MockitoAnnotations.initMocks(this);
////        goalWeb = new GoalWeb(UUID.randomUUID().toString(), 5, 7, 6, 6, 6, 6, 6, 6);
////        InputStream is = new ByteArrayInputStream(goalWeb.serialize());
////        interceptorContext = new ReaderInterceptorContextImpl(null, null, null, is, new MessageImpl(), new ArrayList<>());
//
//    }
//
//    @Test
//    public void test(){
//        Assert.assertEquals(5, 5);
//    }
//
////    @Test
////    public void interceptorResult(){
//////        try {
//////            Goal interceptors = new Goal(goalWeb.getId(),
//////                    "",
//////                    goalWeb.getType(),
//////                    goalWeb.getDistance(),
//////                    goalWeb.getDuration(),
//////                    goalWeb.getCalories(),
//////                    goalWeb.getSteps(),
//////                    goalWeb.getFromDate(),
//////                    goalWeb.getToDate(),
//////                    goalWeb.getLastModified(),
//////                    goalWeb.getLastSync());
//////            Assert.assertEquals(interceptor.aroundReadFrom(interceptorContext), interceptors);
//////
//////        } catch (IOException e) {
//////            e.printStackTrace();
//////        }
////    }
//}
