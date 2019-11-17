package com.ljh.custom.base_library.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.ljh.custom.base_library.BuildConfig;

/**
 * Desc: 土司提示工具类
 * Created by ${junhua.li} on 2016/03/22 13:30.
 * Email: lijunhuayc@sina.com
 */
public class MyToast {
    static Toast toast;
    static Application mContext;
    static Handler mHandler;
    static int textViewId = 0;
    static int lineSpacing = 0;

    public static void init(Context context) {
        if (!(context instanceof Application)) {
            throw new RuntimeException("MyToast initialize must in Application.");
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("MyToast initialize must in main thread.");
        }
        mContext = (Application) context;
        toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        mHandler = new Handler(context.getMainLooper());
        lineSpacing = ScreenUtils.dp2px(mContext, 5);
    }

    public static void showToast(final CharSequence text, final int duration, final int gravity) {
        if (toast == null) {
            throw new RuntimeException("MyToast uninitialized in Application");
        }
        mHandler.post(() -> {
            toast.setText(text);
            toast.setDuration(duration);
            toast.setGravity(gravity, 0, ScreenUtils.dp2px(mContext, 64));
            if (textViewId == 0) {
                textViewId = Resources.getSystem().getIdentifier("message", "id", "android");
            }
            TextView textView = toast.getView().findViewById(textViewId);
            if (textView != null) {
                textView.setGravity(Gravity.CENTER);
                textView.setLineSpacing(lineSpacing, 1.0f);
            }
            toast.show();
        });
    }

    /**
     * 仅仅在DEBUG包时提示一些辅助信息
     *
     * @param text
     */
    @Deprecated
    public static void showTestToast(CharSequence text) {
        if (BuildConfig.DEBUG) {
            showToast(text + "\n[DEBUG]", Toast.LENGTH_SHORT, Gravity.BOTTOM);
        }
    }

    public static void showToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT, Gravity.BOTTOM);
    }

    public static void showToast(CharSequence text, int duration) {
        showToast(text, duration, Gravity.BOTTOM);
    }

    /**
     * Toast统一管理类
     */
    private MyToast() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
}