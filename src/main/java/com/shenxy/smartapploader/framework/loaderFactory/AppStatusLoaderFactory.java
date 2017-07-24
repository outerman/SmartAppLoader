package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.loader.ComingAppLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;

import rx.Observable;
import rx.Subscriber;

//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.ComingAppLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;

/**
 * Created by shenxy on 16/4/25.
 * 根据app的status特殊处理的apploader
 * 1)即将推出
 */
public class AppStatusLoaderFactory implements LoaderCreator {
    @Override
    public boolean isApplicable(Activity activity, AppStartInfo appStartInfo) {
        return appStartInfo != null && appStartInfo.appInfo != null
                && appStartInfo.appInfo.status != null && appStartInfo.appInfo.status == 0;
    }

    @Override
    public Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo) {
        return Observable.create(new Observable.OnSubscribe<BaseLoader>() {
            @Override
            public void call(Subscriber<? super BaseLoader> subscriber) {
                try {
                    BaseLoader appBaseLoader = new ComingAppLoader();

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
