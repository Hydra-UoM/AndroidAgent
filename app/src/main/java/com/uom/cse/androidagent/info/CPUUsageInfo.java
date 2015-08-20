package com.uom.cse.androidagent.info;

/**
 * Created by Nirushan on 8/6/2015.
 */
public class CPUUsageInfo extends UsageInfo {
    public static String VARIABLE_CPU_USAGE = "cpuUsage";

    private int cpuUsage;

    public int getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(int cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
}
