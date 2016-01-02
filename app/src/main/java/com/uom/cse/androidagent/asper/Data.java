package com.uom.cse.androidagent.asper;

/**
 * A Data-item representation
 * of a mapped event. Utilized by a worker
 * for engine feeding.
 */

public class Data
{
    // An event name that Esper recognizes
    public String name;
    // The data-payload that represents the event
    public Object[] data;

    /**
     * Constructor
     *
     * @param name
     * @param data
     */
    public Data(String name, Object[] data)
    {
        this.name = name;
        this.data = data;
    }

}
