package com.tracker.shared.entities;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;

public class SerializeHelper {

    public static byte[] serializeSportActivitiesWithOwners(ArrayList<SportActivityWithOwner> sportActivities){
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        ListIterator<SportActivityWithOwner> iterator = sportActivities.listIterator(sportActivities.size());
        int[] sportActivityOffsets = new int[sportActivities.size()];
        int i = 0;
        while(iterator.hasPrevious()){
            SportActivityWithOwner sportActivity = iterator.previous();
            sportActivityOffsets[i] = sportActivity.getSportActivityInt(builder);
            i++;
        }

        int vector = SportActivitiesFlat.createSportActivitiesVector(builder, sportActivityOffsets);
        SportActivitiesFlat.startSportActivitiesFlat(builder);
        SportActivitiesFlat.addSportActivities(builder, vector);
        int activities = SportActivitiesFlat.endSportActivitiesFlat(builder);

        builder.finish(activities);
        return builder.sizedByteArray();
    }

    public static ArrayList<SportActivityWithOwner> deserializeSportActivityWithOwners(byte[] bytes){
        ArrayList<SportActivityWithOwner> sportActivities = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        SportActivitiesWithOwnerFlat sportActivitiesBufferer = SportActivitiesWithOwnerFlat.getRootAsSportActivitiesWithOwnerFlat(buf);

        for(int i = 0; i < sportActivitiesBufferer.sportActivitiesLength(); i++){
            com.tracker.shared.flatbuf.SportActivityWithOwnerFlat sportActivity = sportActivitiesBufferer.sportActivities(i);

            SportActivityWithOwner activity = new SportActivityWithOwner();
            activity.setWorkout(sportActivity.activity());
            activity.setDuration(sportActivity.duration());
            activity.setDistance(sportActivity.distance());
            activity.setSteps(sportActivity.steps());
            activity.setStartTimestamp(sportActivity.startTimestamp());
            activity.setEndTimestamp(sportActivity.endTimestamp());
            activity.setName(sportActivity.name());
            PolylineFlat polylineFlat = sportActivity.startPoint();
            activity.setLatLng(new LatLng(polylineFlat.lat(), polylineFlat.lon()));
            ByteBuffer imgBuffer = sportActivity.profilePicAsByteBuffer();
            if(imgBuffer != null){
                byte[] b = new byte[imgBuffer.remaining()];
                imgBuffer.get(b);
                activity.setProfilePic(b);
            }
            activity.setUserID(sportActivity.userId());
            activity.setActivityID(sportActivity.activityId());


            sportActivities.add(activity);
        }

        return sportActivities;
    }

}
