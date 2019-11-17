package com.ljh.custom.base_library.model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ljh.custom.base_library.utils.Timber;

import java.util.List;

/**
 * Desc: 统一储存 APP 信息, 尽早初始化
 * Created by Junhua.Li
 * Date: 2018/07/23 15:46
 */
public class AppInfoModel {
    private String appName;
    private String packageName;
    private String versionName;
    private int versionCode;
    private int targetSdkVersion;
    private static AppInfoModel mAppInfoModel;

    public static AppInfoModel getInstance() {
        if (null == mAppInfoModel) {
            throw new RuntimeException("this AppInfoModel not init. please init in Application as early as possible ");
        }
        return mAppInfoModel;
    }

    public static void init(Context pContext) {
        initAppInfo(pContext);
    }

    private static void initAppInfo(Context pContext) {
        mAppInfoModel = new AppInfoModel();
        PackageManager pm = pContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(pContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                mAppInfoModel.appName = pm.getApplicationLabel(appInfo).toString();
                mAppInfoModel.packageName = appInfo.packageName;
                mAppInfoModel.versionName = info.versionName;
                mAppInfoModel.versionCode = info.versionCode;
                mAppInfoModel.targetSdkVersion = appInfo.targetSdkVersion;
                Timber.d("about: appName = %s", mAppInfoModel.appName);
                Timber.d("about: packageName = %s", mAppInfoModel.packageName);
                Timber.d("about: versionName = %s", mAppInfoModel.versionName);
                Timber.d("about: versionCode = %s", mAppInfoModel.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public static String getProcessName(Context pContext) {
        ActivityManager am = (ActivityManager) pContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (null == am) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningApps) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
