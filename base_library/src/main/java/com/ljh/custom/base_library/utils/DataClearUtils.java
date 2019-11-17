package com.ljh.custom.base_library.utils;

/* * 文 件 名:  DataCleanManager.java
 * * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 * */

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.ljh.custom.base_library.BuildConfig;

import java.io.File;
import java.math.BigDecimal;

/**
 * Desc: 本应用数据清除管理器
 * Created by ${junhua.li} on 2016/05/10 10:51.
 * Email: lijunhuayc@sina.com
 */
public class DataClearUtils {
    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    public static void cleanInternalFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部files下的内容(/mnt/sdcard/android/data/com.xxx.xxx/files)
     *
     * @param context
     */
    public static void cleanExternalFiles(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalFilesDir(null));
        }
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * * 清除本应用所有的数据[注：不要清除 SharedPreference & Database] * *
     *
     * @param mContext
     * @param filepath
     */
    public static void cleanApplicationData(Context mContext, String... filepath) {
//        cleanInternalCache(mContext);
//        cleanExternalCache(context);
//        cleanDatabases(context);
//        cleanSharedPreference(context);//Umeng Push deviceToken 存这里的,不能清除，否则需要重启APP才能使用 deviceToken[登录需要]
//        cleanInternalFiles(context);
//        cleanExternalFiles(context);

        deleteFilesByDirectory(FileUtils.getExternalCacheDirChildFile(FileUtils.FILE_CACHE_DIR));
        deleteFilesByDirectory(FileUtils.getExternalCacheDirChildFile(FileUtils.GLIDE_CACHE_DIR));
        deleteFilesByDirectory(FileUtils.getExternalCacheDirChildFile(FileUtils.VOLLEY_CACHE_DIR));
        deleteFilesByDirectory(FileUtils.getExternalCacheDirChildFile(FileUtils.FRESCO_CACHE_DIR));
        deleteFilesByDirectory(FileUtils.getExternalCacheDirChildFile(FileUtils.UPLOAD_CACHE_DIR));
        deleteFilesByDirectory(FileUtils.getExternalCacheDirChildFile(FileUtils.IMG_CACHE_DIR));
        if (null != filepath) {
            for (String filePath : filepath) {
                cleanCustomCache(filePath);
            }
        }
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                deleteAllFilesOfDir(item);
            }
        }
    }

    /**
     * 删除指定 path File 及其以下所有 File
     *
     * @param file
     */
    public static void deleteAllFilesOfDir(File file) {
        if (!file.exists())
            return;
        if (file.getAbsolutePath().contains("umeng") || file.getAbsolutePath().contains("Umeng")) {
            Timber.d("deleteFilesByDirectory: this file[%s] is umeng cache, do not delete.", file.getPath());
            return;
        }
        if (file.isFile()) {
            boolean delTag = file.delete();
            Timber.d("deleteFilesByDirectory: delete %s is success = %s", file.getPath(), delTag);
            return;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        boolean delTag = file.delete();
        Timber.d("deleteFilesByDirectory: delete %s is success = %s", file.getPath(), delTag);
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/ 目录，一般存放临时缓存数据
    public static long getFolderSize(File file) {
        if (null == file) return 0;
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                if (f.isDirectory()) {
                    size += getFolderSize(f);
                } else {
                    size += f.length();
                }
            }
        } catch (Exception e) {
            Timber.e(DataClearUtils.class.getSimpleName(), "获取缓存目录大小失败: " + e.getMessage());
            return 0;
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     * @deprecated
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                Timber.d("deleteFolderFile: delete this path exception, msg = %s", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " TB";
    }

    public static String getCacheSize(File file) {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * test method, ergodic files.
     */
    public static void ergodicFileOfDir(File file) {
        if (!file.exists())
            return;
        if (file.getAbsolutePath().contains("umeng") || file.getAbsolutePath().contains("Umeng")) {
            return;
        }
        if (file.isFile()) {
            Timber.d("ergodic: file.path = %s", file.getPath());
//            Timber.d("ergodic: file.absolutePath = %s", file.getAbsolutePath());
            return;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            ergodicFileOfDir(files[i]);
        }
        Timber.d("ergodic: directory.path = %s", file.getPath());
//        Timber.d("ergodic: directory.absolutePath = %s", file.getAbsolutePath());
    }

    public static String getAllCacheSize(Context mContext) {
        double allSize = 0;
        long tempSize;

        if (BuildConfig.DEBUG) {
            Timber.d("--------------------^^^^^^^^-----------------------");
            ergodicFileOfDir(mContext.getExternalCacheDir());//测试方法, 遍历cache 文件夹
            Timber.d("--------------------^^^^^^^^-----------------------");
            ergodicFileOfDir(mContext.getCacheDir());//测试方法, 遍历cache 文件夹
            Timber.d("--------------------^^^^^^^^-----------------------");
            ergodicFileOfDir(mContext.getExternalFilesDir(null));//测试方法, 遍历cache 文件夹
            Timber.d("--------------------^^^^^^^^-----------------------");
            ergodicFileOfDir(mContext.getFilesDir());//测试方法, 遍历cache 文件夹
            Timber.d("--------------------^^^^^^^^-----------------------");
        }

        tempSize = getFolderSize(FileUtils.getExternalCacheDirChildFile(FileUtils.FILE_CACHE_DIR));
        Timber.d("getAllCacheSize: getExternalCacheChildDirFile($s) = %s", FileUtils.FILE_CACHE_DIR, getFormatSize(tempSize));
        allSize += tempSize;

        tempSize = getFolderSize(FileUtils.getExternalCacheDirChildFile(FileUtils.GLIDE_CACHE_DIR));
        Timber.d("getAllCacheSize: getExternalCacheChildDirFile($s) = %s", FileUtils.GLIDE_CACHE_DIR, getFormatSize(tempSize));
        allSize += tempSize;

        tempSize = getFolderSize(FileUtils.getExternalCacheDirChildFile(FileUtils.VOLLEY_CACHE_DIR));
        Timber.d("getAllCacheSize: getExternalCacheChildDirFile($s) = %s", FileUtils.VOLLEY_CACHE_DIR, getFormatSize(tempSize));
        allSize += tempSize;

        tempSize = getFolderSize(FileUtils.getExternalCacheDirChildFile(FileUtils.FRESCO_CACHE_DIR));
        Timber.d("getAllCacheSize: getExternalCacheChildDirFile($s) = %s", FileUtils.FRESCO_CACHE_DIR, getFormatSize(tempSize));
        allSize += tempSize;

        tempSize = getFolderSize(FileUtils.getExternalCacheDirChildFile(FileUtils.UPLOAD_CACHE_DIR));
        Timber.d("getAllCacheSize: getExternalCacheChildDirFile($s) = %s", FileUtils.UPLOAD_CACHE_DIR, getFormatSize(tempSize));
        allSize += tempSize;

        tempSize = getFolderSize(FileUtils.getExternalCacheDirChildFile(FileUtils.IMG_CACHE_DIR));
        Timber.d("getAllCacheSize: getExternalCacheChildDirFile($s) = %s", FileUtils.IMG_CACHE_DIR, getFormatSize(tempSize));
        allSize += tempSize;

        return getFormatSize(allSize);
    }
}