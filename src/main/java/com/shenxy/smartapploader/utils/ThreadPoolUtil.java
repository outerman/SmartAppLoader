package com.shenxy.smartapploader.utils;

import android.content.Context;

import com.shenxy.smartapploader.workTmp.BaseApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//import com.chanjet.app.business.im.iMate.model.SingletonFinalizer;
//import com.chanjet.workcircle.application.BaseApplication;

/**
 * 线程池
 * 
 * @author shenxy
 *
 */
public class ThreadPoolUtil {//implements SingletonFinalizer {
	private static final String LOGTAG = "ThreadPoolUtil";//LogUtils.makeLogTag(ThreadPoolUtil.class);
	private Context context ;
	private static ThreadPoolUtil singleton;
	private int ThreadCount = 5;	//线程池的线程数
	private int ThreadCountForImage = 5;	//线程池的线程数
	private ExecutorService executorService;
	
	private ExecutorService executorServiceForImage;

	private ScheduledExecutorService scheduledExecutorService;
	
	private ThreadPoolUtil(Context context){
		this.context = context;
		setExecutorService(Executors.newCachedThreadPool());
		setExecutorServiceForImage(Executors.newFixedThreadPool(ThreadCountForImage));
		setScheduledExecutorService(Executors.newScheduledThreadPool(ThreadCount));
	}
	private ThreadPoolUtil() {
		this.context = BaseApplication.getContext();
		setExecutorService(Executors.newFixedThreadPool(ThreadCount));
		setExecutorServiceForImage(Executors.newFixedThreadPool(ThreadCountForImage));
		setScheduledExecutorService(Executors.newScheduledThreadPool(ThreadCount));
	}
	
	public static synchronized ThreadPoolUtil getInstance(){
		if (singleton == null)
        {
            singleton = new ThreadPoolUtil();
//            ((BaseApplication) BaseApplication.getContext()).addObject(singleton);
        }
        return singleton;
	}

	public static void setSingleton(ThreadPoolUtil singleton) {
		ThreadPoolUtil.singleton = singleton;
	}

//	@Override
//	public void FinalizeSingleton() {
////		singleton = null;
//		setSingleton(null);
//	}

	public ExecutorService getExecutorService() {
		return executorService;
	}
	private void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
	public ExecutorService getExecutorServiceForImage() {
		return executorServiceForImage;
	}
	private void setExecutorServiceForImage(ExecutorService executorServiceForImage) {
		this.executorServiceForImage = executorServiceForImage;
	}
	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}
	public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}
}
