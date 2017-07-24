//package com.shenxy.smartapploader.framework.loader;
//
////import com.chanjet.workcircle.model.TeleConferenceModel;
////import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//
//import com.shenxy.smartapploader.framework.LightAppConstants;
//
//import rx.Observable;
//import rx.Subscriber;
//
///**
// * Created by shenxy on 16/4/18.
// * 转为电话会议提供的loader
// */
//public class TeleconferenceAppLoader extends BaseLoader {
//
//    @Override
//    public Observable<Boolean> openApp() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                if (LightAppConstants.TELE_CONFERENCE_APPID == getStartInfo().appId) {//若为电话会议,直接打开电话会议app
//                    TeleConferenceModel.getInstance().startTeleConferenceApp(getActivity(), null, "工作");
//                    subscriber.onNext(true);
//                    subscriber.onCompleted();
//                }
//                else {
//                    subscriber.onNext(false);
//                    subscriber.onCompleted();
//                }
//            }
//        });
//    }
//}
