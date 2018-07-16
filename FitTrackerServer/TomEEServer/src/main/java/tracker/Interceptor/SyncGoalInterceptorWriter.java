package tracker.Interceptor;

import com.tracker.shared.Entities.GoalWeb;
import com.tracker.shared.Entities.SerializeHelper;
import tracker.Entities.Goal;
import tracker.WebEntitiesHelper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@tracker.Markers.SyncGoalInterceptorWriter
public class SyncGoalInterceptorWriter implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object missingEntities = context.getEntity();
        ArrayList<GoalWeb> goals = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(Object goal : (List<Goal>)missingEntities){
            goals.add(helper.toGoalWeb((Goal) goal));
        }
        context.getOutputStream().write(SerializeHelper.serializeGoals(goals));
    }
}
