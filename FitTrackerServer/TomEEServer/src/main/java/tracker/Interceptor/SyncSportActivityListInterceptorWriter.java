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
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Provider
@tracker.Markers.SyncSportActivityListInterceptorWriter
public class SyncSportActivityListInterceptorWriter implements WriterInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object missingEntities = context.getEntity();

        ArrayList<SportActivityWeb> sportActivities = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(Object sportActivity : (List<SportActivity>) missingEntities){
            sportActivities.add(helper.toSportActivityWeb((SportActivity) sportActivity));
        }

        context.getOutputStream().write(SerializeHelper.serializeSportActivities(sportActivities));
    }
}