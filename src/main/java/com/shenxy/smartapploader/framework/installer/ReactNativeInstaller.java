//package com.shenxy.smartapploader.framework.installer;
//
//import com.chanjet.workcircle.phonegap.framework.loader.AppLoaderEnv;
//import com.chanjet.workcircle.phonegap.framework.loader.NativePluginAppLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;
//import com.chanjet.workcircle.request.MyWorkQuery;
//
///**
// * Created by shenxy on 16/5/16.
// * ReactNative的安装类,基于ZipInstaller,添加RN运行时环境的检查和安装
// *
// * 设计:
// * 1)该installer继承ZipInstaller,并增加NativePluginInstaller的内部对象
// * 2)各步骤的判断和逻辑,在super的基础上,增加对 NativePluginInstaller内部对象 的判断
// *
// */
//public class ReactNativeInstaller extends ZipInstaller {
//    private NativePluginInstaller runtimeInstaller;
//
//    public ReactNativeInstaller(AppLoaderEnv appLoaderEnv) {
//        super(appLoaderEnv);
//
//        //构造给NativePluginInstaller使用的环境变量
//        NativePluginAppLoader runtimeLoader = new NativePluginAppLoader();
//        runtimeLoader.setActivity(appLoaderEnv.getActivity());
//
//        AppStartInfo runtimeStartInfo = new AppStartInfo();
//        runtimeStartInfo.appId = getStartInfo().appId;
//        runtimeStartInfo.entId = getStartInfo().entId;
//        runtimeStartInfo.appInfo = new MyWorkQuery.MyWorkItem();
//        runtimeStartInfo.appInfo.extension = new MyWorkQuery.Extension();
//        runtimeStartInfo.appInfo.extension.androidPackage = getStartInfo().appInfo.extension.runtimePackage;
//        runtimeStartInfo.appInfo.version = getStartInfo().appInfo.extension.runtimeVersion;
//        runtimeStartInfo.appInfo.downUrl = new MyWorkQuery.DownloadUrlInfo();
//        runtimeStartInfo.appInfo.downUrl.APH = getStartInfo().appInfo.extension.runtimeDownloadUrl;
//
//        runtimeLoader.setStartInfo(runtimeStartInfo);
//        runtimeInstaller = new NativePluginInstaller(runtimeLoader);
//    }
//
//    @Override
//    public void preinstall() {
//        runtimeInstaller.preinstall();
//        super.preinstall();
//    }
//
//    @Override
//    public void downloadApp() {
//        if (!runtimeInstaller.isInstalled()) {
//            runtimeInstaller.downloadApp();
//        }
//        else {
//            super.downloadApp();
//        }
//    }
//
//    @Override
//    public boolean isInstalled() {
//        return runtimeInstaller.isInstalled() && super.isInstalled();
//    }
//
//    public NativePluginInstaller getRuntimeInstaller() {
//        return runtimeInstaller;
//    }
//}
