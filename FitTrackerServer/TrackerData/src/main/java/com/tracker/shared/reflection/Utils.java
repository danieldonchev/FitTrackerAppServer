package com.tracker.shared.reflection;

import java.util.HashSet;
import java.util.Set;

public class Utils {
    private static final Set<Class<?>> WRAPPER_TYPES = getPrimitiveType();

    public static boolean isPrimitiveType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getPrimitiveType()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
}
