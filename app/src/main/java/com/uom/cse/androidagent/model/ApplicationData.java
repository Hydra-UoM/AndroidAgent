package com.uom.cse.androidagent.model;

/**
 * Created by Sirojan on 11/19/2015.
 */
public class ApplicationData {

    private int _id;

    private String _applicationName;

    private String _packageName;

    private String _averageCPU;

    private String _averagePrivateMemoryUsage;

    private String _averageSharedMemoryUsage;

    private String _averageSentData;

    private String _averagereceivedData;

    private String _timestamp;

    private String _pid;

    public ApplicationData(String pid,String applicationName,String packageName,String averageCPU,String averagePrivateMemoryUsage,String averageSharedMemoryUsage,String averageSentData,String averagereceivedData,String timestamp){
        _pid = pid;
        _applicationName = applicationName;
        _packageName = packageName;
        _averageCPU = averageCPU;
        _averagePrivateMemoryUsage = averagePrivateMemoryUsage;
        _averageSharedMemoryUsage = averageSharedMemoryUsage;
        _averageSentData = averageSentData;
        _averagereceivedData = averagereceivedData;
        set_timestamp(timestamp);
    }


    public String get_applicationName() {
        return _applicationName;
    }

    public void set_applicationName(String _applicationName) {
        this._applicationName = _applicationName;
    }

    public String get_averageCPU() {
        return _averageCPU;
    }

    public void set_averageCPU(String _averageCPU) {
        this._averageCPU = _averageCPU;
    }



    public String get_averagePrivateMemoryUsage() {
        return _averagePrivateMemoryUsage;
    }

    public void set_averagePrivateMemoryUsage(String _averagePrivateMemoryUsage) {
        this._averagePrivateMemoryUsage = _averagePrivateMemoryUsage;
    }

    public String get_averageSharedMemoryUsage() {
        return _averageSharedMemoryUsage;
    }

    public void set_averageSharedMemoryUsage(String _averageSharedMemoryUsage) {
        this._averageSharedMemoryUsage = _averageSharedMemoryUsage;
    }

    public String get_averageSentData() {
        return _averageSentData;
    }

    public void set_averageSentData(String _averageSentData) {
        this._averageSentData = _averageSentData;
    }

    public String get_averagereceivedData() {
        return _averagereceivedData;
    }

    public void set_averagereceivedData(String _averagereceivedData) {
        this._averagereceivedData = _averagereceivedData;
    }

    public String get_packageName() {
        return _packageName;
    }

    public void set_packageName(String _packageName) {
        this._packageName = _packageName;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(String _timestamp) {
        this._timestamp = _timestamp;
    }

    public String get_pid() {
        return _pid;
    }

    public void set_pid(String _pid) {
        this._pid = _pid;
    }
}
