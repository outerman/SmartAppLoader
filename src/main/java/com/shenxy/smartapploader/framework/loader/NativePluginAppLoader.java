//package com.shenxy.smartapploader.framework.loader;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.RemoteException;
//import android.widget.Toast;
//
//import com.shenxy.smartapploader.framework.LightAppConstants;
//import com.shenxy.smartapploader.framework.installer.BaseInstaller;
//import com.shenxy.smartapploader.framework.installer.NativePluginForTPlusInstaller;
//import com.shenxy.smartapploader.framework.installer.NativePluginInstaller;
//import com.shenxy.smartapploader.plugins.AppConfig;
//import com.shenxy.smartapploader.utils.StringUtils;
//
//import java.util.List;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;
//import rx.subjects.ReplaySubject;
//
////import com.chanjet.core.Message;
////import com.chanjet.core.MessageListener;
////import com.chanjet.core.utils.JSONExtension;
////import com.chanjet.libutil.util.StringUtils;
////import com.chanjet.workcircle.Auth.AuthInfo;
////import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
////import com.chanjet.workcircle.phonegap.framework.installer.BaseInstaller;
////import com.chanjet.workcircle.phonegap.framework.installer.NativePluginForTPlusInstaller;
////import com.chanjet.workcircle.phonegap.framework.installer.NativePluginInstaller;
////import com.chanjet.workcircle.phonegap.plugins.AppConfig;
////import com.chanjet.workcircle.request.GetCspAuthCodeQuery;
////import com.chanjet.workcircle.util.OrgManager;
////import com.chanjet.workcircle.util.ThreadPoolUtil;
////import com.morgoo.droidplugin.pm.PluginManager;
//
///**
// * Created by shenxy on 16/4/18.
// * native的app插件类型的轻应用loader
// */
//public class NativePluginAppLoader extends BaseLoader {
//
//    @Override
//    public Observable<Boolean> checkStartInfo() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                if (getStartInfo().appInfo == null || getStartInfo().appInfo.extension == null
//                        || StringUtils.isBlank(getStartInfo().appInfo.extension.androidPackage)
//                        || StringUtils.isBlank(getStartInfo().appInfo.extension.androidClass)
//                        || getStartInfo().appInfo.downUrl == null
//                        || StringUtils.isBlank(getStartInfo().appInfo.downUrl.APH)) {
//                    subscriber.onError(new Throwable("缺少轻应用包信息!"));
//                    return;
//                }
//
//                subscriber.onNext(true);
//                subscriber.onCompleted();
//            }
//        });
//    }
//
//    @Override
//    public Observable<BaseInstaller> initInstaller() {
//        return Observable.create(new Observable.OnSubscribe<BaseInstaller>() {
//            @Override
//            public void call(Subscriber<? super BaseInstaller> subscriber) {
//                if (getStartInfo().appInfo.version == LightAppConstants.TPLUS_SELF_UPDATE_CHECK_VERSION) {
//                    subscriber.onNext(new NativePluginForTPlusInstaller(NativePluginAppLoader.this));
//                } else {
//                    subscriber.onNext(new NativePluginInstaller(NativePluginAppLoader.this));
//                }
//                subscriber.onCompleted();
//            }
//        });
//    }
//
//    @Override
//    public Observable<Boolean> downloadAndInstall() {
//        ReplaySubject<Boolean> replaySubject = ReplaySubject.create();
//
//        //外部调用统一在主线程,此处特殊处理为在子线程,所以没用subscriberOn来进行调度
//        ThreadPoolUtil.getInstance().getExecutorService().execute(new Runnable() {
//            @Override
//            public void run() {
//                getInstaller().preinstall();
//
//                if (!getInstaller().isInstalled()) {
//                    getInstaller().downloadApp();
//                    //这种类型app,由于安装包比较大,所以不做阻塞式下载.
//                    //此处启动下载即可,下次打开时,如果已经下载完成,则自动安装
//                }
//
//                replaySubject.onNext(true);
//                replaySubject.onCompleted();
//            }
//        });
//
//        return replaySubject.asObservable();
//
////        return Observable.create(new Observable.OnSubscribe<Boolean>() {
////            @Override
////            public void call(Subscriber<? super Boolean> subscriber) {
////
////                subscriber.onNext(true);
////                subscriber.onCompleted();
////            }
////        });
//    }
//
//    @Override
//    public Observable<Boolean> openApp() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                getAppConfig().subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<AppConfig>() {
//                                       @Override
//                                       public void call(AppConfig appConfig) {
//                                           try {
//                                               openDroidPlugin(getActivity(), getStartInfo().appInfo.extension.androidPackage, appConfig);
//                                               subscriber.onNext(true);
//                                           } catch (RemoteException e) {
//                                               e.printStackTrace();
//                                               subscriber.onNext(false);
//                                           }
//                                           subscriber.onCompleted();
//                                       }
//                                   },
//                                new Action1<Throwable>() {
//                                    @Override
//                                    public void call(Throwable throwable) {
//                                        subscriber.onNext(false);
//                                        subscriber.onCompleted();
//
//                                    }
//                                });
//
//            }
//        });
//    }
//
//    protected void openDroidPlugin(Activity activity, String packageName, AppConfig appConfig) throws RemoteException {
//        List<PackageInfo> infos = PluginManager.getInstance().getInstalledPackages(0);
//        if (infos != null && infos.size() > 0) {
//            for (PackageInfo info : infos) {
//                if (info.packageName.equals(packageName)) {
//                    getCSPCodeAndStartActivity(activity, info, appConfig);
//                }
//            }
//        }
//    }
//
//    private void getCSPCodeAndStartActivity(Activity activity, PackageInfo packageInfo, AppConfig appConfig) {
//        //需要先拼装好intent,因为等query异步返回的时候,getStartInfo等上下文信息,可能已经被销毁
//        PackageManager pm = activity.getPackageManager();
//        Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
//        intent.putExtra("appId", getStartInfo().appId);
//        intent.putExtra("bid", AuthInfo.getInstance().getBid());
//        intent.putExtra("account", AuthInfo.getInstance().getUser());
//        intent.putExtra("orgId", OrgManager.getInstance().getCurrentOrgInfo() == null ? "" : OrgManager.getInstance().getCurrentOrgInfo().orgId);//orgId);
//        intent.putExtra("orgName", OrgManager.getInstance().getCurrentOrgInfo() == null ? "" : OrgManager.getInstance().getCurrentOrgInfo().orgFullName);//orgFullName);
//        intent.putExtra("userName", AuthInfo.getInstance().getUserName());
//        intent.putExtra("config", JSONExtension.toJSONString(appConfig));
//        additionalIntentExtras(intent);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        final GetCspAuthCodeQuery req = new GetCspAuthCodeQuery();
//        req.setWhenUpdate(new MessageListener() {
//            @Override
//            public void process(Message msg) {
//                if (msg.isSucceed()) {
//                    if (req.getResp() == null || req.getResp().code != 0 || req.getResp().data == null) {
//                        Toast.makeText(activity,
//                                req.getResp() == null ? "获取CSP单点登录信息,服务端错误!" : req.getResp().msg,
//                                Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    intent.putExtra("code", req.getResp().data.auth_code);
//
//                    activity.startActivity(intent);
//                } else if (msg.isFailed()) {
//                    Toast.makeText(activity, "获取CSP单点登录信息失败!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        req.send();
//    }
//
//    //给子类添加额外参数的机会
//    protected void additionalIntentExtras(Intent intent) {
//
//    }
//
//}
