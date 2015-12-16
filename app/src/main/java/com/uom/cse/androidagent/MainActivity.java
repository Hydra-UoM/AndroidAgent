package com.uom.cse.androidagent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.uom.cse.androidagent.central_node_services.RegisterDeviceClient;
import com.uom.cse.androidagent.eventAdapters.CPUUsageInfoEventAdapter;
import com.uom.cse.androidagent.eventAdapters.RAMUsageInfoEventAdapter;
import com.uom.cse.androidagent.info.CPUUsageInfo;
import com.uom.cse.androidagent.info.RAMUsageInfo;
import com.uom.cse.androidagent.info.UsageInfoManager;
import com.uom.cse.androidagent.popups.RegisterDevicePop;
import com.uom.cse.androidagent.thriftServer.AndroidAgentServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity {

    private static Context context;
    TextView textView;
    TextView textView2;
    TextView textView3;
    public static Context getContext() {
        return context;
    }
    public static Handler handler;
    public static Handler handler2;
    public static Handler handler3;
    private Event generateEvent(){
        //generate event between with event id between 4000 - 4010
        ArrayList<Number> range = new ArrayList<>();
        range.add(10);
        range.add(25);
        Variable eventId = new Variable("Usage",range);
        Variable description = new Variable("Description","This is a sample event");

        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(eventId);
        variables.add(description);

        Event event = new Event("CPU_Usage",variables);

        return event;
    }

    private List<String> generateQueries(){
        List<String> queries = new ArrayList<String>();
        queries.add("insert into combinedEvent(avg_ram,avg_cpu) select avg( A." + RAMUsageInfo.VARIABLE_SHARED_MEMORY_USAGE + "), " +
                " avg(B." + CPUUsageInfo.VARIABLE_CPU_USAGE + ") from "
                + RAMUsageInfoEventAdapter.EVENT_NAME + ".win:time_batch(60) A, " + CPUUsageInfoEventAdapter.EVENT_NAME + ".win:time_batch(60) B " +
                "where A." + RAMUsageInfo.VARIABLE_APPLICATION_LABEL +" = 'Android System' and B." + CPUUsageInfo.VARIABLE_APPLICATION_LABEL +" = 'Android System'");

        return queries;
    }

    private Task generateTask(){

        Event event = generateEvent();

        List<Event> events = new ArrayList<Event>();
        events.add(event);

        Task task = new Task("aggregation task",generateQueries(),events);

        return task;
    }


    //generate the events data
    private ArrayList<Data> generateData()
    {
        //generate the task with the events and queries
        Task task =  generateTask();

        ArrayList<Data> collection = new ArrayList();
        for (Event event : task.getEvents())
        {
            int count = 1000;
            collection.addAll(Generator.make(count, event));
        }
        Collections.shuffle(collection);

        return collection;
    }

    private void initiateAsper(){

        Asper.addEvent(RAMUsageInfoEventAdapter.getSampleEvent());
        Asper.addEvent(CPUUsageInfoEventAdapter.getSampleEvent());
        List<String> queries = generateQueries();
        // Register all queries
        Asper.addQuery("Selectivity", queries.get(0), getContext());

    }

    private void registerMe(){

    }



    UsageInfoManager infoManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.myText);
        textView2 = (TextView)findViewById(R.id.myText2);
        textView3 = (TextView)findViewById(R.id.myText3);
        context = this;

        infoManager = new UsageInfoManager(context);
        //registerMe();
        startActivity(new Intent(MainActivity.this, RegisterDevicePop.class));

        startEsper();

        AndroidAgentServer server = new AndroidAgentServer(infoManager);
        server.start();

        AsperConfig.AsperQueryBuilder((short)0, (short)0, (short)0, (short)0,(short)0,"",infoManager );
    }

    private void startEsper(){
        //initiate the asper environment
        initiateAsper();
        // get the event collection
        ArrayList<Data> eventCollection = generateData();
        //start the worker to feed the event the Asper



        handler = new Handler() {
            int count = 0;
            public void handleMessage(Message msg) {
                if( count % 10 == 0 ) {
                    textView.setText("");
                }
                String aResponse = msg.getData().getString("message");
                textView.setText(textView.getText() + aResponse);
                count++;
            }
        };

        handler2 = new Handler() {

            public void handleMessage(Message msg) {

                String aResponse = msg.getData().getString("message");
                textView2.setText( textView2.getText() + aResponse);

            }
        };

        handler3 = new Handler() {

            public void handleMessage(Message msg) {

                String aResponse = msg.getData().getString("message");
                textView3.setText(aResponse);

            }
        };

//        UsageInfoManager infoManager = new UsageInfoManager(context){
//
//            ArrayList<Data> collection = new ArrayList<Data>();
//            @Override
//            public void RAMUsageCallBack(List<RAMUsageInfo> RAMUsageList) {
//
//                for(RAMUsageInfo info : RAMUsageList){
//                    Event event = new RAMUsageInfoEventAdapter(info);
//                    collection.add(Generator.make(event));
//                }
//
//                Worker worker1 =new Worker("Worker 1",collection,getContext(),textView, handler);
//                worker1.work();
//            }
//
//        };
//        infoManager.setRAMThreadInterval(100);
//        infoManager.startMonitoringRAMUsage();
//        Worker worker1 =new Worker("Worker 1",eventCollection,getContext(),textView, handler);
//        worker1.start();

        /*

        Thread feedingThread = new Thread(){
            @Override
            public void run() {
                while(true) {
                    List<CPUUsageInfo> cpuUsageInfo = infoManager.getCPUUsageInfo();

                    ArrayList<Data> collection = new ArrayList<Data>();
                    for(CPUUsageInfo info : cpuUsageInfo){
                        //if("Facebook".equals(info.getApplicationLabel())) {
                        Event event = new CPUUsageInfoEventAdapter(info);
                        collection.add(Generator.make(event));
                        //}
                    }

                    Worker worker1 =new Worker("Worker 1",collection,getContext(),textView, handler);
                    worker1.work();
                    collection.clear();
                }
            }
        };

        feedingThread.start();

        Thread feedingThread2 = new Thread(){
            @Override
            public void run() {
                while(true) {
                    List<RAMUsageInfo> ramUsageInfo = infoManager.getRAMUsageInfo();

                    ArrayList<Data> collection = new ArrayList<Data>();

                    for (RAMUsageInfo info : ramUsageInfo){
                        //if("Facebook".equals(info.getApplicationLabel())) {
                        Event event = new RAMUsageInfoEventAdapter(info);
                        collection.add(Generator.make(event));
                        //}
                    }
                    Worker worker1 =new Worker("Worker 1",collection,getContext(),textView, handler);
                    worker1.work();
                    collection.clear();
                }
            }
        };

        feedingThread2.start();

        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
