package tracker.goal.interceptors;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import tracker.goal.Goal;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerType;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@GoalListWriterInterceptor
public class GoalListWriter implements WriterInterceptor {

    private FlatbufferSerializer<GoalWeb> serializer;

    public GoalListWriter(){}

    @Inject
    public GoalListWriter(@SerializerQualifier(SerializerType.Goal) FlatbufferSerializer<GoalWeb> serializer){
        this.serializer = serializer;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object goalsList =  context.getEntity();
        ArrayList<GoalWeb> goalWebs = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(Goal goal : (List<Goal>) goalsList){
            goalWebs.add(helper.toGoalWeb(goal));
        }
        context.getOutputStream().write(serializer.serializeArray(goalWebs));
    }
}
