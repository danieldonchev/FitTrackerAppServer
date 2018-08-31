package com.tracker.shared.entities;

import java.util.ArrayList;
import java.util.UUID;

public class SportActivityWeb extends AbstractWorkout
{
    private UUID id;
    private double distance = 0;
    private long steps;
    private long startTimestamp;
    private long endTimestamp;
    private ArrayList<SplitWeb> splitWebs;
    private SportActivityMap sportActivityMap;
    private long lastModified;

    public SportActivityWeb()
    {
        splitWebs = new ArrayList<>();
        sportActivityMap = new SportActivityMap();
    }

    public SportActivityWeb(String id)
    {
        this.id = UUID.fromString(id);
        splitWebs = new ArrayList<>();
        sportActivityMap = new SportActivityMap();
    }

    private SportActivityWeb(String workout, long duration, int calories)
    {
        super(workout, duration, calories);
        sportActivityMap = new SportActivityMap();
        splitWebs = new ArrayList<>();
    }

    public SportActivityWeb(String id, String workout, long duration, double distance, long steps, int calories, long startTimestamp, long endTimestamp, long lastModified)
    {
        this(workout, duration, calories);
        this.id = UUID.fromString(id);
        this.distance = distance;
        this.steps = steps;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.lastModified = lastModified;
    }

    public SportActivityWeb(String id, String workout, long duration, double distance, long steps, int calories, SportActivityMap sportActivityMap, long startTimestamp,
                            long endTimestamp, long lastModified, ArrayList<SplitWeb> splitWebs)
    {
        this(workout, duration, calories);
        this.id = UUID.fromString(id);
        this.distance = distance;
        this.steps = steps;
        this.sportActivityMap = sportActivityMap;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.lastModified = lastModified;
        this.splitWebs = splitWebs;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public ArrayList<SplitWeb> getSplitWebs() {
        return splitWebs;
    }

    public void setSplitWebs(ArrayList<SplitWeb> splitWebs) {
        this.splitWebs = splitWebs;
    }

    public SportActivityMap getSportActivityMap() {
        return sportActivityMap;
    }

    public void setSportActivityMap(SportActivityMap sportActivityMap) {
        this.sportActivityMap = sportActivityMap;
    }

    public UUID getId() {
        return id;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
