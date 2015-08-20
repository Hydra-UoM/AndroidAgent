package com.uom.cse.androidagent;

import java.util.List;

/**
 * Created by Sirojan on 8/6/2015.
 */
public class AsperConfig {

    public void registerEvent(List<Event> events){
        for(Event event : events){
            Asper.addEvent(event);
        }
    }

}
