package com.tracker.shared.Entities;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.flatbuf.WeightFlat;

import java.nio.ByteBuffer;


public class Weight {

    public double weight;
    public long date;
    public long lastModified;

    public Weight(){}

    public Weight(double weight, long date){
        this.weight = weight;
        this.date = date;
    }

    public Weight(double weight, long date, long lastModified){
        this(weight, date);
        this.lastModified = lastModified;
    }


    public byte[] serialize() {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = weightInt(builder);
        builder.finish(finish);

        ByteBuffer buf = builder.dataBuffer();
        byte[] array = new byte[buf.remaining()];
        buf.get(array);
        return array;
    }


    public Weight deserialize(byte[] bytesRead) {
        ByteBuffer buf = ByteBuffer.wrap(bytesRead);
        WeightFlat weightFlatBufferer = WeightFlat.getRootAsWeight(buf);

        weight = weightFlatBufferer.weight();
        date = weightFlatBufferer.date();
        lastModified = weightFlatBufferer.lastModified();

        return this;
    }

    public int weightInt(FlatBufferBuilder builder){

        WeightFlat.startWeight(builder);
        WeightFlat.addWeight(builder, weight);
        WeightFlat.addDate(builder, date);
        WeightFlat.addLastModified(builder, lastModified);
        return WeightFlat.endWeight(builder);
    }
}
