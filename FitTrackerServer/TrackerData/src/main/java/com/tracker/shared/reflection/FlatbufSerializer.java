package com.tracker.shared.reflection;

import com.google.common.collect.ImmutableBiMap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FlatbufSerializer {
    public static Map<Object, Integer> mappedClasses = new HashMap<>();

    public static void init(String packageName){
        AnnotatedClassMapper mapper = new AnnotatedClassMapper();
        mapper.mapClasses(packageName);
    }


}
