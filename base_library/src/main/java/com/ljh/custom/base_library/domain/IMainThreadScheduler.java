package com.ljh.custom.base_library.domain;

/**
 * Desc:
 * Created by ${junhua.li} on 2017/07/06 16:40.
 * Email: lijunhuayc@sina.com
 */
public interface IMainThreadScheduler {

    void post(final Runnable runnable);

    void postDelayed(final Runnable runnable, long delayMillis);

}
