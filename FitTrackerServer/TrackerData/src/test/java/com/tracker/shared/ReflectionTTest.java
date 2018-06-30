package com.tracker.shared;

import com.tracker.shared.Entities.*;
import com.tracker.shared.reflection.FlatbufSerializer;
import com.tracker.shared.reflection.ReflectionT;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReflectionTTest {



    //@Test
    public void testCreation() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        FlatbufSerializer.init("com.tracker.shared");
        SportActivityMap map = new SportActivityMap();
        map.getPolyline().add(new LatLng(5, 6));
        map.getPolyline().add(new LatLng(5, 6));

        Split split = new Split(1, 50, 50);

        GoalWeb goalWeb = new GoalWeb(UUID.randomUUID(),
                            1,
                            554.42d,
                            180l,
                            50l,
                            300l,
                            1l,
                            2l,
                            1l);
        ReflectionT reflectionT = new ReflectionT(goalWeb);

        byte[] array = reflectionT.serializeObject(split);


        GoalWeb goalWeb1 = new GoalWeb().deserialize(Arrays.copyOfRange(array, 4, array.length));
        SportActivityMap map1 = new SportActivityMap().deserialize(Arrays.copyOfRange(array, 4, array.length));

        Reflections reflections = new Reflections("com.tracker.shared");
//        Set classes = reflections.getSubTypesOf(FlatBufferSerializable.class);
        int b = 5;

    }

}
