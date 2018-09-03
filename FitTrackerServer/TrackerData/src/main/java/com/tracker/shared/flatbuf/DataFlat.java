// automatically generated by the FlatBuffers compiler, do not modify

package com.tracker.shared.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class DataFlat extends Table {
  public static DataFlat getRootAsDataFlat(ByteBuffer _bb) { return getRootAsDataFlat(_bb, new DataFlat()); }
  public static DataFlat getRootAsDataFlat(ByteBuffer _bb, DataFlat obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public DataFlat __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public double distance() { int o = __offset(4); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public int duration() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int steps() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int cadence() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float avgSpeed() { int o = __offset(12); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float currentSpeed() { int o = __offset(14); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }

  public static int createDataFlat(FlatBufferBuilder builder,
      double distance,
      int duration,
      int steps,
      int cadence,
      float avgSpeed,
      float currentSpeed) {
    builder.startObject(6);
    DataFlat.addDistance(builder, distance);
    DataFlat.addCurrentSpeed(builder, currentSpeed);
    DataFlat.addAvgSpeed(builder, avgSpeed);
    DataFlat.addCadence(builder, cadence);
    DataFlat.addSteps(builder, steps);
    DataFlat.addDuration(builder, duration);
    return DataFlat.endDataFlat(builder);
  }

  public static void startDataFlat(FlatBufferBuilder builder) { builder.startObject(6); }
  public static void addDistance(FlatBufferBuilder builder, double distance) { builder.addDouble(0, distance, 0.0); }
  public static void addDuration(FlatBufferBuilder builder, int duration) { builder.addInt(1, duration, 0); }
  public static void addSteps(FlatBufferBuilder builder, int steps) { builder.addInt(2, steps, 0); }
  public static void addCadence(FlatBufferBuilder builder, int cadence) { builder.addInt(3, cadence, 0); }
  public static void addAvgSpeed(FlatBufferBuilder builder, float avgSpeed) { builder.addFloat(4, avgSpeed, 0.0f); }
  public static void addCurrentSpeed(FlatBufferBuilder builder, float currentSpeed) { builder.addFloat(5, currentSpeed, 0.0f); }
  public static int endDataFlat(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
