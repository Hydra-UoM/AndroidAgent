package com.uom.cse.androidagent.asper;

import com.uom.cse.androidagent.main.AgentService;
import com.uom.cse.androidagent.main.MainActivity;
import com.uom.cse.androidagent.central_node_services.RegisterDeviceClient;
import com.uom.cse.androidagent.central_node_services.ThriftAgentProcessInfo;
import com.uom.cse.androidagent.eventAdapters.ProcessInfoEventAdapter;
import com.uom.cse.androidagent.info.Processinfo;
import com.uom.cse.androidagent.info.UsageInfoManager;
import com.uom.cse.androidagent.popups.RegisterDevicePop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirojan on 8/6/2015.
 */
public class AsperConfig {

    public static boolean isFeedAllowed;
    public static boolean isWhileCompleted;

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

        String statement = "select distinct timestamp,pid,processName,packageName,mac,avg(cpuUsage) as avgCPU ,avg(ramUsage) as avgRAM ,avg(sentData) as avgSent,avg(receiveData) as avgReceive  from " + ProcessInfoEventAdapter.EVENT_NAME;

        if(timeInterval != 0){
            statement += ".win:time_batch(" + timeInterval * 60 +")";
            //statement += ".win:time_batch(60) group by processName";
        }
        if(!"".equals(process)){
            statement += " where processName in (" + getProcessesToQuery(process) +")";
            //statement += " where name in ('nameone','name')";
        }

        statement += " group by processName having avg(cpuUsage) >= " + cpuUsage + " and avg(ramUsage) >= " + ramUsage + " and avg(sentData) >= " + sentData + " and avg(receiveData) >= " + receiveData;
        //statement += " cpuUsage > " + filter.getCpuUsage() + " and ramUsage > " + filter.getRamUsage() + " and sentData > " + filter.getSentData() + " and receiveData >" + filter.getReceivedData();


        Asper.addEvent(ProcessInfoEventAdapter.getSampleEvent());
        Asper.addQuery(statement, new Listener("Listener-Selectivity"));

        feedEvent(infoManager);
    }

    public static void feedEvent(final UsageInfoManager infoManager){
        //to reset the previous queries and listeners

        isFeedAllowed = true;
        Thread feedingThread = new Thread(){
            @Override
            public void run() {
                while(isFeedAllowed) {
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
                isWhileCompleted = true;

            }
        };

        feedingThread.start();

    }

    public static void stopFeed(){
        isFeedAllowed =false;
        while(!isWhileCompleted){}
        isWhileCompleted = false;
        Asper.reset();
    }

    public static void sendFullInfoWithoutProcessing(final short timeInterval,final UsageInfoManager infoManager){
        isFeedAllowed = true;
        Thread sendingThread = new Thread(){
            @Override
            public void run() {
                while(isFeedAllowed) {
                    List<Processinfo> processinfos = infoManager.getprocessinfo();
                    List<ThriftAgentProcessInfo> processedEventList = new ArrayList<>();

                    for(Processinfo info : processinfos){
                        ThriftAgentProcessInfo processedEvents = new ThriftAgentProcessInfo();
                        processedEvents.setName(info.getProcessName());
                        processedEvents.setPackageName(info.getPackageName());
                        processedEvents.setCpuUsage(info.getCpuUsage());
                        processedEvents.setRamUsage(info.getPrivateMemoryUsage());
                        processedEvents.setSentData(info.getSentData());
                        processedEvents.setReceiveData(info.getReceivedData());
                        processedEvents.setMac(ProcessInfoEventAdapter.getMACAddress());
                        processedEvents.setTimestamp(info.getTimestamp());
                        processedEvents.setType("Android");
                        processedEvents.setPid(info.getPid());
                        processedEventList.add(processedEvents);
                    }
                    RegisterDeviceClient.pushEvents(AgentService.centralNodeIP,
                            AgentService.centralNodePort, processedEventList);

                    try {
                        Thread.sleep(timeInterval * 60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
                isWhileCompleted = true;

            }
        };

        sendingThread.start();
    }

    public static void sendCriticalInfoWithoutProcessing(final short timeInterval,final UsageInfoManager infoManager){
        isFeedAllowed = true;
        Thread sendingThread = new Thread(){
            @Override
            public void run() {
                while(isFeedAllowed) {
                    /*
                    * get only critical events
                    * */
                    List<Processinfo> processinfos = infoManager.getFilteredProcessinfo("10", "100", "");
                    List<ThriftAgentProcessInfo> processedEventList = new ArrayList<>();

                    for(Processinfo info : processinfos){
                        ThriftAgentProcessInfo processedEvents = new ThriftAgentProcessInfo();
                        processedEvents.setName(info.getProcessName());
                        processedEvents.setPackageName(info.getPackageName());
                        processedEvents.setCpuUsage(info.getCpuUsage());
                        processedEvents.setRamUsage(info.getPrivateMemoryUsage());
                        processedEvents.setSentData(info.getSentData());
                        processedEvents.setReceiveData(info.getReceivedData());
                        processedEvents.setMac(ProcessInfoEventAdapter.getMACAddress());
                        processedEvents.setTimestamp(info.getTimestamp());
                        processedEvents.setType("Android");
                        processedEvents.setPid(info.getPid());
                        processedEventList.add(processedEvents);
                    }
                    RegisterDeviceClient.pushEvents(AgentService.centralNodeIP,
                            AgentService.centralNodePort, processedEventList);

                    try {
                        Thread.sleep(timeInterval * 60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
                isWhileCompleted = true;

            }
        };

        sendingThread.start();
    }

    public static void sendCriticalWithProcessing(final short timeInterval,final UsageInfoManager infoManager){


        Asper.reset();

        String statement = "select distinct timestamp,pid,processName,packageName,mac,avg(cpuUsage) as avgCPU ,avg(ramUsage) as avgRAM ,avg(sentData) as avgSent,avg(receiveData) as avgReceive  from " + ProcessInfoEventAdapter.EVENT_NAME;

        if(timeInterval != 0){
            statement += ".win:time_batch(" + timeInterval * 60 +") group by processName";
            //statement += ".win:time_batch(60) group by processName";
        }

        Asper.addEvent(ProcessInfoEventAdapter.getSampleEvent());
        Asper.addQuery(statement, new Listener("Listener-Selectivity"));

        isFeedAllowed = true;
        Thread feedingThread = new Thread(){
            @Override
            public void run() {
                while(isFeedAllowed) {
                    List<Processinfo> processinfos = infoManager.getFilteredProcessinfo("10", "100", "");;

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
                isWhileCompleted = true;

            }
        };

        feedingThread.start();

    }

}
