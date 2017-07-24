package com.shenxy.smartapploader.app;

/**
 * Created by shenxy on 15/5/26.
 */
public class RestoreInstanceInfo {
    public RestoreInstanceInfo(String pluginName, String pluginAction, String restoreResp){
        this.pluginName = pluginName;
        this.pluginAction = pluginAction;
        this.restoreResp = restoreResp;
    }
//    long timestamp; //创建的时间戳

    public String pluginName;//: String型，回收前调用的类名（类名，不含TWCDV前缀）
    public String pluginAction;//：String型，回收前调用的方法名
    public String restoreResp;//：Json，回收前方法的返回值（包含完整的code、msg、data节）
}
