package com.uom.cse.androidagent.thriftServer;

import android.os.Bundle;
import android.os.Message;

import com.uom.cse.androidagent.MainActivity;
import com.uom.cse.androidagent.info.UsageInfoManager;
import com.uom.cse.androidagent.thriftGeneratedCode.AndroidAgentService;
import com.uom.cse.androidagent.thriftGeneratedCode.TProcessInfo;

import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

public class AndroidAgentHandler implements AndroidAgentService.Iface {
    UsageInfoManager infoManager;

    public AndroidAgentHandler(UsageInfoManager infoManager){
        this.infoManager = infoManager;
    }

    @Override
    public List<TProcessInfo> getAllRunningProcesses() throws TException {

        ArrayList<String> runningProcesses = infoManager.getRunningProcesses();

        List<TProcessInfo> result = new ArrayList<TProcessInfo>();

        for (String process : runningProcesses){
            TProcessInfo processInfo = new TProcessInfo();
            processInfo.setPackageName(process);

            result.add(processInfo);
        }

        Message msgObj = MainActivity.handler.obtainMessage();
        Bundle b = new Bundle();
        String msg = "COMMAND : SEND ALL RUNNING PROCESSES"+"\n";

        b.putString("message", msg);
        msgObj.setData(b);
        MainActivity.handler3.sendMessage(msgObj);

        return result;
    }
}
