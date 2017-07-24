/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.shenxy.smartapploader.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.shenxy.smartapploader.AppConstants;
import com.shenxy.smartapploader.R;
import com.shenxy.smartapploader.plugins.AppConfig;
import com.shenxy.smartapploader.wiget.ChanjetDialog;
import com.shenxy.smartapploader.utils.CrashHandler;
import com.shenxy.smartapploader.utils.DensityUtil;
import com.shenxy.smartapploader.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.PluginManager;
import org.crosswalk.engine.XWalkCordovaView;
//import org.greenrobot.eventbus.EventBus;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;

//import com.chanjet.core.HTTPRequest;
//import com.chanjet.core.HTTPRequestQueue;
//import com.chanjet.libutil.util.DensityUtil;
//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.AppConstants;
//import com.chanjet.workcircle.R;
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.model.TeleConferenceModel;
//import com.chanjet.workcircle.phonegap.crosswalk.CrosswalkPluginManager;
//import com.chanjet.workcircle.phonegap.framework.installer.ZipInstaller;
//import com.chanjet.workcircle.phonegap.plugins.AppConfig;
//import com.chanjet.workcircle.util.LogUtils;
//import com.chanjet.workcircle.util.UpdateVersionUtil;
//import com.chanjet.workcircle.util.behavioranalysis.CrashHandler;
//import com.chanjet.workcircle.widget.ChanjetDialog;
//import com.chanjet.workcircle.widget.ChanjetPopupMenu;
//import com.chanjet.workcircle.widget.MenuItem;
//import com.crustal.activity.CrustalTopBarActivity;
//import com.crustal.activity.topbar.TopBarConfig;
//import com.crustal.activity.topbar.TopBarItem;
//import com.crustal.activity.topbar.TopBarRight;
//import com.lidroid.DownloadManager;
//import com.lidroid.UploadManager;

public class AppMainRxActivity extends CordovaActivity implements CrosswalkActivityInterface {
    private static final String TAG = "AppMainActivity";
//    private ChanjetPopupMenu _popupMenu;
    AppMainViewModel viewModel;
//    UMShareListener shareSnsPostListener;//分享成功回调
    String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

//        HTTPRequestQueue.sharedInstance().initQueue(this);

        viewModel = new AppMainViewModel(this);

        viewModel.initParams(savedInstanceState, getIntent());

//        viewModel.subscribeToDataStore();

        try {
            viewModel.initCrossWalk(this, viewModel.getAppId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean isUsingCrossWalk) {
                            initAndLoadUrl(isUsingCrossWalk);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            CrashHandler.saveCrashLog(e);
        }
//        EventBus.getDefault().register(this);

//        initShareSnsListener();
    }

