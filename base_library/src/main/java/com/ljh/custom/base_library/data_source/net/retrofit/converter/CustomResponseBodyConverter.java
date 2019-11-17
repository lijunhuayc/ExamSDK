package com.ljh.custom.base_library.data_source.net.retrofit.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.utils.DESUtils;
import com.ljh.custom.base_library.utils.Timber;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2018/07/31 10:00
 */
public class CustomResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedReader reader = new BufferedReader(value.charStream());
        String result = "";
        String str;
        while ((str = reader.readLine()) != null) {
            result += str;
        }
        value.close();
        printResponse(result);
        try {
            new JSONObject(result);
        } catch (JSONException pE) {
            //接口返回字符串不是 json 格式时, 认为是 base64编码 通信的接口, 手动封装为 StringResult 类型返回
            result = DESUtils.decode(result);
            if (TextUtils.isEmpty(result)) {
                //解码错误时,构建一个"status=-2"的标准json格式返回
                BaseResult<String> stringResult = new BaseResult<>();
                stringResult.setErrCode(BaseResult.STATUS_REPORT_CUSTOM_ERROR);
                stringResult.setData(result);
                stringResult.setErrMsg("解码错误");
                return (T) stringResult;
            }
        }
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes("UTF-8"))));
        try {
            JsonReader jsonReader = gson.newJsonReader(reader2);
            return adapter.read(jsonReader);
        } finally {
            try {
                reader2.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 打印返回的原始数据流
     *
     * @param result
     */
    private void printResponse(String result) {
        Timber.d("OkHttpClient: response result(原始数据) = %s", result);
    }
}