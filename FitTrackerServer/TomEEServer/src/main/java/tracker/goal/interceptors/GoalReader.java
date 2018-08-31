package tracker.goal.interceptors;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import tracker.authenticate.GenericUser;
import tracker.goal.Goal;
import tracker.utils.serializers.SerializerQualifier;
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

@Provider
@GoalReaderInterceptor
public class GoalReader implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;
    private FlatbufferSerializer<GoalWeb> serializer;

    public GoalReader(){}

    @Inject
    public GoalReader(@SerializerQualifier(SerializerType.Goal) FlatbufferSerializer<GoalWeb> serializer){
        this.serializer = serializer;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

        InputStream inputStream = context.getInputStream();
        GoalWeb goalWeb = serializer.deserialize(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        long timestamp = user.getNewServerTimestamp();
        Goal goal = new Goal(
                goalWeb.getId(),
                user.getId(),
                goalWeb.getType(),
                goalWeb.getDistance(),
                goalWeb.getDuration(),
                goalWeb.getCalories(),
                goalWeb.getSteps(),
                goalWeb.getFromDate(),
                goalWeb.getToDate(),
                goalWeb.getLastModified(),
                timestamp);

        return goal;
    }


}
