package com.shenxy.smartapploader.framework.loader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.shenxy.smartapploader.R;
import com.shenxy.smartapploader.framework.loaderFactory.RemoteUrlAppLoaderFactory;
import com.shenxy.smartapploader.plugins.AppConfig;
import com.shenxy.smartapploader.utils.StringUtils;
import com.shenxy.smartapploader.wiget.ChanjetDialog;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.R;
//import com.chanjet.workcircle.phonegap.framework.loaderFactory.RemoteUrlAppLoaderFactory;
//import com.chanjet.workcircle.phonegap.plugins.AppConfig;
//import com.chanjet.workcircle.widget.ChanjetDialog;

/**
 * Created by shenxy on 16/4/18.
 * remote模式的轻应用loader
 */
public class RemoteDownloadAppLoader extends BaseLoader {
    @Override
    public Observable<Boolean> checkStartInfo() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (StringUtils.isBlank(RemoteUrlAppLoaderFactory.getRemoteString(getStartInfo()))) {
                    subscriber.onError(new Throwable("缺少轻应用包的RemoteUrl信息!"));
                    return;
                }

                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> openApp() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                getAppConfig().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<AppConfig>() {
                                       @Override
                                       public void call(AppConfig appConfig) {
                                           String downloadUrl = RemoteUrlAppLoaderFactory.getRemoteString(getStartInfo());
                                           startApp(getActivity(), downloadUrl);
                                           subscriber.onNext(true);
                                           subscriber.onCompleted();
                                       }
                                   },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        subscriber.onNext(false);
                                        subscriber.onCompleted();
                                    }
                                });
            }
        });
    }

    //由于异步执行runnable,当被执行的时候,apploader已经被release.此时getActivity和getStartInfo返回的都是空,因此先取出来再使用
    private void startApp(Activity activity, String downloadUrl) {
        final ChanjetDialog dialog = new ChanjetDialog(activity);
        dialog.setCancelable(true);
        dialog.setTitleVisible(false);
        dialog.setDialogText("将会开始下载" + getNameFromUrl(downloadUrl) + ",是否继续下载?");
        dialog.setButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        }, "下载", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDownloadUserSystemBrowser(activity, downloadUrl);
                dialog.cancel();
            }
        });
        dialog.setButton1TextColor(activity.getResources().getColor(R.color.dialog_botton1_text));
        dialog.setButton2TextColor(activity.getResources().getColor(R.color.dialog_botton2_text));

        dialog.show();
    }

    private String getNameFromUrl(String downloadUrl) {
        String[] tmp = downloadUrl.split("/");
        if (tmp.length > 0) {
            return tmp[tmp.length - 1];
        }
        else {
            return null;
        }
    }

    private void startDownloadUserSystemBrowser(Activity activity, String downloadUrl) {
        if (StringUtils.isBlank(downloadUrl)) {
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(downloadUrl));
            activity.startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
