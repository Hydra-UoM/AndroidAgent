package com.uom.cse.androidagent.asper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.espertech.esper.client.EPRuntime;
import com.uom.cse.androidagent.asper.Asper;
import com.uom.cse.androidagent.asper.Data;
import com.uom.cse.androidagent.asper.Settings;

import java.util.ArrayList;

/**
 * Processor / Worker
 * Feeds Asper with a set of events
 */

public class Worker extends Thread
{
    // Local storage of events
    private ArrayList<Data> storage;
    // Work flag/indicator
    private volatile boolean keepWorking = true;
    // Local refrence to the CEP - engine
    private EPRuntime engine;
    private TextView textView;
    private Context context;
    public Handler handler;
    /**
     * Constuctor
     *
     * @param name this thread / workers name
     */
    public Worker(String name,ArrayList<Data> data,Context context, TextView textView,Handler handler)
    {

        super.setName(name);
        storage = data;
        engine = Asper.getRuntime();
        this. context = context;
        this.textView=textView;
        this.handler=handler;
    }

    /**
     * Main run procedure.
     * Initiated upon Thread start.
     * <p/>
     * Ensures local repository fillment,
     * warmup procedure invocation
     * and the actual test invokation.
     */
    public void run()
    {


        try
        {


            // Starts work procedure
            work();



        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e(Settings.TAG, "Worker encountered unexpected exception.");
        }
    }



    /**
     * Performs the actual engine
     * feeding / processing procedure.
     * <p/>
     * Loops continuously through a set of
     * data-items until paused.
     */
    public void work()
    {
        // Current storage index
        int index = 0;
        // Continuity indicator
        keepWorking = true;
        // Storage size
        int size = storage.size();
        int i=0;

        Message msgObj = handler.obtainMessage();
        Bundle b = new Bundle();
        String msg = "One Thread is feeding the events to Asper"+"\n";
        b.putString("message", msg);
        msgObj.setData(b);
        handler.sendMessage(msgObj);


        while (i < size)
        {
            i++;
            // Resets the index if
            // it surpasses the storage size.
            if (index >= size)
                index = 0;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Retrieves the next data-item
            Data e = storage.get(index++);
            // Sends / processes the event
                Message msgObj1 = handler.obtainMessage();
                 Bundle b1 = new Bundle();
                String msg1 ="event: " + e.data[0] + " - " + e.data[4] + " - " + e.data[1] + "\n";

             b1.putString("message", msg1);
             msgObj1.setData(b1);
             handler.sendMessage(msgObj1);
            //Toast.makeText(context, "Sample event: EventId:" + index, Toast.LENGTH_LONG).show();
            engine.sendEvent(e.data.clone(), e.name);

        }
    }



}
