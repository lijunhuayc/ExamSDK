package com.ljh.custom.base_library.domain;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Desc: 多任务调度线程池
 * Created by ${junhua.li} on 2017/06/29 15:25.
 * Email: lijunhuayc@sina.com
 */
public class UseCaseThreadPoolScheduler implements IThreadPoolScheduler {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<>(64);
    //    private ThreadPoolExecutor mThreadPoolExecutor;
    private ScheduledExecutorService mScheduledExecutorService;
    private static IThreadPoolScheduler IThreadPoolScheduler;

    public static IThreadPoolScheduler getInstance() {
        if (null == IThreadPoolScheduler) {
            synchronized (UseCaseThreadPoolScheduler.class) {
                if (null == IThreadPoolScheduler) {
                    IThreadPoolScheduler = new UseCaseThreadPoolScheduler();
                }
            }
        }
        return IThreadPoolScheduler;
    }

    private UseCaseThreadPoolScheduler() {
//        this.mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_SECONDS, TIME_UNIT, WORK_QUEUE, sThreadFactory);
        this.mScheduledExecutorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE, new DefaultThreadFactory(getClass().getSimpleName() + "Child"));
    }

    @Override
    public void execute(Runnable runnable) {
//        mThreadPoolExecutor.submit(runnable);
        mScheduledExecutorService.submit(runnable);
    }

//    @Override
//    public void executeDelayed(Runnable runnable, long millisecond) {
//        mScheduledExecutorService.schedule(runnable, millisecond, TimeUnit.MILLISECONDS);
//    }
}
