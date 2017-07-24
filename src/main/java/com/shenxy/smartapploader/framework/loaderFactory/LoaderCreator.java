package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;

import rx.Observable;

//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;
//import rx.Observable;

/**
 * Created by shenxy on 16/4/25.
 * 抽象工厂接口
 */
public interface LoaderCreator {
    boolean isApplicable(Activity activity, AppStartInfo appStartInfo);
    Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo);
}
