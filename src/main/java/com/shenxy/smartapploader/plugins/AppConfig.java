package com.shenxy.smartapploader.plugins;

import java.io.Serializable;

/**
 * Created by shenxy on 14/12/5.
 */
public class AppConfig extends JsBaseResponse implements Serializable {
    public AppConfigData data;


    public static class AppConfigData implements Serializable {
        public String entId;               //企业ID
        public String page;                //页面
        public String tab;                 //tab页签
        public String userid;             //用户id
        public boolean back_to_native;    //表示点击轻应用的后退按钮，应该返回到native
        public String auth;                 //authorization
        public String domain;              //当前服务器
        public AppResData params;          //源业务单据信息
        public String accessToken;        //T+使用
        public String version;              //app的当前版本号name——1.1.5.001
        public LightAppDeviceInfo deviceInfo;           //设备信息
        public String ext;                  //T+等的扩展字段，直接透传
        public int appId;
        public String Host;                 //当前服务器，对应的域名；用于httpdns的场景

        public String entName;              //企业名
        public String entAvatar;            //企业 logo链接
        public String userName;             //用户名
        public String userAvatar;           //用户头像链接

        public AppConfigData() {
            params = new AppResData();
            deviceInfo = new LightAppDeviceInfo();
        }

        public String platformExtension;//透传字段
        public Long startTimestamp;//订购时间
        public Long endTimestamp;//应用订购截止的时间戳
        public Long currentTimeStamp; //当前服务端时间
    }

    public static class LightAppDeviceInfo implements Serializable{
        public String manufacturer;         //制造商（厂商）
        public String model;                 //型号
        public String osRelease;               //系统版本
        public String networkStatus;        //网络状态：UNKNOWN,
        //                                                            ETHERNET,
//                                                            WIFI,
//                                                            CELL_2G,
//                                                            CELL_3G,
//                                                            CELL_4G,
//                                                            CELL,
//                                                            NoNetwork
        public String usedMemory;            //当前内存占用
        public String availableMemory;              //最大可用内存
        public String deviceId;               //authorization中给到服务端的deviceId
        public String deviceType;               //APH表示设备类型
    }

    public static class AppResData implements Serializable {
        public String id;
    }
}