package com.shenxy.smartapploader.framework.startInfo;

import com.shenxy.smartapploader.request.MyWorkQuery;
import com.shenxy.smartapploader.request.WorkMsgDto;

import java.io.Serializable;

/**
 * Created by shenxy on 16/4/17.
 * 应用启动参数
 */
public class AppStartInfo implements Serializable {
    public int appId;
    public String entId;

    public MyWorkQuery.MyWorkItem appInfo;      //服务端返回的app配置信息
    public WorkMsgDto workMsgInfo;              //从工作消息打开时,消息体带入的额外信息
    public RemoteStartParam remoteStartParam;   //在轻应用框架内,用remoteUrl模式打开的非轻应用的页面(营销页面),带入的附加信息

    public boolean isRNRemote;                  //是否使用DevServer来加载RN页面(开发调试阶段)
    /**
     * 以下三项为早期轻应用使用的字段,后续考虑移除
     */
    public String page = "list";                //page: 页面（“list”列表页、”detail”详情页）
    public String tab = "0";                    //shenxy 2015-7-7 简化参数，tab和backToNative两个字段实际已经没有用了，填写默认值。tab;//页签（0、1、2）
    public boolean backToNative = true;         //shenxy 2015-7-7 简化参数，tab和backToNative两个字段实际已经没有用了，填写默认值。//backToNative;;
}
