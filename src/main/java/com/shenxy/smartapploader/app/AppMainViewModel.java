package com.shenxy.smartapploader.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shenxy.smartapploader.AppConstants;
import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.installer.ZipInstaller;
import com.shenxy.smartapploader.plugins.AppConfig;
import com.shenxy.smartapploader.utils.StringUtils;
import com.shenxy.smartapploader.utils.ThreadPoolUtil;

import rx.Observable;
import rx.Subscriber;

//import com.umeng.socialize.UMShareListener;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

//import android.support.annotation.NonNull;
//import com.chanjet.im.activity.SessionListActivity;
//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.rxworkcircle.ink.rx.RxCommand;
//import com.chanjet.rxworkcircle.viewmodels.AbstractViewModel;
//import com.chanjet.workcircle.AppConstants;
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.phonegap.crosswalk.ChanjetXWalkLibDecompressor;
//import com.chanjet.workcircle.phonegap.crosswalk.CrosswalkPluginManager;
//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.installer.ZipInstaller;
//import com.chanjet.workcircle.phonegap.plugins.AppConfig;
//import com.chanjet.workcircle.util.LogUtils;
//import com.chanjet.workcircle.util.ShareUtils;
//import com.chanjet.workcircle.util.ThreadPoolUtil;
//import com.chanjet.workcircle.util.behavioranalysis.CrashHandler;
//import de.greenrobot.event.EventBus;

/**
 * Created by shenxy on 15/11/19.
 */
public class AppMainViewModel {//extends AbstractViewModel {
    private static final String TAG = "AppMainViewModel";
    public static final String TYPE_SHARE = "TYPE_SHARE";
    public static final String TYPE_SHARE_FROMHTML = "TYPE_SHARE_FROMHTML";//分享功能,分享数据从html取值
    private AppIntentAction.AppType appType;
    private String appId;
    private AppConfig appConfig;
    //    private boolean showTitle=true;
    private String appUrl;
    public String appName;
    private String[] shareData;
    private Integer isShowTopTitleBar;//是否显示顶部导航 服务端返回
    private boolean canUseCrosswalk = true;    //用户设置是否可以使用corsswalk
    public static final int SHOW_TITLEBAR = 1;
    public static final int HIDE_TITLEBAR = 0;
    private boolean isRestore = false;
    //    private RxCommand<Void, Boolean> initCrossWalkCommand;
//    private RxCommand<Void, Boolean> cheatCrossWalkCommand;
//    private RxCommand<Void, ShareInfo> getShareInfoCommand;
    private Context context;
    private RestoreInstanceInfo restoreInstanceInfo = null; //如果plugin打开界面，导致系统回收该轻应用，此处保存plugin获得的返回值
    private ShareInfo shareInfo = new ShareInfo();

    public static final String LIGHTAPP_TITLE = "gzq_lightapp_title";
    public static final String LIGHTAPP_CONTENT = "gzq_lightapp_content";
    public static final String LIGHTAPP_IMG = "gzq_lightapp_img";

    public String[] getShareData() {
        return shareData;
    }

//    public boolean isShowTitle() {
//        return showTitle;
//    }

    public AppIntentAction.AppType getAppType() {
        return appType;
    }

