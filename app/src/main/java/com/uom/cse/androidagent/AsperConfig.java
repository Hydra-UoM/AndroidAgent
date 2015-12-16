package com.uom.cse.androidagent;

import com.uom.cse.androidagent.eventAdapters.CPUUsageInfoEventAdapter;
import com.uom.cse.androidagent.eventAdapters.ProcessInfoEventAdapter;
import com.uom.cse.androidagent.info.CPUUsageInfo;
import com.uom.cse.androidagent.info.Processinfo;
import com.uom.cse.androidagent.info.UsageInfoManager;

import java.util.ArrayList;
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

    public static String getProcessesToQuery(String process){
        String []processes = process.split(",");
        String processString = "";
        for (String string : processes) {
            string = "'" + string + "'";
            processString += string + ",";
        }
        return processString.substring(0,processString.length()-1);
    }

    public static void AsperQueryBuilder(short cpuUsage,short ramUsage,short receiveData,short sentData,short timeInterval,String process,UsageInfoManager infoManager){

        Asper.reset();

        String statement = "select processName,mac,avg(cpuUsage) as avgCPU ,avg(ramUsage) as avgRAM ,avg(sentData) as avgSent,avg(receiveData) as avgReceive  from " + ProcessInfoEventAdapter.EVENT_NAME;

        if(timeInterval != 0){
            statement += ".win:time_batch(" + timeInterval * 60 +"sec) group by processName";
            //statement += ".win:time_batch(10sec)";
        }
        if(process != ""){
            statement += " where processName in (" + getProcessesToQuery(process) +") and";
            //statement += " where name in ('nameone','name')";
        }

        statement += " having avg(cpuUsage) > " + cpuUsage + " and avg(ramUsage) > " + ramUsage + " and avg(sentData) > " + sentData + " and avg(receiveData) >" + receiveData;
        //statement += " cpuUsage > " + filter.getCpuUsage() + " and ramUsage > " + filter.getRamUsage() + " and sentData > " + filter.getSentData() + " and receiveData >" + filter.getReceivedData();


        Asper.addEvent(ProcessInfoEventAdapter.getSampleEvent());
        Asper.addQuery("Selectivity", statement , MainActivity.getContext());

        feedEvent(infoManager);
    }

    public static void feedEvent(final UsageInfoManager infoManager){
        //to reset the previous queries and listeners


        Thread feedingThread = new Thread(){
            @Override
            public void run() {
                while(true) {
                    List<Processinfo> processinfos = infoManager.getprocessinfo();

                    ArrayList<Data> collection = new ArrayList<Data>();

                    for(Processinfo info : processinfos){
                        //if("Facebook".equals(info.getApplicationLabel())) {
                        Event event = new ProcessInfoEventAdapter(info);
                        collection.add(Generator.make(event));
                        //}
                    }

                    for(Data data:collection){
                        Asper.getRuntime().sendEvent(data.data.clone(), data.name);
                    }

                    collection.clear();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        };

        feedingThread.start();

    }

}
