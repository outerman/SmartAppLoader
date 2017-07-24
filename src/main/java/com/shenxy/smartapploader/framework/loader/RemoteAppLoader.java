package com.shenxy.smartapploader.framework.loader;

import android.content.Intent;

import com.shenxy.smartapploader.app.AppIntentAction;
import com.shenxy.smartapploader.app.AppMainRxActivity;
import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.loaderFactory.RemoteUrlAppLoaderFactory;
import com.shenxy.smartapploader.plugins.AppConfig;
import com.shenxy.smartapploader.utils.StringUtils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.phonegap.app.AppIntentAction;
//import com.chanjet.workcircle.phonegap.app.AppMainRxActivity;
//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.loaderFactory.RemoteUrlAppLoaderFactory;
//import com.chanjet.workcircle.phonegap.plugins.AppConfig;

/**
 * Created by shenxy on 16/4/18.
 * remote模式的轻应用loader
 */
public class RemoteAppLoader extends BaseLoader {
    @Override
    public Observable<Boolean> checkStartInfo() {
        //针对remoteUrl的情况,检查参数,或修改AppStartInfo的内容
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (StringUtils.isBlank(RemoteUrlAppLoaderFactory.getRemoteString(getStartInfo()))) {
                    subscriber.onError(new Throwable("缺少轻应用包的RemoteUrl信息!"));
                    return;
                }

                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> openApp() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                getAppConfig().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<AppConfig>() {
                                       @Override
                                       public void call(AppConfig appConfig) {
                                           startApp(appConfig);
                                           subscriber.onNext(true);
                                           subscriber.onCompleted();
                                       }
                                   },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        subscriber.onNext(false);
                                        subscriber.onCompleted();
                                    }
                                });
            }
        });
    }

    private void startApp(AppConfig appConfig) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AppMainRxActivity.class);
        intent.putExtra(AppIntentAction.APP_TYPE, AppIntentAction.AppType.RemoteUrl.name());
        intent.putExtra(AppIntentAction.APP_ID, String.valueOf(getStartInfo().appId));
        intent.putExtra(AppIntentAction.APP_CONFIG, appConfig);
        intent.putExtra(AppIntentAction.APP_CAN_USE_CROSSWALK, canUseCrosswalk());
        //remote类型的轻应用
        if (getStartInfo().appInfo != null && getStartInfo().appId != LightAppConstants.DEFAULT_REMOTEURL_APPID) {
            intent.putExtra(AppIntentAction.APP_NAME, getStartInfo().appInfo.name);
            intent.putExtra(AppIntentAction.APP_URL, getStartInfo().appInfo.downUrl.APH);
            intent.putExtra(AppIntentAction.APP_ISSHOW_TITLEBAR, getStartInfo().appInfo.navBar);
        } else if (getStartInfo().remoteStartParam != null) {
            intent.putExtra(AppIntentAction.APP_NAME, getStartInfo().remoteStartParam.titleName);
            intent.putExtra(AppIntentAction.APP_URL, getStartInfo().remoteStartParam.remoteUrl);
            intent.putExtra(AppIntentAction.APP_DATA, getStartInfo().remoteStartParam.data);
            intent.putExtra(AppIntentAction.APP_ISSHOW_TITLEBAR, getStartInfo().remoteStartParam.isShowTitleBar);
        }

        getActivity().startActivity(intent);
    }
}
