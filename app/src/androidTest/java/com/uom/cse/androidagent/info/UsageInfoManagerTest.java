package com.uom.cse.androidagent.info;


import android.content.Context;
import android.test.InstrumentationTestCase;

public class UsageInfoManagerTest  extends InstrumentationTestCase {


    public void testSetRAMThreadInterval(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.setRAMThreadInterval(10);
            usageInfoManager.setRAMThreadInterval(100);
            usageInfoManager.setRAMThreadInterval(1000);
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetRAMUsageInfo(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getRAMUsageInfo();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testRAMUsageCallBack(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.RAMUsageCallBack(null);
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetRunningProcesses(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getRunningProcesses();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetCPUUsageInfo(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getCPUUsageInfo();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetAppLabelByPID(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getAppLabelByPID(2);
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetTotalMemorySize() {
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getTotalMemorySize();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetRamUsedSpace(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getRamUsedSpace();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetRamFreeSpace(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getRamFreeSpace();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetRamSize(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getRamSize();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetCPUUsage() {
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getCPUUsage();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetprocessinfo(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getprocessinfo();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testgetFilteredProcessinfo(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getFilteredProcessinfo(null,null,null);
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisCameraAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.getRamSize();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisTelephonyAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isTelephonyAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisFrontCameraAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isFrontCameraAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisBluetoothAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isBluetoothAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisAudioOutputAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isAudioOutputAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisCameraCapabilityManualSensorAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isCameraCapabilityManualSensorAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisIRSensorAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isIRSensorAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisTVSupportAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isTVSupportAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisWifiAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isWifiAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisLocationServiceAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isLocationServiceAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisGPSLocationAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isGPSLocationAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisLocationNetworkAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isLocationNetworkAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisPrintingAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isPrintingAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisLightSensorAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isLightSensorAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisAccelerometerAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isAccelerometerAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisTempratureSensorAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isTempratureSensorAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisBarometerAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isBarometerAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisGyroscopeSensorAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isGyroscopeSensorAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }

    public void testisProximitySensorAvailable(){
        try{
            Context context = getInstrumentation().getContext();
            UsageInfoManager usageInfoManager = new UsageInfoManager(context);
            usageInfoManager.isProximitySensorAvailable();
            assertTrue(true);
        }catch(Exception ex){
            assertTrue(false);
        }
    }
}
