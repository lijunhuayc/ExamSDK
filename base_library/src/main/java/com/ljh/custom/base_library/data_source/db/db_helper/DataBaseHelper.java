package com.ljh.custom.base_library.data_source.db.db_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Desc: SQLite数据库的帮助类 该类属于扩展类,主要承担数据库初始化和版本升级使用,其他核心全由核心父类完成
 * Created by ${junhua.li} on 2016/07/04 12:38.
 * Email: lijunhuayc@sina.com
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String COMMON_PRIMARY_KEY_ID = "_ID"; // 通用建表_id字段
    private static final String COMMON_PRIMARY_KEY_ID_SQL = COMMON_PRIMARY_KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"; // 通用建表_id字段
    //----------------客服聊天本地消息记录-------------------
    //-------------------------this project-------------------------------
    public static final String LOCAL_MESSAGE_TABLE_NAME = "LOCAL_MESSAGE_TABLE_NAME";
    public static final String LOCAL_MESSAGE_IS_ENABLE = "LOCAL_MESSAGE_IS_ENABLE";
    public static final String LOCAL_MESSAGE_HAS_READ = "LOCAL_MESSAGE_HAS_READ";
    public static final String LOCAL_MESSAGE_DATE = "LOCAL_MESSAGE_DATE";
    public static final String LOCAL_MESSAGE_ID = "LOCAL_MESSAGE_ID";
    public static final String LOCAL_MESSAGE_CONTENT = "LOCAL_MESSAGE_CONTENT";
    public static final String LOCAL_MESSAGE_FILE_URL = "LOCAL_MESSAGE_FILE_URL";
    public static final String LOCAL_MESSAGE_TYPE = "LOCAL_MESSAGE_TYPE";
    public static final String LOCAL_MESSAGE_ITEM_TYPE = "LOCAL_MESSAGE_ITEM_TYPE";
    public static final String LOCAL_MESSAGE_SENDER_ID = "LOCAL_MESSAGE_SENDER_ID";
    public static final String LOCAL_MESSAGE_SENDER_NAME = "LOCAL_MESSAGE_SENDER_NAME";
    public static final String LOCAL_MESSAGE_SENDER_AVATAR = "LOCAL_MESSAGE_SENDER_AVATAR";
    public static final String LOCAL_MESSAGE_RECEIVER_ID = "LOCAL_MESSAGE_RECEIVER_ID";
    public static final String LOCAL_MESSAGE_RECEIVER_NAME = "LOCAL_MESSAGE_RECEIVER_NAME";
    public static final String LOCAL_MESSAGE_HAS_SEND = "LOCAL_MESSAGE_HAS_SEND";
    //--------------------------------------------------------

    public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createLocalMessageTable(db);
    }

    private void createLocalMessageTable(SQLiteDatabase db) {
        db.execSQL("Drop table if exists " + LOCAL_MESSAGE_TABLE_NAME);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LOCAL_MESSAGE_TABLE_NAME
                + "(" + COMMON_PRIMARY_KEY_ID_SQL
                + LOCAL_MESSAGE_IS_ENABLE + " INTEGER,"
                + LOCAL_MESSAGE_HAS_READ + " INTEGER,"
                + LOCAL_MESSAGE_DATE + " INTEGER,"
                + LOCAL_MESSAGE_ID + " NVARCHAR,"
                + LOCAL_MESSAGE_CONTENT + " NVARCHAR,"
                + LOCAL_MESSAGE_FILE_URL + " NVARCHAR,"
                + LOCAL_MESSAGE_TYPE + " INTEGER,"
                + LOCAL_MESSAGE_ITEM_TYPE + " INTEGER,"
                + LOCAL_MESSAGE_SENDER_ID + " NVARCHAR,"
                + LOCAL_MESSAGE_SENDER_NAME + " NVARCHAR,"
                + LOCAL_MESSAGE_SENDER_AVATAR + " NVARCHAR,"
                + LOCAL_MESSAGE_RECEIVER_ID + " NVARCHAR,"
                + LOCAL_MESSAGE_HAS_SEND + " INTEGER,"
                + LOCAL_MESSAGE_RECEIVER_NAME + " NVARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
