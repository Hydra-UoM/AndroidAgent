package com.uom.cse.androidagent.info;

/**
 * Created by Sirojan on 11/1/2015.
 */
public class Processinfo {

    private String processName;

    private String packageName;

    private String cpuUsage;

    private String privateMemoryUsage;

    private String sharedMemoryUsage;

    private String sentData;
    
    private String receivedData;

    private String type;

    private String pid;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }




    public String getSentData() {
        return sentData;
    }

    public void setSentData(String sentData) {
        this.sentData = sentData;
    }

    public String getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getPrivateMemoryUsage() {
        return privateMemoryUsage;
    }

    public void setPrivateMemoryUsage(String privateMemoryUsage) {
        this.privateMemoryUsage = privateMemoryUsage;
    }

    public String getSharedMemoryUsage() {
        return sharedMemoryUsage;
    }

    public void setSharedMemoryUsage(String sharedMemoryUsage) {
        this.sharedMemoryUsage = sharedMemoryUsage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
