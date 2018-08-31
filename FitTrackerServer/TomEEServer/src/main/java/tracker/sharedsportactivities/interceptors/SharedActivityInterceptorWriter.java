package tracker.sharedsportactivities.interceptors;

import com.tracker.shared.entities.SerializeHelper;
import com.tracker.shared.entities.SportActivityWithOwner;
import tracker.sportactivity.SportActivity;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@SharedActivityInterceptor
public class SharedActivityInterceptorWriter implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

        Object sharedActivities = context.getEntity();

        ArrayList<SportActivityWithOwner> sportActivities = new ArrayList<>();
        for(SportActivity sportActivity : (List<SportActivity>) sharedActivities){
            SportActivityWithOwner activity = new SportActivityWithOwner();
            activity.setActivityID(sportActivity.getId().toString());
            activity.setUserID(sportActivity.getUserID().toString());
        }

        context.getOutputStream().write(SerializeHelper.serializeSportActivitiesWithOwners(sportActivities));
    }
}
