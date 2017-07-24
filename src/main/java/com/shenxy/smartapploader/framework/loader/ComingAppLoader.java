package com.shenxy.smartapploader.framework.loader;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by shenxy on 16/4/25.
 * 即将推出
 */
public class ComingAppLoader extends BaseLoader {
    @Override
    public Observable<Boolean> openApp() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    //未上线 进入即将推出
//                    Intent intent = new Intent();
//                    intent.putExtra("orgId", getStartInfo().entId);
//                    intent.setClass(getActivity(), TodoWorkActivity.class);
//                    getActivity().startActivity(intent);

                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e.getLocalizedMessage()));
                }
            }
        });
    }
}
