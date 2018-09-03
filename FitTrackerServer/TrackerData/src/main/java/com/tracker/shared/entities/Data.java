package com.tracker.shared.entities;

import java.util.Objects;

public class Data {

    private double distance;
    private int duration;
    private int steps;
    private int cadence;
    private float avgSpeed;
    private float currentSpeed;

    public Data() {
    }

    public Data(double distance, int duration, int steps, int cadence, float avgSpeed, float currentSpeed) {
        this.distance = distance;
        this.duration = duration;
        this.steps = steps;
        this.cadence = cadence;
        this.avgSpeed = avgSpeed;
        this.currentSpeed = currentSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCadence() {
        return cadence;
    }

    public void setCadence(int cadence) {
        this.cadence = cadence;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Double.compare(data.distance, distance) == 0 &&
                duration == data.duration &&
                steps == data.steps &&
                cadence == data.cadence &&
                Float.compare(data.avgSpeed, avgSpeed) == 0 &&
                Float.compare(data.currentSpeed, currentSpeed) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(distance, duration, steps, cadence, avgSpeed, currentSpeed);
    }

    @Override
    public String toString() {
        return "Data{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", steps=" + steps +
                ", cadence=" + cadence +
                ", avgSpeed=" + avgSpeed +
                ", currentSpeed=" + currentSpeed +
                '}';
    }
}
