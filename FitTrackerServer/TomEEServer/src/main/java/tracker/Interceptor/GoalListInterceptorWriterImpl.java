package tracker.Interceptor;

import com.tracker.shared.Entities.GoalWeb;
import com.tracker.shared.Entities.SerializeHelper;
import tracker.Entities.Goal;
import tracker.Markers.GoalListInterceptorWriter;
import tracker.WebEntitiesHelper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@tracker.Markers.GoalListInterceptorWriter
public class GoalListInterceptorWriterImpl implements WriterInterceptor {
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object goalsList =  context.getEntity();
        ArrayList<GoalWeb> goalWebs = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(Goal goal : (List<Goal>) goalsList){
            goalWebs.add(helper.toGoalWeb(goal));
        }
        context.getOutputStream().write(SerializeHelper.serializeGoals(goalWebs));
    }
}
