package com.tracker.shared.Entities;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.UUID;

@FlatBufferSerializable
public class SportActivity extends AbstractWorkout
{
    private UUID id;
    private double distance = 0;
    private long steps;
    private long startTimestamp;
    private long endTimestamp;
    private ArrayList<Split> splits;
    private SportActivityMap sportActivityMap;
    private long lastModified;

    public SportActivity()
    {
        splits = new ArrayList<>();
        sportActivityMap = new SportActivityMap();
    }

    public SportActivity(UUID id)
    {
        this.id = id;
        splits = new ArrayList<>();
        sportActivityMap = new SportActivityMap();
    }

    private SportActivity(String workout, long duration, int calories, int type)
    {
        super(workout, duration, calories, type);
        sportActivityMap = new SportActivityMap();
        splits = new ArrayList<>();
    }

    public SportActivity(UUID id, String workout, long duration, double distance, long steps, int calories, long startTimestamp, long endTimestamp, int type, long lastModified)
    {
        this(workout, duration, calories, type);
        this.id = id;
        this.distance = distance;
        this.steps = steps;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.lastModified = lastModified;
    }

    public SportActivity(UUID id, String workout, long duration, double distance, long steps, int calories, SportActivityMap sportActivityMap, long startTimestamp,
                         long endTimestamp, int type, long lastModified, ArrayList<Split> splits)
    {
        this(workout, duration, calories, type);
        this.id = id;
        this.distance = distance;
        this.steps = steps;
        this.sportActivityMap = sportActivityMap;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.lastModified = lastModified;
        this.splits = splits;
    }


    public byte[] serialize() {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = getSportActivityInt(builder);

        builder.finish(finish);

        ByteBuffer buf = builder.dataBuffer();
        byte[] array = new byte[buf.remaining()];
        buf.get(array);
        return array;
    }


    public SportActivity deserialize(byte[] bytesRead) {
        ByteBuffer buf = ByteBuffer.wrap(bytesRead);
        SportActivityFlat sportActivityFlatBufferer = SportActivityFlat.getRootAsSportActivity(buf);

        this.id = UUID.fromString(sportActivityFlatBufferer.id());
        this.workout = sportActivityFlatBufferer.activity();
        this.startTimestamp = sportActivityFlatBufferer.startTimestamp();
        this.endTimestamp = sportActivityFlatBufferer.endTimestamp();
        this.distance = sportActivityFlatBufferer.distance();
        this.duration = sportActivityFlatBufferer.duration();
        this.steps = sportActivityFlatBufferer.steps();
        this.calories = sportActivityFlatBufferer.calories();
        this.type = sportActivityFlatBufferer.type();
        this.lastModified = sportActivityFlatBufferer.lastModified();


        if(sportActivityFlatBufferer.sportActivityMap() != null){
            for(int i = 0; i < sportActivityFlatBufferer.sportActivityMap().markersLength(); i++)
            {
                MarkersFlat marker = sportActivityFlatBufferer.sportActivityMap().markers(i);
                sportActivityMap.getMarkers().add(new LatLng(marker.lat(), marker.lon()));
            }
            for(int i = 0; i < sportActivityFlatBufferer.sportActivityMap().polylineLength(); i++)
            {
                PolylineFlat polylineFlat = sportActivityFlatBufferer.sportActivityMap().polyline(i);
                sportActivityMap.getPolyline().add(new LatLng(polylineFlat.lat(), polylineFlat.lon()));
            }
        }
        if(sportActivityFlatBufferer.splits() != null){
            for(int i = 0; i < sportActivityFlatBufferer.splits().splitsLength(); i++)
            {
                SplitFlat splitFlat = sportActivityFlatBufferer.splits().splits(i);
                splits.add(new Split(splitFlat.id(), splitFlat.duration(), splitFlat.distance()));
            }
        }

        return this;
    }

    public int getSportActivityInt(FlatBufferBuilder builder){
        int idString = builder.createString(id.toString());
        int activityString = builder.createString(workout);

        int cardioMapInt = 0;
        if(this.sportActivityMap != null){
            cardioMapInt = this.sportActivityMap.getBufferInt(builder);
        }
        int splits = getSplitsBufferInt(builder);

        SportActivityFlat.startSportActivity(builder);
        SportActivityFlat.addId(builder, idString);
        SportActivityFlat.addActivity(builder, activityString);
        SportActivityFlat.addSplits(builder, splits);
        SportActivityFlat.addSportActivityMap(builder, cardioMapInt);
        SportActivityFlat.addStartTimestamp(builder, startTimestamp);
        SportActivityFlat.addEndTimestamp(builder, endTimestamp);
        SportActivityFlat.addDuration(builder, duration);
        SportActivityFlat.addDistance(builder, distance);
        SportActivityFlat.addSteps(builder, steps);
        SportActivityFlat.addCalories(builder, calories);
        SportActivityFlat.addType(builder, type);
        SportActivityFlat.addLastModified(builder, lastModified);

        return SportActivityFlat.endSportActivity(builder);
    }

    public int getSplitsBufferInt(FlatBufferBuilder builder)
    {
        if(splits != null){
            ListIterator<Split> splitListIterator = splits.listIterator(splits.size());

            SplitsFlat.startSplitsVector(builder, splits.size());

            while(splitListIterator.hasPrevious())
            {
                Split split = splitListIterator.previous();
                SplitFlat.createSplit(builder, split.getId(), split.getDistance(), split.getDuration());
            }

            int splits = builder.endVector();

            SplitsFlat.startSplits(builder);
            SplitsFlat.addSplits(builder, splits);
            return SplitsFlat.endSplits(builder);
        }


        return 0;
    }

    public ArrayList<Split> getSplitsFromFlatBuffSplits(SplitsFlat flatBuffSplitsFlat){
        if(flatBuffSplitsFlat != null){
            for(int i = 0; i < flatBuffSplitsFlat.splitsLength(); i++)
            {
                SplitFlat splitFlat = flatBuffSplitsFlat.splits(i);
                this.splits.add(new Split(splitFlat.id(), splitFlat.duration(), splitFlat.distance()));
            }
        }
        return splits;
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

    public ArrayList<Split> getSplits() {
        return splits;
    }

    public void setSplits(ArrayList<Split> splits) {
        this.splits = splits;
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