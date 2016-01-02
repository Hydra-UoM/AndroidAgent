package com.uom.cse.androidagent.popups;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uom.cse.androidagent.R;
import com.uom.cse.androidagent.central_node_services.RegisterDeviceClient;

/**
 * Created by Nirushan on 11/2/2015.
 */
public class RegisterDevicePop extends Activity{
    public static EditText centralNodeIP;
    public static EditText centralNodeport;
    Button registerButton;
    static TextView registerStatus;
    /*
    static Handler messageupdateHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_device_pop);

        centralNodeIP = (EditText)findViewById(R.id.txtIpAddress);
        centralNodeport = (EditText)findViewById(R.id.txtPort);
        registerButton = (Button)findViewById(R.id.btnRegister);
        registerStatus = (TextView)findViewById(R.id.lblMsg);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.3));

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        registerDevice(centralNodeIP.getText().toString(),Integer.parseInt(centralNodeport.getText().toString()));
                    }
                }
        );

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
*/
//    public static void showRegistrationStatus(boolean state){
//
//        Message msgObj = messageupdateHandler.obtainMessage();
//        Bundle b = new Bundle();
//        String msg;
//        if(state){
//            msg = "Device Registered";
//        }else{
//            msg = "Error in Registration";
//        }
//        b.putString("message", msg);
//        msgObj.setData(b);
//        messageupdateHandler.sendMessage(msgObj);
//    }

//    public void registerDevice(String cenralnodeIPAddress, int centralnodePort){
//
//        messageupdateHandler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                String aResponse = msg.getData().getString("message");
//                registerStatus.setText(aResponse);
//            }
//        };
//
//        RegisterDeviceClient.registerMeAsync(getMACAddress(), getIPAddress(), "Android",cenralnodeIPAddress,centralnodePort);
//
//
//    }


}
