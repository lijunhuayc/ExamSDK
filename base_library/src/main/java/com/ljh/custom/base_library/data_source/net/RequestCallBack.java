package com.ljh.custom.base_library.data_source.net;

/**
 * Desc: 接口请求回调
 * Created by ${junhua.li} on 2016/10/31 11:10.
 * Email: lijunhuayc@sina.com
 */
public interface RequestCallBack<RESULT> {
    void onSuccess(RESULT result);

    void onResponseError(int eCode, String eMsg);
}
