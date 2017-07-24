package com.shenxy.smartapploader.framework.loaderFactory;

import android.app.Activity;

import com.shenxy.smartapploader.framework.LightAppConstants;
import com.shenxy.smartapploader.framework.loader.BaseLoader;
import com.shenxy.smartapploader.framework.loader.UnknowTypeAppLoader;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;
import com.shenxy.smartapploader.utils.StringUtils;

import java.lang.reflect.Constructor;

import rx.Observable;
import rx.Subscriber;

//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.phonegap.framework.LightAppConstants;
//import com.chanjet.workcircle.phonegap.framework.loader.BaseLoader;
//import com.chanjet.workcircle.phonegap.framework.loader.UnknowTypeAppLoader;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;

/**
 * Created by shenxy on 16/4/25.
 * AppLoaderFactory的管理类
 * 增加抽象工厂,因为考虑到loader不是简单根据type字段区分:
 * 1)特殊处理的app
 * 2)可能和应用的是否安装等具体业务有关(如ThirdPartyApp类型)
 */
public class AppLoaderManager {
    /**
     * 扩展接口,用于扩展LoaderFactory以及相关Loader
     */
    public void addLoaderFactory(int index, LoaderCreator loaderFactory) {
        LightAppConstants.factoryList.add(index, loaderFactory);
    }

    public Observable<BaseLoader> createAppLoader(Activity activity, AppStartInfo appStartInfo) {
        if (appStartInfo.appInfo == null || appStartInfo.appInfo.appType == null) {
            return Observable.create(new Observable.OnSubscribe<BaseLoader>() {
                @Override
                public void call(Subscriber<? super BaseLoader> subscriber) {
                    subscriber.onError(new Throwable("缺少参数appType！"));
                }
            });
        }

        for (LoaderCreator loaderCreator : LightAppConstants.factoryList) {
            if (loaderCreator.isApplicable(activity, appStartInfo)) {
                return loaderCreator.createAppLoader(activity, appStartInfo);
            }
        }

        //如果从配置中没有适配的facotry,则返回默认基础factory
        return new AppTypeLoaderFactory().createAppLoader(activity, appStartInfo);
    }


    //根据loaderClassName创建BaseLoader.
    //配置的loaderClassName可能是指向BaseLoader的继承类, 也可能是指向实现了LoaderCreator接口的Factory类
    public Observable<BaseLoader> createAppLoaderByClassName(Activity activity, AppStartInfo appStartInfo, String loaderClassName) {
        if (StringUtils.isBlank(loaderClassName)) {
            return getBaseLoaderObservable(new UnknowTypeAppLoader());
        }

        BaseLoader appBaseLoader = null;
        try {
            Class<?> appLoaderClass = Class.forName(loaderClassName);
            Constructor<?> constructor = appLoaderClass.getConstructor();
            Object loaderOrFactory = constructor.newInstance();
            if (loaderOrFactory instanceof LoaderCreator) {
                LoaderCreator loaderCreator = (LoaderCreator) loaderOrFactory;
                if (loaderCreator.isApplicable(activity, appStartInfo)) {
                    return loaderCreator.createAppLoader(activity, appStartInfo);
                }
            } else if (loaderOrFactory instanceof BaseLoader) {
                appBaseLoader = (BaseLoader) loaderOrFactory;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getBaseLoaderObservable(appBaseLoader);
    }

    private Observable<BaseLoader> getBaseLoaderObservable(BaseLoader baseLoader) {
        return Observable.create(new Observable.OnSubscribe<BaseLoader>() {
            @Override
            public void call(Subscriber<? super BaseLoader> subscriber) {
                subscriber.onNext(baseLoader);
                subscriber.onCompleted();
            }
        });
    }
}
