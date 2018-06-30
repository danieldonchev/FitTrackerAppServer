package com.tracker.shared.reflection;

import com.tracker.shared.Entities.FlatBufferSerializable;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

import static com.tracker.shared.reflection.FlatbufSerializer.mappedClasses;

public class AnnotatedClassMapper {

    public  void mapClasses(String packageName){
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(FlatBufferSerializable.class);

        int classNumber = 1;
        for(Object object : classes){
            mappedClasses.put(object, classNumber);
            int mapp = mappedClasses.get(object);
            classNumber++;

        }

//        GoalFlat goal = new GoalFlat();
//        Class clazz = goal.getClass();
//        int goalNumber = mappedClasses.get(clazz);
        int b = 5;

    }
}