    private ChanjetDialog unknowPluginDialog;
    private void initUnknowPluginListener() {
        //shenxy 2016-5-25 增加对不支持的插件调用的提示
        getAppView().getPluginManager().setUnknowPluginExecListener(new PluginManager.UnknowPluginExecListener() {
            @Override
            public void OnUnknowPluginExec(String service, String pluginName) {
                try {
                    showUnknowPluginDialog();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void showUnknowPluginDialog() {
        if (unknowPluginDialog == null) {
            unknowPluginDialog = new ChanjetDialog(AppMainRxActivity.this);
            unknowPluginDialog.setTitleVisible(false);
            unknowPluginDialog.setDialogText("该版本不支持当前操作，请升级到最新版本");

            unknowPluginDialog.setButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unknowPluginDialog.cancel();

//                    new UpdateVersionUtil().updateVersionWithCheck(AppMainRxActivity.this);
                }
            });
            unknowPluginDialog.setButton1TextColor(getResources().getColor(R.color.topbar_background));
        }

        if (!unknowPluginDialog.isShowing()) {
            unknowPluginDialog.show();
        }
    }

    private void initAndLoadUrl(Boolean isUsingCrossWalk) {
        setIsPluginWebViewEngine(isUsingCrossWalk);

        if (viewModel == null) {
            return;
        }

        if (appView == null) {
            init();
        }

//        initCrosswalkIfPlugin();

        String url = viewModel.getLaunchUrl();

        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "没有需要打开的轻应用", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //加载页面
//        if (AppIntentAction.AppType.RemoteUrl.equals(viewModel.getAppType())) {
//            setShareInfo();
//        }

        loadUrl(url);
        initUnknowPluginListener();

        if (getIntent().hasExtra(AppIntentAction.APP_NAME)) {
            LogUtils.LightAppLog("LightAppOpenCount", getIntent().getStringExtra(AppIntentAction.APP_NAME));
        }

//        BaseApplication.context.addActivity(this);
//
//        new AppAdditionalTask().doAdditionalTask(viewModel.getAppId());

        super.onResume();
    }

//    private void initCrosswalkIfPlugin() {
//        //因为不是使用内核自己的压缩包,和解压逻辑,所以需要一些欺骗工作
//        if (CrosswalkPluginManager.getInstance().getCrosswalkMode() == CrosswalkPluginManager.CrosswalkMode.PLUGIN) {
//            viewModel.getCheatCrossWalkCommand()
//                    .execute()
//                    .filter(new Func1<Boolean, Boolean>() {
//                        @Override
//                        public Boolean call(Boolean needReloadUrl) {
//                            return needReloadUrl;
//                        }
//                    })
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(isCrossWalk -> {
//                        loadUrl(viewModel.getLaunchUrl());
//                    });
//        }
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (viewModel == null) {
            return;
        }

        outState.putString(AppIntentAction.APP_TYPE, viewModel.getAppType().name());
        outState.putString(AppIntentAction.APP_ID, viewModel.getAppId());
        outState.putSerializable(AppIntentAction.APP_CONFIG, viewModel.getAppConfig());
        outState.putSerializable(AppIntentAction.APP_URL, viewModel.getAppUrl());
        outState.putSerializable(AppIntentAction.APP_DATA, viewModel.getShareData());
        outState.putSerializable(AppIntentAction.APP_ISSHOW_TITLEBAR, viewModel.getIsShowTopTitleBar());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, " DownloadManager  UploadManager  AppMainActivity  onDestroy");
        super.onDestroy();
//        UploadManager.getInstance().removeAllLightAppFileUpload();
//        DownloadManager.getInstance().removeAllDownload();

//        BaseApplication.context.removeActivity(this);

        clearWebviewCache();

//        cancelRequests();

//        EventBus.getDefault().unregister(this);
    }

//    public void cancelRequests() {
//        if (HTTPRequest.isRequestResponder(this)) {
//            HTTPRequestQueue.sharedInstance().cancelRequestByResponder(this);
//        }
//    }

    private void clearWebviewCache() {
        if (appView == null) {
            return;
        }

        try {
            appView.clearCache();
            appView.clearHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        appView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//        appView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        appView.clearCache(true);
//        appView.destroyDrawingCache();
    }

    @Override
    protected void createViews() {
        super.createViews();
        Log.d(TAG, "createViews");
        if (viewModel == null) {
            return;
        }

        //如果使用crosswalk浏览器，如果是，则显示一个logo
        if (isPluginWebViewEngine()) {
            ImageView logoImageView = new ImageView(this);
            logoImageView.setImageResource(R.drawable.tw_about_gzq);
            logoImageView.setAlpha(0.3f);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.bottomMargin = DensityUtil.dip2px(this, 10);
            layoutParams.leftMargin = DensityUtil.dip2px(this, 10);
            getWindow().addContentView(logoImageView, layoutParams);

            logoImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    logoImageView.animate().alpha(0).setDuration(500);
//                    logoImageView.setVisibility(View.GONE);
                }
            }, 3000);
        }

//        if (viewModel.getAppType().equals(AppIntentAction.AppType.RemoteUrl)) {
//            initRightPopMenu();
//        }
//        if (!viewModel.isShowLightAppTitleBar()) {
//            hideTitleBar();
//        } else {//设置title
//            if (StringUtils.isNotBlank(viewModel.appName)) {
//                titleObserver = false;//不监听页面中title标签
//                setTitle(viewModel.appName);
//            }
//        }

    }

//    private void initRightPopMenu() {
//        TopBarConfig topBarConfig = new TopBarConfig(this);
//        TopBarRight topBarRight = new TopBarRight();
//        topBarRight.type = TopBarRight.Type.other;
//        TopBarItem topBarItem = new TopBarItem();
//        topBarItem.fontIcon = "more";
//        if (topBarRight.items == null) {
//            topBarRight.items = new ArrayList<>();
//        }
//        topBarRight.items.add(topBarItem);
//        topBarConfig.right = topBarRight;
//        applyTopBarConfig(topBarConfig);
//    }
//
//    @Override
//    protected void rightClick(TopBarConfig config) {
//        super.rightClick(config);
//        if (config.right.type == TopBarRight.Type.other) {
//            showPopWindow();
//        }
//    }
//
//    private void initShareSnsListener() {
//        shareSnsPostListener = new UMShareListener() {
//            @Override
//            public void onResult(SHARE_MEDIA share_media) {
////                if (StringUtils.isNotBlank(viewModel.getCurShareInfo().success)) {
////                    loadUrl("javascript:" + mTitleBarInfo.shareInfo.success);
////                }
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
////                if (StringUtils.isNotBlank(mTitleBarInfo.shareInfo.cancel)) {
////                    loadUrl("javascript:" + mTitleBarInfo.shareInfo.cancel);
////                }
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA share_media) {
////                if (StringUtils.isNotBlank(mTitleBarInfo.shareInfo.cancel)) {
////                    loadUrl("javascript:" + mTitleBarInfo.shareInfo.cancel);
////                }
//            }
//
//        };
//    }
/*
    private void showPopWindow() {
        //判断当前用户是否为企业管理员
        if (_popupMenu == null) {
            _popupMenu = new ChanjetPopupMenu(AppMainRxActivity.this, ChanjetPopupMenu.MenuStyle.DropDown);
            _popupMenu.setOnItemSelectedListener(new ChanjetPopupMenu.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MenuItem item) {
                    if (item == null) {
                        return;
                    }
                    switch (item.getId()) {
                        case 1://分享
                            if (viewModel != null && viewModel.getCurShareInfo() != null) {
                                if (StringUtils.isBlank(viewModel.getCurShareInfo().shareTitle) || StringUtils.isBlank(viewModel.getCurShareInfo().shareContent)) {
                                    viewModel.setShareData(title, title, viewModel.getCurShareInfo().shareImageUrl, viewModel.getCurShareInfo().shareUrl);
                                }
                                viewModel.shareUrl(shareSnsPostListener);
                            }

//                            new TWCDVScreenshot().takeScreenshotBitmap(AppMainRxActivity.this,
//                                StorageUtil.GetStorePath(StorageUtil.Type.image) + "0111.jpg");

                            break;
                        case 2://群聊
                            viewModel.createGroupChat();
                            break;
                        case 3://电话会议
                            TeleConferenceModel.getInstance().startTeleConferenceApp(AppMainRxActivity.this, null, "网页");
                            break;
                        case 4:
                            viewModel.openWebBySysBrowser();
                            break;
                        case 5://刷新
//                            initProgressView();
                            loadUrl(viewModel.getAppUrl());
                            appView.getEngine().loadUrl(viewModel.getAppUrl(), true);
                            break;
                        case 6://复制链接
                            if (viewModel != null) {
                                viewModel.copyText(viewModel.getAppUrl());
                                Toast.makeText(AppMainRxActivity.this, "已复制到剪贴板", Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }
            });

            MenuItem shareMenuItem = new MenuItem();
            shareMenuItem.setTitle("分享");
            shareMenuItem.setId(1);
            shareMenuItem.setIcon(getResources().getDrawable(R.drawable.icon_pop_share));
            if (viewModel.getCurShareInfo() != null && StringUtils.isNotBlank(viewModel.getCurShareInfo().shareUrl)) {
                _popupMenu.addMenuItem(shareMenuItem);
            }
            MenuItem groupChatMenuItem = new MenuItem();
            groupChatMenuItem.setTitle("群聊");
            groupChatMenuItem.setId(2);
            groupChatMenuItem.setIcon(getResources().getDrawable(R.drawable.icon_pop_groupchat));
            if ("1".equals("0")) {
                _popupMenu.addMenuItem(groupChatMenuItem);
            }
            MenuItem teleConferMenuItem = new MenuItem();
            teleConferMenuItem.setTitle("电话会议");
            teleConferMenuItem.setId(3);
            teleConferMenuItem.setIcon(getResources().getDrawable(R.drawable.icon_pop_teleconference));
            if ("1".equals("0")) {
                _popupMenu.addMenuItem(teleConferMenuItem);
            }

            MenuItem browserMenuItem = new MenuItem();
            browserMenuItem.setTitle("在浏览器打开");
            browserMenuItem.setId(4);
            browserMenuItem.setIcon(getResources().getDrawable(R.drawable.icon_pop_openbrower));
//            if("1".equals(mTitleBarInfo.browerOpen)){
            _popupMenu.addMenuItem(browserMenuItem);
//            }

            MenuItem refreshMenuItem = new MenuItem();
            refreshMenuItem.setTitle("刷新");
            refreshMenuItem.setId(5);
            refreshMenuItem.setIcon(getResources().getDrawable(R.drawable.icon_pop_refresh));
//            if("1".equals(mTitleBarInfo.refresh)){
            _popupMenu.addMenuItem(refreshMenuItem);
//            }

            MenuItem copyUrlMenuItem = new MenuItem();
            copyUrlMenuItem.setTitle("复制链接");
            copyUrlMenuItem.setId(6);
            copyUrlMenuItem.setIcon(getResources().getDrawable(R.drawable.icon_pop_copyurl));
//            if("1".equals(mTitleBarInfo.copyUrl)){
            _popupMenu.addMenuItem(copyUrlMenuItem);
//            }
        }
        //caolei
        _popupMenu.updateMarginTop(30);
        _popupMenu.showWithAnimation(rightLayout, R.style.CircleListPlusMenuAnimation);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //shenxy 友盟统计
        MobclickAgent.onResume(this);
        if (appView != null && !TextUtils.isEmpty(appView.getUrl())) {
            loadUrl("javascript:resume()");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //shenxy 友盟统计
        MobclickAgent.onResume(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && viewModel != null) {
            if (appView == null || TextUtils.isEmpty(appView.getUrl())) {
                finish();
                return true;
            }
            backPress();
//            if (AppIntentAction.AppType.LightApp.equals(viewModel.getAppType())) {
//            } else if (AppIntentAction.AppType.RemoteUrl.equals(viewModel.getAppType())) {
//                //若appId为null或者-1 则表示为url跳转 by wk
//                if (viewModel.getAppId() == null || viewModel.getAppId().equals(String.valueOf(RemoteUrlAppManager.remoteUrlAppId))) {
//                    finish();
//                }
//            }
        }
        return false;
    }

    //js提供的方法——键盘返回按钮
    private void backPress() {
        if (appView != null && !TextUtils.isEmpty(appView.getUrl())) {
            if (!viewModel.isShowLightAppTitleBar() && AppIntentAction.AppType.LightApp.equals(viewModel.getAppType())) {// 当轻应用顶部导航不显示时,点击back键调用js方法
                loadUrl("javascript:back()");
                return;
            }
            if (appView.getEngine().canGoBack()) {
                appView.getEngine().goBack();
            } else {
                finish();
            }
        }
    }

    public String getAppId() {
        if (viewModel != null) {
            return viewModel.getAppId();
        }
        return null;
    }

    public AppIntentAction.AppType getAppType() {
        if (viewModel != null) {
            return viewModel.getAppType();
        }
        return null;
    }

    public AppConfig getAppConfig() {
        if (viewModel != null) {
            return viewModel.getAppConfig();
        }
        return null;
    }

    //根据类名，获取插件名
    public String getPlugiNameByClassName(String className) {
        if (this.pluginEntries != null && this.pluginEntries.size() > 0) {
            for (PluginEntry pluginEntry : this.pluginEntries) {
                if (pluginEntry != null && pluginEntry.pluginClass != null && pluginEntry.pluginClass.equals(className)) {
                    return pluginEntry.service;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "onStop before died!");
    }

    public void setRestoreInstanceInfo(RestoreInstanceInfo restoreInstanceInfo) {
        if (viewModel != null) {
            viewModel.setRestoreInstanceInfo(restoreInstanceInfo);
        }
    }

    public RestoreInstanceInfo getRestoreInstanceInfo() {
        if (viewModel != null) {
            return viewModel.getRestoreInstanceInfo();
        }
        return null;
    }


    public void onEventAsync(Integer integer) {
        if (integer == AppConstants.EVENT_SELECTEMPLOYEE_GROUPCHAT) {
            finish();
        }
    }

    @Override
    public boolean isUsingCrosswalk() {
        return viewModel != null && AppMainViewModel.isUsingCrosswalk;
    }

    @Override
    public XWalkCordovaView getXWalkCordovaView() {
        if (isUsingCrosswalk()) {
            return (XWalkCordovaView) getAppView().getEngine().getView();
        } else {
            return null;
        }
    }

//    /**
//     * 隐藏顶部导航
//     */
//    public void hideTitleBar() {
//        int index = root.indexOfChild(navigationView);
//        if (index >= 0) {
//            root.removeView(navigationView);
//        }
//    }
//
//    @Override
//    public void onItemSelected(MenuItem item) {
//
//    }
//
//    public Object onMessage(String id, Object data) {
//        Log.e(TAG, "onMessage:" + id + " data:" + (data == null ? "" : data.toString()));
//        if (id.equals("onReceivedTitle")) {
//            title = data.toString();
//            title = (title == null) ? "" : title;
//        }
//        return super.onMessage(id, data);
//    }
//
//    private void setShareInfo() {
//        viewModel.getShareInfoCommand()
//                .execute()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<AppMainViewModel.ShareInfo>() {
//                    @Override
//                    public void call(AppMainViewModel.ShareInfo shareInfo) {
//                        if (shareInfo != null) {
////                            topbar_btn_right.setBackgroundResource(R.drawable.icon_share);
////                      u       topbar_btn_right.setText("");
//                            //如果title和content为空,则用recieveTitle填充
//                            if (StringUtils.isBlank(shareInfo.shareTitle)) {
//                                shareInfo.shareTitle = title;
//                            }
//                            if (StringUtils.isBlank(shareInfo.shareContent)) {
//                                shareInfo.shareContent = title;
//                            }
//                            viewModel.setShareData(shareInfo.shareTitle, shareInfo.shareContent, shareInfo.shareImageUrl, shareInfo.shareUrl);
//                        }
//                    }
//                });
//    }

//    @Override
//    protected void onRequireAppRootDir(String rootDir) {
//        if (viewModel != null && StringUtils.isNotBlank(viewModel.getAppId())) {
//            rootDir = ZipInstaller.getInstallRoot() + viewModel.getAppId() + "/";
//        }
//        super.onRequireAppRootDir(rootDir);
//    }
}
