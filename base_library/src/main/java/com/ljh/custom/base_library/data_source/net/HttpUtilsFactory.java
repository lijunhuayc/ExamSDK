package com.ljh.custom.base_library.data_source.net;

import com.ljh.custom.base_library.data_source.net.net_okhttp3.OkHttp3Utils;

/**
 * Desc:
 * Created by ${junhua.li} on 2017/07/04 10:28.
 * Email: lijunhuayc@sina.com
 */
public class HttpUtilsFactory {
    //    static final int TYPE_VOLLEY = 1;
    static final int TYPE_OKHTTP3 = 2;

    static IHttpUtils createHttpUtils(int type) {
        switch (type) {
//            case TYPE_VOLLEY:
//                return VolleyHttpUtils.getInstance();
            case TYPE_OKHTTP3:
                return OkHttp3Utils.getInstance();
            default:
                return null;
        }
    }
}
