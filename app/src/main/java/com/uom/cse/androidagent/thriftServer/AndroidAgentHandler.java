package com.uom.cse.androidagent.thriftServer;

import android.os.Bundle;
import android.os.Message;

import com.uom.cse.androidagent.MainActivity;
import com.uom.cse.androidagent.info.CPUUsageInfo;
import com.uom.cse.androidagent.info.Processinfo;
import com.uom.cse.androidagent.info.RAMUsageInfo;
import com.uom.cse.androidagent.info.UsageInfoManager;
import com.uom.cse.androidagent.thriftGeneratedCode.AndroidAgentService;
import com.uom.cse.androidagent.thriftGeneratedCode.DeviceOverallInfo;
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

    @Override
    public List<TProcessInfo> getRAMUsageInfo() throws TException {

        List<TProcessInfo> processInfo = new ArrayList<>();

        List<RAMUsageInfo> ramUsage = infoManager.getRAMUsageInfo();

        for(RAMUsageInfo tempRAMUsage : ramUsage){
            TProcessInfo tempProcessInfo = new TProcessInfo();
            tempProcessInfo.setPackageName(tempRAMUsage.getApplicationLabel());
            tempProcessInfo.setPrivateRAMUsage(tempRAMUsage.getPrivateMemoryUsage() + "");
            tempProcessInfo.setSharedRAMUsage(tempRAMUsage.getSharedMemoryUsage() + "");
            processInfo.add(tempProcessInfo);
        }

        Message msgObj = MainActivity.handler.obtainMessage();
        Bundle b = new Bundle();
        String msg = "COMMAND : SEND ALL RAM USAGE"+"\n";

        b.putString("message", msg);
        msgObj.setData(b);
        MainActivity.handler3.sendMessage(msgObj);

        return processInfo;
    }

    @Override
    public List<TProcessInfo> getCPUUsageInfo() throws TException {

        List<TProcessInfo> processInfo = new ArrayList<>();

        List<CPUUsageInfo> cpuUsage = infoManager.getCPUUsageInfo();

        for(CPUUsageInfo tempCPUUsage : cpuUsage){
            TProcessInfo tempProcessInfo = new TProcessInfo();
            tempProcessInfo.setPackageName(tempCPUUsage.getApplicationLabel());
            tempProcessInfo.setProcessCPUUsage(tempCPUUsage.getCpuUsage() + "");
            processInfo.add(tempProcessInfo);
        }

        Message msgObj = MainActivity.handler.obtainMessage();
        Bundle b = new Bundle();
        String msg = "COMMAND : SEND ALL CPU USAGE"+"\n";

        b.putString("message", msg);
        msgObj.setData(b);
        MainActivity.handler3.sendMessage(msgObj);

        return processInfo;
    }

    @Override
    public List<TProcessInfo> getInternetUsage() throws TException {
        return null;
    }

    @Override
    public DeviceOverallInfo getOverallBasicInfo() throws TException {

        DeviceOverallInfo overallInfo = new DeviceOverallInfo();

        overallInfo.battery = infoManager.getBatteryLevel();
        overallInfo.cpuUsage = infoManager.getCPUUsage() + "";
        overallInfo.ramFreeMemory = infoManager.getRamFreeSpace();
        overallInfo.ramUsedMemory = infoManager.getRamUsedSpace();

        return overallInfo;
    }

    @Override
    public String getOverallCpuInfo() throws TException {
        return this.infoManager.getCPUUsageInfo() + "";
    }

    @Override
    public String getOverallRamFreeMemoryInfo() throws TException {
        return this.infoManager.getRamFreeSpace();
    }

    @Override
    public String getOverallRamUsedMemoryInfo() throws TException {
        return this.infoManager.getRamUsedSpace();
    }

    @Override
    public String getBattery() throws TException {
        return this.infoManager.getBatteryLevel();
    }

    @Override
    public List<TProcessInfo> getAllRunningProcessesWithInfo() throws TException {
        List<TProcessInfo> processInfoCollection = new ArrayList<>();
        List<Processinfo> processInfoList = infoManager.getprocessinfo();

        for(Processinfo processinfo : processInfoList){
            TProcessInfo tempProcessInfo = new TProcessInfo();
            tempProcessInfo.setName(processinfo.getProcessName());
            tempProcessInfo.setPackageName(processinfo.getPackageName());
            tempProcessInfo.setSharedRAMUsage(processinfo.getSharedMemoryUsage());
            tempProcessInfo.setProcessCPUUsage(processinfo.getCpuUsage());
            tempProcessInfo.setSentData(processinfo.getSentData());
            tempProcessInfo.setReceiveData(processinfo.getReceivedData());
            tempProcessInfo.setPrivateRAMUsage(processinfo.getPrivateMemoryUsage());
            tempProcessInfo.setPid(processinfo.getPid());
            tempProcessInfo.setType(processinfo.getType());
            processInfoCollection.add(tempProcessInfo);
        }
        return processInfoCollection;
    }
}
