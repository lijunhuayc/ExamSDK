package com.ljh.custom.base_library.domain;

/**
 * Desc:
 * Created by ${junhua.li} on 2017/06/29 15:13.
 * Email: lijunhuayc@sina.com
 */
public class UseCaseHandler {
    private static UseCaseHandler mUseCaseHandler;
    private IThreadPoolScheduler mThreadPoolScheduler;

    public static UseCaseHandler getInstance() {
        if (null == mUseCaseHandler) {
            synchronized (UseCaseHandler.class) {
                if (null == mUseCaseHandler) {
                    mUseCaseHandler = new UseCaseHandler(UseCaseThreadPoolScheduler.getInstance());
                }
            }
        }
        return mUseCaseHandler;
    }

    private UseCaseHandler(IThreadPoolScheduler mThreadPoolScheduler) {
        this.mThreadPoolScheduler = mThreadPoolScheduler;
    }

    public <REQ_V extends UseCase.RequestValues, RES_V extends UseCase.ResponseValues> void execute(final UseCase<REQ_V, RES_V> mUseCase) {
        if (null != mUseCase) {
            mThreadPoolScheduler.execute(mUseCase);
        }
    }

}
