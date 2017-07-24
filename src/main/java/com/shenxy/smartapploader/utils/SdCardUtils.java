package com.shenxy.smartapploader.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

//import com.chanjet.app.common.local.activity.TW_ChatActivity;

public class SdCardUtils {
	private static final String LOGTAG = "SdCardUtils";//LogUtils.makeLogTag(SdCardUtils.class);
	private static Reader m_Fr;
    private static BufferedReader m_Readbuf;
    
	/***************************************************
     * 检测SDCard是否可写
     */
    public static boolean isSdcardWritable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
    /***************************************************
     * 检测SDCard是否可读
     */
    public static boolean isSdcardReadable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
                || Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

	/**
	 * 检查sd卡上是否有足够的空间，以供下载轻应用等附件
	 */
	public static boolean isSdcardEnoughStorage(String filePath){
		if (StringUtils.isNotBlank(filePath)) {
			long availableStorage = getAvailableStore(filePath);
			if (availableStorage > 1000000) { //至少1M空间
				return true;
			} else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	/**
 　　* 获取存储卡的剩余容量，单位为字节
 　　* @param filePath
 　　* @return availableSpare
 　　*/
	public static long getAvailableStore(String filePath) {
		// 取得sdcard文件路径
		StatFs statFs = new StatFs(filePath);
		// 获取block的SIZE
		long blocSize = statFs.getBlockSize();
		// 获取BLOCK数量
		// long totalBlocks = statFs.getBlockCount();
		// 可使用的Block的数量
		long availaBlock = statFs.getAvailableBlocks();
		// long total = totalBlocks * blocSize;
		long availableSpare = availaBlock * blocSize;
		return availableSpare;
	}

    
  	/**
  	 * 读取文本文件，输出为String
  	 * @param filepath
  	 * @return
  	 */
  	public static String ReadStreamFile(String filepath)
  	{
  		//String str = Environment.getExternalStorageDirectory() + "/" + "test.apk";
  		//str == "mnt/sdcard/test.apk";
      	File file = new File(filepath);
      	String strRet = "";
  		if(!file.exists())
  		{
  			Log.d(LOGTAG, "ReadStreamFile:file not exists!");
  			return null;
  		}
  		try
  		{
//  			m_Fr = new FileReader(filepath);
			m_Fr = new InputStreamReader(new FileInputStream(filepath), "UTF-8");
  			m_Readbuf = new BufferedReader(m_Fr); 
  			String str ;
  			
  			while((str=m_Readbuf.readLine()) != null)     
  			{         
  				strRet +=str;
  			}   
  		}   
  		catch (IOException ex) 
  		{    
  			Log.e(LOGTAG, "ReadStreamFile:file:IOException!", ex);
  			return null;
  		}
      	return strRet;
      }
  	 
  	/**
  	 * 写入文本文件，输入 为String类型
  	 * @param filepath
  	 * @return
  	 */
  	public static boolean WriteFile(String filepath, String strContent)
  	{
  		//String str = Environment.getExternalStorageDirectory() + "/" + "test.apk";
  		//str == "mnt/sdcard/test.apk";
      	File file = new File(filepath);
      	
//      	String strRet = null;
  		
  		try
  		{
  			if(!file.exists())
  	  		{
  				File destDir = new File(filepath.substring(0, filepath.lastIndexOf("/")));
  				if (!destDir.exists()) {
  					destDir.mkdirs();
  				}
  			  
  	  			file.createNewFile();
  	  			Log.d(LOGTAG, "WriteFile not exists,create it!");
  	  		}
//  			FileWriter m_Fw = new FileWriter(file);
			OutputStreamWriter m_Fw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
  			if (StringUtils.isNotBlank(strContent)){
  				m_Fw.write(strContent);
  				m_Fw.flush();
  			}
  			  
  		}   
  		catch (IOException ex) 
  		{    
  			Log.e(LOGTAG, "ReadStreamFile:file:IOException!", ex);
  			return false;
  		}
      	return true;
      }
}
