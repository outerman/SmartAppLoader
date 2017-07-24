package com.shenxy.smartapploader.framework;

import android.app.Activity;
import android.widget.Toast;

import com.shenxy.smartapploader.framework.installer.BaseInstaller;
import com.shenxy.smartapploader.framework.installer.RemoteAppInstaller;
import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.loaderFactory.AppLoaderManager;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;
import com.shenxy.smartapploader.framework.startInfo.DebugStartInfoUpdater;
import com.shenxy.smartapploader.framework.startInfo.StartInfoUpdater;
import com.shenxy.smartapploader.request.MyWorkQuery;
import com.shenxy.smartapploader.request.WorkMsgDto;
import com.shenxy.smartapploader.utils.StringUtils;
import com.shenxy.smartapploader.workTmp.BaseApplication;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.entity.WorkMsgDto;
//import com.chanjet.workcircle.phonegap.framework.installer.BaseInstaller;
//import com.chanjet.workcircle.phonegap.framework.installer.RemoteAppInstaller;
//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.loaderFactory.AppLoaderManager;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;
//import com.chanjet.workcircle.phonegap.framework.startInfo.DebugStartInfoUpdater;
//import com.chanjet.workcircle.phonegap.framework.startInfo.StartInfoUpdater;
//import com.chanjet.workcircle.request.MyWorkQuery;
//import com.chanjet.workcircle.request.OrgListQuery;
//import com.chanjet.workcircle.util.OrgManager;

/**
 * Created by shenxy on 16/4/16.
 * 插件应用管理类
 */
public class LightAppManager {
    //从消息打开时,appInfo每次调用并缓存;先使用缓存打开,避免接口慢导致打开速度慢
    //installer的接口统一
    //TODO:native类型的app,服务端到端的方案.插件加固?
    //校验orgid和appid
    //Factory类+ loader类的可配置可扩展
    //TODO:developer相关loader\列表等
    //TODO:app的认证:key验证
    //TODO:插件权限管理
    //react native类型的app
    //TODO:抽取到独立的module中
    //TODO:统一跳转
    BaseLoader appBaseLoader;

    private static class SingletonHolder {
        private static LightAppManager instance = new LightAppManager();
    }

    public static LightAppManager getInstance() {
        return SingletonHolder.instance;
    }

    public void startRemoteUrlLightApp(final Activity activity, final String remoteUrl, final String titleName, String[] data, Integer isShowTitleBar) {
        startRemoteUrlLightApp(activity, LightAppConstants.DEFAULT_REMOTEURL_APPID, remoteUrl, titleName, data, isShowTitleBar);
    }

    public void startRemoteUrlLightApp(Activity activity, int appId, String remoteUrl, String titleName, String[] data, Integer isShowTitleBar) {
        if (activity == null) {
            return;
        }
        BaseApplication.context = activity;

        String currentOrgId = String.valueOf(LightAppConstants.DEFAULT_ORGID);
//        OrgListQuery.OrgInfo currentOrgInfo = OrgManager.getInstance().getCurrentOrgInfo();
//        if (currentOrgInfo != null) {
//            currentOrgId = currentOrgInfo.orgId;
//        }

        final AppStartInfo startInfo = new AppStartInfo();
        startInfo.appId = LightAppConstants.DEFAULT_REMOTEURL_APPID;
        startInfo.entId = currentOrgId;
        startInfo.appInfo = new RemoteAppInstaller().makeRemoteAppInfo();
        startInfo.remoteStartParam = new RemoteAppInstaller().makeRemoteStartInfo(remoteUrl, titleName, data, isShowTitleBar);

        updateAppInfoAndStartApp(activity, startInfo);
    }

