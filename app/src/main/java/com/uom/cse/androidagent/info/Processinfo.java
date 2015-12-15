package com.uom.cse.androidagent.info;

/**
 * Created by Sirojan on 11/1/2015.
 */
public class Processinfo {

    private String processName;

    private String packageName;

    private double cpuUsage;

    private double privateMemoryUsage;

    private double sharedMemoryUsage;

    private double sentData;
    
    private double receivedData;

    private String type;

    private String pid;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }




    public double getSentData() {
        return sentData;
    }

    public void setSentData(double sentData) {
        this.sentData = sentData;
    }

    public double getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(double receivedData) {
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

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getPrivateMemoryUsage() {
        return privateMemoryUsage;
    }

    public void setPrivateMemoryUsage(double privateMemoryUsage) {
        this.privateMemoryUsage = privateMemoryUsage;
    }

    public double getSharedMemoryUsage() {
        return sharedMemoryUsage;
    }

    public void setSharedMemoryUsage(double sharedMemoryUsage) {
        this.sharedMemoryUsage = sharedMemoryUsage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
