package com.tracker.shared.Entities;


import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.GoalFlat;

import java.nio.ByteBuffer;
import java.util.UUID;


@FlatBufferSerializable
public class GoalWeb {
        private String id;
        private int type;
        private double distance;
        private long duration;
        private long calories;
        private long steps;
        private long fromDate;
        private long toDate;
        private long lastModified;
        private long lastSync;

    public GoalWeb() {}

    public GoalWeb(String id, int type, double distance, long duration, long calories, long steps, long fromDate, long toDate, long lastModified){
        this.id = id;
        this.type = type;
        this.distance = distance;
        this.duration = duration;
        this.calories = calories;
        this.steps = steps;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.lastModified = lastModified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public byte[] serialize() {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = getGoalInt(builder);
        builder.finish(finish);
        return builder.sizedByteArray();
    }


    public GoalWeb deserialize(byte[] bytesRead) {
        ByteBuffer buf = ByteBuffer.wrap(bytesRead);
        GoalFlat goalFlatBufferer = GoalFlat.getRootAsGoalFlat(buf);

        id = goalFlatBufferer.id();
        type = goalFlatBufferer.type();
        distance = goalFlatBufferer.distance();
        duration = goalFlatBufferer.duration();
        calories = goalFlatBufferer.calories();
        steps = goalFlatBufferer.steps();
        fromDate = goalFlatBufferer.fromDate();
        toDate = goalFlatBufferer.toDate();
        lastModified = goalFlatBufferer.lastModified();

        return this;
    }

    public int getGoalInt(FlatBufferBuilder builder){
        int id = builder.createString(this.id);

        GoalFlat.startGoalFlat(builder);
        GoalFlat.addId(builder, id);
        GoalFlat.addType(builder, type);
        GoalFlat.addDistance(builder, distance);
        GoalFlat.addDuration(builder, duration);
        GoalFlat.addCalories(builder, calories);
        GoalFlat.addSteps(builder, steps);
        GoalFlat.addFromDate(builder, fromDate);
        GoalFlat.addToDate(builder, toDate);
        GoalFlat.addLastModified(builder, lastModified);
        return GoalFlat.endGoalFlat(builder);
    }

    public int getType() {
        return type;
    }

    public double getDistance() {
        return distance;
    }

    public long getDuration() {
        return duration;
    }

    public long getCalories() {
        return calories;
    }

    public long getSteps() {
        return steps;
    }

    public long getFromDate() {
        return fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public long getLastModified() {
        return lastModified;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setCalories(long calories) {
        this.calories = calories;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
