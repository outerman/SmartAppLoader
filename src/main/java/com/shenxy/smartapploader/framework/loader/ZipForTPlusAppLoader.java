//package com.shenxy.smartapploader.framework.loader;
//
////import com.chanjet.core.Message;
////import com.chanjet.core.MessageListener;
////import com.chanjet.libutil.util.StringUtils;
////import com.chanjet.workcircle.R;
////import com.chanjet.workcircle.application.BaseApplication;
////import com.chanjet.workcircle.phonegap.framework.installer.ZipInstaller;
////import com.chanjet.workcircle.request.LightAppVersionQuery;
////import com.chanjet.workcircle.request.MyWorkQuery;
//
//import com.shenxy.smartapploader.request.MyWorkQuery;
//
//import rx.Observable;
//import rx.Subscriber;
//
///**
// * Created by shenxy on 16/4/18.
// * T+类的zip轻应用,版本号不是单向增加的
// * 1)不做预安装处理
// * 2)检查是否需要重新安装的逻辑
// */
//public class ZipForTPlusAppLoader extends ZipAppLoader {
//
//    @Override
//    protected Observable<Boolean> isNeedInstall() {
//        return loadAppNeedCheckVersion();
//    }
//
//    //加载需要服务端检查版本的轻应用（如T+）
//    private Observable<Boolean> loadAppNeedCheckVersion() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                final MyWorkQuery.MyWorkItem appInfo = getStartInfo().appInfo;
//                final LightAppVersionQuery query = new LightAppVersionQuery();
//                query.getReq().appId = getStartInfo().appId;
//                query.getReq().orgId = Long.parseLong(getStartInfo().entId);
//                query.setWhenUpdate(new MessageListener() {
//                    @Override
//                    public void process(Message msg) {
//                        if (msg.isSucceed()) {
//                            if (query.getResp() != null && query.getResp().code == 0) {
//                                //写入返回的版本号，以保持后续写入版本号和检查版本号逻辑的一致性
//                                appInfo.version = query.getResp().data.version;
//                                if (appInfo.downUrl == null) {
//                                    appInfo.downUrl = new MyWorkQuery.DownloadUrlInfo();
//                                }
//                                appInfo.downUrl.APH = query.getResp().data.APH;
//                                appInfo.downUrl.IPH = query.getResp().data.IPH;
//
//                                if (getStartInfo() == null || getActivity() == null) {
//                                    subscriber.onNext(false);
//                                    subscriber.onCompleted();
//                                    return;
//                                }
//                                //与当前安装版本比较，确定是否重新安装
//                                if (!ZipInstaller.isAppInstalled(getStartInfo().appId)
//                                        || !ZipInstaller.getAppVersion(getStartInfo().appId).equals(appInfo.version)) {
//                                    subscriber.onNext(true);
//                                } else {
//                                    subscriber.onNext(false);
//                                }
//                                subscriber.onCompleted();
//                            } else if (query.getResp() != null && StringUtils.isNotBlank(query.getResp().msg)) {
//                                subscriber.onError(new Throwable(query.getResp().msg));
//                            } else {
//                                subscriber.onError(new Throwable(BaseApplication.getContext().getText(R.string.error_network).toString()));
//                            }
//                        } else if (msg.isFailed()) {
//                            subscriber.onError(new Throwable(BaseApplication.getContext().getText(R.string.error_network).toString()));
//                        }
//                    }
//                });
//                query.send();
//            }
//        });
//
//    }
//}
