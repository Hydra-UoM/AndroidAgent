package com.uom.cse.androidagent;

import java.util.List;

/**
 * Represents a data-item
 * which models and external Event.
 */

public class Event
{
    // Event identifier
    private String name;
    // Collecton of variables
    private List<Variable> variables;
    // Determines wether this event should be preloaded
    private int preload;

    /**
     * Constructor
     *
     * @param name event name
     * @param variables collection of event attriutes
     */
    public Event(String name, List<Variable> variables)
    {
        this.name = name;
        this.variables = variables;
    }

    /**
     * Determines wether or not this
     * event should be pre-loaded.
     *
     * @return true or false
     */
    public boolean shouldPreload()
    {
        if (preload > 0)
            return true;
        else
            return false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Variable> getVariables()
    {
        return variables;
    }

    public void setVariables(List<Variable> variables)
    {
        this.variables = variables;
    }

    public int getPreload()
    {
        return preload;
    }

    public void setPreload(int preload)
    {
        this.preload = preload;
    }

}
