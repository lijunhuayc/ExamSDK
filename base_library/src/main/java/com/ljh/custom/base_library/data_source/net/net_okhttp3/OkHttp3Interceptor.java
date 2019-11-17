package com.ljh.custom.base_library.data_source.net.net_okhttp3;

import com.ljh.custom.base_library.BuildConfig;
import com.ljh.custom.base_library.data_source.net.BaseLibraryAPIService;
import com.ljh.custom.base_library.utils.Timber;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Desc: OkHttp3 网络拦截器
 * Created by Junhua.Li
 * Date: 2018/07/23 14:10
 */
public class OkHttp3Interceptor implements Interceptor {
    public OkHttp3Interceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // TO-DO: 2018/6/29 0029 网络 Request&Response 拦截处理
        Request oldRequest = chain.request();
        Request newRequest = createNewRequest(oldRequest);
        printRequestParams(newRequest);
        Response response = chain.proceed(newRequest);//可做 Response 拦截处理
        return response;
    }

    /**
     * 仅 DEBUG 执行网络参数打印
     *
     * @param pNewRequest
     */
    private void printRequestParams(Request pNewRequest) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("")
                .append("OkHttpClient.intercept:").append("\n")
                .append("OkHttpClient: request headers = ").append(pNewRequest.headers())/*.append("\n")*/
                .append("OkHttpClient: request method = ").append(pNewRequest.method()).append("\n")
                .append("OkHttpClient: request path = ").append(pNewRequest.url().encodedPath()).append("\n")
                .append("OkHttpClient: request params = ").append(pNewRequest.url().query()).append("\n")
                .append("OkHttpClient: request url = ").append(pNewRequest.url()).append("\n")
                .append("OkHttpClient: request body = ").append(pNewRequest.body()).append("\n");
        RequestBody requestBody = pNewRequest.body();
        if (null != requestBody) {
            stringBuilder.append("OkHttpClient: request body.contentType = ").append(requestBody.contentType()).append("\n");
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) pNewRequest.body();
                Class formBodyClass = FormBody.class;
                try {
                    Field encodedNamesField = formBodyClass.getDeclaredField("encodedNames");
                    encodedNamesField.setAccessible(true);
                    List<String> encodedNames = (List<String>) encodedNamesField.get(formBody);
                    Field encodedValuesField = formBodyClass.getDeclaredField("encodedValues");
                    encodedValuesField.setAccessible(true);
                    List<String> encodedValues = (List<String>) encodedValuesField.get(formBody);
                    if (null != encodedNames) {
                        if (null != encodedValues && encodedValues.size() > encodedNames.size()) {
                            encodedValues = encodedValues.subList(0, encodedNames.size());
                        }
                        stringBuilder.append("OkHttpClient: request body(POST params): \n");
                        for (int i = 0; i < encodedNames.size(); i++) {
                            stringBuilder.append("\t\t\t\t").append(encodedNames.get(i)).append(" = ");
                            if (null != encodedValues && i < encodedValues.size()) {
                                stringBuilder.append(encodedValues.get(i));
                            }
                            stringBuilder.append("\n");
                        }
                    }
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
            } else {
                requestBody.contentType();
                Buffer buffer = new Buffer();
                try {
                    requestBody.writeTo(buffer);
                    Charset charset = Charset.defaultCharset();//defaultCharset is UTF-8
                    stringBuilder.append("OkHttpClient: request body.content = ").append(buffer.readString(charset)).append("\n");
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
            }
        }
        Timber.d(stringBuilder.toString());
    }

    private Request createNewRequest(Request pRequest) {
        HttpUrl.Builder builder = pRequest.url()
                .newBuilder()
//                .setEncodedQueryParameter("ver", AppInfoModel.getInstance().getVersionName())
//                .setEncodedQueryParameter("dev", "6")//终端类型:6-android服务商
//                .setEncodedQueryParameter("out", "json")
                ;
        /**
         * @see BaseLibraryAPIService#postDataReportTest(String)
         */
        if (!"true".equals(pRequest.header("not-token"))) {
//            builder.setEncodedQueryParameter("access_token", mUserInfoProvider.getToken());
        }
        return pRequest.newBuilder()
                .method(pRequest.method(), pRequest.body())
                .url(builder.build())
                .build();
    }
}
