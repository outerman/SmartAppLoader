package com.shenxy.smartapploader.plugins;

/**
 * Created by shenxy on 14/12/5.
 */
public class AppExternalConfig extends JsBaseResponse {
    public AppExternalConfigData data;

    public AppExternalConfig(){
        data = new AppExternalConfigData();
    }

    public static class AppExternalConfigData {
        public String entId;               //企业ID
        public String userid;             //用户id
    }

}