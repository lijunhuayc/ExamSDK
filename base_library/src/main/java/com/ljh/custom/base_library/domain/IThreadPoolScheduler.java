package com.ljh.custom.base_library.domain;

/**
 * Desc: 线程调度接口
 * Created by ${junhua.li} on 2017/06/29 15:14.
 * Email: lijunhuayc@sina.com
 */
public interface IThreadPoolScheduler {
    void execute(Runnable runnable);
}
