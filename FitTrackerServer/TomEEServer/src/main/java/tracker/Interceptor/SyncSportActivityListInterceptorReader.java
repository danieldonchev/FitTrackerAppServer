package tracker.Interceptor;

import com.tracker.shared.Entities.SerializeHelper;
import com.tracker.shared.Entities.SportActivityWeb;
import sun.misc.IOUtils;
import tracker.Entities.SportActivity;
import tracker.Entities.Users.GenericUser;
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
@tracker.Markers.SyncSportActivityListInterceptorReader
public class SyncSportActivityListInterceptorReader implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream inputStream = context.getInputStream();
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        long timestamp = user.getNewServerTimestamp();
        ArrayList<SportActivityWeb> sportActivitiesWeb = SerializeHelper.deserializeSportActivities(IOUtils.readFully(inputStream, -1, true));
        ArrayList<SportActivity> sportActivities = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(SportActivityWeb sportActivityWeb : sportActivitiesWeb){
            sportActivities.add(helper.toSportActivity(sportActivityWeb, user, timestamp));
        }

        return sportActivities;
    }
}