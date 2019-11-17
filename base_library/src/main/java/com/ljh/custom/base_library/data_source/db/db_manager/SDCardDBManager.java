package com.ljh.custom.base_library.data_source.db.db_manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.ljh.custom.base_library.data_source.SharedPreferencesUtils;
import com.ljh.custom.base_library.data_source.db.db_helper.DataBaseHelper;
import com.ljh.custom.base_library.utils.FileUtils;
import com.ljh.custom.base_library.utils.Timber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Desc: Assert 数据库管理类
 * Created by ${junhua.li} on 2016/07/04 13:20.
 * Email: lijunhuayc@sina.com
 */
public class SDCardDBManager extends BaseDBManager {
    private String databasePath;
    private int version = 1;//assert数据库版本号
    private int dbAssertId;//db assert id
    private static SDCardDBManager dBManager = null;

    public static long getSDFreeSize() {
        try {
            File exception = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(exception.getPath());
            long blockSize = sf.getBlockSizeLong();
            long freeBlocks = sf.getAvailableBlocksLong();
            return freeBlocks * blockSize / 1024L / 1024L;
        } catch (Exception e) {
            Timber.d("getSDFreeSize: %s", e);
        }

        return 0L;
    }

    public SDCardDBManager(Context mContext, String databasePath, String databaseName) {
        super(mContext, databaseName);
        this.databasePath = databasePath;
    }

    /**
     * @param mContext
     * @return
     */
    public static String getNativeDBPath(Context mContext) {
        return FileUtils.getExternalFileDirChildPath(FileUtils.FILE_DB_DIR);
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setDBAssertId(int dbAssertId) {
        this.dbAssertId = dbAssertId;
    }

    /**
     * @param mContext
     * @param dbPath
     * @return
     * @author ljh @desc
     */
    public static SDCardDBManager getInstance(Context mContext, String dbPath, String databaseName) {
        if (null == dBManager) {
            synchronized (NormalDBManager.class) {
                if (null == dBManager) {
                    if (TextUtils.isEmpty(dbPath)) {
                        throw new RuntimeException("dbPath can't be empty!");
                    }
                    Log.d(TAG, "getInstance: dbPath = \"" + dbPath + "\"");
                    dBManager = new SDCardDBManager(mContext, dbPath, databaseName);
                }
            }
        }
        return dBManager;
    }

    @Override
    public SQLiteDatabase openDatabase() {
        String path = databasePath + "/" + databaseName;
        Timber.d("openDatabase: dbPath = %s", path);
        int oldVersion = SharedPreferencesUtils.getInt(SharedPreferencesUtils.Key.SP_KEY_DB_VERSION, 0);
        File dbFile = new File(path);
        if (!dbFile.exists() || oldVersion < getVersion()) {
            upgradeNativeDB(dbFile);
        }
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == database) {
            dbFile.delete();
        }
        return database;
    }

    private static final int BUFFER_SIZE = 400000;

    /**
     * 复制 DB文件资源到本地文件
     *
     * @param dbFile
     */
    private void upgradeNativeDB(File dbFile) {
        Timber.d("upgradeNativeDB: 升级本地数据库");
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            }
            is = this.mContext.getResources().openRawResource(dbAssertId); //欲导入的数据库
            fos = new FileOutputStream(dbFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            SharedPreferencesUtils.getInt(SharedPreferencesUtils.Key.SP_KEY_DB_VERSION, getVersion());//拷贝成功才更新版本号
        } catch (Exception e) {
            e.printStackTrace();
            Timber.d("upgradeNativeDB: 升级本地数据库失败");
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public DataBaseHelper getDatabaseHelper() {
        return null;
    }
}
