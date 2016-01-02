package com.uom.cse.androidagent.asper;

import java.util.ArrayList;

public class Variable<T>
{
    //
    String name;
    //
    T value;
    //
    ArrayList<Number> range;

    public Variable(String name, T value)
    {
        this.name = name;
        this.value = value;
    }

    public Variable(String name, ArrayList<Number> range)
    {
        this.name = name;
        this.range = range;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public ArrayList<Number> getRange()
    {
        return range;
    }

    public void setRange(ArrayList<Number> range)
    {
        this.range = range;
    }

    public boolean hasValue()
    {
        if (value != null)
            return true;
        else
            return false;
    }

    public boolean hasRange()
    {
        if (range != null)
            return true;
        else
            return false;
    }
}