    public String getAppId() {
        return appId;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public String getAppUrl() {
        return appUrl;
    }

//    public RxCommand<Void, Boolean> getInitCrossWalkCommand() {
//        return initCrossWalkCommand;
//    }
//
//    public RxCommand<Void, Boolean> getCheatCrossWalkCommand() {
//        return cheatCrossWalkCommand;
//    }
//
//    public RxCommand<Void, ShareInfo> getShareInfoCommand() {
//        return getShareInfoCommand;
//    }

    public void setRestoreInstanceInfo(RestoreInstanceInfo restoreInstanceInfo) {
        this.restoreInstanceInfo = restoreInstanceInfo;
    }

    public Integer getIsShowTopTitleBar() {
        return isShowTopTitleBar;
    }

    public RestoreInstanceInfo getRestoreInstanceInfo() {
        return restoreInstanceInfo;
    }

    public boolean isCanUseCrosswalk() {
        return canUseCrosswalk;
    }


    public AppMainViewModel(Context context) {
//        super(context);
        this.context = context;
    }

//    @Override
//    protected void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
////        initCrossWalkCommand = new RxCommand<Void, Boolean>(new Func1<Void, Observable<Boolean>>() {
////            @Override
////            public Observable<Boolean> call(Void aVoid) {
////                return AppMainViewModel.this.initCrossWalk();
////            }
////        });
//
//        cheatCrossWalkCommand = new RxCommand<Void, Boolean>(new Func1<Void, Observable<Boolean>>() {
//            @Override
//            public Observable<Boolean> call(Void aVoid) {
//                return AppMainViewModel.this.cheatCrossWalkIfPlugin();
//            }
//        });
//
//        getShareInfoCommand = new RxCommand<Void, ShareInfo>(new Func1<Void, Observable<ShareInfo>>() {
//            @Override
//            public Observable<ShareInfo> call(Void aVoid) {
//                return AppMainViewModel.this.getShareInfo();
//            }
//        });
//    }

    public void initParams(Bundle savedInstanceState, Intent intent) {
        if (savedInstanceState != null && savedInstanceState.containsKey(AppIntentAction.APP_CONFIG)) {
            restoreParams(savedInstanceState);
            isRestore = true;
        } else {
            getParams(intent);
        }
    }

    private void getParams(Intent intent) {
        String appTypeStr = intent.getStringExtra(AppIntentAction.APP_TYPE);
        appType = AppIntentAction.AppType.valueOf(appTypeStr);
        appConfig = (AppConfig) intent.getSerializableExtra(AppIntentAction.APP_CONFIG);
//        if (appConfig != null && appConfig.data != null) {
//            showTitle = (appConfig.data.appId == RemoteUrlAppManager.remoteUrlAppId
//                    || (AppConstants.IS_LIGHTAPP_DEBUG() && appType.equals(AppIntentAction.AppType.RemoteUrl)));
//        }

        if (intent.hasExtra(AppIntentAction.APP_ID)) {
            appId = intent.getStringExtra(AppIntentAction.APP_ID);
        }

        if (intent.hasExtra(AppIntentAction.APP_URL)) {
            appUrl = intent.getStringExtra(AppIntentAction.APP_URL);
        }

        if (intent.hasExtra(AppIntentAction.APP_DATA)) {
            shareData = intent.getStringArrayExtra(AppIntentAction.APP_DATA);
        }
        if (intent.hasExtra(AppIntentAction.APP_NAME)) {
            appName = intent.getStringExtra(AppIntentAction.APP_NAME);
        }
        if (intent.hasExtra(AppIntentAction.APP_ISSHOW_TITLEBAR)) {
            isShowTopTitleBar = intent.getIntExtra(AppIntentAction.APP_ISSHOW_TITLEBAR, AppMainViewModel.SHOW_TITLEBAR);
        } else {//默认显示顶部导航
            if (appConfig == null || appConfig.data == null || appConfig.data.appId == LightAppConstants.DEFAULT_REMOTEURL_APPID
                    || (AppConstants.IS_LIGHTAPP_DEBUG() && appType.equals(AppIntentAction.AppType.RemoteUrl))) {
                isShowTopTitleBar = AppMainViewModel.SHOW_TITLEBAR;
            } else {
                isShowTopTitleBar = AppMainViewModel.HIDE_TITLEBAR;
            }
        }

        canUseCrosswalk = intent.getBooleanExtra(AppIntentAction.APP_CAN_USE_CROSSWALK, true);
//        canUseCrosswalk = !appId.equals(String.valueOf(LightAppConstants.MAIL_263_APPID));  //由于263邮箱需要使用websql,在当前版本的crosswalk不支持,所以禁用
    }

    private void restoreParams(Bundle savedInstanceState) {
        String appTypeStr = savedInstanceState.getString(AppIntentAction.APP_TYPE);
        appType = AppIntentAction.AppType.valueOf(appTypeStr);
        appConfig = (AppConfig) savedInstanceState.getSerializable(AppIntentAction.APP_CONFIG);
//        if (appConfig != null && appConfig.data != null) {
//            showTitle = (appConfig.data.appId == RemoteUrlAppManager.remoteUrlAppId);
//        }

        if (savedInstanceState.containsKey(AppIntentAction.APP_ID)) {
            appId = savedInstanceState.getString(AppIntentAction.APP_ID);
        }

        if (savedInstanceState.containsKey(AppIntentAction.APP_URL)) {
            appUrl = savedInstanceState.getString(AppIntentAction.APP_URL);
        }

        if (savedInstanceState.containsKey(AppIntentAction.APP_DATA)) {
            shareData = savedInstanceState.getStringArray(AppIntentAction.APP_DATA);
        }
        if (savedInstanceState.containsKey(AppIntentAction.APP_ISSHOW_TITLEBAR)) {
            isShowTopTitleBar = savedInstanceState.getInt(AppIntentAction.APP_ISSHOW_TITLEBAR, AppMainViewModel.SHOW_TITLEBAR);
        }
        if (savedInstanceState.containsKey(AppIntentAction.APP_NAME)) {
            appName = savedInstanceState.getString(AppIntentAction.APP_NAME);
        }
    }
//
    public static boolean isUsingCrosswalk;

    public Observable<Boolean> initCrossWalk(Context context, String appId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadPoolUtil.getInstance().getExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean isCrossWalk = checkCrossWalkCoreLib(context);
                        isUsingCrosswalk = isCrossWalk && isCanUseCrosswalk();
//                        if (appId != null && isCrossWalk) {
//                            isCrossWalk = isLightAppEnabledCrossWalk(appId);
//                        }

                        subscriber.onNext(isUsingCrosswalk);
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    //暂时仅T+的轻应用(appId大于1000)启用 corsswalk
//    private static boolean isLightAppEnabledCrossWalk(String appId) {
//        return appId != null && StringUtils.isNumeric(appId)
//                && (Integer.parseInt(appId) > 1000 || Integer.parseInt(appId) == 15);
//    }
//
    public static boolean checkCrossWalkCoreLib(Context context) {
        return true;
    }
//        boolean isCrossWalk = false;
//        try {
//            if (CrosswalkPluginManager.getInstance().getCrosswalkMode() == CrosswalkPluginManager.CrosswalkMode.EMBED) {
//                Log.i(TAG, "CrosswalkMode.EMBED");
//                //不在这里做解压缩,利用CrossWalkCoreLib中的既有逻辑
//                if (!ChanjetXWalkLibDecompressor.isDecompressed(context)) {
//                    if (context instanceof BaseWebActivity) {
//                        ((BaseWebActivity) context).showDialog(BaseWebActivity.DIALOG_PROGRESS_LOADING);
//                    }
//                    Log.d(TAG, "CrosswalkCore library decompressing ");
//                    if (!ChanjetXWalkLibDecompressor.isDecompressing()) {
//                        ChanjetXWalkLibDecompressor.decompressLibrary(context);
//                    }
//                    Log.d(TAG, "CrosswalkCore library decompressed");
//                    if (context instanceof BaseWebActivity) {
//                        ((BaseWebActivity) context).dismissDialog(BaseWebActivity.DIALOG_PROGRESS_LOADING);
//                    }
//                }
//                isCrossWalk = CrosswalkPluginManager.getInstance().isXWalkConfigEnable(context) && !ChanjetXWalkLibDecompressor.isDecompressing();
//            } else if (CrosswalkPluginManager.getInstance().getCrosswalkMode() == CrosswalkPluginManager.CrosswalkMode.PLUGIN) {
//                Log.i(TAG, "CrosswalkMode.PLUGIN");
//                if (CrosswalkPluginManager.getInstance().isXWalkEnabled(context)) {
//                    isCrossWalk = true;
//                } else if (!CrosswalkPluginManager.isNotifiedOnce(context)) {
//                    CrosswalkPluginManager.getInstance().checkXWalkCoreDownload(context);
//                    isCrossWalk = false;
//                }
//            } else {
//                Log.i(TAG, "CrosswalkMode.UNABLE");
//                isCrossWalk = false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            CrashHandler.saveCrashLog(e);
//        }
//
//        return isCrossWalk;
//    }
//
    public String getLaunchUrl() {
        //加载页面
        if (appType.equals(AppIntentAction.AppType.LightApp)) {
            Log.i(TAG, "LightApp");
            int radom = (int) (Math.random() * 100000);
            if (StringUtils.isNotBlank(appId)) {
                //新的浏览器内核,延时加载
                return ZipInstaller.getInstallRootURI() + appId + "/index.html" + (isRestore ? "?isRestore=true&radom=" + radom : "?radom=" + radom);
            }

        } else {
            Log.i(TAG, "APP_URLAPP_URL");
            if (StringUtils.isNotBlank(appUrl)) {
                String connectSymbol = appUrl.contains("?") ? "&" : "?";
//                return appUrl + connectSymbol + (isRestore ? "isRestore=true&radom=" + radom : "radom=" + radom);
                return appUrl + (isRestore ? connectSymbol + "isRestore=true" : "");
            }
        }

        return null;
    }
//
//    private Observable<Boolean> cheatCrossWalkIfPlugin() {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                ThreadPoolUtil.getInstance().getExecutorService().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        //模拟内核的初始化逻辑
//                        try {
//                            if (!CrosswalkPluginManager.getInstance().isXWalkEnabled(context)) {
//                                subscriber.onNext(false);
//                                subscriber.onCompleted();
//                                return;
//                            }
//
//                            if (!(context instanceof AppMainRxActivity)) {
//                                subscriber.onNext(false);
//                                subscriber.onCompleted();
//                                return;
//                            }
//
//                            XWalkWebViewEngine webViewEngine = (XWalkWebViewEngine) ((CordovaActivity) context).getAppView().getEngine();
//                            ChanjetXWalkLibDecompressor.loadDecompressedLibrary(context);
//
//                            webViewEngine.setWevViewEngineActiveCompleteListener(new XWalkWebViewEngine.WevViewEngineActiveCompleteListener() {
//                                @Override
//                                public void OnResume() {
//                                    subscriber.onNext(true);
//                                    subscriber.onCompleted();
//                                }
//                            });
//
//                            webViewEngine.getActivityDelegate().onDecompressCompleted();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            CrashHandler.saveCrashLog(e);
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    private Observable<ShareInfo> getShareInfo() {
//        return Observable.create(new Observable.OnSubscribe<ShareInfo>() {
//            @Override
//            public void call(Subscriber<? super ShareInfo> subscriber) {
//                ThreadPoolUtil.getInstance()
//                        .getExecutorService()
//                        .execute(new Runnable() {
//                                     @Override
//                                     public void run() {
//                                         //0:类型 ,1:title 2:content 3:url 4:imageurl
//                                         if (shareData == null || shareData.length != 5 || TextUtils.isEmpty(shareData[0])) {
//                                             subscriber.onNext(null);
//                                             subscriber.onCompleted();
//                                             return;
//                                         }
//
//                                         String type = shareData[0];
//
//                                         if (TYPE_SHARE.equals(type)) {
//                                             shareInfo.shareTitle = shareData[1];
//                                             shareInfo.shareContent = shareData[2];
//                                             shareInfo.shareUrl = shareData[3];
//                                             shareInfo.shareImageUrl = shareData[4];
//
//                                             subscriber.onNext(shareInfo);
//                                             subscriber.onCompleted();
//                                         } else if (TYPE_SHARE_FROMHTML.equals(type)) {//分享功能 -分享数据从html中获取
//                                             shareInfo.shareUrl = shareData[3];
//
//                                             Document document = null;
//                                             try {
//                                                 document = Jsoup.connect(shareInfo.shareUrl).timeout(100000).post();
//
//                                                 if (document != null) {
//                                                     Elements elements = document.select("meta");
//                                                     if (elements != null) {
//                                                         for (Element element : elements) {
//                                                             if (element.attr("name").equals(LIGHTAPP_TITLE)) {
//                                                                 shareInfo.shareTitle = element.attr("content");
//                                                             }
//                                                             if (element.attr("name").equals(LIGHTAPP_CONTENT)) {
//                                                                 shareInfo.shareContent = element.attr("content");
//                                                             }
//                                                             if (element.attr("name").equals(LIGHTAPP_IMG)) {
//                                                                 shareInfo.shareImageUrl = element.attr("content");
//                                                             }
//                                                         }
//                                                     }
//                                                     if (StringUtils.isNotBlank(shareInfo.shareContent) && StringUtils.isNotBlank(shareInfo.shareTitle)) {
//                                                         subscriber.onNext(shareInfo);
//                                                         subscriber.onCompleted();
//                                                     } else {//若meta中为空,则title和content为页面的title
//                                                         shareInfo.shareTitle = document.title();
//                                                         shareInfo.shareContent = shareInfo.shareTitle;
//                                                         subscriber.onNext(shareInfo);
//                                                         subscriber.onCompleted();
//                                                     }
//                                                 }
//                                             } catch (Exception e) {
//                                                 e.fillInStackTrace();
//                                                 LogUtils.E(TAG, e.getLocalizedMessage());
//                                             }
//                                         }
//                                     }
//                                 }
//                        );
//            }
//        });
//    }

    /**
     * 服务端控制,是否显示顶部导航
     *
     * @return
     */
    public boolean isShowLightAppTitleBar() {
        if (isShowTopTitleBar != null && SHOW_TITLEBAR == isShowTopTitleBar) {
            return true;
        }
        return false;
    }
//
//    public void shareUrl(UMShareListener snsPostListener) {
//        if (StringUtils.isBlank(shareInfo.shareImageUrl)) {
//            shareInfo.shareImageUrl = AppConstants.getDefaultAppIconUrl();
//        }
//        if (StringUtils.isBlank(shareInfo.shareTitle) && StringUtils.isBlank(shareInfo.shareContent)) {
//            return;
//        }
//        ShareUtils.share((Activity) context, shareInfo.shareTitle, shareInfo.shareContent, shareInfo.shareImageUrl, shareInfo.shareUrl, snsPostListener);
//    }
//
//    public void setShareData(String shareTitle, String shareContent, String iconUrl, String shareUrl) {
//        shareInfo = new ShareInfo();
//        shareInfo.shareTitle = shareTitle;
//        shareInfo.shareContent = shareContent;
//        shareInfo.shareImageUrl = iconUrl;
//        shareInfo.shareUrl = shareUrl;
//    }
//
//    /**
//     * 第三方浏览器打开
//     */
//    public void openWebBySysBrowser() {
//        Uri uri = Uri.parse(getLaunchUrl());
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        if (context != null) {
//            context.startActivity(intent);
//        }
//    }
//
//    /**
//     * 创建群聊
//     */
//    public void createGroupChat() {
//        ArrayList<Activity> activities = BaseApplication.context.getActivities();
//        for (Activity activity : activities) {
//            if (activity instanceof SessionListActivity) {
//                ((SessionListActivity) activity).createGroupChat();
//            }
//        }
//    }

    /**
     * 复制内容到剪贴板
     *
     * @param content
     */
    public void copyText(String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(content, content);
        clipboardManager.setPrimaryClip(clipData);
    }

    public ShareInfo getCurShareInfo() {
        return shareInfo;
    }

    public class ShareInfo {
        public String shareTitle;
        public String shareContent;
        public String shareUrl;
        public String shareImageUrl;
    }

//    /**
//     * 测试 模拟顶部导航数据
//     */
//    public void testTitleBar() {
//        TitleBarInfo titleBarInfo = new TitleBarInfo();
//
//
//        TitleBarHelpBtnInfo helpBtnInfo = new TitleBarHelpBtnInfo();
//        helpBtnInfo.iconUrl = AppConstants.getDefaultAppIconUrl();
//        helpBtnInfo.onclick = "alert('帮助文档')";
//        EventBus.getDefault().post(helpBtnInfo);
////        titleBarInfo.showHelpBtn=helpBtnInfo;//帮助按钮
//
//        TitleBarCustomBtnInfo customBtnInfo = new TitleBarCustomBtnInfo();
//        customBtnInfo.onclick = "alert('自定义事件')";
//        customBtnInfo.text = "自定义";
//        titleBarInfo.showCustomBtn = customBtnInfo;
//        EventBus.getDefault().post(titleBarInfo);
//
//    }
}
