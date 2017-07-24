package com.shenxy.smartapploader.framework.installer;

import android.app.Activity;

import com.shenxy.smartapploader.framework.loader.AppLoaderEnv;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;

/**
 * Created by shenxy on 16/4/26.
 * 轻应用Installer的基类
 * 考虑到4-1迭代发版,暂不做这个重构
 */
public class BaseInstaller {
    private AppLoaderEnv appLoaderEnv;

    public BaseInstaller(AppLoaderEnv appLoaderEnv) {
        this.appLoaderEnv = appLoaderEnv;
    }

    public final AppStartInfo getStartInfo() {
        return appLoaderEnv.getStartInfo();
    }

    public final Activity getActivity() {
        return appLoaderEnv.getActivity();
    }

    //installer的四个基本方法
    //1)检查并安装内置包
    public void preinstall() {

    }

    //2)下载安装包/升级包
    public void downloadApp() {

    }

    //3)安装 安装包
    public boolean installApp(String targetFilePath) {
        return true;
    }

    //4)检查是否已安装(最新的包)
    public boolean isInstalled() {
        return true;
    }

}
