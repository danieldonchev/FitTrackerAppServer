package com.tracker.shared;

public interface FlatBufferSerializable<T>
{
    byte[] serialize();
    T deserialize(byte[] bytesRead);
}
