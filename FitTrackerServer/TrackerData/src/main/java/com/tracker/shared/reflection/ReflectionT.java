package com.tracker.shared.reflection;

import com.google.flatbuffers.FlatBufferBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.stream.Collectors;

import static com.tracker.shared.reflection.FlatbufSerializer.mappedClasses;

public class ReflectionT {

    private Object object;

    public ReflectionT(Object object) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        this.object = object;
    }

    // Get class fields
    public Field[] getClassFields(Object object) throws ClassNotFoundException {
        Class clazz = Class.forName(object.getClass().getName());
        Field[] fields = clazz.getDeclaredFields();

        return fields;
    }

    // Get methods to be invoked to serialize the object
    public ArrayList<Method> getInvokingMethods(Object flatbufObject) throws NoSuchMethodException, ClassNotFoundException {
        ArrayList<Method> methods = new ArrayList<>();
        for(Field field : getClassFields(this.object)){
            String fieldName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            String methodName = "add" + fieldName;
            Class<?>[] args = null;
            for(Method method : flatbufObject.getClass().getDeclaredMethods()){
                if(method.getName().equals(methodName)){
                    args = method.getParameterTypes();
                }
            }
            methods.add(flatbufObject.getClass().getDeclaredMethod(methodName, args));
        }
        return methods;
    }

    public HashMap<Integer, Object> getMethodsForInvocation(ArrayList<Method> methods, Field[] fields, FlatBufferBuilder builder) throws ClassNotFoundException, IllegalAccessException {
        HashMap<Integer, Object> hashMap = new HashMap<>();
        for(int i = 0; i < methods.size(); i++){
            fields[i].setAccessible(true);
            Object val = fields[i].get(object);
            Class valClazz = Class.forName(val.getClass().getName());
            Object ob = (valClazz.cast(val));
            if (ob instanceof String) {
                int intFromString = builder.createString((String) ob);
                hashMap.put(i, intFromString);
            } else if(!Utils.isPrimitiveType(ob.getClass())){

            } else {
                hashMap.put(i, ob);
            }
        }
        return hashMap;
    }


    public byte[] serializeObject(Object object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = getObjectInt(builder, object);

        builder.finish(finish);
        int classInt = mappedClasses.get(object.getClass());
        builder.putInt(classInt);

        byte[] array = builder.sizedByteArray();
        byte[] subArray = Arrays.copyOfRange(array, 0, 4);
        ByteBuffer wrapped = ByteBuffer.wrap(subArray);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        int num = wrapped.getInt();

        int b = 5;

        return builder.sizedByteArray();
    }

    public int getObjectInt(FlatBufferBuilder builder, Object object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException {
        Class clazz = Class.forName(object.getClass().getName());
        Field[] fields = clazz.getDeclaredFields();

        //serialization
        String classSimpleName = object.getClass().getSimpleName();
        String className = "." + classSimpleName;

        Object instance = Class.forName("com.tracker.shared.flatbuf" + className).getDeclaredConstructor().newInstance();
        Method startMethod = instance.getClass().getDeclaredMethod("start" + classSimpleName, FlatBufferBuilder.class);
        Method endMethod = instance.getClass().getDeclaredMethod("end" + classSimpleName, FlatBufferBuilder.class);

        ArrayList<Method> methods = getInvokingMethods(instance);

        HashMap<Integer, Object> hashMap = getMethodsForInvocation(methods, fields, builder);

        startMethod.invoke(instance, builder);

        for (Map.Entry<Integer, Object> entry : hashMap.entrySet()) {
            methods.get(entry.getKey()).invoke(instance, builder, entry.getValue());
        }
        Object intVal = endMethod.invoke(instance, builder);
        int finish = (Integer) intVal;

        return finish;
    }

    public Object getSerializedClassType(byte[] array){
        byte[] subArray = Arrays.copyOfRange(array, 0, 4);
        ByteBuffer wrapped = ByteBuffer.wrap(subArray);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        int num = wrapped.getInt();
        Object object = mappedClasses.entrySet()
                .stream().filter(entry -> Objects.equals(entry.getValue(), num))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        return object;
    }



    public void deserialize(byte[] bytesRead){
        ByteBuffer buf = ByteBuffer.wrap(bytesRead);

    }

}
