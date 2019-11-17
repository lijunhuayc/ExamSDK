package com.ljh.custom.base_library.data_source.net.retrofit;

import com.ljh.custom.base_library.data_source.net.WebAPI;
import com.ljh.custom.base_library.data_source.net.net_okhttp3.OkHttp3CookieJar;
import com.ljh.custom.base_library.data_source.net.net_okhttp3.OkHttp3Interceptor;
import com.ljh.custom.base_library.data_source.net.retrofit.converter.CustomConverterFactory;
import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.PageBean;
import com.ljh.custom.base_library.receiver.LibProvider;
import com.ljh.custom.base_library.utils.MyToast;
import com.ljh.custom.base_library.utils.NetworkUtils;
import com.ljh.custom.base_library.utils.Timber;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static com.ljh.custom.base_library.model.BaseResult.STATUS_INTERNAL_ERROR;
import static com.ljh.custom.base_library.model.BaseResult.STATUS_NETWORK_FAILURE;

/**
 * Desc: Retrofit 封装
 * Created by Junhua.Li
 * Date: 2018/06/29 09:45
 */
public class RetrofitUtils {
    private static Retrofit sRetrofit;
    private static RetrofitUtils sRetrofitUtils;
//    @Autowired(name = RouterTable.PROVIDER_GLOBAL_HOST_PROVIDER_IMPL)
//    GlobalHostProvider mGlobalHostProvider;

    public static final RetrofitUtils getInstance() {
        if (null == sRetrofitUtils) {
            synchronized (RetrofitUtils.class) {
                if (null == sRetrofitUtils) {
                    sRetrofitUtils = new RetrofitUtils();
                }
            }
        }
        return sRetrofitUtils;
    }

    private RetrofitUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new OkHttp3CookieJar())
                .addInterceptor(new OkHttp3Interceptor())
                .build();
        sRetrofit = new Retrofit.Builder()
//                .baseUrl(mGlobalHostProvider.getHost())
                .baseUrl(WebAPI.HOST)
                .client(okHttpClient)
                .addConverterFactory(CustomConverterFactory.create())
                .build();
    }

    private <API_SERVICE> API_SERVICE getAPIService(Class<API_SERVICE> pClass) {
        return sRetrofit.create(pClass);
    }

    public <API_SERVICE, RESULT> boolean request(Class<API_SERVICE> pClass, RetrofitCallback<API_SERVICE, RESULT> pObtainCallback) {
        Call<BaseResult<RESULT>> call = pObtainCallback.getAPI(getAPIService(pClass));
        if (null != call) {
            if (NetworkUtils.isNetworkAvailable(LibProvider.getLibContext())) {
                call.enqueue(pObtainCallback);
                return true;
            } else {
                MyToast.showToast("网络异常，请检查您的网络连接");
                pObtainCallback.onError(STATUS_NETWORK_FAILURE, "网络异常，请检查您的网络连接");
                pObtainCallback.onFinish();
                return false;
            }
        } else {
            Timber.d("RetrofitCallback.request: this Call<Result> is null. please return on method RetrofitCallback.getAPI()");
            pObtainCallback.onError(STATUS_INTERNAL_ERROR, "Sorry! 出了点意外");
            pObtainCallback.onFinish();
            return false;
        }
    }

    /**
     * @param <API_SERVICE> Retrofit ApiService
     * @param <RESULT>      解析结果 Result Model
     */
    public static abstract class RetrofitCallback<API_SERVICE, RESULT> implements Callback<BaseResult<RESULT>> {
        @Override
        public void onResponse(Call<BaseResult<RESULT>> call, retrofit2.Response<BaseResult<RESULT>> response) {
            /**
             * @see com.ljh.custom.base_library.data_source.net.retrofit.converter.CustomResponseBodyConverter
             * 在转换器中对返回的特殊原始数据做了一定处理
             */
            BaseResult<RESULT> baseResult = response.body();
            if (null == baseResult) {
                String errResult = "";
                ResponseBody errBody = response.errorBody();
                if (null != errBody) {
                    try {
                        errResult = errBody.string();
                    } catch (IOException pE) {
                        pE.printStackTrace();
                    }
                }
                Timber.d("RetrofitCallback.onResponse: errResult = %s", errResult);
                onError(STATUS_INTERNAL_ERROR, "获取数据失败");
                onFinish();
                return;
            }

            Timber.d("RetrofitCallback.onResponse: result(处理后) = %s", baseResult);//需要Bean/Model类实现toString方法才能完全打印data字段的对象信息
            try {
                if (baseResult.isReturnSuccess()) {
                    onGetPage(baseResult.getPage());
                    onSuccess(baseResult.getData());
                } else {
                    Timber.d("RetrofitCallback.onResponse: error. more = %s", baseResult.getMore());
                    switch (baseResult.getStatus()) {
                        case BaseResult.NOT_LOGIN:
//                            ARouter.getInstance().build(RouterTable.USERINFO_LOGIN_ACTIVITY)
//                                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    .navigation(BaseLibraryApplication.getApplication());
                            break;
                        default:
                            onError(baseResult.getStatus(), baseResult.getMsg());
                            break;
                    }
                }
            } catch (Exception e) {
                Timber.d("RetrofitCallback.onResponse: exception = %s", e.getMessage());
                e.printStackTrace();
                onError(STATUS_INTERNAL_ERROR, "获取数据失败");
            }
            onFinish();
        }

        @Override
        public void onFailure(Call<BaseResult<RESULT>> call, Throwable t) {
            Timber.d("RetrofitCallback.onFailure: error = %s", t);
            onError(STATUS_NETWORK_FAILURE, "获取数据失败");
            onFinish();
        }

        public abstract void onSuccess(RESULT model);

        public void onGetPage(PageBean pPageBean) {

        }

        public abstract void onError(int pStatus, String pMessage);

        public abstract Call<BaseResult<RESULT>> getAPI(API_SERVICE pT);

        public abstract void onFinish();
    }
}
