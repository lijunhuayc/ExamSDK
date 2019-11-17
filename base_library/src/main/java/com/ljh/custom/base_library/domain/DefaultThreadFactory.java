package com.ljh.custom.base_library.domain;

import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2018/08/16 16:19
 */
public class DefaultThreadFactory implements ThreadFactory {
    private final AtomicInteger mCount = new AtomicInteger(1);
    private String mThreadName;

    public DefaultThreadFactory(@NonNull String pThreadName) {
        this.mThreadName = pThreadName;
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, String.format(Locale.CHINA, "%s-%s#%s", Thread.currentThread().getName(), mThreadName, mCount.getAndIncrement()));
    }
}
