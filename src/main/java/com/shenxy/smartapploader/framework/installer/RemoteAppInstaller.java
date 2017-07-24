package com.shenxy.smartapploader.framework.installer;

//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.startInfo.RemoteStartParam;
//import com.chanjet.workcircle.request.MyWorkQuery;

import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.startInfo.RemoteStartParam;
import com.shenxy.smartapploader.request.MyWorkQuery;

/**
 * Created by shenxy on 16/4/19.
 * remote模式的app的初始化类
 */
public class RemoteAppInstaller {
    public MyWorkQuery.MyWorkItem makeRemoteAppInfo() {
        MyWorkQuery.MyWorkItem remoteWorkItem = new MyWorkQuery.MyWorkItem();
        remoteWorkItem.appId = LightAppConstants.DEFAULT_REMOTEURL_APPID;
        remoteWorkItem.appType = 2;
        remoteWorkItem.status = 1;  //已发布
//        remoteWorkItem.name = titleName;
//        remoteWorkItem.navBar = showTitlebar;
//
//
//        startInfo.remoteStartParam = new RemoteStartParam();
//        startInfo.remoteStartParam.remoteUrl = remoteUrl;
//        startInfo.remoteStartParam.titleName = titleName;
//        startInfo.remoteStartParam.data = data;
//        startInfo.remoteStartParam.isShowTitleBar = isShowTitleBar;



        return remoteWorkItem;
    }

    public RemoteStartParam makeRemoteStartInfo(String remoteUrl, String titleName, String[] data, Integer showTitlebar) {
        RemoteStartParam remoteStartParam = new RemoteStartParam();
        remoteStartParam.remoteUrl = remoteUrl;
        remoteStartParam.titleName = titleName;
        remoteStartParam.data = data;
        remoteStartParam.isShowTitleBar = showTitlebar;

        return remoteStartParam;
    }
}
