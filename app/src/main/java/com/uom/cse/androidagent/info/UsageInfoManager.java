package com.uom.cse.androidagent.info;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Created by Nirushan on 7/22/2015.
 */
public class UsageInfoManager {

    private Context context;
    private long RAMThreadInterval = 200;
    private boolean RAMThreadFlag = true;

    public UsageInfoManager(Context context){
        this.context = context;
    }

    public void setRAMThreadInterval(long RAMThreadInterval){
        this.RAMThreadInterval = RAMThreadInterval;
    }

    public List<RAMUsageInfo> getRAMUsageInfo(){

        List<RAMUsageInfo> infoList = new ArrayList<>();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        Map<Integer, String> pidMap = new TreeMap<Integer, String>();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            RAMUsageInfo info = new RAMUsageInfo();

            info.setPackageName(runningAppProcessInfo.processName);

            try {

                // get application name
                CharSequence c = packageManager.getApplicationLabel(packageManager.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA));
                info.setApplicationLabel(c.toString());

            } catch (Exception e) {
                //Name Not Found Exception
                info.setApplicationLabel("LABEL NAME NOT FOUND");
            }

            String type = "NONE";
            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND){
                type = "Background process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE){
                type = "Service";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE){
                type = "Perceptible";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){
                type = "Visible process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                type = "Foreground process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY){
                type = "Empty";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE){
                type = "Gone";
            }
            info.setProcessType(type);

            int pids[] = new int[1];
            pids[0] = runningAppProcessInfo.pid;

            info.setPid(runningAppProcessInfo.pid);

            android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);

            for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray)
            {
                info.setPrivateMemoryUsage(pidMemoryInfo.getTotalPrivateDirty());
                info.setSharedMemoryUsage(pidMemoryInfo.getTotalSharedDirty());
            }
            infoList.add(info);
        }
        return infoList;
    }

    public void startMonitoringRAMUsage(){
        RAMThreadFlag = true;

        Thread RAMUsageThread = new Thread(){
            @Override
            public void run() {
                while (RAMThreadFlag){

                    RAMUsageCallBack(getRAMUsageInfo());

                    try {
                        Thread.sleep(RAMThreadInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        RAMUsageThread.start();
    }

    public void stopMonitoringRAMUsage(){
        RAMThreadFlag = false;
    }

    public void RAMUsageCallBack(List<RAMUsageInfo> RAMUsageList){

    }

    public ArrayList<String> getRunningProcesses(){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        ArrayList<String> result = new ArrayList<String>();

        Map<Integer, String> pidMap = new TreeMap<Integer, String>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {

            String processName = "";
            String packageName = runningAppProcessInfo.processName;

            try {
                // get application name
                CharSequence c = packageManager.getApplicationLabel(packageManager.getApplicationInfo(runningAppProcessInfo.processName,
                        PackageManager.GET_META_DATA));
                processName = c.toString();
            } catch (Exception e) {
                //Name Not FOund Exception
                processName = runningAppProcessInfo.processName;
            }

            String type = "None";
            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND){
                type = "Background process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE){
                type = "Service";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE){
                type = "Perceptible";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){
                type = "Visible process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                type = "Foreground process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY){
                type = "Empty";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE){
                type = "Gone";
            }

            String value = "";
            value += " Process name : "+ processName +"\n";
            value += (" Type of process: " + type + "\n");
            value += (" Package name: " + packageName + "\n\n");
            result.add(value);
        }

        return result;
    }

    public List<CPUUsageInfo> getCPUUsageInfo(){
        List<CPUUsageInfo> cpuUsageInfo = new ArrayList<CPUUsageInfo>();
        try {
            Process process = Runtime.getRuntime().exec("top");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();

            String line;
            int i = 0;
            int lineNo = 0;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                CPUUsageInfo tempUsage = new CPUUsageInfo();
                if (line.isEmpty() && i == 2) {
                    Log.d("BACK", "Retour apres 3 espaces");
                    String msg = "PID Process CPU \n" + output.toString();
                    output.setLength(0);
                    i = 0;
                    lineNo = 0;
                    count++;
                    if(count>1)
                        break;

                } else if (line.isEmpty() && i == 1) {
                    Log.d("BACK", "2 espaces");
                    output.append("loading...\n \n");
                    i = 2;
                } else if (line.isEmpty() && i == 0) {
                    Log.d("BACK", "1 espaces");
                    //output.append("loading...\n \n");
                    i = 1;
                } else {
                    Log.d("BACK", "LigneTxt");
                    if(lineNo==3){
                        String pId = interpreteCommand(line)[0];
                        String processName = interpreteCommand(line)[8];
                        String cpu = interpreteCommand(line)[2];

                        int usage = 0;
                        if(processName.equals("")){
                            processName = "Background process";
                        }
                        if (cpu.length() > 0){
                            usage = Integer.parseInt(cpu.substring(0, cpu.length()-1));
                        }
                        if(usage>0) {
                            output.append(pId + " " + processName + " " + cpu + "\n");

                        }
                        tempUsage.setPid(Integer.parseInt(pId));
                        tempUsage.setApplicationLabel(processName);
                        tempUsage.setCpuUsage(usage);
                        cpuUsageInfo.add(tempUsage);
                        //output.append(line + "\n");
                    }else{
                        lineNo++;
                    }

                    i = 0;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuUsageInfo;
    }

    private String[] interpreteCommand(String line){
        StringTokenizer strTkn = new StringTokenizer(line, " ");
        ArrayList<String> arrLis = new ArrayList<String>(line.length());

        while(strTkn.hasMoreTokens())
            arrLis.add(strTkn.nextToken());

        String[] comm = arrLis.toArray(new String[0]);


        comm[8] = getAppLabelByPID(Integer.parseInt(comm[0]));
        return comm;
    }

    public String getAppLabelByPID(int pid){
        PackageManager pm = context.getPackageManager();
        ActivityManager manager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String processName = "";
        for(ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()){
            if(processInfo.pid == pid){

                String packageName = processInfo.processName;

                try {
                    // get application name
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(processInfo.processName, PackageManager.GET_META_DATA));
                    processName = c.toString();
                } catch (Exception e) {
                    //Name Not FOund Exception
                    processName = processInfo.processName;
                }
                break;
            }
        }
        return processName;
    }

    public String getBatteryLevel(){

        Intent intent  = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int    level   = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int    scale   = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int    percent = (level*100)/scale;
        return String.valueOf(percent) + "%";

    }

    public ActivityManager.MemoryInfo getTotalMemorySize() {

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = (mi.totalMem-mi.availMem) / 1048576L;
        return mi;
    }

    public String getRamUsedSpace(){
       return (getTotalMemorySize().totalMem - getTotalMemorySize().availMem) / 1048576L + "MB";
    }

    public String getRamFreeSpace(){
        return getTotalMemorySize().availMem / 1048576L + "MB";
    }

    public String getRamSize(){
        return getTotalMemorySize().totalMem / 1048576L + "MB";
    }

    public float getCPUUsage() {

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" +");  // Split on one or more spaces

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;

    }

    public List<Processinfo> getprocessinfo(){

        List<CPUUsageInfo> processCPUdetails = getCPUUsageInfo();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        String value="";
        Map<Integer, String> pidMap = new TreeMap<Integer, String>();
        List<Processinfo> processinfoList = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {

            String processName = "";
            String packageName = runningAppProcessInfo.processName;

            try {
                // get application name
                CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA));
                processName = c.toString();
            } catch (Exception e) {
                //Name Not FOund Exception
                processName = runningAppProcessInfo.processName;
            }

            String type = "None";
            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND){
                type = "Background process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE){
                type = "Service";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE){
                type = "Perceptible";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){
                type = "Visible process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                type = "Foreground process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY){
                type = "Empty";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE){
                type = "Gone";
            }


            int pids[] = new int[1];
            pids[0] = runningAppProcessInfo.pid;
            android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);

            Processinfo processinfo = new Processinfo();
            Long tsLong = System.currentTimeMillis()/1000;
            processinfo.setTimestamp(tsLong.toString());

            for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray)
            {
                processinfo.setProcessName(processName);
                processinfo.setPrivateMemoryUsage(pidMemoryInfo.getTotalPrivateDirty());
                processinfo.setSharedMemoryUsage(pidMemoryInfo.getTotalSharedDirty());
                processinfo.setType(type);
                processinfo.setPackageName(packageName);
                processinfo.setPid(String.valueOf(runningAppProcessInfo.pid));
            }
            for(CPUUsageInfo cpuUsageInfo : processCPUdetails){
                if(cpuUsageInfo.getPid() == runningAppProcessInfo.pid){
                    processinfo.setCpuUsage(cpuUsageInfo.getCpuUsage());
                }
            }
            long received = TrafficStats.getUidRxBytes(runningAppProcessInfo.uid)/ 1048576L;
            long send   = TrafficStats.getUidTxBytes(runningAppProcessInfo.uid)/ 1048576L;
            processinfo.setReceivedData(received);
            processinfo.setSentData(send);
            processinfoList.add(processinfo);
        }
        return processinfoList;
    }

    public List<Processinfo> getFilteredProcessinfo(String cpuUsage,String ramUsage,String processNameParam){

        List<CPUUsageInfo> processCPUdetails = getCPUUsageInfo();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        String value="";
        Map<Integer, String> pidMap = new TreeMap<Integer, String>();
        List<Processinfo> processinfoList = new ArrayList<>();
        outerloop:
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            String applicationName;
            try {
                // get application name
                CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA));
                applicationName = c.toString();
            } catch (Exception e) {
                //Name Not FOund Exception
                applicationName = runningAppProcessInfo.processName;
            }

            if(!("".equals(processNameParam))){
                if(!(applicationName.equals(processNameParam))){
                    continue;
                }
            }
            String processName = "";
            String packageName = runningAppProcessInfo.processName;

            try {
                // get application name
                CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA));
                processName = c.toString();
            } catch (Exception e) {
                //Name Not FOund Exception
                processName = runningAppProcessInfo.processName;
            }

            String type = "None";
            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND){
                type = "Background process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE){
                type = "Service";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE){
                type = "Perceptible";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){
                type = "Visible process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                type = "Foreground process";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY){
                type = "Empty";
            }

            if (runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE){
                type = "Gone";
            }


            int pids[] = new int[1];
            pids[0] = runningAppProcessInfo.pid;
            android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);

            Processinfo processinfo = new Processinfo();
            Long tsLong = System.currentTimeMillis()/1000;
            processinfo.setTimestamp(tsLong.toString());

            for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray)
            {
                if(!("0".equals(ramUsage))){
                    if( pidMemoryInfo.getTotalPrivateDirty()<Integer.parseInt(ramUsage)){
                        continue outerloop;
                    }
                }
                processinfo.setProcessName(processName);
                processinfo.setPrivateMemoryUsage(pidMemoryInfo.getTotalPrivateDirty());
                processinfo.setSharedMemoryUsage(pidMemoryInfo.getTotalSharedDirty());
                processinfo.setType(type);
                processinfo.setPackageName(packageName);
                processinfo.setPid(String.valueOf(runningAppProcessInfo.pid));
            }
            for(CPUUsageInfo cpuUsageInfo : processCPUdetails){

                if(cpuUsageInfo.getPid() == runningAppProcessInfo.pid){
                    if(!("0".equals(cpuUsage))){
                        if(cpuUsageInfo.getCpuUsage()<Integer.parseInt(cpuUsage)){
                            continue outerloop;
                        }
                    }
                    processinfo.setCpuUsage(cpuUsageInfo.getCpuUsage());
                }
            }
            long received = TrafficStats.getUidRxBytes(runningAppProcessInfo.uid)/ 1048576L;
            long send   = TrafficStats.getUidTxBytes(runningAppProcessInfo.uid)/ 1048576L;
            processinfo.setReceivedData(received);
            processinfo.setSentData(send);
            processinfoList.add(processinfo);
        }
        return processinfoList;
    }

    public boolean isCameraAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public boolean isTelephonyAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    public boolean isFrontCameraAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    public boolean isBluetoothAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
    }

    public boolean isAudioOutputAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT);
    }

    //The camera device can be manually controlled (3A algorithms such as auto-exposure, and auto-focus can be bypassed)
    public boolean isCameraCapabilityManualSensorAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_CAPABILITY_MANUAL_SENSOR);
    }

    public boolean isIRSensorAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CONSUMER_IR);
    }

    public boolean isTVSupportAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LIVE_TV);
    }

    public boolean isWifiAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_WIFI);
    }

    public boolean isLocationServiceAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LOCATION);
    }

    public boolean isGPSLocationAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    public boolean isLocationNetworkAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);
    }

    public boolean isPrintingAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_PRINTING);
    }

    public boolean isLightSensorAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
    }

    public boolean isAccelerometerAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
    }

    public boolean isTempratureSensorAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE);
    }

    public boolean isBarometerAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER);
    }

    public boolean isGyroscopeSensorAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
    }

    public boolean isProximitySensorAvailable(){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY);
    }
}
