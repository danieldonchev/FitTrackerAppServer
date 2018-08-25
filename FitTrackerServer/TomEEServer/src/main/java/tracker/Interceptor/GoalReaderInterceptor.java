package tracker.Interceptor;

import com.google.common.io.ByteStreams;
import com.tracker.shared.Entities.GoalWeb;
import tracker.Entities.GenericUser;
import tracker.Entities.Goal;
import tracker.Markers.GoalInterceptor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Provider
@GoalInterceptor
public class GoalReaderInterceptor implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

        InputStream inputStream = context.getInputStream();
        GoalWeb goalWeb = new GoalWeb().deserialize(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        long timestamp = user.getNewServerTimestamp();
        Goal goal = new Goal(
                UUID.fromString(goalWeb.getId()),
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
