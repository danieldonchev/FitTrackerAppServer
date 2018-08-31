package tracker.goal.interceptors;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import tracker.goal.Goal;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.serializers.SerializerType;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

@Provider
@GoalWriterInterceptor
public class GoalWriter implements WriterInterceptor {

    private FlatbufferSerializer<GoalWeb> serializer;

    public GoalWriter() {
    }

    @Inject
    public GoalWriter(@SerializerQualifier(SerializerType.Goal) FlatbufferSerializer<GoalWeb> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        Goal goal = (Goal) writerInterceptorContext.getEntity();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        GoalWeb goalWeb = helper.toGoalWeb(goal);
        writerInterceptorContext.setEntity(serializer.serialize(goalWeb));
    }
}
