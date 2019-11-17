package com.ljh.custom.base_library;

import com.ljh.custom.base_library.model.UserModel;

/**
 * Desc: 外部调用设置token
 * Created by Junhua.Li
 * Date: 2019/11/17 14:26
 */
public class ExamSDK {
    private static UserModel sUserModel;
    private static String UserType = "front";

    public static void init(UserModel pUserModel) {
        sUserModel = pUserModel;
    }

    public static String getToken() {
        if (null == sUserModel) {
            throw new RuntimeException("not initialize SDK.");
        }
        return sUserModel.getToken();
    }

    public static UserModel getUserModel() {
        return sUserModel;
    }

    public static void setUserType(String pUserType) {
        UserType = pUserType;
    }

    public static String getUserType() {
        return UserType;
    }
}
