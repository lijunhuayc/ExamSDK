package com.ljh.custom.base_library.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * Desc: 屏幕工具类
 * Created by ${junhua.li} on 2017/06/14 14:44.
 * Email: lijunhuayc@sina.com
 */
public class ScreenUtils {
    private ScreenUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕尺寸
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获得状态栏高度
     */
    public int getStatusBarHeight(Context pContext) {
        int resourceId = pContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return pContext.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获得状态栏的高度
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public static void printScreenInfo(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        Timber.d("screen: density = %s", outMetrics.density);
        Timber.d("screen: densityDpi = %s", outMetrics.densityDpi);
        Timber.d("screen: heightPixels = %s", outMetrics.heightPixels);
        Timber.d("screen: widthPixels = %s", outMetrics.widthPixels);
        Timber.d("screen: scaledDensity = %s", outMetrics.scaledDensity);
        Timber.d("screen: xdpi = %s", outMetrics.xdpi);
        Timber.d("screen: ydpi = %s", outMetrics.ydpi);
        Timber.d("screen: getStatusHeight = %s", getStatusHeight(mContext));
    }

    /**
     * KeyguardManager:[锁屏\未锁屏] 如果flag为true，表示有两种状态：a、屏幕是黑的 b、目前正处于解锁状态 。
     * 如果flag为false，表示目前未锁屏
     *
     * @param context
     * @return
     */
    public static boolean isLockScreen(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        return flag;
    }

    /**
     * PowerManager:[亮屏\暗屏] 屏幕“亮”，表示有两种状态：a、未锁屏 b、目前正处于解锁状态 。这两种状态屏幕都是亮的
     * 屏幕“暗”，表示目前屏幕是黑的 。
     *
     * @param context
     * @return
     */
    public static boolean isLightScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        return isScreenOn;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / scale + 0.5f);
    }

    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
