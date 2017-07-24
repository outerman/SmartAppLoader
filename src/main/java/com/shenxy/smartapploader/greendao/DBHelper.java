package com.shenxy.smartapploader.greendao;

import android.content.Context;

import com.shenxy.smartapploader.greendao.dao.db.DaoSession;
import com.shenxy.smartapploader.greendao.dao.db.LightAppAuthItemDao;
import com.shenxy.smartapploader.greendao.dao.db.TWDownloadFilesEntityDao;
import com.shenxy.smartapploader.workTmp.BaseApplication;


/**
 * DataBase 数据操作类
 * <p/>
 */
public class DBHelper {

    private static DBHelper instance;

    private TWDownloadFilesEntityDao _tWDownloadFilesEntityDao;

    private LightAppAuthItemDao lightAppAuthItemDao;

    private static DaoSession _daoSession;

    private DBHelper() {

    }

    public static DBHelper getInstance() {
        return getInstance(BaseApplication.getContext(), 9999L); //TODO 先写一个固定的号
    }

    public static DBHelper getInstance(Context context, Long userId) {
        if (instance == null) {
            initDBHelperInstance(context, userId);
        }

        return instance;
    }

    private static void initDBHelperInstance(Context context, Long userBid) {
        instance = new DBHelper();

        _daoSession = GreenDaoUtils.getDaoSession(context, userBid);

        instance._tWDownloadFilesEntityDao = _daoSession.getTWDownloadFilesEntityDao();

        instance.lightAppAuthItemDao = _daoSession.getLightAppAuthItemDao();

    }


    /**
     * 轻应用插件权限
     * @return
     */
    public LightAppAuthItemDao getLightAppAuthItemDao() {
        return lightAppAuthItemDao;
    }

    public void setLightAppAuthItemDao(LightAppAuthItemDao lightAppAuthItemDao) {
        this.lightAppAuthItemDao = lightAppAuthItemDao;
    }

    public TWDownloadFilesEntityDao getTWDownloadFilesEntityDao() {
        return _tWDownloadFilesEntityDao;
    }
}
