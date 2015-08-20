package info;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Created by Nirushan on 7/22/2015.
 */
public class UsageInfoManager {
    //get application context
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

}
