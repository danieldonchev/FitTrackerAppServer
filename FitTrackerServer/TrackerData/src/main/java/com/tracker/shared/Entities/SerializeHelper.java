package com.tracker.shared.Entities;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.UUID;

public class SerializeHelper {

    public static byte[] serializeSportActivities(ArrayList<SportActivityWeb> sportActivities){
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        ListIterator<SportActivityWeb> iterator = sportActivities.listIterator(sportActivities.size());
        int[] sportActivityOffsets = new int[sportActivities.size()];
        int i = 0;
        while(iterator.hasPrevious()){
            SportActivityWeb sportActivityWeb = iterator.previous();
            sportActivityOffsets[i] = sportActivityWeb.getSportActivityInt(builder);
            i++;
        }

        int vector = SportActivitiesFlat.createSportActivitiesVector(builder, sportActivityOffsets);
        SportActivitiesFlat.startSportActivities(builder);
        SportActivitiesFlat.addSportActivities(builder, vector);
        int activities = SportActivitiesFlat.endSportActivities(builder);

        builder.finish(activities);

        ByteBuffer buf = builder.dataBuffer();
        byte[] array = new byte[buf.remaining()];
        buf.get(array);
        return array;
    }

    public static ArrayList<SportActivityWeb> deserializeSportActivities(byte[] bytes){
        ArrayList<SportActivityWeb> sportActivities = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        SportActivitiesFlat sportActivitiesFlatBufferer = SportActivitiesFlat.getRootAsSportActivities(buf);

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
                activity.getSplitsFromFlatBuffSplits(sportActivityFlat.splits());
            }


            sportActivities.add(activity);
        }

        return sportActivities;
    }

    public static byte[] serializeGoals(ArrayList<GoalWeb> goalWebs){
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        ListIterator<GoalWeb> iterator = goalWebs.listIterator(goalWebs.size());
        int[] goalsOffset = new int[goalWebs.size()];
        int i = 0;
        while(iterator.hasPrevious()){
            GoalWeb goalWeb = iterator.previous();
            goalsOffset[i] = goalWeb.getGoalInt(builder);
            i++;
        }

        int vector = GoalsFlat.createGoalsVector(builder, goalsOffset);
        GoalsFlat.startGoals(builder);
        GoalsFlat.addGoals(builder, vector);
        int activities = GoalsFlat.endGoals(builder);

        builder.finish(activities);

        ByteBuffer buf = builder.dataBuffer();
        byte[] array = new byte[buf.remaining()];
        buf.get(array);
        return array;
    }

    public static ArrayList<GoalWeb> deserializeGoals(byte[] bytes){
        ArrayList<GoalWeb> goalWebs = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        GoalsFlat goalsFlatBufferer = GoalsFlat.getRootAsGoals(buf);

        for(int i = 0; i < goalsFlatBufferer.goalsLength(); i++){
            GoalFlat goalFlat = goalsFlatBufferer.goals(i);

            GoalWeb newGoalWeb = new GoalWeb(goalFlat.id(),
                    goalFlat.type(),
                    goalFlat.distance(),
                    goalFlat.duration(),
                    goalFlat.calories(),
                    goalFlat.steps(),
                    goalFlat.fromDate(),
                    goalFlat.toDate(),
                    goalFlat.lastModified());

            goalWebs.add(newGoalWeb);
        }

        return goalWebs;
    }

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
        SportActivitiesFlat.startSportActivities(builder);
        SportActivitiesFlat.addSportActivities(builder, vector);
        int activities = SportActivitiesFlat.endSportActivities(builder);

        builder.finish(activities);

        ByteBuffer buf = builder.dataBuffer();
        byte[] array = new byte[buf.remaining()];
        buf.get(array);
        return array;
    }

    public static ArrayList<SportActivityWithOwner> deserializeSportActivityWithOwners(byte[] bytes){
        ArrayList<SportActivityWithOwner> sportActivities = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        SportActivitiesWithOwnerFlat sportActivitiesBufferer = SportActivitiesWithOwnerFlat.getRootAsSportActivitiesWithOwner(buf);

        for(int i = 0; i < sportActivitiesBufferer.sportActivitiesLength(); i++){
            com.tracker.shared.flatbuf.SportActivityWithOwner sportActivity = sportActivitiesBufferer.sportActivities(i);

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

    public static byte[] serializeWeights(ArrayList<WeightWeb> weightWebs){
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        ListIterator<WeightWeb> iterator = weightWebs.listIterator(weightWebs.size());
        int[] weightsOffset = new int[weightWebs.size()];
        int i = 0;
        while(iterator.hasPrevious()){
            WeightWeb weightWeb = iterator.previous();
            weightsOffset[i] = weightWeb.weightInt(builder);
            i++;
        }

        int vector = WeightsFlat.createWeightsVector(builder, weightsOffset);
        WeightsFlat.startWeights(builder);
        WeightsFlat.addWeights(builder, vector);
        int weightsInt = WeightsFlat.endWeights(builder);

        builder.finish(weightsInt);

        ByteBuffer buf = builder.dataBuffer();
        byte[] array = new byte[buf.remaining()];
        buf.get(array);
        return array;
    }

    public static ArrayList<WeightWeb> deserializeWeights(byte[] bytes){
        ArrayList<WeightWeb> weightWebs = new ArrayList<>();

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        WeightsFlat weightsFlatBufferer = WeightsFlat.getRootAsWeights(buf);

        for(int i = 0; i < weightsFlatBufferer.weightsLength(); i++){
            WeightFlat weightFlat = weightsFlatBufferer.weights(i);
            weightWebs.add(new WeightWeb(weightFlat.weight(), weightFlat.date(), weightFlat.lastModified()));
        }

        return weightWebs;
    }
}
