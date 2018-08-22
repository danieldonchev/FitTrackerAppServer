package com.tracker.shared.Entities;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.MarkersFlat;
import com.tracker.shared.flatbuf.PolylineFlat;
import com.tracker.shared.flatbuf.SportActivityMapFlat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;

public class SportActivityMap
{
    private ArrayList<LatLng> polyline;
    private ArrayList<LatLng> markers;

    public SportActivityMap()
    {
        polyline = new ArrayList<>();
        markers = new ArrayList<>();
    }

    public ArrayList<LatLng> getPolyline() {
        return polyline;
    }

    public void setPolyline(ArrayList<LatLng> polyline) {
        this.polyline = polyline;
    }

    public ArrayList<LatLng> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<LatLng> markers) {
        this.markers = markers;
    }


    public byte[] serialize()
    {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int finish = getBufferInt(builder);

        builder.finish(finish);
        return builder.sizedByteArray();
    }


    public SportActivityMap deserialize(byte[] bytesRead)
    {
        ByteBuffer buf = ByteBuffer.wrap(bytesRead);
        SportActivityMapFlat sportActivityMapFlat = SportActivityMapFlat.getRootAsSportActivityMapFlat(buf);

        return deserializeFromFlatBuffMap(sportActivityMapFlat);
    }

    public int getBufferInt(FlatBufferBuilder builder)
    {
        ListIterator<LatLng> polyLineIterator = this.polyline.listIterator(this.polyline.size());
        ListIterator<LatLng> markerIterator = this.markers.listIterator(this.markers.size());

        SportActivityMapFlat.startMarkersVector(builder, this.markers.size());

        while(markerIterator.hasPrevious())
        {
            LatLng latLng = markerIterator.previous();
            MarkersFlat.createMarkersFlat(builder, latLng.latitude, latLng.longitude, 0);
        }

        int markers = builder.endVector();

        SportActivityMapFlat.startPolylineVector(builder, this.polyline.size());

        while(polyLineIterator.hasPrevious())
        {
            LatLng latLng = polyLineIterator.previous();
            PolylineFlat.createPolylineFlat(builder, latLng.latitude, latLng.longitude);
        }

        int polyline = builder.endVector();

        SportActivityMapFlat.startSportActivityMapFlat(builder);
        SportActivityMapFlat.addMarkers(builder, markers);
        SportActivityMapFlat.addPolyline(builder, polyline);

        return SportActivityMapFlat.endSportActivityMapFlat(builder);
    }

    public SportActivityMap deserializeFromFlatBuffMap(SportActivityMapFlat map){
        for(int i = 0; i < map.markersLength(); i++)
        {
            MarkersFlat marker = map.markers(i);
            this.markers.add(new LatLng(marker.lat(), marker.lon()));
        }
        for(int i = 0; i < map.polylineLength(); i++)
        {
            PolylineFlat polylineFlat = map.polyline(i);
            this.polyline.add(new LatLng(polylineFlat.lat(), polylineFlat.lon()));
        }

        return this;
    }
}
