package com.shenxy.smartapploader.request;


import com.shenxy.smartapploader.utils.Utils2;

import java.io.Serializable;

/**
 * 工作消息
 *
 * @author
 */
public class WorkMsgDto implements Serializable {
    public String type;
    public long resId;
    public long appId;
    public String appname;
    public long orgid;
    public String orgname;
//    public UserDto user;//userinfo;
    public String mtitle;
    public String stitle;
    public String summary;
    public String quote;
    public String createtime;
    public int auditCount;
    public int unreadCount;
    public Long userid;
    public String ext; //T+等的扩展字段，直接透传
    public String logo;
    public String url;//点击条目跳转到wap页的地址  目前只冷启动消息会用

    public CalanderTask task;//添加日历提醒的task

    //保存该item对应IMMessageDto的id,用于本地sdk操作 by wk
    public Long localMsgId;
    public String getCreateOnFormatString() {
        if (this.createtime != null) {
            return Utils2.formatStringTime(createtime);
        }
        return "";

    }


    public class CalanderTask implements Serializable {
        public String title;
        public long startTime;
        public int remindTime;
        public long endTime;
    }
}
