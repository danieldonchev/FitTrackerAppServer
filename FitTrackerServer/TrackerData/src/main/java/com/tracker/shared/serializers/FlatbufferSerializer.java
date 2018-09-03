package com.tracker.shared.serializers;

import java.util.ArrayList;

public interface FlatbufferSerializer<T> {

    byte[] serialize(T t);
    T deserialize(byte[] bytes);
    byte[] serializeArray(ArrayList<T> list);
    ArrayList<T> deserializeArray(byte[] bytes);
}
