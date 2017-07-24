package com.shenxy.smartapploader.request;

import java.io.Serializable;
import java.util.List;

/**
 * 圈子默认图标
 */
public class MyWorkQuery  {

    public static class Request {
        public String userid;
    }

    public static class Response extends BaseResponse {
//        public Integer code;
//        public String msg;
        public List<MyWorkItem> data;
    }

    public static class MyWorkItem implements Serializable {
        public String desp;
        public String orgId;
        public String iconUrl;
        public IconUrl iconUrl_new;
        public DownloadUrlInfo downUrl;
        public String _id;
        public String name;
        public Integer orderBy;
        public Integer status = 0;
        public Integer supporNum = 0;
        public Integer suppored = 0;
        public Integer initNum = 0;
        public Integer version;
        public Integer auditNum = 0;
        public Integer unReadNum = 0;
        public Integer appType;     //0：下载zip包的轻应用（version为-1，则版本号需要在线检查的h5轻应用，否则，根据版本号递增做升级判断）
                                    // 1：客户端单点登录的轻应用
                                    // 2：RemoteUrl类型的轻应用
                                    // 10000：ApkPlugin的原生轻应用
        public Integer appId;
        public Extension extension;
        public Integer navBar = 1;// # 轻应用顶部导航栏配置项, defaul # 0: 无导航栏, 1: 有默认导航栏;shenxy——默认带导航栏
        public Long startTimestamp;//订购时间
        public Long endTimestamp;//应用订购截止的时间戳
        public String platformExtension;//透传字段


    }

    public static class Extension implements Serializable{
        public String androidPackage;
        public String androidClass;
        public Integer useCrossWalk = 0;    //是否使用Crosswalk内核,0-使用, 1-不使用

        public String runtimePackage;       //RN运行时包的packageName
        public Integer runtimeVersion = 0;  //RN运行时包的version
        public String runtimeDownloadUrl;   //RN运行时包的url
    }

    public static class DownloadUrlInfo implements Serializable {
        public String APH;
        public String IPH;
    }
    public static class IconUrl implements Serializable {
        public String APH;
        public String IPH;
        public String APH_BLANK;
        public String IPH_BLANK;

    }

//    @Override
//    protected Class<Request> getRequestClassType() {
//        return Request.class;
//    }
//
//    @Override
//    protected Class<Response> getResponseClassType() {
//        return Response.class;
//    }
//
//    public MyWorkQuery() {
//        setRequestMethod(HTTPRequest.Method.GET);
//        setUrlPattern(AppConstants.getHOST() + AppConstants.API_WORK_LIST);
//    }
}
