package com.ljh.custom.base_library.data_source.db.db_manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.ljh.custom.base_library.data_source.db.db_helper.DataBaseHelper;

/**
 * Desc: SQLite数据库管理类
 * Tips: 主要负责数据库资源的初始化,开启,关闭,以及获得DatabaseHelper帮助类操作
 * Created by ${junhua.li} on 2016/07/04 13:20.
 * Email: lijunhuayc@sina.com
 */
public class NormalDBManager extends BaseDBManager {
    private static NormalDBManager dBManager = null;

    public NormalDBManager(Context mContext, String dbName) {
        super(mContext, dbName);
    }

    public NormalDBManager(Context mContext) {
        super(mContext);
    }

    /**
     * @param mContext
     * @param databaseName 当前登陆用户名
     * @return
     * @author ljh @desc
     */
    public static NormalDBManager getInstance(Context mContext, String databaseName) {
        if (null == dBManager) {
            synchronized (NormalDBManager.class) {
                if (null == dBManager) {
                    if (TextUtils.isEmpty(databaseName)) {
                        databaseName = NormalDBManager.DEFAULT_DB_NAME;
                    }
                    Log.d(TAG, "getInstance: databaseName = \"" + databaseName + "\"");
                    dBManager = new NormalDBManager(mContext, databaseName);
                }
            }
        }
        return dBManager;
    }

    @Override
    public SQLiteDatabase openDatabase() {
        return getDatabaseHelper().getWritableDatabase();
    }

    @Override
    public DataBaseHelper getDatabaseHelper() {
        return new DataBaseHelper(mContext, this.databaseName, null, getVersion());
    }

}
