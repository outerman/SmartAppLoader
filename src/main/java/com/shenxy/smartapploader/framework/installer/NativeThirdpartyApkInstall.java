//package com.shenxy.smartapploader.framework.installer;
//
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.phonegap.framework.loader.AppLoaderEnv;
//import com.chanjet.workcircle.util.DownloadApkAndInstallUtil;
//
//import java.util.List;
//
///**
// * Created by shenxy on 16/4/21.
// * 第三方apk引用的加载类
// */
//public class NativeThirdpartyApkInstall extends BaseInstaller {
//    public NativeThirdpartyApkInstall(AppLoaderEnv appLoaderEnv) {
//        super(appLoaderEnv);
//    }
//
//    //暂不需要loader来执行下载,在startInfo中也没有apk的下载链接(只有app未安装时,介绍页H5的zip包链接)
////    @Override
////    public void downloadApp() {
////        downloadApk();
////    }
//
//    @Override
//    public boolean isInstalled() {
//        return isApkInstalled(getStartInfo().appInfo.extension.androidPackage);
//    }
//
//    public static void downloadApk(String appUrl) {
//        new DownloadApkAndInstallUtil(BaseApplication.getContext())
//                .downloadAndInstallApk(
//                        appUrl,
//                        DownloadApkAndInstallUtil.getDefaultDownloadPath(),
//                        DownloadApkAndInstallUtil.getDefaultDownloadFileName() + ".apk",
//                        null);
//    }
//
//    public static boolean isApkInstalled(String packageName) {
//        PackageManager pm = BaseApplication.getContext().getPackageManager();
//        List<PackageInfo> infos = pm.getInstalledPackages(0);
//        for (PackageInfo info : infos) {
//            if (info.packageName.equalsIgnoreCase(packageName))
//                return true;
//        }
//        return false;
//    }
//}
