package com.tracker.shared.entities;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SportActivityWeb {

    private UUID id;
    private String activity;
    private int type;
    private int calories;
    private int totalElevation;
    private int totalDenivelation;
    private long startTimestamp;
    private long endTimestamp;
    private ArrayList<Point> points;
    private ArrayList<Data> datas;
    private ArrayList<Split> splits;
    private long lastModified;

    public SportActivityWeb() {
    }

    public double getDistance(){
        return datas.get(datas.size() - 1).getDistance();
    }

    public int getDurationSeconds(){
        return (int)(endTimestamp - startTimestamp) / 1000;
    }

    public int getCadence(){
        return getSteps() * 60 / getDurationSeconds();
    }

    public int getSteps(){
        return datas.get(datas.size() - 1).getSteps();
    }

    public double getAvgSpeed(){
        return getDistance() / getDurationSeconds();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getTotalElevation() {
        return totalElevation;
    }

    public void setTotalElevation(int totalElevation) {
        this.totalElevation = totalElevation;
    }

    public int getTotalDenivelation() {
        return totalDenivelation;
    }

    public void setTotalDenivelation(int totalDenivelation) {
        this.totalDenivelation = totalDenivelation;
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

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Data> datas) {
        this.datas = datas;
    }

    public ArrayList<Split> getSplits() {
        return splits;
    }

    public void setSplits(ArrayList<Split> splits) {
        this.splits = splits;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportActivityWeb that = (SportActivityWeb) o;
        return type == that.type &&
                calories == that.calories &&
                totalElevation == that.totalElevation &&
                totalDenivelation == that.totalDenivelation &&
                startTimestamp == that.startTimestamp &&
                endTimestamp == that.endTimestamp &&
                Objects.equals(id, that.id) &&
                Objects.equals(activity, that.activity) &&
                Objects.equals(points, that.points) &&
                Objects.equals(datas, that.datas) &&
                Objects.equals(splits, that.splits);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, activity, type, calories, totalElevation, totalDenivelation, startTimestamp, endTimestamp, points, datas, splits);
    }

    @Override
    public String toString() {
        return "SportActivityWeb{" +
                "id=" + id +
                ", activity='" + activity + '\'' +
                ", type=" + type +
                ", calories=" + calories +
                ", totalElevation=" + totalElevation +
                ", totalDenivelation=" + totalDenivelation +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", points=" + points +
                ", datas=" + datas +
                ", splits=" + splits +
                '}';
    }
}
