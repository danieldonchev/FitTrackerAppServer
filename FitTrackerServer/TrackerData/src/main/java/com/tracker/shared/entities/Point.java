package com.tracker.shared.entities;

import java.util.Objects;

public class Point {

    private double lat;
    private double lon;
    private int elevation;
    private int gpsAccuracy;

    public Point() {
    }

    public Point(double lat, double lon, int elevation, int gpsAccuracy) {
        this.lat = lat;
        this.lon = lon;
        this.elevation = elevation;
        this.gpsAccuracy = gpsAccuracy;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public int getGpsAccuracy() {
        return gpsAccuracy;
    }

    public void setGpsAccuracy(int gpsAccuracy) {
        this.gpsAccuracy = gpsAccuracy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.lat, lat) == 0 &&
                Double.compare(point.lon, lon) == 0 &&
                elevation == point.elevation &&
                gpsAccuracy == point.gpsAccuracy;
    }

    @Override
    public int hashCode() {

        return Objects.hash(lat, lon, elevation, gpsAccuracy);
    }

    @Override
    public String toString() {
        return "Point{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", elevation=" + elevation +
                ", gpsAccuracy=" + gpsAccuracy +
                '}';
    }
}
