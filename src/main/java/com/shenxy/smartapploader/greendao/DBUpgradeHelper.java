package com.shenxy.smartapploader.greendao;

import android.database.sqlite.SQLiteDatabase;

//由于 DaoMaster 由 GreenDao 自动生成，所以将数据库的升级、降级代码写成一个辅助类，
//在 DaoMaster 的 onUpgrade 中调用 upgrade ，在 onDowngrade （如果有需要）中调用 downgrade

/**
 * SQLite 数据库升级、降级辅助工具类
 */
public class DBUpgradeHelper {

    public interface DBUpgradeListener {
        void OnUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    public static void setListener(DBUpgradeListener listener) {
        DBUpgradeHelper.listener = listener;
    }

    private static DBUpgradeListener listener;

    /**
     * 数据库版本升级时需要调用，
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO 数据库升级
        try {
            if (listener != null) {
                listener.OnUpgrade(db, oldVersion, newVersion);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
