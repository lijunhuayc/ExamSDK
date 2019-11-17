package com.ljh.custom.base_library.domain;

/**
 * Desc: one Case
 * Created by ${junhua.li} on 2017/06/29 15:13.
 * Email: lijunhuayc@sina.com
 */
public abstract class UseCase<REQ_V extends UseCase.RequestValues, RES_V extends UseCase.ResponseValues> implements Runnable {
    private REQ_V mRequestValues;
    private UseCaseCallback<RES_V> mUseCaseCallback;

    public void setRequestValues(REQ_V mRequestValues) {
        this.mRequestValues = mRequestValues;
    }

    public REQ_V getRequestValues() {
        return mRequestValues;
    }

    public void setUseCaseCallback(UseCaseCallback<RES_V> mUseCaseCallback) {
        this.mUseCaseCallback = mUseCaseCallback;
    }

    public UseCaseCallback<RES_V> getUseCaseCallback() {
        return mUseCaseCallback;
    }

    @Override
    public void run() {
        executeUseCase(mRequestValues);
    }

    /**
     * 此方法运行在线程池中, getUseCaseCallback()的运行线程由子类自由决定
     *
     * @param mRequestValues
     */
    protected abstract void executeUseCase(REQ_V mRequestValues);

    /**
     * Request Data
     */
    public interface RequestValues {
    }

    /**
     * Response Data
     */
    public interface ResponseValues {
    }

    public interface UseCaseCallback<RES_V_ extends ResponseValues> {
        void onSuccess(RES_V_ response);

        void onError(String message);
    }

}
