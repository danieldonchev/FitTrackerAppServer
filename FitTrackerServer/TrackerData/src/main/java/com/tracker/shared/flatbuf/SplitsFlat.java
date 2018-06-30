// automatically generated by the FlatBuffers compiler, do not modify

package com.tracker.shared.flatbuf;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class SplitsFlat extends Table {
  public static SplitsFlat getRootAsSplits(ByteBuffer _bb) { return getRootAsSplits(_bb, new SplitsFlat()); }
  public static SplitsFlat getRootAsSplits(ByteBuffer _bb, SplitsFlat obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public SplitsFlat __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public SplitFlat splits(int j) { return splits(new SplitFlat(), j); }
  public SplitFlat splits(SplitFlat obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__vector(o) + j * 24, bb) : null; }
  public int splitsLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createSplits(FlatBufferBuilder builder,
      int splitsOffset) {
    builder.startObject(1);
    SplitsFlat.addSplits(builder, splitsOffset);
    return SplitsFlat.endSplits(builder);
  }

  public static void startSplits(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addSplits(FlatBufferBuilder builder, int splitsOffset) { builder.addOffset(0, splitsOffset, 0); }
  public static void startSplitsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(24, numElems, 8); }
  public static int endSplits(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
