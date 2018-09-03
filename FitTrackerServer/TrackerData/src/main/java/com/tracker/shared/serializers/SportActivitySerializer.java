package com.tracker.shared.serializers;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.entities.LatLng;
import com.tracker.shared.entities.SplitWeb;
import com.tracker.shared.entities.SportActivityMap;
import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.flatbuf.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;

public class SportActivitySerializer implements FlatbufferSerializer<SportActivityWeb> {

    @Override
    public byte[] serialize(SportActivityWeb sportActivityWeb) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = getSportActivityInt(builder, sportActivityWeb);

        builder.finish(finish);
        return builder.sizedByteArray();
    }

    @Override
    public SportActivityWeb deserialize(byte[] bytes) {

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        SportActivityFlat sportActivityFlatBufferer = SportActivityFlat.getRootAsSportActivityFlat(buf);

        SportActivityWeb sportActivityWeb = new SportActivityWeb(
                sportActivityFlatBufferer.id(),
                sportActivityFlatBufferer.activity(),
                sportActivityFlatBufferer.duration(),
                sportActivityFlatBufferer.distance(),
                sportActivityFlatBufferer.steps(),
                sportActivityFlatBufferer.calories(),
                sportActivityFlatBufferer.startTimestamp(),
                sportActivityFlatBufferer.endTimestamp(),
                sportActivityFlatBufferer.lastModified()
        );

        ArrayList<LatLng> polyline = new ArrayList<>();
        ArrayList<LatLng> markers = new ArrayList<>();
        ArrayList<SplitWeb> splitWebs = new ArrayList<>();

        if(sportActivityFlatBufferer.sportActivityMap() != null){
            for(int i = 0; i < sportActivityFlatBufferer.sportActivityMap().markersLength(); i++)
            {
                MarkersFlat marker = sportActivityFlatBufferer.sportActivityMap().markers(i);
                markers.add(new LatLng(marker.lat(), marker.lon()));
            }
            for(int i = 0; i < sportActivityFlatBufferer.sportActivityMap().polylineLength(); i++)
            {
                PolylineFlat polylineFlat = sportActivityFlatBufferer.sportActivityMap().polyline(i);
                polyline.add(new LatLng(polylineFlat.lat(), polylineFlat.lon()));
            }
        }
        if(sportActivityFlatBufferer.splits() != null){
            for(int i = 0; i < sportActivityFlatBufferer.splits().splitsLength(); i++)
            {
                SplitFlat splitFlat = sportActivityFlatBufferer.splits().splits(i);
                splitWebs.add(new SplitWeb(splitFlat.id(), splitFlat.duration(), splitFlat.distance()));
            }
        }

        sportActivityWeb.getSportActivityMap().setPolyline(polyline);
        sportActivityWeb.getSportActivityMap().setMarkers(markers);
        sportActivityWeb.setSplitWebs(splitWebs);

        return sportActivityWeb;
    }

    @Override
    public byte[] serializeArray(ArrayList<SportActivityWeb> list) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        ListIterator<SportActivityWeb> iterator = list.listIterator(list.size());
        int[] sportActivityOffsets = new int[list.size()];
        int i = 0;
        while(iterator.hasPrevious()){
            SportActivityWeb sportActivityWeb = iterator.previous();
            sportActivityOffsets[i] = getSportActivityInt(builder, sportActivityWeb);
            i++;
        }

        int vector = SportActivitiesFlat.createSportActivitiesVector(builder, sportActivityOffsets);
        SportActivitiesFlat.startSportActivitiesFlat(builder);
        SportActivitiesFlat.addSportActivities(builder, vector);
        int activities = SportActivitiesFlat.endSportActivitiesFlat(builder);

        builder.finish(activities);
        return builder.sizedByteArray();
    }

    @Override
    public ArrayList<SportActivityWeb> deserializeArray(byte[] bytes) {
        ArrayList<SportActivityWeb> sportActivities = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        SportActivitiesFlat sportActivitiesFlatBufferer = SportActivitiesFlat.getRootAsSportActivitiesFlat(buf);

        for(int i = 0; i < sportActivitiesFlatBufferer.sportActivitiesLength(); i++){
            SportActivityFlat sportActivityFlat = sportActivitiesFlatBufferer.sportActivities(i);

            SportActivityMapFlat sportActivityMapFlat = sportActivityFlat.sportActivityMap();
            SportActivityMap map = new SportActivityMap();
            if(sportActivityMapFlat != null){
                map.deserializeFromFlatBuffMap(sportActivityMapFlat);
            }

            SportActivityWeb activity = new SportActivityWeb(sportActivityFlat.id(),
                    sportActivityFlat.activity(),
                    sportActivityFlat.duration(),
                    sportActivityFlat.distance(),
                    sportActivityFlat.steps(),
                    sportActivityFlat.calories(),
                    sportActivityFlat.startTimestamp(),
                    sportActivityFlat.endTimestamp(),
                    sportActivityFlat.lastModified());

            activity.setSportActivityMap(map);

            SplitsFlat splitsFlat = sportActivityFlat.splits();
            if(splitsFlat != null){
                getSplitsFromFlatBuffSplits(sportActivityFlat.splits());
            }


            sportActivities.add(activity);
        }

        return sportActivities;
    }

    public int getSportActivityInt(FlatBufferBuilder builder, SportActivityWeb sportActivity){
        int idString = builder.createString(sportActivity.getId().toString());
        int activityString = builder.createString(sportActivity.getWorkout());

        int cardioMapInt = 0;
        if(sportActivity.getSportActivityMap() != null){
            cardioMapInt = sportActivity.getSportActivityMap().getBufferInt(builder);
        }
        int splits = getSplitsBufferInt(builder, sportActivity.getSplitWebs());

        SportActivityFlat.startSportActivityFlat(builder);
        SportActivityFlat.addId(builder, idString);
        SportActivityFlat.addActivity(builder, activityString);
        SportActivityFlat.addSplits(builder, splits);
        SportActivityFlat.addSportActivityMap(builder, cardioMapInt);
        SportActivityFlat.addStartTimestamp(builder, sportActivity.getStartTimestamp());
        SportActivityFlat.addEndTimestamp(builder, sportActivity.getEndTimestamp());
        SportActivityFlat.addDuration(builder, sportActivity.getDuration());
        SportActivityFlat.addDistance(builder, sportActivity.getDistance());
        SportActivityFlat.addSteps(builder, sportActivity.getSteps());
        SportActivityFlat.addCalories(builder, sportActivity.getCalories());
        SportActivityFlat.addLastModified(builder, sportActivity.getLastModified());

        return SportActivityFlat.endSportActivityFlat(builder);
    }

    public int getSplitsBufferInt(FlatBufferBuilder builder, ArrayList<SplitWeb> splitWebs)
    {
        if(splitWebs != null){
            ListIterator<SplitWeb> splitListIterator = splitWebs.listIterator(splitWebs.size());

            SplitsFlat.startSplitsVector(builder, splitWebs.size());

            while(splitListIterator.hasPrevious())
            {
                SplitWeb splitWeb = splitListIterator.previous();
                SplitFlat.createSplitFlat(builder, splitWeb.getId(), splitWeb.getDistance(), splitWeb.getDuration());
            }

            int splits = builder.endVector();

            SplitsFlat.startSplitsFlat(builder);
            SplitsFlat.addSplits(builder, splits);
            return SplitsFlat.endSplitsFlat(builder);
        }


        return 0;
    }

    public ArrayList<SplitWeb> getSplitsFromFlatBuffSplits(SplitsFlat flatBuffSplitsFlat){
        ArrayList<SplitWeb> splitWebs = new ArrayList<>();
        if(flatBuffSplitsFlat != null){
            for(int i = 0; i < flatBuffSplitsFlat.splitsLength(); i++)
            {
                SplitFlat splitFlat = flatBuffSplitsFlat.splits(i);
                splitWebs.add(new SplitWeb(splitFlat.id(), splitFlat.duration(), splitFlat.distance()));
            }
        }
        return splitWebs;
    }

    public int getMapBufferInt(FlatBufferBuilder builder, SportActivityMap map)
    {
        ListIterator<LatLng> polyLineIterator = map.getPolyline().listIterator(map.getPolyline().size());
        ListIterator<LatLng> markerIterator = map.getMarkers().listIterator(map.getMarkers().size());

        SportActivityMapFlat.startMarkersVector(builder, map.getMarkers().size());

        while(markerIterator.hasPrevious())
        {
            LatLng latLng = markerIterator.previous();
            MarkersFlat.createMarkersFlat(builder, latLng.latitude, latLng.longitude, 0);
        }

        int markers = builder.endVector();

        SportActivityMapFlat.startPolylineVector(builder, map.getPolyline().size());

        while(polyLineIterator.hasPrevious())
        {
            LatLng latLng = polyLineIterator.previous();
            PolylineFlat.createPolylineFlat(builder, latLng.latitude, latLng.longitude);
        }

        int polyline = builder.endVector();

        SportActivityMapFlat.startSportActivityMapFlat(builder);
        SportActivityMapFlat.addMarkers(builder, markers);
        SportActivityMapFlat.addPolyline(builder, polyline);

        return SportActivityMapFlat.endSportActivityMapFlat(builder);
    }
}
