package com.ljh.custom.base_library.data_source.net.net_okhttp3;

import com.google.common.collect.Maps;
import com.ljh.custom.base_library.ExamSDK;
import com.ljh.custom.base_library.data_source.net.HttpUtils;
import com.ljh.custom.base_library.data_source.net.IHttpUtils;
import com.ljh.custom.base_library.data_source.net.RequestCallBack;
import com.ljh.custom.base_library.data_source.net.WebAPI;
import com.ljh.custom.base_library.domain.MainThreadScheduler;
import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.utils.MD5Utils;
import com.ljh.custom.base_library.utils.Timber;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Desc: OkHttp3 工具类
 * Created by ${junhua.li} on 2017/07/04 09:24.
 * Email: lijunhuayc@sina.com
 */
public class OkHttp3Utils implements IHttpUtils {
    private static IHttpUtils iHttpUtils;
    private ParamType paramType = ParamType.JSON;//POST 参数格式
    private static final OkHttpClient.Builder sOkHttpClientBuilder =
            new OkHttpClient.Builder()
                    .cookieJar(new OkHttp3CookieJar())
                    .addInterceptor(new OkHttp3Interceptor());

    public OkHttp3Utils() {
    }

    public enum ParamType {
        KEY_VALUE, JSON
    }

    public static IHttpUtils getInstance() {
        if (null == iHttpUtils) {
            iHttpUtils = new OkHttp3Utils();
        }
        return iHttpUtils;
    }

    @Override
    public Object convertMethod(int method) {
        switch (method) {
            case HttpUtils.Method.METHOD_GET:
                return "METHOD_GET";
            case HttpUtils.Method.METHOD_POST:
                return "METHOD_POST";
            case HttpUtils.Method.METHOD_PUT:
                return "METHOD_PUT";
            case HttpUtils.Method.METHOD_DELETE:
                return "METHOD_DELETE";
            default:
                return "";
        }
    }

    public void setParamType(ParamType paramType) {
        this.paramType = paramType;
    }

    private ParamType getParamType() {
        return paramType;
    }

    @Override
    public <RESULT> String requestAsync(int method, String url, Map<String, String> params, Class<RESULT> clazz, RequestCallBack<RESULT> requestCallBack) {
        params = checkParams(params);
//        url = HttpURLAPI.convertAPIUrl(url);
        String jsonParams = gson.toJson(params);
        String requestTag = MD5Utils.md5((url + method + jsonParams));
        Request request = getRequest(method, url, params);

        sOkHttpClientBuilder
                .connectTimeout(45, TimeUnit.SECONDS)
                .build()
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Timber.d("requestAsync exception. %s", e.toString());
                        MainThreadScheduler.getInstance().post(() -> requestCallBack.onResponseError(-1, "请求失败"));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            ResponseBody body = response.body();
                            if (response.isSuccessful() && null != body) {
                                String responseStr = body.string();
                                Timber.d("api_response: %s", responseStr);
                                RESULT result = gson.fromJson(responseStr, clazz);
                                MainThreadScheduler.getInstance().post(() -> {
                                    if (null != result) {
                                        if (result instanceof BaseResult) {
                                            BaseResult object = (BaseResult) result;
                                            if (object.isSuccess()) {
                                                requestCallBack.onSuccess(result);
                                            } else {
                                                if (object.getStatus() == BaseResult.NOT_LOGIN) {
//                                                    ARouter.getInstance().build(RouterTable.USERINFO_LOGIN_ACTIVITY)
//                                                            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                                            .navigation(BaseLibraryApplication.getApplication());
                                                    return;
                                                } else {
                                                    requestCallBack.onResponseError(object.getStatus(), object.getErrMsg());
                                                }
                                            }
                                        } else {
                                            requestCallBack.onSuccess(result);//非BaseObject类型暂定success // TODO: 2017/07/04
                                        }
                                    } else {
                                        Timber.d("requestAsync failed. result is null.");
                                        requestCallBack.onResponseError(-1, "请求失败");
                                    }
                                });
                            } else {
                                MainThreadScheduler.getInstance().post(() -> {
                                    Timber.d("requestAsync failed.");
                                    requestCallBack.onResponseError(-1, "请求失败");
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            MainThreadScheduler.getInstance().post(() -> {
                                Timber.d("requestFuture exception.");
                                requestCallBack.onResponseError(-1, "请求失败");
                            });
                        }
                    }
                });
        return requestTag;
    }

    @Override
    public <RESULT> RESULT requestFuture(int method, String url, Map<String, String> params, Class<RESULT> clazz, int timeout) {
        params = checkParams(params);
//        url = HttpURLAPI.convertAPIUrl(url);
        Request request = getRequest(method, url, params);
        try {
            Response response = sOkHttpClientBuilder
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .build()
                    .newCall(request)
                    .execute();
            ResponseBody body = response.body();
            if (response.isSuccessful() && null != body) {
                String responseStr = body.string();
                Timber.d("api_response: %s", responseStr);
                return gson.fromJson(responseStr, clazz);
            } else {
                Timber.d("requestFuture failed.");
                return null;
            }
        } catch (IOException e) {
            Timber.d("requestFuture exception.");
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, String> checkParams(Map<String, String> params) {
        if (null == params) {
            params = Maps.newHashMap();
        }
        params.put("token", ExamSDK.getToken());
        params.put("UserType", ExamSDK.getUserType());
        return params;
    }

    private Request getRequest(int method, String url, Map<String, String> params) {
        Timber.d("api_method: %s", convertMethod(method));
        if (method == HttpUtils.Method.METHOD_GET || method == HttpUtils.Method.METHOD_DELETE) {
            url = WebAPI.buildGetURL(url, params);
        } else {
            url = url + "?token=" + ExamSDK.getToken() + "&UserType=" + ExamSDK.getUserType();
            Timber.d("api_params: %s", gson.toJson(params));
        }
        Timber.d("api_url: %s", url);

        Request.Builder builder = new Request.Builder().url(url);
        RequestBody requestBody = getRequestBody(params);
        switch (method) {
            case HttpUtils.Method.METHOD_GET:
                builder.get();
                break;
            case HttpUtils.Method.METHOD_POST:
                builder.post(requestBody);
                break;
            case HttpUtils.Method.METHOD_PUT:
                builder.put(requestBody);
                break;
            case HttpUtils.Method.METHOD_DELETE:
                builder.delete(requestBody);
                break;
            default:
                Timber.d("param 'method' exception.");
                throw new RuntimeException("param 'method' exception.");
        }
        return builder.build();
    }

    private RequestBody getRequestBody(Map<String, String> params) {
        if (getParamType() == ParamType.JSON) {
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            return RequestBody.create(mediaType, gson.toJson(params));
        } else if (getParamType() == ParamType.KEY_VALUE) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (String key : params.keySet()) {
                formBodyBuilder.addEncoded(key, params.get(key));
            }
            return formBodyBuilder.build();
        } else {
            Timber.d("field 'paramType' exception.");
            throw new RuntimeException("field 'paramType' exception.");
        }
    }
}
