package com.ljh.custom.base_library.domain;

import android.os.Handler;
import android.os.Looper;

/**
 * Desc: 主线程执行器
 * Created by ${junhua.li} on 2017/07/06 16:38.
 * Email: lijunhuayc@sina.com
 */
public class MainThreadScheduler implements IMainThreadScheduler {
    private static IMainThreadScheduler sMainThread;
    private Handler mHandler;

    private MainThreadScheduler() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    @Override
    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    public static IMainThreadScheduler getInstance() {
        if (sMainThread == null) {
            sMainThread = new MainThreadScheduler();
        }
        return sMainThread;
    }

}
