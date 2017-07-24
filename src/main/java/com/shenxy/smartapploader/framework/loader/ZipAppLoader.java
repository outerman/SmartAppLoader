package com.shenxy.smartapploader.framework.loader;

import android.content.Intent;

import com.shenxy.smartapploader.app.AppIntentAction;
import com.shenxy.smartapploader.app.AppMainRxActivity;
import com.shenxy.smartapploader.app.AppZipDownloading;
import com.shenxy.smartapploader.framework.installer.BaseInstaller;
import com.shenxy.smartapploader.framework.installer.ZipInstaller;
import com.shenxy.smartapploader.plugins.AppConfig;
import com.shenxy.smartapploader.utils.StatusEvent;
import com.shenxy.smartapploader.workTmp.BaseApplication;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

//import de.greenrobot.event.EventBus;

//import com.chanjet.im.event.StatusEvent;
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.phonegap.app.AppIntentAction;
//import com.chanjet.workcircle.phonegap.app.AppMainRxActivity;
//import com.chanjet.workcircle.phonegap.app.AppZipDownloading;
//import com.chanjet.workcircle.phonegap.framework.installer.BaseInstaller;
//import com.chanjet.workcircle.phonegap.framework.installer.ZipInstaller;
//import com.chanjet.workcircle.phonegap.plugins.AppConfig;

/**
 * Created by shenxy on 16/4/17.
 * zip包类型的H5轻应用
 */
public class ZipAppLoader extends BaseLoader {
    ReplaySubject<Boolean> replaySubject;

    public ZipAppLoader() {
        EventBus.getDefault().register(this);
    }

    @Override
    public Observable<BaseInstaller> initInstaller() {
        return Observable.create(new Observable.OnSubscribe<BaseInstaller>() {
            @Override
            public void call(Subscriber<? super BaseInstaller> subscriber) {
                subscriber.onNext(new ZipInstaller(ZipAppLoader.this));
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> downloadAndInstall() {
        replaySubject = ReplaySubject.create();

        getInstaller().preinstall();

        isNeedInstall()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isNeedInstall) {
                        if (isNeedInstall) {
                            getInstaller().downloadApp();
                        } else {
                            replaySubject.onNext(true);
                            replaySubject.onCompleted();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        replaySubject.onError(throwable);
                    }
                });

        return replaySubject.asObservable();
    }

    protected Observable<Boolean> isNeedInstall() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(!getInstaller().isInstalled());
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
                                           tryToOpenApp(appConfig);
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

    @Override
    public void releaseLoader() {
        super.releaseLoader();
        EventBus.getDefault().unregister(this);
    }


    //接收安装的事件
    public void onEventAsync(StatusEvent event) {
        if (event == null || event.id == null) {
            return;
        }

        //只处理成功的消息即可，失败的停留在下载界面，用户点击返回
        if (event.eventName.equals(AppZipDownloading.EVENT_ZIP_APP_INSTALL_SUCCEED)
                && event.id != null && event.id.equals(getStartInfo().appId)
                && event.params != null) {
            replaySubject.onNext(true);
            replaySubject.onCompleted();
            replaySubject = null;
        } else if (event.eventName.equals(AppZipDownloading.EVENT_ZIP_APP_INSTALL_FAILED)
                && event.id != null && event.id.equals(getStartInfo().appId)) {
            replaySubject.onNext(false);
            replaySubject.onCompleted();
            replaySubject = null;
        }
    }

    private boolean tryToOpenApp(AppConfig appConfig) {
        try {
            Intent intent = new Intent();
            intent.putExtra(AppIntentAction.APP_NAME, getStartInfo().appInfo.name);
            intent.putExtra(AppIntentAction.APP_ISSHOW_TITLEBAR, getStartInfo().appInfo.navBar);
            intent.putExtra(AppIntentAction.APP_ID, getStartInfo().appId + "");
            intent.putExtra(AppIntentAction.APP_TYPE, AppIntentAction.AppType.LightApp.name());
            intent.putExtra(AppIntentAction.APP_CONFIG, appConfig);
            intent.putExtra(AppIntentAction.APP_CAN_USE_CROSSWALK, canUseCrosswalk());
            intent.setClass(BaseApplication.getContext(), AppMainRxActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//不使用这个参数，因为在魅族等手机上有bug；A、B互相使用该参数启动后，关闭界面退回到桌面
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().startActivity(intent);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
