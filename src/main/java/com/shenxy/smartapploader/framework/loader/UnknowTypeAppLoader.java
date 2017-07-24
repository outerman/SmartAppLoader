package com.shenxy.smartapploader.framework.loader;

import android.view.View;

import com.shenxy.smartapploader.R;
import com.shenxy.smartapploader.wiget.ChanjetDialog;

import rx.Observable;
import rx.Subscriber;

//import com.chanjet.workcircle.R;
//import com.chanjet.workcircle.util.UpdateVersionUtil;
//import com.chanjet.workcircle.widget.ChanjetDialog;

/**
 * Created by shenxy on 16/5/25.
 * 不认识的appType对应的loader,只出个提示框即可
 */
public class UnknowTypeAppLoader extends BaseLoader {
    @Override
    public Observable<Boolean> openApp() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    showUnknowTypeAppDialog();

                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e.getLocalizedMessage()));
                }
            }
        });
    }

    private void showUnknowTypeAppDialog() {
        final ChanjetDialog dialog = new ChanjetDialog(getActivity());
        dialog.setTitleVisible(false);
        dialog.setDialogText("该版本不支持当前轻应用，请升级到最新版本");

        dialog.setButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

//                        new UpdateVersionUtil().updateVersionWithCheck(getActivity());
                    }
                });
        dialog.setButton1TextColor(getActivity().getResources().getColor(R.color.topbar_background));
        dialog.show();
    }
}
