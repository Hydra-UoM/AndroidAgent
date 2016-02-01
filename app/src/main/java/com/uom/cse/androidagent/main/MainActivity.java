package com.uom.cse.androidagent.main;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uom.cse.androidagent.R;
import com.uom.cse.androidagent.asper.Asper;
import com.uom.cse.androidagent.asper.Data;
import com.uom.cse.androidagent.asper.Event;
import com.uom.cse.androidagent.asper.Generator;
import com.uom.cse.androidagent.asper.Task;
import com.uom.cse.androidagent.asper.Variable;
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

    public static EditText centralNodeIP;
    public static EditText centralNodeport;
    Button registerButton;
    Button startService;
    Button stopService;

    static TextView registerStatus;


    static Handler messageupdateHandler;

    private static Context context;
//    TextView textView;
//    TextView textView2;
//    TextView textView3;
    public static Context getContext() {
        return context;
    }
    public static Handler handler;
    public static Handler handler2;
    public static Handler handler3;
    UsageInfoManager infoManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        centralNodeIP = (EditText)findViewById(R.id.txtIpAddress);
        centralNodeport = (EditText)findViewById(R.id.txtPort);
        registerButton = (Button)findViewById(R.id.btnRegister);
        startService = (Button)findViewById(R.id.btnStartService);
        stopService = (Button)findViewById(R.id.btnStopService);
        registerStatus = (TextView)findViewById(R.id.lblMsg);

        context = this;

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(),AgentService.class);
//                intent.putExtra("ip",centralNodeIP.getText().toString());
//                intent.putExtra("port",centralNodeport.getText().toString());
//                startService(intent);

                infoManager = new UsageInfoManager(context);
                //registerMe();
                //startActivity(new Intent(MainActivity.this, RegisterDevicePop.class));

                //startEsper();

                AndroidAgentServer server = new AndroidAgentServer(infoManager);
                server.start();

                registerDevice(centralNodeIP.getText().toString(), Integer.parseInt(centralNodeport.getText().toString()));
                RegisterDeviceClient.getCommand(getMACAddress(), getIPAddress(), "Android", centralNodeIP.getText().toString(), Integer.parseInt(centralNodeport.getText().toString()));


            }
        });

    }

    public String getMACAddress(){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return macAddress;
    }

    public String getIPAddress(){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String ipAddress = Formatter.formatIpAddress(wInfo.getIpAddress());
        return ipAddress;
    }

    public void registerDevice(String cenralnodeIPAddress, int centralnodePort){

        messageupdateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String aResponse = msg.getData().getString("message");
                MainActivity.registerStatus.setText(aResponse);
            }
        };

        RegisterDeviceClient.registerMeAsync(getMACAddress(), getIPAddress(), "Android", cenralnodeIPAddress, centralnodePort);


    }

    public static void showRegistrationStatus(boolean state){

        Message msgObj = messageupdateHandler.obtainMessage();
        Bundle b = new Bundle();
        String msg;
        if(state){
            msg = "Device Registered";
        }else{
            msg = "Error in Registration";
        }
        b.putString("message", msg);
        msgObj.setData(b);
        messageupdateHandler.sendMessage(msgObj);
    }

    public static void showCommandStatus(boolean state){
        Message msgObj = messageupdateHandler.obtainMessage();
        Bundle b = new Bundle();
        String msg;
        if(state){
            msg = "Command Received";
        }else{
            msg = "Error in get Command";
        }
        b.putString("message", msg);
        msgObj.setData(b);
        messageupdateHandler.sendMessage(msgObj);
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
