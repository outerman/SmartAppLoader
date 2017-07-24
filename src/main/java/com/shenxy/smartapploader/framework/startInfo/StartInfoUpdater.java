package com.shenxy.smartapploader.framework.startInfo;

//import com.chanjet.core.Message;
//import com.chanjet.core.MessageListener;
//import com.chanjet.workcircle.R;
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.model.ChangeOrgModel;
//import com.chanjet.workcircle.request.AppInfoQuery;
//import com.chanjet.workcircle.request.MyWorkQuery;

import com.shenxy.smartapploader.request.MyWorkQuery;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by shenxy on 16/4/18.
 * 更新AppStartInfo
 */
public class StartInfoUpdater {
    public Observable<MyWorkQuery.MyWorkItem> checkStartInfo(AppStartInfo startInfo) {
        return Observable.create(new Observable.OnSubscribe<MyWorkQuery.MyWorkItem>() {
            @Override
            public void call(Subscriber<? super MyWorkQuery.MyWorkItem> subscriber) {
                if (startInfo == null) {
                    subscriber.onError(new Throwable("no startInfo!"));
                    return;
                }

                if (startInfo.appInfo == null) {
                    updateAppInfo(startInfo.entId, startInfo.appId)
                            .subscribe(
                                    new Action1<MyWorkQuery.MyWorkItem>() {
                                        @Override
                                        public void call(MyWorkQuery.MyWorkItem updatedAppInfo) {
                                            if (updatedAppInfo == null) {
                                                subscriber.onError(new Throwable("没有订购该轻应用!"));
                                            } else {
                                                subscriber.onNext(updatedAppInfo);
                                                subscriber.onCompleted();
                                            }
                                        }
                                    },
                                    new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            subscriber.onError(throwable);
                                        }
                                    });
                } else {
                    subscriber.onNext(startInfo.appInfo);
                    subscriber.onCompleted();
                }
            }
        });
    }

    //从缓存或服务端更新轻应用的配置信息appInfo
    private Observable<MyWorkQuery.MyWorkItem> updateAppInfo(String orgId, int appId) {
        return Observable.create(new Observable.OnSubscribe<MyWorkQuery.MyWorkItem>() {
            @Override
            public void call(Subscriber<? super MyWorkQuery.MyWorkItem> subscriber) {
                //如果有缓存,则只从缓存查询
                //TODO yijia
                MyWorkQuery.MyWorkItem cachedAppInfo = null; //new ChangeOrgModel().getCacheWorkItemById(orgId, String.valueOf(appId));
                if (cachedAppInfo != null && cachedAppInfo.appId != null && cachedAppInfo.version != null) {
                    subscriber.onNext(cachedAppInfo);
                    subscriber.onCompleted();
                }
                //如果缓存没有找到,则尝试从服务端获取
                else {
                    updateFromServer(orgId, appId).subscribe(
                            new Action1<MyWorkQuery.MyWorkItem>() {
                                @Override
                                public void call(MyWorkQuery.MyWorkItem appInfo) {
                                    subscriber.onNext(appInfo);
                                    subscriber.onCompleted();
                                }
                            },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    subscriber.onError(throwable);
                                }
                            });
                }
            }
        });
    }

    private Observable<MyWorkQuery.MyWorkItem> updateFromServer(String orgId, int appId) {
        return Observable.create(new Observable.OnSubscribe<MyWorkQuery.MyWorkItem>() {
            @Override
            public void call(Subscriber<? super MyWorkQuery.MyWorkItem> subscriber) {
                //TODO yijia
//                final AppInfoQuery query = new AppInfoQuery();
//                query.getReq().appId = (long) appId;
//                query.getReq().orgId = Long.parseLong(orgId);
//                query.setWhenUpdate(new MessageListener() {
//                    @Override
//                    public void process(Message msg) {
//                        if (msg.isSucceed()) {
//                            if (query.getResp() != null) {
//                                if (query.getResp().code != 0) {
//                                    subscriber.onError(new Throwable(query.getResp().msg));
////                                    subscriber.onNext(null);
//                                } else if (query.getResp().data == null) {
//                                    subscriber.onError(new Throwable("没有订购该应用"));
//                                } else {
//                                    new ChangeOrgModel().saveSingleWorkItemToCache(query.getResp().data);
//                                    subscriber.onNext(query.getResp().data);
//                                }
//                            } else {
//                                subscriber.onError(new Throwable(BaseApplication.getContext().getResources().getString(R.string.error_network)));
//                            }
//
//                            subscriber.onCompleted();
//                        } else if (msg.isFailed()) {
//                            subscriber.onError(new Throwable(BaseApplication.getContext().getResources().getString(R.string.error_network)));
//                            subscriber.onCompleted();
//                        }
//                    }
//                });
//                query.send();
            }
        });
    }
}
