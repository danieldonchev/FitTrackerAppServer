package tracker.Interceptor;

import com.tracker.shared.Entities.LatLng;
import com.tracker.shared.Entities.SplitWeb;
import com.tracker.shared.Entities.SportActivityWeb;
import com.vividsolutions.jts.geom.*;
import sun.misc.IOUtils;
import tracker.Entities.Split;
import tracker.Entities.SportActivity;
import tracker.Entities.Users.GenericUser;
import tracker.Markers.SportActivityInterceptorReader;
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
@SportActivityInterceptorReader
public class SportActivityReaderInterceptor implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream inputStream = context.getInputStream();
        SportActivityWeb sportActivityWeb = new SportActivityWeb().deserialize(IOUtils.readFully(inputStream, -1, true));
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        long timestamp = user.getNewServerTimestamp();

        WebEntitiesHelper helper = new WebEntitiesHelper();
        SportActivity sportActivity = helper.toSportActivity(sportActivityWeb, user, timestamp);
        return sportActivity;
    }
}
