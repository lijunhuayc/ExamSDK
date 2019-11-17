package com.ljh.custom.base_library.data_source.net;

import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.SystemConfigModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2018/07/25 15:25
 */
public interface BaseLibraryAPIService {
    /**
     * 数据上报 正式环境(使用专用域名)
     *
     * @return
     */
    @Headers({"not-token:true"})
    @POST("http://statistics.yicheku.com.cn:41180/statistics_v1_0_0/new/report/event/upload")
    Call<BaseResult<String>> postDataReportRelease(@Body String bodyContent);

    /**
     * 数据上报 测试环境
     *
     * @return
     */
    @Headers({"not-token:true"})
    @POST("http://192.168.2.210:41180/statistics_v1_0_0/new/report/event/upload")
    Call<BaseResult<String>> postDataReportTest(@Body String bodyContent);

    /**
     * 数据上报 210测试环境
     *
     * @return
     */
    @Headers({"not-token:true"})
    @POST("http://192.168.2.210:41180/statistics_v1_0_0/new/report/event/upload")
    Call<BaseResult<String>> postDataReportTest210(@Body String bodyContent);

    /**
     * 数据上报 200测试环境
     *
     * @return
     */
    @Headers({"not-token:true"})
    @POST("http://192.168.2.200:41180/statistics_v1_0_0/new/report/event/upload")
    Call<BaseResult<String>> postDataReportTest200(@Body String bodyContent);

    /**
     * 获取系统上报配置表
     *
     * @return
     */
    @Headers({"not-token:true"})
    @GET("sysConfig/getByConfigKey")
    Call<BaseResult<SystemConfigModel>> getSystemConfig(@Query("configKey") String configKey);
}
