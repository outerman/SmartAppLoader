package com.shenxy.smartapploader.framework;

import com.shenxy.smartapploader.framework.loaderFactory.AppIdLoaderFactory;
import com.shenxy.smartapploader.framework.loaderFactory.AppStatusLoaderFactory;
import com.shenxy.smartapploader.framework.loaderFactory.AppTypeLoaderFactory;
import com.shenxy.smartapploader.framework.loaderFactory.LoaderCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenxy on 16/4/26.
 * 常量类
 */
public class LightAppConstants {
//    public static final String RN_RUNTIME_PACKAGENAME = "com.lightappreactnativebridge";  //RN运行时包的包名

    public static final int DEFAULT_ORGID = -1;                     //当没有当前企业时orgId的填充值
    public static final int DEFAULT_REMOTEURL_APPID = -1;           //remote模式时,没有appId的填充值
    public static final int DEFAULT_APP_TYPE = -1;                  //当没有appType时,填充值.(例如"更多")
    public static final int TPLUS_SELF_UPDATE_CHECK_VERSION = -1;   //T+的应用,自行进行升级检查,在服务端配置的特殊version

    public static final int MAIL_263_APPID = 24;            //263邮箱的appId
    public static final int TELE_CONFERENCE_APPID = 17;     //电话会议appId
    public static final int SHOW_HIDE_APP = -10;            //"更多"按钮的appId(这个只在本地出现)
    public static final int PUBLIC_NOTICE_APPID = 13;       //公告的appId
    public static final int APPROVAL_APPID = 3;             //审批的appId
    public static final int TASK_APPID = 12;                //任务的appId
    public static final int OUTSIDE_SIGNIN_APP = 11;        //外勤签到的appId
    public static final int WORK_REPORT_APP = 14;           //外勤签到的appId

    public static final int APP_TYPE_ZIP = 0;
    public static final int APP_TYPE_THIRD_PARTY_APP = 1;
    public static final int APP_TYPE_REMOTE_URL = 2;
    public static final int APP_TYPE_NATIVE_PLUGIN = 3;
    public static final int APP_TYPE_DEVELOPER = 4;
    public static final int APP_TYPE_GREY_USER = 5;
    public static final int APP_TYPE_REACT_NATIVE = 1000;

    public static final int CAN_USE_CROSSWALK_DEFAULT = 0;          //可以使用Crosswalk的配置值,如果不为此值,则不使用Crosswalk内核

    public static List<LoaderCreator> factoryList = new ArrayList<>();
    public static Map<Integer, String> appIdLoaderConfig = new HashMap<>();
    public static Map<Integer, String> appTypeLoaderConfig = new HashMap<>();

    static {
        //此处配置LoaderFactory,可扩展,根据加入先后的优先顺序,依次处理
        factoryList.add(new AppStatusLoaderFactory());      //根据status做处理的轻应用
        factoryList.add(new AppIdLoaderFactory());          //根据AppId做处理的轻应用
        factoryList.add(new AppTypeLoaderFactory());        //根据AppType做分类处理的轻应用

        //此处配置AppIdLoaderFactory管理的Loader: 可以配置BaseLoader类,或LoaderCreator接口的工厂类
        /*appIdLoaderConfig.put(TELE_CONFERENCE_APPID, "com.chanjet.workcircle.phonegap.framework.loader.TeleconferenceAppLoader");
        appIdLoaderConfig.put(SHOW_HIDE_APP, "com.chanjet.workcircle.phonegap.framework.loader.HideAppLoader");
*/


        //此处配置AppTypeLoaderFactory管理的Loader(主要扩展方式,默认方式)
        //可以配置BaseLoader类,或LoaderCreator接口的工厂类
        // 0：下载H5的zip包的轻应用   （version为-1，则版本号需要在线检查的h5轻应用，否则，根据版本号递增做升级判断）
        // 1：客户端单点登录的轻应用    (可能apk单点登录,可能zip包)
        // 2：RemoteUrl类型的轻应用    (可能用webview打开页面,可能用系统浏览器打开下载文件)
        // 3：ApkPlugin的原生轻应用
        appTypeLoaderConfig.put(APP_TYPE_ZIP, "com.shenxy.smartapploader.framework.loaderFactory.ZipAppLoaderFactory");
        appTypeLoaderConfig.put(APP_TYPE_THIRD_PARTY_APP, "com.shenxy.smartapploader.framework.loaderFactory.ThirdPartyAppLoaderFactory");
        appTypeLoaderConfig.put(APP_TYPE_REMOTE_URL, "com.shenxy.smartapploader.framework.loaderFactory.RemoteUrlAppLoaderFactory");
        appTypeLoaderConfig.put(APP_TYPE_NATIVE_PLUGIN, "com.shenxy.smartapploader.framework.loader.NativePluginAppLoader");
        appTypeLoaderConfig.put(APP_TYPE_DEVELOPER, "com.shenxy.smartapploader.framework.loader.DeveloperAppLoader");//TODO:本地联调入口
        appTypeLoaderConfig.put(APP_TYPE_GREY_USER, "com.shenxy.smartapploader.framework.loader.DeveloperAppLoader");//TODO:灰度发布类型
        appTypeLoaderConfig.put(APP_TYPE_REACT_NATIVE, "com.shenxy.smartapploader.framework.loader.ReactNativeAppLoader");//实验:ReactNative
    }
}
