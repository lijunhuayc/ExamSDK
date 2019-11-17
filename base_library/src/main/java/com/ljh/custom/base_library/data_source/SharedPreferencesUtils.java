package com.ljh.custom.base_library.data_source;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Desc: SharedPreferences 统一工具类
 * Created by Junhua.Li
 * Date: 2018/05/29 17:37
 */
public class SharedPreferencesUtils {
    public interface Key {
        String SP_KEY_LOGIN_TOKEN = "SP_KEY_LOGIN_TOKEN";
        String SP_KEY_LOGIN_NAME = "SP_KEY_LOGIN_NAME";
        String SP_KEY_PASSWORD = "SP_KEY_PASSWORD";
        String SP_KEY_USER_BEAN = "SP_KEY_USER_BEAN";
        String SP_KEY_DEVICE_TOKEN = "SP_KEY_DEVICE_TOKEN";
        String SP_KEY_DB_VERSION = "SP_KEY_DB_VERSION";
        String SP_KEY_CHANNEL_NAME = "SP_KEY_CHANNEL_NAME";
        String SP_KEY_LAST_LOCATION_AREA_INFO = "sp_last_location_area_info_key";//上一次缓存的位置信息
        String SP_KEY_CHECK_UPDATE_INFO = "SP_KEY_CHECK_UPDATE_INFO";
        String SP_KEY_LAUNCH_ADV_PIC = "launch_adv_pic";
        String SP_KEY_MAIN_BOTTOM_ICON = "sp_key_main_bottom_icon";//缓存主界面底部导航图标,下次启动时使用
    }

    private static Context mApplication;
    private static SharedPreferences sharedPreferences = null;

    public static <T extends Application> SharedPreferences init(T mContext) {
        mApplication = mContext;
        if (null == sharedPreferences) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mApplication);
        }
        return sharedPreferences;
    }

    synchronized public static String getString(String key, String defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, defValue);
        }
        return defValue;
    }

    synchronized public static boolean setString(String key, String value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    synchronized public static boolean setBoolean(String key, boolean value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }

    synchronized public static boolean getBoolean(String key, boolean defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defValue);
        }
        return defValue;
    }

    synchronized public static boolean setInt(String key, int value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    synchronized public static int getInt(String key, int defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, defValue);
        }
        return defValue;
    }

    synchronized public static boolean setLong(String key, long value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    synchronized public static long getLong(String key, long defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, defValue);
        }
        return defValue;
    }

    synchronized public static boolean setFloat(String key, float value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(key, value);
            return editor.commit();
        }
        return false;
    }

    synchronized public static float getFloat(String key, float defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(key, defValue);
        }
        return defValue;
    }

    synchronized public static boolean deleteKey(String key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            return editor.commit();
        }
        return false;
    }
}
