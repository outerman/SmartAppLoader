package com.shenxy.smartapploader.framework.startInfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenxy.smartapploader.AppConstants;
import com.shenxy.smartapploader.R;
import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.request.MyWorkQuery;
import com.shenxy.smartapploader.utils.StringUtils;
import com.shenxy.smartapploader.wiget.ChanjetDialog;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.ReplaySubject;

//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.AppConstants;
//import com.chanjet.workcircle.R;
//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.request.MyWorkQuery;
//import com.chanjet.workcircle.widget.ChanjetDialog;

/**
 * Created by shenxy on 16/4/18.
 * Debug模式下的AppStartInfo构建
 */
public class DebugStartInfoUpdater {
    public static final int DEBUG_FORCE_UPDATE_VERSION = Integer.MAX_VALUE;//这个值在zip包类型里,会强制下载更新
    private Activity activity;
    private AppStartInfo appStartInfo;

    public Activity getActivity() {
        return activity;
    }

    public AppStartInfo getStartInfo() {
        return appStartInfo;
    }

    public DebugStartInfoUpdater(Activity activity, AppStartInfo appStartInfo) {
        this.activity = activity;
        this.appStartInfo = appStartInfo;
    }

    public Observable<Boolean> checkDebugStartInfo() {
        if (AppConstants.IS_LIGHTAPP_DEBUG()) {
            return showDebugDialog();
        } else {
            return Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            });
        }
    }

    public Observable<Boolean> showDebugDialog() {
        ReplaySubject<Boolean> dialogReplaySubject = ReplaySubject.create();
        Activity activity = getActivity();
        AppStartInfo startInfo = getStartInfo();

        final ChanjetDialog dialog = new ChanjetDialog(activity);
        dialog.setTitleVisible(false);

        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView paramTextView = new TextView(activity);
        paramTextView.setText("附加参数platformExtension");
        final EditText paramEditText = new EditText(activity);
        paramEditText.setText(getLastParamValue());
        TextView textView = new TextView(activity);
        textView.setText("指定打开地址");
        final EditText editText = new EditText(activity);
        editText.setText(getLastUrl());
        final CheckBox checkBox = new CheckBox(activity);
        checkBox.setText("isRemoteUrl");
        checkBox.setChecked(getLastRemoteCheck());
        final CheckBox navBarCheckBox = new CheckBox(activity);
        navBarCheckBox.setText("isShowNavBar");
        navBarCheckBox.setChecked(getIsShowNavBarCheck());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 20;
        layoutParams.rightMargin = 20;
        layoutParams.topMargin = 20;
        layoutParams.weight = 1;
        LinearLayout.LayoutParams layoutParamsEdit = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsEdit.leftMargin = 20;
        layoutParamsEdit.rightMargin = 20;
        layoutParamsEdit.topMargin = 20;
        layoutParamsEdit.weight = 2;
        linearLayout.addView(paramTextView, layoutParams);
        linearLayout.addView(paramEditText, layoutParamsEdit);
        linearLayout.addView(textView, layoutParams);
        linearLayout.addView(editText, layoutParamsEdit);
        linearLayout.addView(checkBox, layoutParams);
        linearLayout.addView(navBarCheckBox, layoutParams);
        dialog.setContentView(linearLayout);

        dialog.setButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (startInfo.appInfo == null) {
                            startInfo.appInfo = new MyWorkQuery.MyWorkItem();
                        }
                        if (startInfo.appInfo.downUrl == null) {
                            startInfo.appInfo.downUrl = new MyWorkQuery.DownloadUrlInfo();
                        }
                        startInfo.appInfo.downUrl.APH = editText.getText().toString().trim();//dialog.getEditTextContent();
                        saveLastUrl(startInfo.appInfo.downUrl.APH);
                        saveLastRemoteCheck(checkBox.isChecked());
                        saveIsShowNavBarCheck(navBarCheckBox.isChecked());
                        if (startInfo.appInfo != null) {
                            String mockPlatformExtension = paramEditText.getText().toString().trim();
                            if (StringUtils.isNotBlank(mockPlatformExtension)) {  //优先使用手工输入的值,否则使用点击按钮带入的值
                                startInfo.appInfo.platformExtension = mockPlatformExtension;
                            } else if (getStartInfo() != null && getStartInfo().appInfo != null) {
                                startInfo.appInfo.platformExtension = getStartInfo().appInfo.platformExtension;
                            }
                            saveLastParamValue(mockPlatformExtension);
                        }

                        if (navBarCheckBox.isChecked() && startInfo.appInfo != null) {
                            startInfo.appInfo.navBar = 1;
                        } else if (getStartInfo() != null && getStartInfo().appInfo != null) {
                            startInfo.appInfo.navBar = getStartInfo().appInfo.navBar;
                        } else {
                            startInfo.appInfo.navBar = 0;
                        }

                        if (checkBox.isChecked()) {
                            startInfo.appInfo.appType = 2;
                            startInfo.appInfo.appId = LightAppConstants.DEFAULT_REMOTEURL_APPID;
                            startInfo.isRNRemote = true;
                        } else if (getStartInfo() != null && getStartInfo().appInfo != null) {
                            startInfo.appInfo.appType = getStartInfo().appInfo.appType;
                            startInfo.appInfo.version = DEBUG_FORCE_UPDATE_VERSION;
                        } else {
                            startInfo.appInfo.appType = 0;
                            startInfo.appInfo.version = DEBUG_FORCE_UPDATE_VERSION;
                        }
                        dialog.cancel();

                        dialogReplaySubject.onNext(true);
                        dialogReplaySubject.onCompleted();
                    }
                },
                "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                        getStartInfo().isRNRemote = checkBox.isChecked();

                        dialogReplaySubject.onNext(true);
                        dialogReplaySubject.onCompleted();
                    }
                });
        dialog.setButton1TextColor(activity.getResources().getColor(R.color.topbar_background));
        dialog.show();

        return dialogReplaySubject.asObservable();
    }

    SharedPreferences sharedPreferencesApp;

    private String getLastUrl() {
        if (sharedPreferencesApp == null && getActivity() != null) {
            sharedPreferencesApp = getActivity().getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            return sharedPreferencesApp.getString("LastEditUrl", "");
        } else {
            return "";
        }
    }

    private void saveLastUrl(String url) {
        if (sharedPreferencesApp == null && getActivity() != null) {
            sharedPreferencesApp = getActivity().getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            SharedPreferences.Editor editor = sharedPreferencesApp.edit();
            editor.putString("LastEditUrl", url).apply();
        }
    }

    private boolean getLastRemoteCheck() {
        if (sharedPreferencesApp == null && getActivity() != null) {
            sharedPreferencesApp = getActivity().getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            return sharedPreferencesApp.getBoolean("LastRemoteUrlCheck", false);
        } else {
            return false;
        }
    }

    private void saveLastRemoteCheck(boolean isChecked) {
        if (sharedPreferencesApp == null && getActivity() != null) {
            sharedPreferencesApp = getActivity().getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            SharedPreferences.Editor editor = sharedPreferencesApp.edit();
            editor.putBoolean("LastRemoteUrlCheck", isChecked).apply();
        }
    }

    private String getLastParamValue() {
        if (sharedPreferencesApp == null && activity != null) {
            sharedPreferencesApp = activity.getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            return sharedPreferencesApp.getString("LastParamValue", "");
        } else {
            return "";
        }
    }

    private void saveLastParamValue(String url) {
        if (sharedPreferencesApp == null && activity != null) {
            sharedPreferencesApp = activity.getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            SharedPreferences.Editor editor = sharedPreferencesApp.edit();
            editor.putString("LastParamValue", url).apply();
        }
    }

    private void saveIsShowNavBarCheck(boolean isChecked) {
        if (sharedPreferencesApp == null && activity != null) {
            sharedPreferencesApp = activity.getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            SharedPreferences.Editor editor = sharedPreferencesApp.edit();
            editor.putBoolean("IsShowNavBarCheck", isChecked).apply();
        }
    }

    private boolean getIsShowNavBarCheck() {
        if (sharedPreferencesApp == null && activity != null) {
            sharedPreferencesApp = activity.getSharedPreferences("AppManagerPrefer", Context.MODE_PRIVATE);
        }

        if (sharedPreferencesApp != null) {
            return sharedPreferencesApp.getBoolean("IsShowNavBarCheck", false);
        } else {
            return false;
        }
    }
}
