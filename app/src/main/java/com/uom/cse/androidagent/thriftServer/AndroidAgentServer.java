package com.uom.cse.androidagent.thriftServer;

import com.uom.cse.androidagent.info.UsageInfoManager;
import com.uom.cse.androidagent.thriftGeneratedCode.AndroidAgentService;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class AndroidAgentServer extends Thread{

    public static AndroidAgentHandler handler;

    public static AndroidAgentService.Processor processor;

    UsageInfoManager infoManager;

    public AndroidAgentServer(UsageInfoManager infoManager){

        this.infoManager = infoManager;
    }

    @Override
    public void run() {

        handler = new AndroidAgentHandler(infoManager);

        processor = new AndroidAgentService.Processor(handler);

        simple(processor);
    }

    public static void simple(AndroidAgentService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
