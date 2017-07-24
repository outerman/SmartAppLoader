package com.shenxy.smartapploader.auth;
//
//import com.chanjet.greendao.DBHelper;
//import com.chanjet.workcircle.dao.LightAppAuth.LightAppAuthItem;
//import com.chanjet.workcircle.dao.db.LightAppAuthItemDao;
//
//import de.greenrobot.dao.query.QueryBuilder;

import com.shenxy.smartapploader.greendao.DBHelper;
import com.shenxy.smartapploader.greendao.dao.LightAppAuth.LightAppAuthItem;
import com.shenxy.smartapploader.greendao.dao.db.LightAppAuthItemDao;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 轻应用插件，权限管理类
 * 从服务端获取配置、保存配置、根据配置验证权限
 *
 * 做“白名单”控制
 * Created by shenxy on 15/1/8.
 */
public class AppPluginAuthManager {
    private static AppPluginAuthManager instance;

    public static AppPluginAuthManager getInstance(){
        if (instance == null){
            instance = new AppPluginAuthManager();
        }
        return instance;
    }

    public interface AppPluginAuthUpdateListener{
        public void OnUpdateSucceed();
        public void OnUpdateFailed();
//        public void OnUpdateFinished();
    }

    /**
     * 从服务端获取配置信息
     * 目前调用场景：1、已安装的轻应用，未初始化权限（老用户升级、初始化时获取失败）
     *              2、未安装的轻应用，初次安装，下载
     *              3、已安装的轻应用，升级，下载
     */
    public void updateAppPluginAuthConfig(int appId, AppPluginAuthUpdateListener listener){
        //先调用服务端接口，下载配置信息

        //服务端暂未实现，直接返回
        saveAuth(appId, "", "", 1); //空字符串，表示所有
        listener.OnUpdateSucceed();
    }

    /**
     * 保存配置信息到数据库
     * @param appId
     * @param pluginName
     * @param pluginMethod
     * @param value 1--有权限； 2--无权限
     * @return
     */
    private boolean saveAuth(int appId, String pluginName, String pluginMethod, int value){
        LightAppAuthItem lightAppAuthItem = new LightAppAuthItem();
        lightAppAuthItem.setAppId(appId);
        lightAppAuthItem.setPluginMethod(pluginMethod);
        lightAppAuthItem.setPluginName(pluginName);
        lightAppAuthItem.setId(getPrimaryKey(appId, pluginName, pluginMethod));
        lightAppAuthItem.setAuthValue(value);
        DBHelper.getInstance().getLightAppAuthItemDao().insertOrReplace(lightAppAuthItem);

        return true;
    }

    //从数据库加载缓存配置

    /**
     *     验证该appid的插件name是否有权限，白名单策略
     * @param appId
     * @param pluginName
     * @param pluginMethod
     * @return  是否有权限：1--有权限； 0--无权限; -1--未初始化
     */
    public int checkAuth(int appId, String pluginName, String pluginMethod){
        //先查询通用权限
        LightAppAuthItem lightAppAuthItem = DBHelper.getInstance().getLightAppAuthItemDao().queryBuilder()
                                            .where(LightAppAuthItemDao.Properties.Id.eq(appId + "_" + "_")).unique();
        if (lightAppAuthItem !=null && lightAppAuthItem.getAuthValue() != null){
            if (lightAppAuthItem.getAuthValue().equals(1)) {
                return 1;
            }
            else {
                return 0;
            }
        }
        //再检查特定功能的权限
        else {
            lightAppAuthItem = DBHelper.getInstance().getLightAppAuthItemDao().queryBuilder()
                                .where(LightAppAuthItemDao.Properties.Id.eq(getPrimaryKey(appId, pluginName, pluginMethod))).unique();
            if (lightAppAuthItem != null && lightAppAuthItem.getAuthValue() != null){
                if (lightAppAuthItem.getAuthValue().equals(1)) {
                    return 1;
                }
                else{
                    return 0;
                }
            }
            //没有通用权限，也没有指定权限: 1、该appId的配置未初始化
            //                          2、该appId的权限已经初始化，但是没有当前请求的该项
            else {
                if (isAuthInited(appId)){
                    return 0;   //白名单控制，如果已经初始化，但是没有该项，认为没有权限
                }
                else {
                    return -1;
                }
            }
        }
    }

    /**
     * 检查该appId的权限是否已经初始化过
     * @param appId
     * @return
     */
    public boolean isAuthInited(int appId){
        QueryBuilder<LightAppAuthItem> lightAppAuthItemQueryBuilder=DBHelper.getInstance().getLightAppAuthItemDao().queryBuilder()
                .where(LightAppAuthItemDao.Properties.AppId.eq(appId + ""));
        if(lightAppAuthItemQueryBuilder==null){
            return false;
        }
        long authItemCount = lightAppAuthItemQueryBuilder.count();
        if (authItemCount > 0){
            return true;
        }

        return false;
    }

    private String getPrimaryKey(int appId, String pluginName, String pluginMethod){
        return appId + "_" + (pluginName == null ? "" : pluginName) + "_" + (pluginMethod == null ? "" : pluginMethod);
    }

}
