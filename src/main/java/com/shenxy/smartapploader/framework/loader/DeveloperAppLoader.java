package com.shenxy.smartapploader.framework.loader;

import rx.Observable;

/**
 * Created by shenxy on 16/4/18.
 * 应用调试入口
 * native代码实现,安装开发者的dev状态的应用,以做调试
 */
public class DeveloperAppLoader extends BaseLoader {
    @Override
    public Observable<Boolean> openApp() {
//        TODO:1)debug入口 + 本地安装的开发版app
        // 服务端为开发者提供"安装开发应用"的入口;
        // 列表中列出本地安装的应用;
        // 应用中打包key来验证其开发者身份及其对应的调试app
        return super.openApp();
    }
}