    public void startApp(Activity activity, String appId, String entId, String page, MyWorkQuery.MyWorkItem appInfo, WorkMsgDto msg) {
        if (activity == null || !StringUtils.isNumeric(appId)) {
            return;
        }
        BaseApplication.context = activity;

        final AppStartInfo startInfo = new AppStartInfo();
        startInfo.appId = Integer.parseInt(appId);
        startInfo.entId = StringUtils.isBlank(entId) ? String.valueOf(LightAppConstants.DEFAULT_ORGID) : entId;
        startInfo.page = StringUtils.isBlank(page) ? "list" : page;//page: 页面（“list”列表页、”detail”详情页）"home"
        startInfo.appInfo = appInfo;
        startInfo.workMsgInfo = msg;


        //先检查debug模式,再检查checkStartInfo,然后再createLoader
        new DebugStartInfoUpdater(activity, startInfo).checkDebugStartInfo()
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean isGoOn) {
                        return isGoOn;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        updateAppInfoAndStartApp(activity, startInfo);
                    }
                });
    }

    private void updateAppInfoAndStartApp(Activity activity, AppStartInfo startInfo) {
        new StartInfoUpdater().checkStartInfo(startInfo)
                .filter(new Func1<MyWorkQuery.MyWorkItem, Boolean>() {
                    @Override
                    public Boolean call(MyWorkQuery.MyWorkItem myWorkItem) {
                        return myWorkItem != null;
                    }
                })
                .subscribe(
                        new Action1<MyWorkQuery.MyWorkItem>() {
                            @Override
                            public void call(MyWorkQuery.MyWorkItem myWorkItem) {
                                if (startInfo.appInfo == null) {
                                    if (myWorkItem != null) {
                                        startInfo.appInfo = myWorkItem;
                                    } else {
                                        Toast.makeText(activity, "缺少app信息,无法打开!", Toast.LENGTH_LONG).show();
                                    }
                                }
                                createLoaderAndLaunchApp(activity, startInfo);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (!activity.isFinishing()) {
                                    Toast.makeText(activity, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }

    private void createLoaderAndLaunchApp(final Activity activity, AppStartInfo startInfo) {
        //连续异步操作,在此处以后,startInfo.appInfo不为空
        //1)创建loader
        new AppLoaderManager().createAppLoader(activity, startInfo)
                .filter(new Func1<BaseLoader, Boolean>() {
                    @Override
                    public Boolean call(BaseLoader baseLoader) {
                        appBaseLoader = baseLoader;
                        return baseLoader != null;
                    }
                })
                .switchMap(new Func1<BaseLoader, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(BaseLoader baseLoader) {
                        //2)检查/修改StartInfo
                        appBaseLoader.setStartInfo(startInfo);
                        appBaseLoader.setActivity(activity);
                        return appBaseLoader.checkStartInfo();
                    }
                })
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean isStartInfoChecked) {
                        return isStartInfoChecked;
                    }
                })
                .switchMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        //3)下载app权限列表
                        return appBaseLoader.checkAuth();
                    }
                })
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean isAuth) {
                        return isAuth;
                    }
                })
                .switchMap(new Func1<Boolean, Observable<BaseInstaller>>() {
                    @Override
                    public Observable<BaseInstaller> call(Boolean aBoolean) {
                        //4)初始化该Loader的installer
                        return appBaseLoader.initInstaller();
                    }
                })
                .filter(new Func1<BaseInstaller, Boolean>() {
                    @Override
                    public Boolean call(BaseInstaller baseInstaller) {
                        return baseInstaller != null;
                    }
                })
                .switchMap(new Func1<BaseInstaller, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(BaseInstaller installer) {
                        //5)检查并下载\安装app
                        appBaseLoader.setInstaller(installer);
                        return appBaseLoader.downloadAndInstall();
                    }
                })
//                .subscribeOn(Schedulers.io())
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean isInstalled) {
                        return isInstalled;
                    }
                })
                .switchMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        //6)打开app
                        return appBaseLoader.openApp();
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        //7)释放loader
                        releaseApploader();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!activity.isFinishing()) {
                            Toast.makeText(activity, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                        releaseApploader();
                    }
                });
    }

    //不管加载是否成功,释放AppLoader,避免对Activity的引用导致内存占用
    private void releaseApploader() {
        if (appBaseLoader != null) {
            //如果releaseLoader以后,loader中的异步操作有可能得不到getActivity和getStartInfo(例如,在dialog的确定按钮中使用getActivity等)
//            appBaseLoader.releaseLoader();
            appBaseLoader = null;
        }
    }

    //用于选图和上传插件之间，共享上传图片和选中的原图之间的对应关系
    private Map<String, String> photoSourceMap = new HashMap<>();

    public String getCachedSource(String uploadPhotoPath) {
        return photoSourceMap.get(uploadPhotoPath);
    }

    public void putCachedSource(String uploadPhotoPath, String sourcePhotoPath) {
        photoSourceMap.put(uploadPhotoPath, sourcePhotoPath);
    }
}
