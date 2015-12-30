package com.uom.cse.androidagent.central_node_services;

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
                    RegisterDevicePop.showRegistrationStatus(true);
                } catch (TException x) {
                    RegisterDevicePop.showRegistrationStatus(false);
                    x.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private static void perform(RegisterDeviceService.Client client,
                                String deviceId, String IPAddress, String type) throws TException
    {
        Device device = new Device(deviceId, IPAddress, type);

        client.registerDevice(device);
    }

    public static void pushEvents(final String centralNodeIP, final int centralNodePort, List<ThriftAgentProcessInfo> thriftAgentProcessInfo){
        try {

            TTransport transport;

            transport = new TSocket(centralNodeIP, centralNodePort);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            RegisterDeviceService.Client client = new RegisterDeviceService.Client(protocol);

            performPush(client,thriftAgentProcessInfo);

            transport.close();

        } catch (TException x) {
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
