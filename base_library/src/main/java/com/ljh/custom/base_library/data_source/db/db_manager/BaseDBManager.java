package com.ljh.custom.base_library.data_source.db.db_manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ljh.custom.base_library.data_source.db.db_helper.DataBaseHelper;

/**
 * Desc: DB_Manager 基类
 * Created by ${junhua.li} on 2016/07/04 13:28.
 * Email: lijunhuayc@sina.com
 */
public abstract class BaseDBManager {
    public static final String DEFAULT_DB_NAME = "exam_sdk_db";
    protected static final String TAG = BaseDBManager.class.getName();
    private int version = 3;//自建数据库版本号
    protected String databaseName;
    protected Context mContext = null;

    public BaseDBManager(Context mContext, String databaseName) {
        this(mContext);
        this.databaseName = databaseName;
    }

    public BaseDBManager(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getVersion() {
        return version;
    }

    /**
     * close the database 注意:当事务成功或者一次性操作完毕时候再关闭
     */
    public void closeDatabase(SQLiteDatabase dataBase, Cursor cursor) {
        if (null != dataBase) {
            dataBase.close();
        }
        if (null != cursor) {
            cursor.close();
        }
    }

    /**
     * open a database 注:SQLiteDatabase资源一旦被关闭,该底层会重新产生一个新的SQLiteDatabase
     */
    public abstract SQLiteDatabase openDatabase();

    /**
     * To obtain a DataBaseHelper instance, but this native database don't use this method.
     *
     * @return
     */
    public abstract DataBaseHelper getDatabaseHelper();

}
