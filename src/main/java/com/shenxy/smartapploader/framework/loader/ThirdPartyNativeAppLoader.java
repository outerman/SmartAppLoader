//package com.shenxy.smartapploader.framework.loader;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//
//import com.shenxy.smartapploader.framework.installer.BaseInstaller;
//import com.shenxy.smartapploader.framework.installer.NativeThirdpartyApkInstall;
//import com.shenxy.smartapploader.request.MyWorkQuery;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.functions.Action1;
//
////import com.chanjet.core.Message;
////import com.chanjet.core.MessageListener;
////import com.chanjet.libutil.util.MD5Util;
////import com.chanjet.workcircle.Auth.AuthInfo;
////import com.chanjet.workcircle.R;
////import com.chanjet.workcircle.phonegap.framework.installer.BaseInstaller;
////import com.chanjet.workcircle.phonegap.framework.installer.NativeThirdpartyApkInstall;
////import com.chanjet.workcircle.request.GetCspAuthCodeQuery;
////import com.chanjet.workcircle.request.MyWorkQuery;
////import com.chanjet.workcircle.util.OrgManager;
//
///**
// * Created by shenxy on 16/4/18.
// * 第三方的apk类型的单点登录app的loader
// */
//public class ThirdPartyNativeAppLoader extends BaseLoader {
//    @Override
//    public Observable<Boolean> checkStartInfo() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                if (getStartInfo().appInfo == null || getStartInfo().appInfo.extension == null) {
//                    subscriber.onError(new Throwable("没有轻应用信息!"));
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
//                subscriber.onNext(new NativeThirdpartyApkInstall(ThirdPartyNativeAppLoader.this));
//                subscriber.onCompleted();
//            }
//        });
//    }
//
//    @Override
//    public Observable<Boolean> openApp() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                MyWorkQuery.MyWorkItem appInfo = getStartInfo().appInfo;
//
//                String className = appInfo.extension.androidClass;//"com.chanjet.csp.customer.ui.SplashActivity";
//                String packageName = appInfo.extension.androidPackage;//"com.chanjet.csp.customer";
//                if (getInstaller().isInstalled()) {
//                    startApk(getActivity(), packageName, className)
//                            .subscribe(new Action1<Boolean>() {
//                                @Override
//                                public void call(Boolean isStarted) {
//                                    subscriber.onNext(isStarted);
//                                    subscriber.onCompleted();
//                                }
//                            }, new Action1<Throwable>() {
//                                @Override
//                                public void call(Throwable throwable) {
//                                    subscriber.onError(throwable);
//                                }
//                            });
//
//                } else {
//                    subscriber.onError(new Throwable("还没有安装该三方应用!"));
//                }
//            }
//        });
//    }
//
//    public Observable<Boolean> startApk(final Context ccontext, final String packageName, final String className) {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                //获取ticket
//                final GetCspAuthCodeQuery req = new GetCspAuthCodeQuery();
//                req.setWhenUpdate(new MessageListener() {
//                    @Override
//                    public void process(Message msg) {
//                        if (msg.isSucceed()) {
//                            if (req.getResp() != null) {
//                                if (req.getResp().code != 0) {
//                                    subscriber.onError(new Throwable(req.getResp().msg));
//                                } else {
//                                    try {
//                                        String code = req.getResp().data.auth_code;
//                                        ComponentName cn = new ComponentName(packageName, className);
//                                        Intent intent = new Intent();
////                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                                        intent.setComponent(cn);
//                                        intent.putExtra("appId", getStartInfo().appId);
//                                        intent.putExtra("bid", AuthInfo.getInstance().getBid());
//                                        intent.putExtra("account", AuthInfo.getInstance().getUser());
//                                        intent.putExtra("code", code);
//                                        intent.putExtra("orgId", OrgManager.getInstance().getCurrentOrgInfo() == null ? 0 : OrgManager.getInstance().getCurrentOrgInfo().orgId);//orgId);
//                                        intent.putExtra("orgName", OrgManager.getInstance().getCurrentOrgInfo() == null ? "" : OrgManager.getInstance().getCurrentOrgInfo().orgFullName);//orgFullName);
//                                        intent.putExtra("userName", AuthInfo.getInstance().getUserName());
//                                        intent.putExtra("token", MD5Util.md5Hex(AuthInfo.getInstance().getAccessToken()));//MD5.md5(AuthInfo.getInstance().getAccessToken()));
//                                        ccontext.startActivity(intent);
//
//                                        subscriber.onNext(true);
//                                        subscriber.onCompleted();
//                                    } catch (Exception e) { //ActivityNotFoundException
//                                        e.printStackTrace();
//                                        subscriber.onError(new Throwable(e.getLocalizedMessage()));
//                                    }
//                                }
//
//                            } else {
//                                subscriber.onError(new Throwable(getActivity().getText(R.string.error_network).toString()));
//                            }
//                        } else if (msg.isFailed()) {
//                            subscriber.onError(new Throwable(getActivity().getText(R.string.error_network).toString()));
//                        }
//                    }
//                });
//                req.send();
//            }
//        });
//    }
//}
