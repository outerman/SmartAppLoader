package com.shenxy.smartapploader.framework.loader;

import android.app.Activity;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;

/**
 * Created by shenxy on 16/5/12.
 * AppLoader暴露给Installer的 接口,以获取installer宿主AppLoader的环境参数
 */
public interface AppLoaderEnv {
    Activity getActivity();
    AppStartInfo getStartInfo();
}
