package com.ljh.custom.base_library.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Desc: 接口状态基类
 * PS: 所有 xx-RESULT 均继承此类 [所有 xx-RESULT 仅在数据解析时使用, 数据层->业务层后均使用 xx-DATA]
 * Created by Junhua.Li
 * Date: 2018/04/03 17:11
 */
public class BaseResult<T> {
    public static final int NOT_LOGIN = 3000;
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_SERVICE_ERROR = -1;//服务器返回的错误码
    public static final int STATUS_INTERNAL_ERROR = -2;//内部回调处理异常错误码
    public static final int STATUS_NETWORK_FAILURE = -3;//网络连接失败
    public static final int STATUS_REPORT_CUSTOM_ERROR = -99;//数据上报解码异常
    private int errCode;
    private String errMsg;
    private String more;
    private boolean success;
    private PageBean page;
    @SerializedName(value = "data", alternate = {"datas", "list"})
    private T data;
    private boolean hasNextPage;

    public void setData(T pData) {
        data = pData;
    }

    public T getData() {
        return data;
    }

    public boolean isReturnSuccess() {
//        return errCode == STATUS_SUCCESS;
        return success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean pSuccess) {
        success = pSuccess;
    }

    public boolean isNotLogin() {
        return errCode == NOT_LOGIN;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int status) {
        this.errCode = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String message) {
        this.errMsg = message;
    }

    public String getMore() {
        try {
            return TextUtils.isEmpty(more) ? more : URLDecoder.decode(more, "UTF-8");
        } catch (UnsupportedEncodingException pE) {
            pE.printStackTrace();
            return more;
        }
    }

    public void setMore(String pMore) {
        more = pMore;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean pHasNextPage) {
        hasNextPage = pHasNextPage;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", more='" + more + '\'' +
                ", page=" + page +
                ", data=" + data +
                '}';
    }
}
