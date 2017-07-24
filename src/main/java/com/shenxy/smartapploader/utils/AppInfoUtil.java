package com.shenxy.smartapploader.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.shenxy.smartapploader.workTmp.BaseApplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 应用信息
 *
 * @author zhongcw 2013-8-27
 */
public class AppInfoUtil {
    public static String getPacketName(Context context) {
        return context.getPackageName();
    }

    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = "";
        if (packInfo != null) {
           version = packInfo.versionName;
        }
        return version;
    }


    // 获取CPU名字
    public static String getCpuName() {
        try {
//            FileReader fr = new FileReader("/proc/cpuinfo");
            InputStreamReader fr = new InputStreamReader(new FileInputStream("/proc/cpuinfo"),"UTF-8");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            String[] array = text.split(":\\s+", 2);
            if (array.length >= 2) {
                return array[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2;
        try {
//            FileReader fr = new FileReader(str1);
            InputStreamReader fr = new InputStreamReader(new FileInputStream(str1),"UTF-8");
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Log.i("AppInfoUtil", "---" + str2);
                if (str2.contains("MemTotal")){
                    return str2.replaceAll("(?i)"+"MemTotal","").replaceAll("(?i)"+":","").trim();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

//    public long[] getRomMemroy() {
//        long[] romInfo = new long[2];
//        //Total rom memory
//        romInfo[0] = getTotalInternalMemorySize();
//
//        //Available rom memory
//        File path = Environment.getDataDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSize();
//        long availableBlocks = stat.getAvailableBlocks();
//        romInfo[1] = blockSize * availableBlocks;
////        getVersion();
//        return romInfo;
//    }
//    //rom大小
//    public static long getTotalInternalMemorySize() {
//        File path = Environment.getDataDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSize();
//        long totalBlocks = stat.getBlockCount();
//        return totalBlocks * blockSize;
//    }

    //手机型号
    public static String getMachineModel(){
        return Build.MODEL;
    }
    /**
     * 是否进入后台
     */
    public static boolean isOpenCurApp() {
        ActivityManager am =  (ActivityManager) BaseApplication.context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessesLst = am.getRunningAppProcesses();
        Log.d("isOpenCurApp"," appProcessesLst size= "+appProcessesLst.size());
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessesLst) {
            Log.d("isOpenCurApp"," appProcess.processName ="+appProcess.processName);
            if (appProcess.processName.equals(BaseApplication.context.getPackageName())) {
                Log.d("isOpenCurApp"," appProcess.importance ="+appProcess.importance );
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcess.importance==ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    return true;
                }else{
                    return  false;
                }
            }
        }
        return false;
    }
    public static  int getCurAppImportance(){
        ActivityManager am =  (ActivityManager) BaseApplication.context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessesLst = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessesLst) {
            Log.d("isOpenCurApp", " appProcess.processName =" + appProcess.processName);
            if (appProcess.processName.equals(BaseApplication.context.getPackageName())) {
                return appProcess.importance;
             }
        }
        return -1;
     }
    public static void openApp(String packageName){
        final PackageManager pm = BaseApplication.context.getPackageManager();
        Intent i = pm.getLaunchIntentForPackage(packageName);
        if (i != null){
            BaseApplication.context.startActivity(i);
        }
    }
}
