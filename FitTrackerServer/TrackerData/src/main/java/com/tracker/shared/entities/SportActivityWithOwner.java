package com.tracker.shared.entities;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.PolylineFlat;
import com.tracker.shared.flatbuf.SportActivityFlat;

import java.nio.ByteBuffer;

public class SportActivityWithOwner extends AbstractWorkout{

    private String activityID;
    private String userID;
    private String name;
    private double distance = 0;
    private long steps;
    private long startTimestamp;
    private long endTimestamp;
    private LatLng latLng;
    private byte[] profilePic;

    public SportActivityWithOwner(){}



    public byte[] serialize() {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = getSportActivityInt(builder);

        builder.finish(finish);
        return builder.sizedByteArray();
    }


    public Object deserialize(byte[] bytesRead) {
        ByteBuffer buf = ByteBuffer.wrap(bytesRead);
        com.tracker.shared.flatbuf.SportActivityWithOwner sportActivityWithOwner = com.tracker.shared.flatbuf.SportActivityWithOwner.getRootAsSportActivityWithOwner(buf);

        this.name = sportActivityWithOwner.name();
        this.userID = sportActivityWithOwner.userId();
        this.activityID = sportActivityWithOwner.activityId();
        this.workout = sportActivityWithOwner.activity();
        this.startTimestamp = sportActivityWithOwner.startTimestamp();
        this.endTimestamp = sportActivityWithOwner.endTimestamp();
        this.distance = sportActivityWithOwner.distance();
        this.duration = sportActivityWithOwner.duration();
        this.steps = sportActivityWithOwner.steps();
        PolylineFlat polylineFlat = sportActivityWithOwner.startPoint();
        latLng = new LatLng(polylineFlat.lat(), polylineFlat.lon());
        ByteBuffer imgBuffer = sportActivityWithOwner.profilePicAsByteBuffer();
        if(imgBuffer != null){
            byte[] b = new byte[imgBuffer.remaining()];
            imgBuffer.get(b);
            profilePic = b;
        }

        return this;
    }

    public int getSportActivityInt(FlatBufferBuilder builder){
        int activityString = builder.createString(workout);
        int nameString = builder.createString(name);
        int idString = builder.createString(userID);
        int activityIDString = builder.createString(activityID);
        int profPic = 0;
        if(profilePic != null){
            profPic = builder.createByteVector(profilePic);
        }

        SportActivityFlat.startSportActivityFlat(builder);

        com.tracker.shared.flatbuf.SportActivityWithOwner.addActivity(builder, activityString);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addName(builder, nameString);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addActivityId(builder, activityIDString);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addUserId(builder, idString);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addStartTimestamp(builder, startTimestamp);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addEndTimestamp(builder, endTimestamp);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addDuration(builder, duration);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addDistance(builder, distance);
        com.tracker.shared.flatbuf.SportActivityWithOwner.addStartPoint(builder, PolylineFlat.createPolylineFlat(builder, latLng.latitude, latLng.longitude));
        com.tracker.shared.flatbuf.SportActivityWithOwner.addProfilePic(builder, profPic);

        return com.tracker.shared.flatbuf.SportActivityWithOwner.endSportActivityWithOwner(builder);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
