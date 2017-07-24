//package com.shenxy.smartapploader.framework.loader;
//
//import android.content.Intent;
//
//import com.chanjet.rxworkcircle.app.HideWorkActivity;
//
//import rx.Observable;
//import rx.Subscriber;
//
///**
// * Created by shenxy on 16/5/10.
// * "更多"按钮,打开隐藏app列表
// */
//public class HideAppLoader extends BaseLoader {
//    @Override
//    public Observable<Boolean> openApp() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                try {
//                    //打开"更多",显示隐藏的app
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), HideWorkActivity.class);
//                    getActivity().startActivity(intent);
//
//                    subscriber.onNext(true);
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    subscriber.onError(new Throwable(e.getLocalizedMessage()));
//                }
//            }
//        });
//    }
//}
