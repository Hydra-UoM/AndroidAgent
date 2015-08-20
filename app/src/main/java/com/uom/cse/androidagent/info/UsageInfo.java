package com.uom.cse.androidagent.info;

/**
 * Created by Nirushan on 7/22/2015.
 */
public class UsageInfo {
    public static String VARIABLE_PID = "pid";
    public static String VARIABLE_PACKAGE_NAME = "packageName";
    public static String VARIABLE_APPLICATION_LABEL = "applicationLabel";
    public static String VARIABLE_PROCESS_TYPE = "processType";
    public static String VARIABLE_EVENT_TYPE = "eventType";


    //to store the Id of the process which creates the event
    private int pid;

    //to store the package name of the process which creates the event
    private String packageName;

    //to store the application label of the process which creates the event
    private String ApplicationLabel;

    //to store the process type of the process which creates the event
    private String processType;

    //to store the event type
    private String eventType;


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApplicationLabel() {
        return ApplicationLabel;
    }

    public void setApplicationLabel(String applicationLabel) {
        ApplicationLabel = applicationLabel;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
