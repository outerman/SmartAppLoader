package com.shenxy.smartapploader.framework.startInfo;

import java.io.Serializable;

/**
 * Created by shenxy on 16/4/19.
 * 用于启动remote模式的app,增加的补充参数
 */
public class RemoteStartParam implements Serializable {
    public String remoteUrl;
    public String titleName;
    public String[] data;
    public Integer isShowTitleBar;
}
