package com.shenxy.smartapploader.framework.loader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shenxy.smartapploader.AppConstants;
import com.shenxy.smartapploader.auth.AppPluginAuthManager;
import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.installer.BaseInstaller;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;
import com.shenxy.smartapploader.plugins.AppConfig;
import com.shenxy.smartapploader.utils.AppInfoUtil;
import com.shenxy.smartapploader.utils.DeviceInfo;
import com.shenxy.smartapploader.utils.NetworkStatusUtil;
import com.shenxy.smartapploader.workTmp.BaseApplication;

import rx.Observable;
import rx.Subscriber;

//import android.support.annotation.NonNull;
//
//import com.chanjet.im.service.ServerTimeService;
//import com.chanjet.libutil.util.DeviceInfo;
//import com.chanjet.workcircle.AppConstants;
//import com.chanjet.workcircle.Auth.AuthInfo;
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.phonegap.auth.AppPluginAuthManager;
//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.installer.BaseInstaller;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;
//import com.chanjet.workcircle.phonegap.plugins.AppConfig;
//import com.chanjet.workcircle.phonegap.plugins.TWCDVHttpDns;
//import com.chanjet.workcircle.request.OrgListQuery;
//import com.chanjet.workcircle.util.AppInfoUtil;
//import com.chanjet.workcircle.util.NetworkStatusUtil;
//import com.chanjet.workcircle.util.OrgManager;

/**
 * Created by shenxy on 16/4/17.
 * 轻应用加载器基类
 */
public abstract class BaseLoader implements AppLoaderEnv {
    private Activity activity;
    private AppStartInfo startInfo;
    private BaseInstaller installer;

    @Override
    public final Activity getActivity() {
        return activity;
    }

    public final void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public final AppStartInfo getStartInfo() {
        return startInfo;
    }

    public final void setStartInfo(AppStartInfo startInfo) {
        this.startInfo = startInfo;
    }

    public BaseInstaller getInstaller() {
        return installer;
    }

    public void setInstaller(BaseInstaller installer) {
        this.installer = installer;
    }

    /**
     * 1)轻应用加载过程,会依次调用以下方法
     * 2)所有步骤都是异步操作,可以弹框等进行阻断,待有响应(点击弹框)后,再进行下一步处理
     * 3)类中的默认处理:所有步骤都立刻返回true
     */
    //1)检查StartInfo,除了基本的appInfo和appType的检查以外的特定loader相关的检查;如果需要进行修改
    public Observable<Boolean> checkStartInfo() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    //2)检查权限
    public Observable<Boolean> checkAuth() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                //检查插件权限是否已经初始化，如果没有则初始化先
                if (!AppPluginAuthManager.getInstance().isAuthInited(getStartInfo().appId)) {
                    AppPluginAuthManager.getInstance().updateAppPluginAuthConfig(
                            getStartInfo().appId,
                            new AppPluginAuthManager.AppPluginAuthUpdateListener() {
                                @Override
                                public void OnUpdateSucceed() {
                                    subscriber.onNext(true);
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void OnUpdateFailed() {
                                    subscriber.onError(new Throwable("应用权限未初始化!"));
                                }
                            });
                } else {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            }
        });
    }

//    //3)检查并执行预安装(安装包内置的轻应用,耗时,可能有界面)
//    public Observable<Boolean> checkPreInstall() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                subscriber.onNext(true);
//                subscriber.onCompleted();
//            }
//        });
//    }

    //3)初始化该Loader的installer,用于处理下载\安装\更新等相关逻辑
    public Observable<BaseInstaller> initInstaller() {
        return Observable.create(new Observable.OnSubscribe<BaseInstaller>() {
            @Override
            public void call(Subscriber<? super BaseInstaller> subscriber) {
                BaseInstaller defaultInstaller = new BaseInstaller(BaseLoader.this);

                subscriber.onNext(defaultInstaller);
                subscriber.onCompleted();
            }
        });
    }

    //4)下载并安装应用(耗时,可能有界面)
    public Observable<Boolean> downloadAndInstall() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    //5)打开应用
    public Observable<Boolean> openApp() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    //6)结束Loader前做释放处理
    public void releaseLoader() {
        this.activity = null;
        this.startInfo = null;
    }

