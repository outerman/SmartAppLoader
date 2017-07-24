//package com.shenxy.smartapploader.framework.installer;
//
//import com.shenxy.smartapploader.framework.loader.AppLoaderEnv;
//
///**
// * Created by shenxy on 16/5/24.
// * T+的Native插件,自行检查升级
// */
//public class NativePluginForTPlusInstaller extends NativePluginInstaller{
//    public NativePluginForTPlusInstaller(AppLoaderEnv appLoaderEnv) {
//        super(appLoaderEnv);
//    }
//
//    //预安装步骤,只需要检查是否有待安装的已经下载好的包
////    @Override
////    public void preinstall() {
////        String pluginApkName = getApkLocalFileName(getStartInfo().appId);
////        String downloadFilePath = DEFAULT_DOWNLOAD_PACKAGE_PATH + pluginApkName;
////        //上次下载的包 和 内置的包,如果有比服务端最新的,则安装其中最新的
////        int downloadPackageVersion = -1;
////        if (new File(downloadFilePath).exists()) { //下载完成后才拷贝出来的,所以只要存在就是下载完成的
////            downloadPackageVersion = getApkPackageVersionCode(getActivity(), downloadFilePath);
////        }
////
////        if (downloadPackageVersion > getInstalledVersionCode(getStartInfo().appInfo.extension.androidPackage)) {
////            installApp(downloadFilePath);
////        }
////    }
//
//    @Override
//    public boolean isInstalled() {
//        return false;  //固定返回false
//    }
//
//    @Override
//    public void downloadApp() {
//        //不做网络下载
//    }
//}
