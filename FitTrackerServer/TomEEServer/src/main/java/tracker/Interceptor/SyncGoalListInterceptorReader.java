package tracker.Interceptor;

import com.tracker.shared.Entities.GoalWeb;
import com.tracker.shared.Entities.SerializeHelper;
import org.apache.commons.io.IOUtils;
import tracker.Entities.GenericUser;
import tracker.Entities.Goal;
import tracker.WebEntitiesHelper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Provider
@tracker.Markers.SyncGoalListInterceptorReader
public class SyncGoalListInterceptorReader implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        InputStream is = context.getInputStream();
        ArrayList<Goal> goals = new ArrayList<>();
        ArrayList<GoalWeb> goalWebs = SerializeHelper.deserializeGoals(IOUtils.toByteArray(is));
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(GoalWeb goalWeb : goalWebs){
            goals.add(helper.toGoal(goalWeb, user));
        }

        return goals;
    }
}
