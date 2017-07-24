package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.loader.RemoteAppLoader;
import com.shenxy.smartapploader.framework.loader.RemoteDownloadAppLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;
import com.shenxy.smartapploader.utils.StringUtils;
import com.shenxy.smartapploader.utils.WeiboURLClickSpan;

import rx.Observable;
import rx.Subscriber;

//import android.support.annotation.Nullable;
//
//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.RemoteAppLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.RemoteDownloadAppLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;
//import com.chanjet.workcircle.util.WeiboURLClickSpan;

/**
 * Created by shenxy on 16/6/14.
 * RemoteUrlAppLoader的工厂类
 * 可能用webview打开页面,可能用系统浏览器打开下载文件
 */
public class RemoteUrlAppLoaderFactory implements LoaderCreator {
    @Override
    public boolean isApplicable(Activity activity, AppStartInfo appStartInfo) {
        return appStartInfo != null && appStartInfo.appInfo != null && appStartInfo.appInfo.appType == LightAppConstants.APP_TYPE_REMOTE_URL;
    }

    public Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo) {
        return Observable.create(new Observable.OnSubscribe<BaseLoader>() {
            @Override
            public void call(Subscriber<? super BaseLoader> subscriber) {
                try {
                    BaseLoader appBaseLoader = null;

                    String remoteUrl = getRemoteString(appStartInfo);

                    if (StringUtils.isNotBlank(remoteUrl)) {
                        if (WeiboURLClickSpan.isOpenInSystemBroswer(remoteUrl)) {
                            appBaseLoader = new RemoteDownloadAppLoader();
                        } else {
                            appBaseLoader = new RemoteAppLoader();
                        }
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

    public static String getRemoteString(AppStartInfo appStartInfo) {
        if (appStartInfo != null && appStartInfo.appInfo != null && appStartInfo.appInfo.downUrl != null
            && StringUtils.isNotBlank(appStartInfo.appInfo.downUrl.APH)) {

            return appStartInfo.appInfo.downUrl.APH;
        }
        else if (appStartInfo != null && appStartInfo.remoteStartParam != null
                && StringUtils.isNotBlank(appStartInfo.remoteStartParam.remoteUrl)) {

            return appStartInfo.remoteStartParam.remoteUrl;
        }
        return null;
    }
}
