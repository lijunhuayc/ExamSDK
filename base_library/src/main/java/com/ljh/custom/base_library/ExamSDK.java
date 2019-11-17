package com.ljh.custom.base_library;

/**
 * Desc: 外部调用设置token
 * Created by Junhua.Li
 * Date: 2019/11/17 14:26
 */
public class ExamSDK {
    private static String token;
    private static String UserType = "front";

    public static void init(String pToken) {
        token = pToken;
    }

    public static String getToken() {
        return token;
    }

    public static String getUserType() {
        return UserType;
    }
}
