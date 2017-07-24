package com.shenxy.smartapploader.framework.installer;

import android.content.Intent;
import android.util.Log;

import com.shenxy.smartapploader.app.AppIntentAction;
import com.shenxy.smartapploader.app.AppZipDownloading;
import com.shenxy.smartapploader.app.PreInstallAppAndOpenTask;
import com.shenxy.smartapploader.framework.loader.AppLoaderEnv;
import com.shenxy.smartapploader.framework.startInfo.DebugStartInfoUpdater;
import com.shenxy.smartapploader.utils.JSONExtension;
import com.shenxy.smartapploader.workTmp.BaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

//import com.chanjet.core.utils.JSONExtension;
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.phonegap.app.AppIntentAction;
//import com.chanjet.workcircle.phonegap.app.AppZipDownloading;
//import com.chanjet.workcircle.phonegap.app.PreInstallAppAndOpenTask;
//import com.chanjet.workcircle.phonegap.framework.loader.AppLoaderEnv;
//import com.chanjet.workcircle.phonegap.framework.startInfo.DebugStartInfoUpdater;

/**
 * Created by shenxy on 16/4/17.
 * zip包类型轻应用的安装类
 */
public class ZipInstaller extends BaseInstaller {
    public ZipInstaller(AppLoaderEnv appLoaderEnv) {
        super(appLoaderEnv);
    }

    @Override
    public void preinstall() {
        //shenxy 2015-3-27 增加预安装逻辑：如果打在包里的轻应用已经是最新的，则不用下载，直接使用打在包里的即可
        //只有zip包类型的轻应用，才有预安装逻辑  //T+类型的版本号不确定的zip包轻应用，也不预装
        if (getStartInfo().appInfo != null && getStartInfo().appInfo.appType != null
                && getStartInfo().appInfo.appType == 0 && getStartInfo().appInfo.version != null && getStartInfo().appInfo.version != -1) {
            String preInstallFolder = ZipInstaller.getAppFolderName(getStartInfo().appId); //打包在assets中的文件夹名
            Integer preInstallAppVersion = ZipInstaller.getPreInstallAppVersion(preInstallFolder);
            //没有安装，或者预安装版本比已安装版本更加新（app升级）
            if (!isInstalled() || preInstallAppVersion > ZipInstaller.getAppVersion(getStartInfo().appId)) {
                try {
                    String[] preInstall = getActivity().getAssets().list(preInstallFolder);  //如果文件夹存在
                    if (preInstall.length > 0) {
                        int remoteVersion = getStartInfo().appInfo.version;
                        if (preInstallAppVersion >= remoteVersion) {
//                            PreInstallAppAndOpenTask preInstallAppAndOpenTask = new PreInstallAppAndOpenTask(getActivity());
//                            preInstallAppAndOpenTask.setPreInstallListener(new PreInstallAppAndOpenTask.PreInstallListener() {
//                                @Override
//                                public void OnPreInstallFinish() {
//                                    subscriber.onNext(true);
//                                    subscriber.onCompleted();
//                                }
//                            });
//
//                            preInstallAppAndOpenTask.execute(getStartInfo().appId, preInstallAppVersion);

                            new PreInstallAppAndOpenTask(getActivity()).preInstallZip(getStartInfo().appId, preInstallAppVersion);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void downloadApp() {
        try {
            Intent intent = new Intent();
            intent.setClass(getActivity(), AppZipDownloading.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AppIntentAction.APP_START_INFO, getStartInfo());
            getActivity().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean installApp(String targetFilePath) {
        //zip包的安装在PreInstallAppAndOpenTask(内置包)和AppZipDownloading(下载包)进行安装,此处不做处理
        return super.installApp(targetFilePath);
    }

    @Override
    public boolean isInstalled() {
        //是否安装了最新版本
        return ZipInstaller.isAppInstalled(getStartInfo().appId)
                && ZipInstaller.getAppVersion(getStartInfo().appId) >= getStartInfo().appInfo.version
                && getStartInfo().appInfo.version != DebugStartInfoUpdater.DEBUG_FORCE_UPDATE_VERSION;
    }


    //是否安装了应用
    public static boolean isAppInstalled(Integer appId) {
        String appFolderName = getAppFolderName(appId);
        File startupFile = new File(getInstallRoot() + appFolderName + "/index.html");
        File startupFileRN = new File(getInstallRoot() + appFolderName + "/index.android.js"); //ReactNative的启动文件
        return startupFile.exists() || startupFileRN.exists();
    }


    //根据appId确定轻应用的安装文件夹名
    public static String getAppFolderName(int appId) {
        return String.valueOf(appId);
    }

    //获取打包在apk中的轻应用的版本号
    public static Integer getPreInstallAppVersion(String appFolderName) {
        try {
            InputStream versionFileInputStream = BaseApplication.getContext().getAssets().open(appFolderName + "/version.json");
            return getAppVersion(versionFileInputStream);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return 0;
    }

    //获取当前安装app的版本号
    public static Integer getAppVersion(int appId) {
        File startupFile = new File(getInstallRoot() + getAppFolderName(appId) + "/version.json");
        if (!startupFile.exists()) {
            return 0;
        }
        try {
            FileInputStream iStream = new FileInputStream(startupFile);
            return getAppVersion(iStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static Integer getAppVersion(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {
            if (inputStream.available() > 0) {
                BufferedReader reader;

                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String tempString;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
                    sb.append(tempString);
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppVersion appVersion = JSONExtension.parseObject(sb.toString(), AppVersion.class);

        if (appVersion != null) {
            Log.i("AppManager", "appVersion.version:" + appVersion.version + "");
            return appVersion.version;
        } else {
            return 0;
        }
    }

    public static String getInstallRoot() {
        return BaseApplication.getContext().getFilesDir() + "/";
    }

    public static String getInstallRootURI() {
        return "file:///" + getInstallRoot();
    }
}
