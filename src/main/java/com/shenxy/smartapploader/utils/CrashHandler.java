package com.shenxy.smartapploader.utils;

import android.content.Context;
import android.util.Log;

//import com.chanjet.workcircle.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";

	/** 系统默认的UncaughtException处理类 */ 
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */ 
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */ 
	private Context mContext;
	
	/** 获取CrashHandler实例 ,单例模式*/ 
	public static CrashHandler getInstance() {
		if (INSTANCE == null) { 
			INSTANCE = new CrashHandler();
		} 
		return INSTANCE; 
	} 
	
	/** 
	* 初始化,注册Context对象, 
	* 获取系统默认的UncaughtException处理器, 
	* 设置该CrashHandler为程序的默认处理器 
	* @param ctx 
	*/ 
	public void init(Context ctx) { 
		mContext = ctx; 
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler(); 
		Thread.setDefaultUncaughtExceptionHandler(this); 
	} 
	
	/** 
	* 当UncaughtException发生时会转入该函数来处理 
	*/ 
	@Override 
	public void uncaughtException(final Thread thread, final Throwable ex) {
        new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "CrashHandler  handleException! thread.getName():" + thread.getName()
                        + ", thread.getId():" + thread.getId() + ", thread.getPriority():" + thread.getPriority(), ex);
				saveCrashLog(ex);
            }
        }.start();

		//shenxy 2015-4-1 所有异常都不抛出（可能造成闪退的现象)
		if (!handleException(ex) && mDefaultHandler != null && thread.getName().equals("main")) {
			//如果用户没有处理则让系统默认的异常处理器来处理(弹出“工作圈已停止”的框)
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			//Sleep一会后结束程序 
			try { 
				Thread.sleep(500);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			} 

		}
	} 
	
	/** 
	* 自定义错误处理,收集错误信息
	* @param ex 异常信息
	* @return true:如果处理了该异常信息;否则返回false 
	*/ 
	private boolean handleException(final Throwable ex) {
		//shenxy 友盟统计：这种情况，由于未回调系统默认异常处理，可能会影响友盟umeng抓取错误日志，因此做手工提交
		MobclickAgent.reportError(mContext, ex);
		MobclickAgent.onKillProcess(mContext); //在主动终止进程前，调用该方法，让umeng做推出前的保存

//		saveCrashLog(ex);

		return true; 
	}

	public static void saveCrashLog(Throwable ex) {
		saveExceptionLog(TAG, "Crash", ex);
	}

	public static void saveExceptionLog(String tag, String bugMsg, Throwable ex) {
		try {
			String errorMsg = "";
			if (ex == null) {
				errorMsg = "handleException --- tag=" + tag + ", ex==null";
			} else {
				Writer info = new StringWriter();
				PrintWriter printWriter = new PrintWriter(info);
				ex.printStackTrace(printWriter);
				errorMsg = info.toString();
				printWriter.close();
			}
			//保存crash信息
			LogUtils.T(tag, bugMsg, errorMsg, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

