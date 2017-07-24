//package com.shenxy.smartapploader.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com.shenxy.smartapploader.utils.StringUtils;
//
///**
// * 针对NativePlugin类型轻应用,接收升级请求
// */
//public class NativePluginUpdateReceiver extends BroadcastReceiver {
//    private final String intentFilter = "com.chanjet.workcircle.NATIVE_PLUGIN_UPDATE";  //与AndroidManifest.xml保持一致
//    public static final String urlExtraName = "DOWNLOAD_URL_EXTRA";
//    public static final String appIdExtraName = "APPID_URL_EXTRA";
//    public static final String orgIdExtraName = "ORGID_URL_EXTRA";
//    public static final String appkeyExtraName = "APPKEY_URL_EXTRA";  //填写app在工作圈开放平台的key,暂时可以不填
//
//    public NativePluginUpdateReceiver() {
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        //接收请求,执行下载
//        String packageUrl = intent.getStringExtra(urlExtraName);
//        int appId = intent.getIntExtra(appIdExtraName, -1);
//        String orgId = intent.getStringExtra(orgIdExtraName);
//        if (StringUtils.isBlank(packageUrl) || appId < 0 || StringUtils.isBlank(orgId)) {
//            return;
//        }
//
//        //需要出dialog,在activity里处理
//        Intent activityIntent = new Intent();
//        activityIntent.setClass(context, NativePluginUpdateActivity.class);
//        activityIntent.putExtra(urlExtraName, packageUrl);
//        activityIntent.putExtra(appIdExtraName, appId);
//        activityIntent.putExtra(orgIdExtraName, orgId);
//        activityIntent.putExtra(NativePluginUpdateActivity.actionExtraName, NativePluginUpdateActivity.ActionType.download.name());
//        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(activityIntent);
//    }
//}
