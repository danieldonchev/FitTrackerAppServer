package com.tracker.shared.serializers;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.flatbuf.GoalFlat;
import com.tracker.shared.flatbuf.GoalsFlat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.UUID;


public class GoalSerializer implements FlatbufferSerializer<GoalWeb> {


    public byte[] serialize(GoalWeb goalWeb) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = getGoalInt(builder, goalWeb);
        builder.finish(finish);
        return builder.sizedByteArray();
    }


    public GoalWeb deserialize(byte[] bytes) {
        GoalWeb goalWeb = new GoalWeb();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        GoalFlat goalFlatBufferer = GoalFlat.getRootAsGoalFlat(buf);

        goalWeb.setId(UUID.fromString(goalFlatBufferer.id()));
        goalWeb.setType(goalFlatBufferer.type());
        goalWeb.setDistance(goalFlatBufferer.distance());
        goalWeb.setDuration(goalFlatBufferer.duration());
        goalWeb.setCalories(goalFlatBufferer.calories());
        goalWeb.setSteps(goalFlatBufferer.steps());
        goalWeb.setFromDate(goalFlatBufferer.fromDate());
        goalWeb.setToDate(goalFlatBufferer.toDate());
        goalWeb.setLastModified(goalFlatBufferer.lastModified());

        return goalWeb;
    }


    public byte[] serializeArray(ArrayList<GoalWeb> list) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        ListIterator<GoalWeb> iterator = list.listIterator(list.size());
        int[] goalsOffset = new int[list.size()];
        int i = 0;
        while(iterator.hasPrevious()){
            GoalWeb goalWeb = iterator.previous();
            goalsOffset[i] = getGoalInt(builder, goalWeb);
            i++;
        }

        int vector = GoalsFlat.createGoalsVector(builder, goalsOffset);
        GoalsFlat.startGoalsFlat(builder);
        GoalsFlat.addGoals(builder, vector);
        int activities = GoalsFlat.endGoalsFlat(builder);

        builder.finish(activities);
        return builder.sizedByteArray();
    }


    public ArrayList<GoalWeb> deserializeArray(byte[] bytes) {
        ArrayList<GoalWeb> goalWebs = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        GoalsFlat goalsFlatBufferer = GoalsFlat.getRootAsGoalsFlat(buf);

        for(int i = 0; i < goalsFlatBufferer.goalsLength(); i++) {
            GoalFlat goalFlat = goalsFlatBufferer.goals(i);

            GoalWeb newGoalWeb = new GoalWeb(UUID.fromString(goalFlat.id()),
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

    public int getGoalInt(FlatBufferBuilder builder, GoalWeb goal){
        int id = builder.createString(goal.getId().toString());

        GoalFlat.startGoalFlat(builder);
        GoalFlat.addId(builder, id);
        GoalFlat.addType(builder, goal.getType());
        GoalFlat.addDistance(builder, goal.getDistance());
        GoalFlat.addDuration(builder, goal.getDuration());
        GoalFlat.addCalories(builder, goal.getCalories());
        GoalFlat.addSteps(builder, goal.getSteps());
        GoalFlat.addFromDate(builder, goal.getFromDate());
        GoalFlat.addToDate(builder, goal.getToDate());
        GoalFlat.addLastModified(builder, goal.getLastModified());
        return GoalFlat.endGoalFlat(builder);
    }
}
