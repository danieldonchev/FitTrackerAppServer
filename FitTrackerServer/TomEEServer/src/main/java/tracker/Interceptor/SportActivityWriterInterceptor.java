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
        SportActivityWeb sportActivityWeb = new SportActivityWeb(sportActivity.getSportActivityKey().getId());

        ArrayList<LatLng> polyline = new ArrayList<>();
        if(sportActivity.getPolyline() != null){
            for(Coordinate coordinate : sportActivity.getPolyline().getCoordinates()){
                polyline.add(new LatLng(coordinate.x, coordinate.y));
            }
        }
        ArrayList<LatLng> markers = new ArrayList<>();
        if(sportActivity.getMarkers() != null){
            for(Coordinate coordinate : sportActivity.getMarkers().getCoordinates()){
                markers.add(new LatLng(coordinate.x, coordinate.y));
            }
        }
        ArrayList<SplitWeb> splitWebs = new ArrayList<>();
        if(sportActivity.getSplits() != null){
            for(Split split : sportActivity.getSplits()){
                splitWebs.add(new SplitWeb(split.getSplitKey().getId(), split.getDuration(), split.getDistance()));
            }
        }


        SportActivityMap sportActivityMap = new SportActivityMap();
        sportActivityMap.setMarkers(markers);
        sportActivityMap.setPolyline(polyline);

        sportActivityWeb.setSportActivityMap(sportActivityMap);
        sportActivityWeb.setSplitWebs(splitWebs);
        sportActivityWeb.setDistance(sportActivity.getDistance());
        sportActivityWeb.setDuration(sportActivity.getDuration());
        sportActivityWeb.setCalories(sportActivity.getCalories());
        sportActivityWeb.setSteps(sportActivity.getSteps());
        sportActivityWeb.setStartTimestamp(sportActivity.getStartTimestamp());
        sportActivityWeb.setEndTimestamp(sportActivity.getEndTimestamp());
        sportActivityWeb.setLastModified(sportActivity.getLastModified());
        sportActivityWeb.setWorkout(sportActivity.getActivity());

        context.getOutputStream().write(sportActivityWeb.serialize());

    }
}
