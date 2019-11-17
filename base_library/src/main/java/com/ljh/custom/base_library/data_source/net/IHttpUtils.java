package com.ljh.custom.base_library.data_source.net;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Desc:
 * Created by ${junhua.li} on 2017/07/04 10:24.
 * Email: lijunhuayc@sina.com
 */
public interface IHttpUtils {
    Gson gson = new Gson();

    Object convertMethod(int method);

    <RESULT> String requestAsync(int method, String url, Map<String, String> params, Class<RESULT> clazz, RequestCallBack<RESULT> requestCallBack);

    <RESULT> RESULT requestFuture(int method, String url, Map<String, String> params, Class<RESULT> clazz, int timeout);
}
