package com.home24.api;

/**
 * Simple emulator for parameter
 */
public class Argument
{
    private Object serializedValue;

    private Class<?> classReference;

    public Argument(Class<?> classReference, Object value)
    {
        this.classReference  = classReference;
        this.serializedValue = value;
    }

    public Object getValue()
    {
        return this.serializedValue;
    }

    public Class<?> getClassReference()
    {
        return this.classReference;
    }
}