//    @NonNull
    protected Observable<AppConfig> getAppConfig() {
        return Observable.create(new Observable.OnSubscribe<AppConfig>() {
            @Override
            public void call(Subscriber<? super AppConfig> subscriber) {
                subscriber.onNext(getBaseAppConfig());
                subscriber.onCompleted();
            }
        });
    }

    private AppConfig getBaseAppConfig() {
        AppConfig appConfig = getNewAppConfig();
        appConfig.data = new AppConfig.AppConfigData();
        appConfig.data.appId = getStartInfo().appId;

//        appConfig.data.domain = TWCDVHttpDns.switchToHttpDNS(AppConstants.getHOST());
//        appConfig.data.auth = AuthInfo.getAuth();
//        appConfig.data.accessToken = AuthInfo.getInstance().getAccessToken();
//        appConfig.data.back_to_native = getStartInfo().backToNative;
//        appConfig.data.entId = getStartInfo().entId;
//        appConfig.data.page = getStartInfo().page;
//        appConfig.data.tab = getStartInfo().tab;
//        appConfig.data.userid = AuthInfo.getInstance().getBid() + "";
//        appConfig.data.Host = AppConstants.getHOST().toLowerCase(Locale.CHINA).replace("http://", "").replace("https://", "");
//        if (getStartInfo().appInfo != null) {
//            appConfig.data.platformExtension = getStartInfo().appInfo.platformExtension;
//            appConfig.data.startTimestamp=getStartInfo().appInfo.startTimestamp;
//            appConfig.data.endTimestamp=getStartInfo().appInfo.endTimestamp;
//        }
//        appConfig.data.currentTimeStamp = ServerTimeService.getInstance().getServerTimeStamp();
//        com.chanjet.core.utils.Log.d("AppManager", "PlatformExtension:" + appConfig.data.platformExtension);
//        //shenxy 2015-4-8 增加字段，供轻应用显示
//        List<OrgListQuery.OrgInfo> orgListAtLogin = OrgManager.getInstance().getOrgInfoList();
//        for (OrgListQuery.OrgInfo orgInfo : orgListAtLogin) {
//            if (orgInfo.orgId.equals(getStartInfo().entId)) {
//                appConfig.data.entName = orgInfo.orgFullName;
//                appConfig.data.entAvatar = orgInfo.orgLogo;
//                break;
//            }
//        }
//        appConfig.data.userName = AuthInfo.getInstance().getName();
//        appConfig.data.userAvatar = AuthInfo.getInstance().getHeadPic();

        appConfig.data.version = AppInfoUtil.getVersionName(getActivity());
        appConfig.data.deviceInfo.deviceId = DeviceInfo.getDeviceId(getActivity());
        appConfig.data.deviceInfo.deviceType = AppConstants.CLIENT_ID_HEAD;
        appConfig.data.deviceInfo.manufacturer = android.os.Build.MANUFACTURER;
        appConfig.data.deviceInfo.model = android.os.Build.MODEL;
        appConfig.data.deviceInfo.osRelease = android.os.Build.VERSION.RELEASE;
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            appConfig.data.deviceInfo.networkStatus = NetworkStatusUtil.getCordovaNetworkStatus(networkInfo.getType(), networkInfo.getSubtype()).name();
        } else {
            appConfig.data.deviceInfo.networkStatus = NetworkStatusUtil.CordovaNetworkStatus.NoNetwork.name();
        }
        appConfig.data.deviceInfo.usedMemory = Runtime.getRuntime().maxMemory() + "";
        appConfig.data.deviceInfo.availableMemory = Runtime.getRuntime().totalMemory() + "";
        if (getStartInfo().workMsgInfo != null) {
            appConfig.data.params.id = getStartInfo().workMsgInfo.resId + "";
            appConfig.data.ext = getStartInfo().workMsgInfo.ext;
        }
        return appConfig;
    }

    protected boolean canUseCrosswalk() {
        return //getStartInfo().appId != LightAppConstants.MAIL_263_APPID &&
                (getStartInfo() == null
                        || getStartInfo().appInfo == null
                        || getStartInfo().appInfo.extension == null
                        || getStartInfo().appInfo.extension.useCrossWalk == LightAppConstants.CAN_USE_CROSSWALK_DEFAULT);

    }

    protected final AppConfig getNewAppConfig() {
        AppConfig appConfig = new AppConfig();
        appConfig.code = 0;
        appConfig.msg = "";
        appConfig.data = new AppConfig.AppConfigData();

        return appConfig;
    }
}
