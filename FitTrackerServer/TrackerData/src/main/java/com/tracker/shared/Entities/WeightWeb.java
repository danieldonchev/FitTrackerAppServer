package com.tracker.shared.Entities;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.WeightFlat;

import java.nio.ByteBuffer;


public class WeightWeb {

    public double weight;
    public long date;
    public long lastModified;

    public WeightWeb(){}

    public WeightWeb(double weight, long date){
        this.weight = weight;
        this.date = date;
    }

    public WeightWeb(double weight, long date, long lastModified){
        this(weight, date);
        this.lastModified = lastModified;
    }


    public byte[] serialize() {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = weightInt(builder);
        builder.finish(finish);
        return builder.sizedByteArray();
    }


    public WeightWeb deserialize(byte[] bytesRead) {
        ByteBuffer buf = ByteBuffer.wrap(bytesRead);
        WeightFlat weightFlatBufferer = WeightFlat.getRootAsWeightFlat(buf);

        weight = weightFlatBufferer.weight();
        date = weightFlatBufferer.date();
        lastModified = weightFlatBufferer.lastModified();

        return this;
    }

    public int weightInt(FlatBufferBuilder builder){

        WeightFlat.startWeightFlat(builder);
        WeightFlat.addWeight(builder, weight);
        WeightFlat.addDate(builder, date);
        WeightFlat.addLastModified(builder, lastModified);
        return WeightFlat.endWeightFlat(builder);
    }
}
