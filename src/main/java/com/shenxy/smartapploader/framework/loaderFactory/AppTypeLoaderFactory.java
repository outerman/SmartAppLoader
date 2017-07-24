package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;

import rx.Observable;

//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;

/**
 * Created by shenxy on 16/4/17.
 * AppLoader的工厂类
 */
public class AppTypeLoaderFactory implements LoaderCreator {
    @Override
    public boolean isApplicable(Activity activity, AppStartInfo appStartInfo) {
        return appStartInfo != null && appStartInfo.appInfo != null; //默认的loader
    }

    public Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo) {
        int appType = appStartInfo.appInfo.appType;
        String loaderClassName = LightAppConstants.appTypeLoaderConfig.get(appType);

        return new AppLoaderManager().createAppLoaderByClassName(activity, appStartInfo, loaderClassName);
    }
}
