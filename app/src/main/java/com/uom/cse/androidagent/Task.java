package com.uom.cse.androidagent;

import java.util.ArrayList;
import java.util.List;

public class Task
{
    //
    String label;
    //
    List<String> queries;
    //
    List<Event> events;

    public Task(String label, List<String> queries, List<Event> events)
    {
        this.label = label;
        this.queries = queries;
        this.events = events;
    }

    public List<Event> getActiveEvents()
    {
        List<Event> l = new ArrayList<Event>();

        for(Event e : events)
        {
            if(!e.shouldPreload())
            {
                l.add(e);
            }
        }

        return l;
    }


    public List<Event> getPassiveEvents()
    {
        List<Event> l = new ArrayList<Event>();

        for(Event e : events)
        {
            if(e.shouldPreload())
            {
                l.add(e);
            }
        }

        return l;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<String> getQueries()
    {
        return queries;
    }

    public void setQueries(List<String> queries)
    {
        this.queries = queries;
    }

    public List<Event> getEvents()
    {
        return events;
    }

    public void setEvents(List<Event> events)
    {
        this.events = events;
    }

    @Override
    public String toString()
    {
        return label;
    }


}
