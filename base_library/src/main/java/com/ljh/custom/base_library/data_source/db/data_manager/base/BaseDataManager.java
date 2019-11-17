package com.ljh.custom.base_library.data_source.db.data_manager.base;

import android.content.Context;

import com.ljh.custom.base_library.data_source.db.db_manager.BaseDBManager;

/**
 * Desc: 所有 DataManager 相关类继承此类
 * Created by ${junhua.li} on 2016/07/04 12:42.
 * Email: lijunhuayc@sina.com
 */
public abstract class BaseDataManager {
    public static String TAG = BaseDataManager.class.getSimpleName();
    public static final String ASC = " asc ";
    public static final String DESC = " desc ";
    protected BaseDBManager manager = null;
    protected Context mContext;

    public BaseDataManager(Context mContext) {
        this.mContext = mContext;
        TAG = this.getClass().getSimpleName();
    }

    /**
     * 按用户名创建的数据库在切换用户的时候, 当前单例保存的对象依然是上一个用户的数据。
     * 所以 需要判断当前数据库名是否与用户名相关联。否则需要重建单例对象。
     *
     * @param object
     * @return
     */
    protected static final boolean objIsNull(BaseDataManager object) {
        return object == null || !object.manager.getDatabaseName().equals("exam_sdk");
    }

    /**
     * 通过反射实例化 cursor中的数据到实体类
     * Tips: 待改造
     * @param cursor
     * @param index
     * @return
     */
//    protected Object cursor2Obj(Cursor cursor, int index){
//        return null;
//    }
}
