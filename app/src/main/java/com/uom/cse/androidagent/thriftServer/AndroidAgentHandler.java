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
import com.uom.cse.androidagent.thriftGeneratedCode.SensorDetails;
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

    @Override
    public List<SensorDetails> getSensorDetails() throws TException {
        List<SensorDetails> sensorDetails = new ArrayList<>();

        SensorDetails sensorDetail = new SensorDetails();
        sensorDetail.sensorName = "Accelerometer";
        sensorDetail.availability = String.valueOf(infoManager.isAccelerometerAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Audio Output";
        sensorDetail.availability = String.valueOf(infoManager.isAudioOutputAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Barometer";
        sensorDetail.availability = String.valueOf(infoManager.isBarometerAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Bluetooth";
        sensorDetail.availability = String.valueOf(infoManager.isBluetoothAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Back Camera";
        sensorDetail.availability = String.valueOf(infoManager.isCameraAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Front Camera";
        sensorDetail.availability = String.valueOf(infoManager.isFrontCameraAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Camera capable manual sensor";
        sensorDetail.availability = String.valueOf(infoManager.isCameraCapabilityManualSensorAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "GPS Location";
        sensorDetail.availability = String.valueOf(infoManager.isGPSLocationAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Gyroscope Sensor";
        sensorDetail.availability = String.valueOf(infoManager.isGyroscopeSensorAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "IR Sensor";
        sensorDetail.availability = String.valueOf(infoManager.isIRSensorAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Light Sensor";
        sensorDetail.availability = String.valueOf(infoManager.isLightSensorAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Location Network";
        sensorDetail.availability = String.valueOf(infoManager.isLocationNetworkAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Location Service";
        sensorDetail.availability = String.valueOf(infoManager.isLocationServiceAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Printing Support";
        sensorDetail.availability = String.valueOf(infoManager.isPrintingAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Proximity Sensor";
        sensorDetail.availability = String.valueOf(infoManager.isProximitySensorAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Telephony";
        sensorDetail.availability = String.valueOf(infoManager.isTelephonyAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "Temprature Sensor";
        sensorDetail.availability = String.valueOf(infoManager.isTempratureSensorAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail.sensorName = "TV Support";
        sensorDetail.availability = String.valueOf(infoManager.isTVSupportAvailable());
        sensorDetails.add(sensorDetail);
        return sensorDetails;
    }

    @Override
    public List<TProcessInfo> getFilteredProcessInfo(String cpuUsage, String ramUsage, String processName) throws TException {
        List<TProcessInfo> processInfoCollection = new ArrayList<>();
        List<Processinfo> processInfoList = infoManager.getFilteredProcessinfo(cpuUsage,ramUsage,processName);

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
