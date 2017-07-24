package com.shenxy.smartapploader.greendao;

import android.content.Context;

import com.shenxy.smartapploader.greendao.dao.db.DaoMaster;
import com.shenxy.smartapploader.greendao.dao.db.DaoSession;

/**
 * GreenDao 工具类
 * <p/>
 * Created by szh on 2014-04-01.
 */
public class GreenDaoUtils {

    private static DaoMaster daoMaster;

    private static DaoSession daoSession;

    //登录用户的 BID
    private static Long userBid;

    private GreenDaoUtils() {

    }

    private static DaoMaster getDaoMaster(Context context, Long currentUserBid) {
        String DBName;

        //为每一个登录用户创建独立的数据库
        if (currentUserBid != null && currentUserBid > 0) {
            DBName = currentUserBid + ".db";
        } else {
            DBName = "chanjetWorkCircle.db";
        }
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DBName, null);

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());

        return daoMaster;
    }


    public static DaoSession getDaoSession(Context context, Long userBid) {
        if (!userBid.equals(GreenDaoUtils.userBid)) {
            GreenDaoUtils.userBid = userBid;
            daoMaster = getDaoMaster(context, userBid);

            daoSession = daoMaster.newSession();
        }

        return daoSession;
    }
}
