package com.tracker.shared.Entities;

public class SplitWeb
{
    private int id;
    private long duration;
    private double distance;

    public SplitWeb(int id) {
        this.id = id;
    }

    public SplitWeb(int id, long duration, double distance) {
        this(id);
        this.duration = duration;
        this.distance = distance;
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

    public int getId() {
        return id;
    }
}
