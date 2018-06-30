// automatically generated by the FlatBuffers compiler, do not modify

package com.tracker.shared.flatbuf;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Struct;

import java.nio.ByteBuffer;

@SuppressWarnings("unused")
public final class SplitFlat extends Struct {
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public SplitFlat __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int id() { return bb.getInt(bb_pos + 0); }
  public double distance() { return bb.getDouble(bb_pos + 8); }
  public long duration() { return bb.getLong(bb_pos + 16); }

  public static int createSplit(FlatBufferBuilder builder, int id, double distance, long duration) {
    builder.prep(8, 24);
    builder.putLong(duration);
    builder.putDouble(distance);
    builder.pad(4);
    builder.putInt(id);
    return builder.offset();
  }
}
