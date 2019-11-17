package com.ljh.custom.base_library.data_source.net;

import java.util.Map;

/**
 * Desc: HttpUtils包装类
 * Created by ${junhua.li} on 2017/07/04 09:28.
 * Email: lijunhuayc@sina.com
 */
public class HttpUtils implements IHttpUtils {
    private static IHttpUtils iHttpUtils;

    public static IHttpUtils getInstance(int... type) {
        if (null == iHttpUtils) {
            int utilsType = HttpUtilsFactory.TYPE_OKHTTP3;
            if (null != type && type.length > 0) {
                utilsType = type[0];
            }
            iHttpUtils = HttpUtilsFactory.createHttpUtils(utilsType);
        }
        return iHttpUtils;
    }

    /**
     * 异步请求
     */
    @Override
    public <RESULT> String requestAsync(int method, String url, Map<String, String> params, Class<RESULT> clazz, RequestCallBack<RESULT> requestCallBack) {
        return iHttpUtils.requestAsync(method, url, params, clazz, requestCallBack);
    }

    /**
     * 同步请求
     */
    @Override
    public <RESULT> RESULT requestFuture(int method, String url, Map<String, String> params, Class<RESULT> clazz, int timeout) {
        return iHttpUtils.requestFuture(method, url, params, clazz, timeout);
    }

    @Override
    public Object convertMethod(int method) {
        return null;
    }

    public interface Method {
        public static final int METHOD_GET = 1;
        public static final int METHOD_PUT = 2;
        public static final int METHOD_POST = 4;
        public static final int METHOD_DELETE = 8;
    }

}
