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

        ArrayList<Split> splits = new ArrayList<>();
        for(SplitWeb splitWeb : sportActivityWeb.getSplitWebs()){
            Split split = new Split(splitWeb.getId(),
                    sportActivityWeb.getId(),
                    user.getId(),
                    splitWeb.getDuration(),
                    splitWeb.getDistance());
            splits.add(split);
        }
        GeometryFactory factory = new GeometryFactory();
        ArrayList<Coordinate> polylineCoordinates = new ArrayList<>();
        for(LatLng latLng : sportActivityWeb.getSportActivityMap().getPolyline()){
            Coordinate coordinate = new Coordinate();
            coordinate.x = latLng.latitude;
            coordinate.y = latLng.longitude;
            polylineCoordinates.add(coordinate);
        }
        ArrayList<Coordinate> markerCoordinates = new ArrayList<>();
        for(LatLng latLng : sportActivityWeb.getSportActivityMap().getMarkers()){
            Coordinate coordinate = new Coordinate();
            coordinate.x = latLng.latitude;
            coordinate.y = latLng.longitude;
            markerCoordinates.add(coordinate);
        }
        LineString lineString = factory.createLineString(polylineCoordinates.toArray(new Coordinate[polylineCoordinates.size()]));
        MultiPoint multiPoint = factory.createMultiPoint(markerCoordinates.toArray(new Coordinate[markerCoordinates.size()]));

        SportActivity sportActivity = new SportActivity(sportActivityWeb.getId(),
                user.getId(),
                sportActivityWeb.getWorkout(),
                sportActivityWeb.getDistance(),
                sportActivityWeb.getSteps(),
                sportActivityWeb.getStartTimestamp(),
                sportActivityWeb.getEndTimestamp(),
                splits,
                lineString,
                multiPoint,
                sportActivityWeb.getLastModified(),
                timestamp);

        return sportActivity;
    }
}
