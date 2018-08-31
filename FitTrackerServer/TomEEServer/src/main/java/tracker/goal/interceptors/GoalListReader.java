package tracker.goal.interceptors;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import org.apache.commons.io.IOUtils;
import tracker.authenticate.GenericUser;
import tracker.goal.Goal;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerType;

import javax.inject.Inject;
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
@GoalListReaderInterceptor
public class GoalListReader implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;
    private FlatbufferSerializer<GoalWeb> serializer;

    public GoalListReader(){}

    @Inject
    public GoalListReader(@SerializerQualifier(SerializerType.Goal) FlatbufferSerializer<GoalWeb> serializer){
        this.serializer = serializer;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        InputStream is = context.getInputStream();
        ArrayList<Goal> goals = new ArrayList<>();
        ArrayList<GoalWeb> goalWebs = serializer.deserializeArray(IOUtils.toByteArray(is));
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(GoalWeb goalWeb : goalWebs){
            goals.add(helper.toGoal(goalWeb, user));
        }

        return goals;
    }
}
