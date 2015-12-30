package com.uom.cse.androidagent.thriftServer;

import android.os.Bundle;
import android.os.Message;

import com.uom.cse.androidagent.AsperConfig;
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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AndroidAgentHandler implements AndroidAgentService.Iface {

    UsageInfoManager infoManager;

    public AndroidAgentHandler(UsageInfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @Override
    public boolean removeAllCommands() throws TException {
        AsperConfig.stopFeed();
        return true;
    }

    @Override
    public boolean getFullInfoWithoutProcessing(short timeInterval) throws TException {
        AsperConfig.sendFullInfoWithoutProcessing(timeInterval, infoManager);
        return true;
    }

    @Override
    public boolean getCriticalInfoWithoutProcessing(short timeInterval) throws TException {
        AsperConfig.sendCriticalInfoWithoutProcessing(timeInterval, infoManager);
        return true;
    }

    @Override
    public boolean getCriticalWithProcessing(short timeInterval) throws TException {
        AsperConfig.sendCriticalWithProcessing(timeInterval,infoManager);
        return true;
    }

    @Override
    public List<TProcessInfo> getAllRunningProcesses() throws TException {

        ArrayList<String> runningProcesses = infoManager.getRunningProcesses();

        List<TProcessInfo> result = new ArrayList<TProcessInfo>();

        for (String process : runningProcesses) {
            TProcessInfo processInfo = new TProcessInfo();
            processInfo.setPackageName(process);

            result.add(processInfo);
        }

        Message msgObj = MainActivity.handler.obtainMessage();
        Bundle b = new Bundle();
        String msg = "COMMAND : SEND ALL RUNNING PROCESSES" + "\n";

        b.putString("message", msg);
        msgObj.setData(b);
        MainActivity.handler3.sendMessage(msgObj);

        return result;
    }

    @Override
    public List<TProcessInfo> getRAMUsageInfo() throws TException {

        List<TProcessInfo> processInfo = new ArrayList<>();

        List<RAMUsageInfo> ramUsage = infoManager.getRAMUsageInfo();

        for (RAMUsageInfo tempRAMUsage : ramUsage) {
            TProcessInfo tempProcessInfo = new TProcessInfo();
            tempProcessInfo.setPackageName(tempRAMUsage.getApplicationLabel());
            tempProcessInfo.setPrivateRAMUsage(tempRAMUsage.getPrivateMemoryUsage() + "");
            tempProcessInfo.setSharedRAMUsage(tempRAMUsage.getSharedMemoryUsage() + "");
            processInfo.add(tempProcessInfo);
        }

        Message msgObj = MainActivity.handler.obtainMessage();
        Bundle b = new Bundle();
        String msg = "COMMAND : SEND ALL RAM USAGE" + "\n";

        b.putString("message", msg);
        msgObj.setData(b);
        MainActivity.handler3.sendMessage(msgObj);

        return processInfo;
    }

    @Override
    public List<TProcessInfo> getCPUUsageInfo() throws TException {

        List<TProcessInfo> processInfo = new ArrayList<>();

        List<CPUUsageInfo> cpuUsage = infoManager.getCPUUsageInfo();

        for (CPUUsageInfo tempCPUUsage : cpuUsage) {
            TProcessInfo tempProcessInfo = new TProcessInfo();
            tempProcessInfo.setPackageName(tempCPUUsage.getApplicationLabel());
            tempProcessInfo.setProcessCPUUsage(tempCPUUsage.getCpuUsage() + "");
            processInfo.add(tempProcessInfo);
        }

        Message msgObj = MainActivity.handler.obtainMessage();
        Bundle b = new Bundle();
        String msg = "COMMAND : SEND ALL CPU USAGE" + "\n";

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

        for (Processinfo processinfo : processInfoList) {
            TProcessInfo tempProcessInfo = new TProcessInfo();
            tempProcessInfo.setName(processinfo.getProcessName());
            tempProcessInfo.setPackageName(processinfo.getPackageName());
            tempProcessInfo.setSharedRAMUsage(String.valueOf(processinfo.getSharedMemoryUsage()));
            tempProcessInfo.setProcessCPUUsage(String.valueOf(processinfo.getCpuUsage()));
            tempProcessInfo.setSentData(String.valueOf(processinfo.getSentData()));
            tempProcessInfo.setReceiveData(String.valueOf(processinfo.getReceivedData()));
            tempProcessInfo.setPrivateRAMUsage(String.valueOf(processinfo.getPrivateMemoryUsage()));
            tempProcessInfo.setPid(String.valueOf(processinfo.getPid()));
            tempProcessInfo.setType(String.valueOf(processinfo.getType()));
            processInfoCollection.add(tempProcessInfo);
        }
        return processInfoCollection;
    }

    @Override
    public List<SensorDetails> getSensorDetails() throws TException {
        List<SensorDetails> sensorDetails = new ArrayList<>();

        SensorDetails sensorDetail = new SensorDetails();
        SensorDetails sensorDetail1 = new SensorDetails();
        SensorDetails sensorDetail2 = new SensorDetails();
        SensorDetails sensorDetail3 = new SensorDetails();
        SensorDetails sensorDetail4 = new SensorDetails();
        SensorDetails sensorDetail5 = new SensorDetails();
        SensorDetails sensorDetail6 = new SensorDetails();
        SensorDetails sensorDetail7 = new SensorDetails();
        SensorDetails sensorDetail8 = new SensorDetails();
        SensorDetails sensorDetail9 = new SensorDetails();
        SensorDetails sensorDetail10 = new SensorDetails();
        SensorDetails sensorDetail11 = new SensorDetails();
        SensorDetails sensorDetail12 = new SensorDetails();
        SensorDetails sensorDetail13 = new SensorDetails();
        SensorDetails sensorDetail14 = new SensorDetails();
        SensorDetails sensorDetail15 = new SensorDetails();
        SensorDetails sensorDetail16 = new SensorDetails();
        SensorDetails sensorDetail17 = new SensorDetails();
        sensorDetail.sensorName = "Accelerometer";
        sensorDetail.availability = String.valueOf(infoManager.isAccelerometerAvailable());
        sensorDetails.add(sensorDetail);
        sensorDetail1.sensorName = "Audio Output";
        sensorDetail1.availability = String.valueOf(infoManager.isAudioOutputAvailable());
        sensorDetails.add(sensorDetail1);
        sensorDetail2.sensorName = "Barometer";
        sensorDetail2.availability = String.valueOf(infoManager.isBarometerAvailable());
        sensorDetails.add(sensorDetail2);
        sensorDetail3.sensorName = "Bluetooth";
        sensorDetail3.availability = String.valueOf(infoManager.isBluetoothAvailable());
        sensorDetails.add(sensorDetail3);
        sensorDetail4.sensorName = "Back Camera";
        sensorDetail4.availability = String.valueOf(infoManager.isCameraAvailable());
        sensorDetails.add(sensorDetail4);
        sensorDetail5.sensorName = "Front Camera";
        sensorDetail5.availability = String.valueOf(infoManager.isFrontCameraAvailable());
        sensorDetails.add(sensorDetail5);
        sensorDetail6.sensorName = "Camera capable manual sensor";
        sensorDetail6.availability = String.valueOf(infoManager.isCameraCapabilityManualSensorAvailable());
        sensorDetails.add(sensorDetail6);
        sensorDetail7.sensorName = "GPS Location";
        sensorDetail7.availability = String.valueOf(infoManager.isGPSLocationAvailable());
        sensorDetails.add(sensorDetail7);
        sensorDetail8.sensorName = "Gyroscope Sensor";
        sensorDetail8.availability = String.valueOf(infoManager.isGyroscopeSensorAvailable());
        sensorDetails.add(sensorDetail8);
        sensorDetail9.sensorName = "IR Sensor";
        sensorDetail9.availability = String.valueOf(infoManager.isIRSensorAvailable());
        sensorDetails.add(sensorDetail9);
        sensorDetail10.sensorName = "Light Sensor";
        sensorDetail10.availability = String.valueOf(infoManager.isLightSensorAvailable());
        sensorDetails.add(sensorDetail10);
        sensorDetail11.sensorName = "Location Network";
        sensorDetail11.availability = String.valueOf(infoManager.isLocationNetworkAvailable());
        sensorDetails.add(sensorDetail11);
        sensorDetail12.sensorName = "Location Service";
        sensorDetail12.availability = String.valueOf(infoManager.isLocationServiceAvailable());
        sensorDetails.add(sensorDetail12);
        sensorDetail13.sensorName = "Printing Support";
        sensorDetail13.availability = String.valueOf(infoManager.isPrintingAvailable());
        sensorDetails.add(sensorDetail13);
        sensorDetail14.sensorName = "Proximity Sensor";
        sensorDetail14.availability = String.valueOf(infoManager.isProximitySensorAvailable());
        sensorDetails.add(sensorDetail14);
        sensorDetail15.sensorName = "Telephony";
        sensorDetail15.availability = String.valueOf(infoManager.isTelephonyAvailable());
        sensorDetails.add(sensorDetail15);
        sensorDetail16.sensorName = "Temprature Sensor";
        sensorDetail16.availability = String.valueOf(infoManager.isTempratureSensorAvailable());
        sensorDetails.add(sensorDetail16);
        sensorDetail17.sensorName = "TV Support";
        sensorDetail17.availability = String.valueOf(infoManager.isTVSupportAvailable());
        sensorDetails.add(sensorDetail17);
        return sensorDetails;
    }

    @Override
    public List<TProcessInfo> getFilteredProcessInfo(String cpuUsage, String ramUsage, String processName) throws TException {
        List<TProcessInfo> processInfoCollection = new ArrayList<>();
        List<Processinfo> processInfoList = infoManager.getFilteredProcessinfo(cpuUsage, ramUsage, processName);

        for (Processinfo processinfo : processInfoList) {
            TProcessInfo tempProcessInfo = new TProcessInfo();
            tempProcessInfo.setName(processinfo.getProcessName());
            tempProcessInfo.setPackageName(processinfo.getPackageName());
            tempProcessInfo.setSharedRAMUsage(String.valueOf(processinfo.getSharedMemoryUsage()));
            tempProcessInfo.setProcessCPUUsage(String.valueOf(processinfo.getCpuUsage()));
            tempProcessInfo.setSentData(String.valueOf(processinfo.getSentData()));
            tempProcessInfo.setReceiveData(String.valueOf(processinfo.getReceivedData()));
            tempProcessInfo.setPrivateRAMUsage(String.valueOf(processinfo.getPrivateMemoryUsage()));
            tempProcessInfo.setPid(String.valueOf(processinfo.getPid()));
            tempProcessInfo.setType(String.valueOf(processinfo.getType()));
            processInfoCollection.add(tempProcessInfo);
        }
        return processInfoCollection;
    }

    @Override
    public boolean testNetwork(ByteBuffer data) throws TException {
        return true;
    }

    @Override
    public boolean deployCommand(final short cpuUsage,final short ramUsage,final short receiveData,final short sentData,final short timeInterval,final String process) throws TException {
        Executor eventFeedExecutor = Executors.newSingleThreadExecutor();
        // run the task using a thread from the thread pool:
        eventFeedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                AsperConfig.AsperQueryBuilder(cpuUsage, ramUsage, receiveData, sentData, timeInterval, process, infoManager);
            }
        });
        return true;
    }


}
