package com.uom.cse.androidagent.central_node_services;

import android.bluetooth.BluetoothAdapter;

import com.uom.cse.androidagent.eventAdapters.ProcessInfoEventAdapter;
import com.uom.cse.androidagent.main.AgentService;
import com.uom.cse.androidagent.main.MainActivity;
import com.uom.cse.androidagent.model.ApplicationData;
import com.uom.cse.androidagent.model.DatabaseHandler;
import com.uom.cse.androidagent.popups.RegisterDevicePop;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.List;

/**
 * Created by Nirushan on 11/1/2015.
 */
public class RegisterDeviceClient {

    private static boolean isRegistered = false;

    public static void registerMe(String deviceId, String IPAddress, String type){
        try {
            TTransport transport;

            transport = new TSocket("192.168.1.2", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            RegisterDeviceService.Client client = new RegisterDeviceService.Client(protocol);

            perform(client, deviceId, IPAddress, type);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    public static void registerMeAsync(final String deviceId, final String IPAddress, final String type, final String centralNodeIP, final int centralNodePort){

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    TTransport transport;

                    transport = new TSocket(centralNodeIP, centralNodePort);
                    transport.open();

                    TProtocol protocol = new TBinaryProtocol(transport);
                    RegisterDeviceService.Client client = new RegisterDeviceService.Client(protocol);

                    perform(client, deviceId, IPAddress, type);

                    transport.close();
                    AgentService.showRegistrationStatus(true);
                    isRegistered = true;
                } catch (TException x) {
                    AgentService.showRegistrationStatus(false);
                    x.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private static void perform(RegisterDeviceService.Client client,
                                String deviceId, String IPAddress, String type) throws TException
    {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        Device device = new Device(deviceId, IPAddress, "", "Android", "MyGroup", deviceName);

        client.registerDevice(device);
    }

    public static void getCommand(final String deviceId, final String IPAddress, final String type, final String centralNodeIP, final int centralNodePort){

        while(!isRegistered){

        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    TTransport transport;

                    transport = new TSocket(centralNodeIP, centralNodePort);
                    transport.open();

                    TProtocol protocol = new TBinaryProtocol(transport);
                    RegisterDeviceService.Client client = new RegisterDeviceService.Client(protocol);

                    performGetCommand(client, deviceId, IPAddress, type);

                    transport.close();
                    AgentService.showCommandStatus(true);
                } catch (TException x) {
                    AgentService.showCommandStatus(false);
                    x.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private static void performGetCommand(RegisterDeviceService.Client client,
                                          String deviceId, String IPAddress, String type) throws TException{
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        Device device = new Device(deviceId, IPAddress, "", "Android", "MyGroup", deviceName);

        client.getCommands(device);
    }

    public static void pushEvents(final String centralNodeIP, final int centralNodePort, List<ThriftAgentProcessInfo> thriftAgentProcessInfo){
        try {

            TTransport transport;

            transport = new TSocket(centralNodeIP, centralNodePort);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            RegisterDeviceService.Client client = new RegisterDeviceService.Client(protocol);

            List<ApplicationData> eventsFromDB = DatabaseHandler.getInstance().getAllAppInfo();

            if(!eventsFromDB.isEmpty()){
                for(ApplicationData applicationData:eventsFromDB){
                    ThriftAgentProcessInfo processInfo = new ThriftAgentProcessInfo();
                    processInfo.setName(applicationData.get_applicationName());
                    processInfo.setPackageName(applicationData.get_packageName());
                    processInfo.setCpuUsage(Double.parseDouble(applicationData.get_averageCPU()));
                    processInfo.setRamUsage(Double.parseDouble(applicationData.get_averageSharedMemoryUsage()));
                    processInfo.setSentData(Double.parseDouble(applicationData.get_averageSentData()));
                    processInfo.setReceiveData(Double.parseDouble(applicationData.get_averagereceivedData()));
                    processInfo.setTimestamp(applicationData.get_timestamp());
                    processInfo.setType("Android");
                    processInfo.setPid(applicationData.get_pid());
                    processInfo.setMac(ProcessInfoEventAdapter.getMACAddress());
                    thriftAgentProcessInfo.add(processInfo);
                }
            }

            performPush(client,thriftAgentProcessInfo);

            transport.close();

        } catch (TException x) {
            for(ThriftAgentProcessInfo info : thriftAgentProcessInfo){
                ApplicationData data = new ApplicationData(info.getName(),info.getPackageName(),String.valueOf(info.getCpuUsage()),"",String.valueOf(info.getRamUsage()), String.valueOf(info.getSentData()), String.valueOf(info.getReceiveData()), info.getTimestamp(),info.getPid());
                DatabaseHandler.getInstance().createAppInfo(data);
            }
            x.printStackTrace();
        }
    }

    private static void performPush(RegisterDeviceService.Client client, List<ThriftAgentProcessInfo> thriftAgentProcessInfos){

        try {
            client.pushProcessesInfo(thriftAgentProcessInfos);
        } catch (TException e) {
            e.printStackTrace();
        }

    }

}
