package com.ljh.examsdk.net;

import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2018/07/25 15:25
 */
public interface UserAPIService {
    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("api/user/queryUserDetailForApp")
    Call<BaseResult<UserModel>> queryUserDetailForApp(@Query("phone") String phone);

    @Headers({"not-token:true"})
    @FormUrlEncoded
    @POST("/api/user/login")
    Call<BaseResult<UserModel>> login(@Field("phone") String phone, @Field("password") String password);
}
