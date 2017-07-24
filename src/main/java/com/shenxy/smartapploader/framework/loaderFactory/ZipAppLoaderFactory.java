package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.loader.ZipAppLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;

import rx.Observable;
import rx.Subscriber;

//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.ZipAppLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.ZipForTPlusAppLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;

/**
 * Created by shenxy on 16/5/20.
 * ZipAppLoader的工厂类
 */
public class ZipAppLoaderFactory implements LoaderCreator {
    @Override
    public boolean isApplicable(Activity activity, AppStartInfo appStartInfo) {
        return appStartInfo != null && appStartInfo.appInfo != null && appStartInfo.appInfo.appType == LightAppConstants.APP_TYPE_ZIP;
    }

    public Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo) {
        return Observable.create(new Observable.OnSubscribe<BaseLoader>() {
            @Override
            public void call(Subscriber<? super BaseLoader> subscriber) {
                try {
                    BaseLoader appBaseLoader = null;
                    int appType = appStartInfo.appInfo.appType;

                    if (appType == LightAppConstants.APP_TYPE_ZIP) {
//                        if (appStartInfo.appInfo.version == LightAppConstants.TPLUS_SELF_UPDATE_CHECK_VERSION) {
//                            appBaseLoader = new ZipForTPlusAppLoader();
//                        } else {
                            appBaseLoader = new ZipAppLoader();
//                        }
                    }

                    subscriber.onNext(appBaseLoader);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }
            }
        });
    }
}
