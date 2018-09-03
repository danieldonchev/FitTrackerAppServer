package tracker.goal.interceptors;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import org.apache.commons.io.IOUtils;
import tracker.authentication.users.UserPrincipal;
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

    private FlatbufferSerializer<GoalWeb> serializer;
    private UserPrincipal user;

    public GoalListReader(){}

    @Inject
    public GoalListReader(@SerializerQualifier(SerializerType.Goal) FlatbufferSerializer<GoalWeb> serializer,
                          UserPrincipal user){
        this.serializer = serializer;
        this.user = user;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream is = context.getInputStream();
        ArrayList<Goal> goals = new ArrayList<>();
        ArrayList<GoalWeb> goalWebs = serializer.deserializeArray(IOUtils.toByteArray(is));
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(GoalWeb goalWeb : goalWebs){
            goals.add(helper.toGoal(goalWeb, this.user));
        }

        return goals;
    }
}
