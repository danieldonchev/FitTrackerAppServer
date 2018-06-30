package tracker.Interceptor;

import com.tracker.shared.Entities.GoalWeb;
import sun.misc.IOUtils;
import tracker.Entities.Goal;
import tracker.Markers.GoalInterceptor;
import tracker.Entities.Users.GenericUser;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;

@Provider
@GoalInterceptor
public class GoalReaderInterceptor  implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

        InputStream inputStream = context.getInputStream();
        GoalWeb goalWeb = new GoalWeb().deserialize(IOUtils.readFully(inputStream, -1, true));
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        long timestamp = user.getNewServerTimestamp();

        Goal goal = new Goal(goalWeb.getId(),
                user.getId().toString(),
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