package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.loader.ZipAppLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;
import com.shenxy.smartapploader.request.MyWorkQuery;

import rx.Observable;
import rx.Subscriber;

//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.installer.NativeThirdpartyApkInstall;
//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.ThirdPartyNativeAppLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.ZipAppLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;
//import com.chanjet.workcircle.request.MyWorkQuery;

/**
 * Created by shenxy on 16/4/25.
 */
public class ThirdPartyAppLoaderFactory implements LoaderCreator {
    @Override
    public boolean isApplicable(Activity activity, AppStartInfo appStartInfo) {
        return appStartInfo != null && appStartInfo.appInfo != null && appStartInfo.appInfo.appType == LightAppConstants.APP_TYPE_THIRD_PARTY_APP;
    }

    @Override
    public Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo) {
        return Observable.create(new Observable.OnSubscribe<BaseLoader>() {
            @Override
            public void call(Subscriber<? super BaseLoader> subscriber) {
                MyWorkQuery.MyWorkItem appInfo = appStartInfo.appInfo;
                if (appInfo == null || appInfo.extension == null) {
                    subscriber.onError(new Throwable("缺少必要参数"));
                    return;
                }

                String className = appInfo.extension.androidClass;//"com.chanjet.csp.customer.ui.SplashActivity";
                String packageName = appInfo.extension.androidPackage;//"com.chanjet.csp.customer";
//                if (NativeThirdpartyApkInstall.isApkInstalled(packageName)) {
//                    subscriber.onNext(new ThirdPartyNativeAppLoader());
//                } else {
                    subscriber.onNext(new ZipAppLoader());
//                }
                subscriber.onCompleted();
            }
        });
    }
}
