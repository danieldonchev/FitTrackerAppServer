package com.tracker.shared.serializers;

import com.tracker.shared.entities.GoalWeb;

import java.util.ArrayList;

public class Serializer<T> implements FlatbufferSerializer<T> {

    @Override
    public byte[] serialize(T t) {
        if(t instanceof GoalWeb){
            return new byte[1];
        }
        return new byte[0];
    }

    @Override
    public T deserialize(byte[] bytes) {
        return null;
    }

    @Override
    public byte[] serializeArray(ArrayList<T> list) {
        return new byte[0];
    }

    @Override
    public ArrayList<T> deserializeArray(byte[] bytes) {
        return null;
    }
}
