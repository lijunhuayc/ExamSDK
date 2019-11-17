package com.ljh.custom.base_library.data_source.net;

import android.text.TextUtils;

import com.ljh.custom.base_library.BuildConfig;
import com.ljh.custom.base_library.data_source.SharedPreferencesUtils;
import com.ljh.custom.base_library.utils.Timber;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static com.ljh.custom.base_library.data_source.SharedPreferencesUtils.Key.SP_KEY_CHANNEL_NAME;

/**
 * Desc: HOST 配置
 * Created by Junhua.Li
 * Date: 2018/09/18 11:43
 */
public class WebAPI {
    public static String HOST_IP = "http://sc.yicheku.com.cn";//服务器地址
    public static String PORT = ":8060";
    //接口模块前缀
    public static String HOST = HOST_IP + PORT;//服务器地址器地址
    public static final String PICTURE_HOST = "http://oss2.yicheku.com.cn";//图片服务器地址
    public static final String STATIC_WEB_HOST = "http://oss2.yicheku.com.cn";//静态网页域名
    public static String STATISTICS_HOST = "http://statistics.yicheku.com.cn:41180";//统计服务
    public static final String UPLOAD_APP_HOST = "http://oss2.yicheku.com.cn";//APP更新
    public static final String WEB_ARTICLE_HOST = "http://api2.yicheku.com.cn";//服务器地址
    // 聊天服务器
    public static final String DOMAIN_DEV_200 = "http://192.168.2.200";//开发环境200
    public static final String DOMAIN_TEST_210 = "http://192.168.2.210";//测试环境210
    public static final String DOMAIN_TEST_RELEASE = "http://sc.yicheku.com.cn";//线上测试地址
    private static final String BUILD_DOMAIN = DOMAIN_DEV_200;

    static {
        if (BuildConfig.DEBUG) {
            HOST_IP = BUILD_DOMAIN;
            String channelName = SharedPreferencesUtils.getString(SP_KEY_CHANNEL_NAME, "");
            if ("TEST200".equalsIgnoreCase(channelName)) {//library module 的BuildConfig.FLAVOR为空
                HOST_IP = DOMAIN_DEV_200;
            } else if ("TEST210".equalsIgnoreCase(channelName)) {
                HOST_IP = DOMAIN_TEST_210;
            } else if ("channel_mine".equalsIgnoreCase(channelName)) {
                HOST_IP = DOMAIN_TEST_RELEASE;
            }
            resetInit();
        }
    }

    static {//若有动态修改的服务器IP则使用动态修改的(仅DEBUG环境)
        if (BuildConfig.DEBUG) {
            String host = SharedPreferencesUtils.getString("host", "");
            String port = SharedPreferencesUtils.getString("port", "");
            if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(port)) {
                HOST_IP = host;
                PORT = port;
                resetInit();
            }
        }
    }

    private static void resetInit() {
        HOST = HOST_IP + PORT;
        STATISTICS_HOST = HOST_IP + ":41180";
    }

    public static String convertPicURL(String picURL) {
        if (TextUtils.isEmpty(picURL)) {
            return "";
        }
        if (picURL.startsWith("http")) {
            return picURL;
        } else {
            File file = new File(picURL);
            if (file.exists()) {
                return "file://" + picURL;
            } else {
                return PICTURE_HOST + picURL;
            }
        }
    }

    /**
     * 构建 GET 方式请求 URL
     *
     * @param url
     * @param params
     */
    public static String buildGetURL(String url, Map<String, String> params) {
        return buildGetURL(url, params, "UTF-8");
    }

    private static String buildGetURL(String url, Map<String, String> params, String paramsEncoding) {
        StringBuilder sBuilder = new StringBuilder(url);
        if (null != params) {
            int qMarkIndex = sBuilder.indexOf("?");//问号在URL中的位置
            int length = sBuilder.length();//URL总长度
            if (qMarkIndex == length - 1) {//问号处于末尾
            } else if (qMarkIndex == -1) {//无问号
                sBuilder.append("?");
            } else {//有问号且不处于末尾
                sBuilder.append("&");
            }
            try {
                for (String key : params.keySet()) {
                    sBuilder.append(URLEncoder.encode(key, paramsEncoding));
                    sBuilder.append("=");
                    sBuilder.append(URLEncoder.encode(params.get(key), paramsEncoding));
                    sBuilder.append("&");
                }
                char lastStr = sBuilder.charAt(sBuilder.length() - 1);
                if ('?' == lastStr || '&' == lastStr) {
                    sBuilder.deleteCharAt(sBuilder.length() - 1);
                }
            } catch (UnsupportedEncodingException var6) {
                Timber.d("buildGetURL: %s Encoding not supported, exception info = \n %s", paramsEncoding, var6.getMessage());
                var6.printStackTrace();
                return sBuilder.toString();
            }
        }
//        Timber.i("buildGetURL: build url = %s", sBuilder.toString());
        return sBuilder.toString();
    }
}
