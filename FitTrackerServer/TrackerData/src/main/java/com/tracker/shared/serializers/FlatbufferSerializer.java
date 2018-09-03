package com.tracker.shared.serializers;

import java.io.Serializable;
import java.util.ArrayList;

public interface FlatbufferSerializer<T> extends Serializable {

    byte[] serialize(T t);
    T deserialize(byte[] bytes);
    byte[] serializeArray(ArrayList<T> list);
    ArrayList<T> deserializeArray(byte[] bytes);
}
