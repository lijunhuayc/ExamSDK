package com.ljh.custom.base_library.data_source;

import com.ljh.custom.base_library.BuildConfig;
import com.ljh.custom.base_library.data_source.net.WebAPI;

/**
 * Desc: 存放所有 web 静态页面地址
 * Created by Junhua.Li
 * Date: 2018/06/26 21:09
 */
public class WEBHTMLConstants {
    public static final String STATIC_WEB_HOST_RELEASE = "http://oss2.yicheku.com.cn";
    public static final String STATIC_WEB_HOST_TEST = WebAPI.DOMAIN_TEST_210;
    public static String STATIC_WEB_HOST = STATIC_WEB_HOST_TEST;

    static {
        if (BuildConfig.DEBUG) {
            STATIC_WEB_HOST = STATIC_WEB_HOST_TEST;
        } else {
            STATIC_WEB_HOST = STATIC_WEB_HOST_RELEASE;
        }
    }

    public static final String COOPERATION_PROTOCOL_HTML_URL = STATIC_WEB_HOST + "/cheku_yd/agreement/cooperation_protocol_agreement.html";//第一车酷汽车综合服务三方协议
    public static final String FWS_PROTOCOL_HTML_URL = STATIC_WEB_HOST + "/cheku_yd/agreement/fws_service_provider_agreement.html";//第一车酷汽车服务商协议
    public static final String ABOUT_SERVICE_APP_HTML_URL = STATIC_WEB_HOST + "/cheku_yd/fws_about_us.html";

}
