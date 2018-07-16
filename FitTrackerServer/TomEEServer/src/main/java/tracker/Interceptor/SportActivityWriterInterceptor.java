package tracker.Interceptor;

import com.tracker.shared.Entities.LatLng;
import com.tracker.shared.Entities.SplitWeb;
import com.tracker.shared.Entities.SportActivityMap;
import com.tracker.shared.Entities.SportActivityWeb;
import com.vividsolutions.jts.geom.Coordinate;
import tracker.Entities.Split;
import tracker.Entities.SportActivity;
import tracker.Markers.SportActivityInterceptorReader;
import tracker.Markers.SportActivityInterceptorWriter;
import tracker.WebEntitiesHelper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;

@Provider
@SportActivityInterceptorWriter
public class SportActivityWriterInterceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        SportActivity sportActivity = (SportActivity) context.getEntity();

        WebEntitiesHelper helper = new WebEntitiesHelper();
        SportActivityWeb sportActivityWeb = helper.toSportActivityWeb(sportActivity);
        context.getOutputStream().write(sportActivityWeb.serialize());

    }
}
