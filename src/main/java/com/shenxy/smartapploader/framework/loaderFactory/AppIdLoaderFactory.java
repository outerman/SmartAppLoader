package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;
import com.shenxy.smartapploader.utils.StringUtils;

import rx.Observable;

//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;

/**
 * Created by shenxy on 16/4/25.
 * 根据appid特殊处理的apploader
 * 1)电话会议
 * 2)即将推出
 * 3)联调入口
 * 4)263只需要确定appid,不需要特殊loader
 */
public class AppIdLoaderFactory implements LoaderCreator {
    @Override
    public boolean isApplicable(Activity activity, AppStartInfo appStartInfo) {
        return appStartInfo != null && StringUtils.isNotBlank(LightAppConstants.appIdLoaderConfig.get(appStartInfo.appId));
    }

    @Override
    public Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo) {
        String loaderClassName = LightAppConstants.appIdLoaderConfig.get(appStartInfo.appId);

        return new AppLoaderManager().createAppLoaderByClassName(activity, appStartInfo, loaderClassName);
    }
}
