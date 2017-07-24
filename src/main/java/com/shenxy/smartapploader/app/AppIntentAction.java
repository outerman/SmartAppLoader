package com.shenxy.smartapploader.app;

public class AppIntentAction {
    public static final String APP_ID = "chanjet.lightapp.intent.APP_ID";
//    public static final String ENT_ID = "chanjet.lightapp.intent.ENT_ID";
//    public static final String PAGE = "chanjet.lightapp.intent.PAGE";
//    public static final String TAB = "chanjet.lightapp.intent.TAB";

    //打开轻应用时候传递参数
    public static final String APP_START_INFO = "chanjet.lightapp.intent.APP_START_INFO";

    //打开url时，传递参数
    public static final String APP_TYPE = "chanjet.lightapp.intent.APP_TYPE";
    public static enum AppType{
        LightApp,
        RemoteUrl
    }
    public static final String APP_URL = "chanjet.lightapp.intent.APP_URL";
    public static final String APP_NAME = "chanjet.lightapp.intent.APP_NAME";

    public static final String APP_CONFIG = "chanjet.lightapp.intent.APP_CONFIG";

    public static final String APP_DATA="chanjet.lightapp.intent.APP_DATA";//原生跳转至appmainactivity封装的参数 by wk

    public static final String APP_ISSHOW_TITLEBAR="chanjet.lightapp.intent_APP_ISSHOW_TITLEBAR";//是否显示顶部导航

    public static final String APP_CAN_USE_CROSSWALK="chanjet.lightapp.APP_CAN_USE_CROSSWALK";//是否可以使用crosswalk (如果条件可以的话)
}
