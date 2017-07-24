//package com.shenxy.smartapploader.framework.loader;
//
//import android.content.Intent;
//import android.os.RemoteException;
//
//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.phonegap.framework.installer.BaseInstaller;
//import com.chanjet.workcircle.phonegap.framework.installer.ReactNativeInstaller;
//import com.chanjet.workcircle.phonegap.framework.installer.ZipInstaller;
//import com.chanjet.workcircle.phonegap.plugins.AppConfig;
//import com.chanjet.workcircle.util.ThreadPoolUtil;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;
//import rx.subjects.ReplaySubject;
//
///**
// * Created by shenxy on 16/5/16
// * ReactNative类型的轻应用,与Zip包方式类似(都是加载离线js).
// * 差别:
// * 1)加载js前,需要检查RN的运行时环境(以NativePlugin类型的app形式存在)——在installer中解决
// * 2)直接用NativePlugin类的运行时app,加载js(index.android.js).而不是index.html——在loader中解决
// */
//public class ReactNativeAppLoader extends NativePluginAppLoader {
//
//    @Override
//    public Observable<Boolean> checkStartInfo() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                if (getStartInfo().appInfo == null || getStartInfo().appInfo.extension == null
//                        || StringUtils.isBlank(getStartInfo().appInfo.extension.runtimePackage)
//                        || StringUtils.isBlank(getStartInfo().appInfo.extension.runtimeDownloadUrl)
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
//                subscriber.onNext(new ReactNativeInstaller(ReactNativeAppLoader.this));
//                subscriber.onCompleted();
//            }
//        });
//    }
//
//    @Override
//    public Observable<Boolean> downloadAndInstall() {
////        getInstaller().preinstall();
////
////        if (!getInstaller().isInstalled()) {
////            getInstaller().downloadApp();
////            //这种类型app,由于安装包比较大,所以不做阻塞式下载.
////            //此处启动下载即可,下次打开时,如果已经下载完成,则自动安装
////
////            //如果是RN的模式,下载NativePlugin的运行时包,如果运行时未安装,则不再往下走;如果运行时已安装,js包未下载,则继续往下走,去下载js包
////            if (!((ReactNativeInstaller) getInstaller()).getRuntimeInstaller().isInstalled()) {
////                return Observable.create(new Observable.OnSubscribe<Boolean>() {
////                    @Override
////                    public void call(Subscriber<? super Boolean> subscriber) {
////                        subscriber.onNext(false);
////                        subscriber.onCompleted();
////                    }
////                });
////            }
////        }
////        return super.downloadAndInstall();
//
//
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
//
//                    //如果是RN的模式,下载NativePlugin的运行时包,如果运行时未安装,则不再往下走;如果运行时已安装,js包未下载,则继续往下走,去下载js包
//                    if (!((ReactNativeInstaller) getInstaller()).getRuntimeInstaller().isInstalled()) {
//                        replaySubject.onNext(false);
//                        replaySubject.onCompleted();
//                        return;
//                    }
//                }
//                replaySubject.onNext(true);
//                replaySubject.onCompleted();
//            }
//        });
//
//        return replaySubject.asObservable();
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
//                                               openDroidPlugin(getActivity(), getStartInfo().appInfo.extension.runtimePackage, appConfig);
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
//                                    }
//                                });
//            }
//        });
//    }
//
//    @Override
//    protected void additionalIntentExtras(Intent intent) {
//        super.additionalIntentExtras(intent);
//        if (!getStartInfo().isRNRemote) {
//            intent.putExtra("launchFilePath", ZipInstaller.getInstallRoot() + getStartInfo().appId + "/index.android.js");
//        }
//    }
//}
