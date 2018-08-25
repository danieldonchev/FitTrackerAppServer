package tracker.Utils;

import com.tracker.shared.Entities.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import tracker.Entities.*;

import java.util.ArrayList;
import java.util.UUID;

public class WebEntitiesHelper {

    public WebEntitiesHelper(){

    }

    public SportActivity toSportActivity(SportActivityWeb sportActivityWeb, GenericUser user, long timestamp){

        ArrayList<Split> splits = new ArrayList<>();
        for(SplitWeb splitWeb : sportActivityWeb.getSplitWebs()){
            Split split = new Split(splitWeb.getId(),
                    UUID.fromString(sportActivityWeb.getId()),
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

        SportActivity sportActivity = new SportActivity(
                UUID.fromString(sportActivityWeb.getId()),
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

    public SportActivityWeb toSportActivityWeb(SportActivity sportActivity){
        SportActivityWeb sportActivityWeb = new SportActivityWeb(sportActivity.getSportActivityKey().getId().toString());

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

        return sportActivityWeb;
    }

    public GoalWeb toGoalWeb(Goal goal){
        GoalWeb goalWeb = new GoalWeb(
                goal.getGoalKey().getId().toString(),
                goal.getType(),
                goal.getDistance(),
                goal.getDuration(),
                goal.getCalories(),
                goal.getSteps(),
                goal.getFromDate(),
                goal.getToDate(),
                goal.getLastModified()
        );
        return goalWeb;
    }

    public Goal toGoal(GoalWeb goalWeb, GenericUser user){
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
                user.getNewServerTimestamp()
        );
        return goal;
    }

    public Weight toWeight(WeightWeb weightWeb, GenericUser user){
        Weight weight = new Weight(user.getId(),
                weightWeb.date,
                weightWeb.weight,
                weightWeb.lastModified,
                user.getNewServerTimestamp());
        return weight;
    }

    public WeightWeb toWeightWeb(Weight weight){
        WeightWeb weightWeb = new WeightWeb(
                weight.getWeight(),
                weight.getWeightKey().getDate(),
                weight.getLastModified());
        return weightWeb;
    }

}
