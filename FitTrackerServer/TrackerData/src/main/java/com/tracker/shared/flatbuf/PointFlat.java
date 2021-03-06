// automatically generated by the FlatBuffers compiler, do not modify

package com.tracker.shared.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class PointFlat extends Struct {
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public PointFlat __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public double lat() { return bb.getDouble(bb_pos + 0); }
  public double lon() { return bb.getDouble(bb_pos + 8); }
  public int elevation() { return bb.getInt(bb_pos + 16); }
  public int gpsAccuracy() { return bb.getInt(bb_pos + 20); }

  public static int createPointFlat(FlatBufferBuilder builder, double lat, double lon, int elevation, int gpsAccuracy) {
    builder.prep(8, 24);
    builder.putInt(gpsAccuracy);
    builder.putInt(elevation);
    builder.putDouble(lon);
    builder.putDouble(lat);
    return builder.offset();
  }
}

