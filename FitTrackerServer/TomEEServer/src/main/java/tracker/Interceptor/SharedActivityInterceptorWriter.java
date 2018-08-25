package tracker.Interceptor;

import com.tracker.shared.Entities.SerializeHelper;
import com.tracker.shared.Entities.SportActivityWithOwner;
import tracker.Entities.SportActivity;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@tracker.Markers.SharedActivityInterceptorWriter
public class SharedActivityInterceptorWriter implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

        Object sharedActivities = context.getEntity();

        ArrayList<SportActivityWithOwner> sportActivities = new ArrayList<>();
        for(SportActivity sportActivity : (List<SportActivity>) sharedActivities){
            SportActivityWithOwner activity = new SportActivityWithOwner();
            activity.setActivityID(sportActivity.getSportActivityKey().getId().toString());
            activity.setUserID(sportActivity.getSportActivityKey().getUserID().toString());


        }

        context.getOutputStream().write(SerializeHelper.serializeSportActivitiesWithOwners(sportActivities));
    }
}
