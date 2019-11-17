package com.ljh.examsdk.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.ljh.custom.base_library.BuildConfig;
import com.ljh.custom.base_library.data_report.ReportDataService;
import com.ljh.custom.base_library.data_source.SharedPreferencesUtils;
import com.ljh.custom.base_library.model.AppInfoModel;
import com.ljh.custom.base_library.utils.FileUtils;
import com.ljh.custom.base_library.utils.MyToast;
import com.ljh.custom.base_library.utils.Timber;

import static com.ljh.custom.base_library.data_source.SharedPreferencesUtils.Key.SP_KEY_CHANNEL_NAME;
import static com.ljh.custom.base_library.utils.FileUtils.FRESCO_CACHE_DIR;

/**
 * Desc: 壳工程(主工程) Application 分发
 * Created by Junhua.Li
 * Date: 2018/06/20 20:50
 */
public class AppApplication extends Application {
    private static Application sApplication;

    static {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
//            layout.setPrimaryColorsId(android.R.color.white, R.color.commonColorBrand);
//            return new ClassicsHeader(context)
//                    .setTextSizeTitle(12)
//                    .setTextSizeTime(9)
//                    .setDrawableSize(15)
//                    ;
//        });
//        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
//            layout.setPrimaryColorsId(android.R.color.white, R.color.commonColorBrand);
//            return new ClassicsFooter(context)
//                    .setTextSizeTitle(12)
//                    .setDrawableSize(15);
//        });
    }

    public static Application getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        //created by lijunhua
        //SharedPreferencesUtils初始化需要在有SharedPreferencesUtils使用的代码前面
        // (比如: ReportDataService.getInstance().init()中会发起网络请求, 会加载WebAPI类触发static代码块,其中会使用到SharedPreferencesUtils)
        //SP 初始化完后预先读取 渠道名(WebAPI 中开发环境配置用)
        SharedPreferencesUtils.init(sApplication);
        AppInfoModel.init(sApplication);
        //--------------
        MyToast.init(sApplication);
        FileUtils.init(sApplication);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                        return;
                    }
                    FakeCrashLibrary.log(priority, tag, message);
                    if (t != null) {
                        if (priority == Log.ERROR) {
                            FakeCrashLibrary.logError(t);
                        } else if (priority == Log.WARN) {
                            FakeCrashLibrary.logWarning(t);
                        }
                    }
                }
            });
        }

        ReportDataService.getInstance().init();//初始化数据上报服务
        Fresco.initialize(sApplication, getImagePipelineConfig(sApplication));// 图片工具初始化
    }

    private static final int MAX_IMAGE_MEMORY_CACHE_SIZE = 8 * ByteConstants.MB;

    private ImagePipelineConfig getImagePipelineConfig(Context context) {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context.getApplicationContext())
                //内存缓存和未解码的内存缓存
                /*.setBitmapMemoryCacheParamsSupplier(new Supplier<MemoryCacheParams>() {
                    public MemoryCacheParams get() {
                        int maxMemory = (int) Runtime.getRuntime().maxMemory();
                        // 四分之一来做图片缓存
                        int maxImageMemoryCacheSize = (maxMemory == 0) ? MAX_IMAGE_MEMORY_CACHE_SIZE : (maxMemory / 4);
                        Logger.i("--->UN 系统可用内容大小：" + maxMemory + ", 设置最大使用内存：" + maxImageMemoryCacheSize);
                        return new MemoryCacheParams(
                                maxImageMemoryCacheSize, // 内存缓存中总图片的最大大小,以字节为单位。
                                20,                     // 内存缓存中图片的最大数量。
                                maxImageMemoryCacheSize / 4, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。(总内存的1/32做缓存 128-4)
                                20,                     // 内存缓存中准备清除的总图片的最大数量。
                                512 * ByteConstants.KB); // 内存缓存中单个图片的最大大小。
                    }
                })*/
                //磁盘缓存
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)
                        //文件根路劲
                        .setBaseDirectoryPath(FileUtils.getExternalCacheDirChildFile(FRESCO_CACHE_DIR))
                        //文件包名
                        .setBaseDirectoryName("fresco")
                        //缓存大小
                        .setMaxCacheSize(40 * ByteConstants.MB).build())
                .setBitmapsConfig(Bitmap.Config.RGB_565)
//                .setWebpSupportEnabled(false)
                .setDownsampleEnabled(true)
                .build();
//                .setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {
//                    @Override
//                    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
//
//                    }
//
//                    @Override
//                    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
//
//                    }
//                });
//        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        return config;
    }
}

final class FakeCrashLibrary {
    public static void log(int priority, String tag, String message) {
        // TODO add log entry to circular buffer.
    }

    public static void logWarning(Throwable t) {
        // TODO report non-fatal warning.
    }

    public static void logError(Throwable t) {
        // TODO report non-fatal error.
    }

    private FakeCrashLibrary() {
        throw new AssertionError("No instances.");
    }
}

