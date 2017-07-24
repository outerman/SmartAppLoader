package com.shenxy.smartapploader.app;

//import com.chanjet.workcircle.phonegap.plugins.AppConfig;

import com.shenxy.smartapploader.plugins.AppConfig;

import org.crosswalk.engine.XWalkCordovaView;

/**
 * Created by shenxy on 16/2/22.
 * activity关于crosswalk的接口
 */
public interface CrosswalkActivityInterface {
    boolean isUsingCrosswalk();
    AppConfig getAppConfig();

    XWalkCordovaView getXWalkCordovaView();
}
