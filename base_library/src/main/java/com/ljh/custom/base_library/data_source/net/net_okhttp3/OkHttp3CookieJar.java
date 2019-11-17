package com.ljh.custom.base_library.data_source.net.net_okhttp3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ljh.custom.base_library.utils.Timber;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Desc: OkHttp3 Cookie 管理
 * Created by Junhua.Li
 * Date: 2018/07/27 10:52
 */
public class OkHttp3CookieJar implements CookieJar {
    private static final CookieManager COOKIE_MANAGER = new CookieManager(new DiskCookieStore(), CookiePolicy.ACCEPT_ORIGINAL_SERVER);

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Timber.d("OkHttp3CookieJar.saveFromResponse[OkHttp3的]: url = %s, cookies = %s", url, cookies);
        try {
            Map<String, List<String>> headers = Maps.newHashMap();
            List<String> cacheCookies = Lists.newArrayList();
            for (Cookie cookie : cookies) {
                cacheCookies.add(cookie.toString());
            }
            headers.put("Set-Cookie", cacheCookies);
            Timber.d("OkHttp3CookieJar.saveFromResponse[缓存到DbCookieStore的]: cookies = %s", cacheCookies);
            COOKIE_MANAGER.put(url.url().toURI(), headers);
        } catch (IOException | URISyntaxException pE) {
            pE.printStackTrace();
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        ArrayList<Cookie> cookies = new ArrayList<>();
        try {
            /**
             * xutils中是这样存的cookie
             * @see HttpRequest#sendRequest()
             */
            Map<String, List<String>> singleMap = COOKIE_MANAGER.get(url.url().toURI(), new HashMap<>(0));
            List<String> cacheCookies = singleMap.get("Cookie");
            Timber.d("OkHttp3CookieJar.loadForRequest: url = %s, cacheCookies = %s", url, cacheCookies);
            if (null == cacheCookies || cacheCookies.isEmpty()) {
                return Collections.emptyList();
            }
            for (String cookieStr : cacheCookies) {
                String[] cookie = cookieStr.replaceAll(" ", "").split(";");
                for (String str : cookie) {
                    String[] kv = str.split("=");
                    if (kv.length >= 2) {
                        cookies.add(new Cookie.Builder()
                                .hostOnlyDomain(url.host())
                                .name(kv[0].trim())
                                .value(kv[1].trim())
                                .build());
                    }
                }
            }
        } catch (Exception pE) {
            pE.printStackTrace();
            return Collections.emptyList();
        }
        return cookies;
    }
}
