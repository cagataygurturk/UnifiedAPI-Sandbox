package com.home24.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * I know this array-list to array sucks, I should come up with the proper design
 */
public class Wrapper
{
    private Loader loader;

    public Wrapper(Loader loader)
    {
        this.loader = loader;
    }

    /**
     * This will call a method on an instance
     */
    public Object callMethod(Object instance, String methodName, Argument... arguments) throws Exception
    {
        Class[]  types  = this.getMethodArgumentsPrototype(arguments);
        Object[] values = this.getMethodArgumentsValues(arguments);
        Method method = instance.getClass().getDeclaredMethod(methodName, types);

        return method.invoke(instance, values);
    }

    /**
     * This will call a method on class name: first instantiate a class and then invoke a call on the instance
     * Note that if default class constructor needs parameters, this will raise an exception
     */
    public Object callMethod(String className, String methodName, Argument... arguments) throws Exception
    {
        Class<?> classRef = this.getClassReference(className);
        Object instance   = classRef.newInstance();

        return this.callMethod(instance, methodName, arguments);
    }

    private Class[] getMethodArgumentsPrototype(Argument... arguments)
    {
        Class[] classArgs = new Class[arguments.length];
        for (int index = 0; index < classArgs.length; index++) {
            classArgs[index] = arguments[index].getClassReference();
        }

        return classArgs;
    }

    private Object[] getMethodArgumentsValues(Argument... arguments)
    {
        Object[] objectArgs = new Object[arguments.length];
        for (int index = 0; index < objectArgs.length; index++) {
            objectArgs[index] = arguments[index].getValue();
        }

        return objectArgs;
    }

    private Class<?> getClassReference(String className) throws ClassNotFoundException
    {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException exception) {
            this.loader.load(className);
            return Class.forName(className);
        }
    }
}
