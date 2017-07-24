package com.shenxy.smartapploader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by David on 14-1-14.
 */
public class DeviceInfo {
    private static final Object lockObject = new Object();

    public static String getDeviceId(Context context) {

        String ID = null;
        String ID_SDCARD = "";

        synchronized (lockObject) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(AppUtilConstants.PREF, Context.MODE_WORLD_READABLE /*Context.MODE_PRIVATE*/);
            SharedPreferences.Editor editor = sharedPrefs.edit();

/*            if (sharedPrefs.contains(AppUtilConstants.DEVICEID)) {
                ID = sharedPrefs.getString(AppUtilConstants.DEVICEID, null);
            }

            if (ID == null || ID.length() <= 4 || isAllZeroString(ID)) {
                TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                try {
                    ID = telephony.getDeviceId();
                    ID = ID.replaceAll("[^0-9a-zA-Z]","");//除去所有非数字和字母的字符
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (ID == null || ID.length() <= 4 || isAllZeroString(ID)) {
                    ID = UUID.randomUUID().toString();
                }

            }

            editor.putString(AppUtilConstants.DEVICEID, ID);
            editor.commit();*/

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File deviceIdFile = new File(Environment.getExternalStorageDirectory(),"SystemID");
                if (deviceIdFile.exists()) {
                    try {
                        FileInputStream in = new FileInputStream(deviceIdFile);
                        //将文件内容全部读入到byte数组

                        int length = (int) deviceIdFile.length();

                        byte[] temp = new byte[length];

                        in.read(temp, 0, length);

                        //将byte数组用UTF-8编码并存入display字符串中
                        //ID =  EncodingUtils.getString(temp, "UTF-8");

                        ID_SDCARD = new String(temp,"UTF-8");

                        //关闭文件file的InputStream
                        in.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (sharedPrefs.contains(AppUtilConstants.DEVICEID)) {
                ID = sharedPrefs.getString(AppUtilConstants.DEVICEID, null);
                if (!ID_SDCARD.equalsIgnoreCase(ID) && !ID_SDCARD.equals("")){
                    editor.putString(AppUtilConstants.DEVICEID, ID_SDCARD);
                    editor.commit();
                    return ID_SDCARD;
                }
            }else{
                ID = ID_SDCARD;
            }

            if (ID == null || ID.length() <= 4 || isAllZeroString(ID)) {
                TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                try {
                    ID = telephony.getDeviceId();
                    ID = ID.replaceAll("[^0-9a-zA-Z]","");//除去所有非数字和字母的字符
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (ID == null || ID.length() <= 4 || isAllZeroString(ID)) {
                    ID = UUID.randomUUID().toString();
                }

            }

            editor.putString(AppUtilConstants.DEVICEID, ID);
            editor.commit();

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //执行存储sdcard方法
                File f = new File(Environment.getExternalStorageDirectory(),"SystemID");
                if (!f.exists()) {
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(f,true);
                        out.write(ID.getBytes("UTF-8"));
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    f.setReadOnly();
                }


            }

        }

        return ID;

    }

    public static boolean isAllZeroString(String str) {

        int len = str.length();
        for (int i = 0; i < len-1; i++) {
            if(!str.substring(i, i+1).equalsIgnoreCase("0")) {
                return false;
            }
        }

        return true;
    }
}
