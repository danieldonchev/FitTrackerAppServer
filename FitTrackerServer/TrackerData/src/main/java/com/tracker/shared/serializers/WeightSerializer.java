package com.tracker.shared.serializers;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.entities.WeightWeb;
import com.tracker.shared.flatbuf.WeightFlat;
import com.tracker.shared.flatbuf.WeightsFlat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;

public class WeightSerializer implements FlatbufferSerializer<WeightWeb> {

    @Override
    public byte[] serialize(WeightWeb weightWeb) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = weightInt(builder, weightWeb);
        builder.finish(finish);
        return builder.sizedByteArray();
    }

    @Override
    public WeightWeb deserialize(byte[] bytes) {
        WeightWeb weight = new WeightWeb();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        WeightFlat weightFlatBufferer = WeightFlat.getRootAsWeightFlat(buf);

        weight.setWeight(weightFlatBufferer.weight());
        weight.setDate(weightFlatBufferer.date());
        weight.setLastModified(weightFlatBufferer.lastModified());

        return weight;
    }

    @Override
    public byte[] serializeArray(ArrayList<WeightWeb> list) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        ListIterator<WeightWeb> iterator = list.listIterator(list.size());
        int[] weightsOffset = new int[list.size()];
        int i = 0;
        while(iterator.hasPrevious()){
            WeightWeb weightWeb = iterator.previous();
            weightsOffset[i] = weightWeb.weightInt(builder);
            i++;
        }

        int vector = WeightsFlat.createWeightsVector(builder, weightsOffset);
        WeightsFlat.startWeightsFlat(builder);
        WeightsFlat.addWeights(builder, vector);
        int weightsInt = WeightsFlat.endWeightsFlat(builder);

        builder.finish(weightsInt);
        return builder.sizedByteArray();
    }

    @Override
    public ArrayList<WeightWeb> deserializeArray(byte[] bytes) {
        ArrayList<WeightWeb> weightWebs = new ArrayList<>();

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        WeightsFlat weightsFlatBufferer = WeightsFlat.getRootAsWeightsFlat(buf);

        for(int i = 0; i < weightsFlatBufferer.weightsLength(); i++){
            WeightFlat weightFlat = weightsFlatBufferer.weights(i);
            weightWebs.add(new WeightWeb(weightFlat.weight(), weightFlat.date(), weightFlat.lastModified()));
        }

        return weightWebs;
    }

    public int weightInt(FlatBufferBuilder builder, WeightWeb weight){

        WeightFlat.startWeightFlat(builder);
        WeightFlat.addWeight(builder, weight.getWeight());
        WeightFlat.addDate(builder, weight.getDate());
        WeightFlat.addLastModified(builder, weight.getLastModified());
        return WeightFlat.endWeightFlat(builder);
    }
}
