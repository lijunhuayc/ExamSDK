package com.ljh.custom.base_library.data_source.net;

import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.ExamItemModel;
import com.ljh.custom.base_library.model.SystemConfigModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2018/07/25 15:25
 */
public interface ExamAPIService {
//    /**
//     *
//     *
//     * @return
//     */
//    @GET("api/examTable/findAllExam?currentPage=1&pageSize=12")
//    Call<BaseResult<SystemConfigModel>> findAllExam(@Query("currentPage") String currentPage,@Query("pageSize") String pageSize);

    /**
     * @return
     */
    @GET("api/examTable/findExamList")
    Call<BaseResult<List<ExamItemModel>>> findExamList(@Query("currentPage") int currentPage, @Query("pageSize") int pageSize, @Query("userId") String userId);

    /**
     * @param currentPage
     * @param pageSize
     * @param authType
     * @param id
     * @param paperNum
     * @param problemCount
     * @param title
     * @param type         0-模拟卷， 1-考试卷, 2-vip
     * @return
     */
    @GET(" /api/paper/findAllPaper?")
    Call<BaseResult<SystemConfigModel>> findAllPaper(@Query("currentPage") String currentPage,
                                                     @Query("pageSize") String pageSize,
                                                     @Query("authType") String authType,
                                                     @Query("id") String id,
                                                     @Query("paperNum") String paperNum,
                                                     @Query("problemCount") String problemCount,
                                                     @Query("title") String title,
                                                     @Query("type") String type);

//    @FormUrlEncoded
//    @POST("/api/baidu/freight")
//    Call<BaseResult<List<LogisticsCostBean>>> getLogisticsCost(@Field("auto_model_id") int modelId,
//                                                               @Field("supplier_id") int supplierId,
//                                                               @Field("address_id") int addressId,
//                                                               @Field("transport_company_id") String logisticsCompanyId);
//
//    @POST("/api2/order/add")
//    Call<BaseResult<LogisticsOrderBean>> createLogisticsOrder(@Body() String body);
}
