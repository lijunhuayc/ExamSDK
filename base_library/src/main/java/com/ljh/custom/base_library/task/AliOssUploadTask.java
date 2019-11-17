package com.ljh.custom.base_library.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.gson.Gson;
import com.ljh.custom.base_library.utils.FileUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created on 2017/6/21.
 * Function:
 *
 * @author Zhipeng.Fan
 */
public class AliOssUploadTask extends AsyncTask<String, Integer, AliOssUploadTask.Result> {
    private static final String boundary = "9431149156168";
    private static final String METHOD_POST = "POST";
    private static final String KEY_USER_AGENT = "User-Agent";
    private static final String VALUE_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)";
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String VALUE_CONTENT_INPUT = "Content-Disposition: form-data; name=";
    private static final String VALUE_OSM = "application/octet-stream";
    private static final String KEY_FILE_DATA = "Content-Disposition: form-data; name=\"file\"; filename=";
    private static final String VALUE_FILE_DATA = "Content-Type: ";
    private static final String VALUE_CONTENT_TYPE = "multipart/form-data; boundary=%s";
    private static final String LINE__ = "--";
    private static final String ENTER = "\r\n";
    private static final String QUOTES = "\"";
    private static final String CHARSET = "UTF-8";
    private static final String ENCODER = "HmacSHA1";
    private static final int TIME_OUT_CONNECT = 15 * 1000;
    private static final int TIME_OUT_READ = 30 * 1000;
    private long lastUpdateTime;
    private long updateInterval = 800;
    private UploadCallBack mUploadCallBack;

    public interface UploadCallBack {
        void onSuccess(String result);

        void onFailure(int status, String message);

        void onFinish();
    }

    public void registerCallBack(@NonNull UploadCallBack pUploadCallBack) {
        this.mUploadCallBack = pUploadCallBack;
    }

    /**
     * 刷新进度间隔，默认800ms
     */
    public void setUpdateInterval(long pUpdateInterval) {
        this.updateInterval = pUpdateInterval;
    }

    @Override
    protected Result doInBackground(String... params) {
        Result result;
        try {
            result = formUpload(params[0], params[1]);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(-1, "上传失败");
    }

    @Override
    protected void onPostExecute(Result s) {
        if (mUploadCallBack == null)
            return;
        if (s.getStatus() == 1) {
            mUploadCallBack.onSuccess(new Gson().toJson(s));
        } else {
            mUploadCallBack.onFailure(s.getStatus(), s.getMsg());
        }
        mUploadCallBack.onFinish();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
//        if (mUploadCallBack instanceof Callback.ProgressCallback) {
//            if (System.currentTimeMillis() - lastUpdateTime > updateInterval) {
//                ((Callback.ProgressCallback) mUploadCallBack).onLoading(values[0], values[1], false);
//                lastUpdateTime = System.currentTimeMillis();
//            }
//        }
    }

    /**
     * 自动构建图片上传并实现图片的上传,自动签名
     *
     * @param localFile   本地文件的绝对路径(eg [ d://datas/xxx.jpg ] [/Users/xxx/xxx.jpg])
     * @param outFileName oss存储的文件名称,注意根目录不能带 ' / '
     * @return
     * @throws Exception
     */
    private Result formUpload(String localFile, String outFileName) throws Exception {
        return formUpload(URL_ALI_BUCKET, buildRequestParams(localFile, outFileName), localFile);
    }

    /**
     * 图片上传,不签名
     *
     * @param formFields 提交的字段列表
     * @param localFile  oss存储的文件名称,注意根目录不能带 ' / '
     * @return
     * @throws Exception
     */
    private Result formUpload(Map<String, String> formFields, String localFile) throws Exception {
        return formUpload(URL_ALI_BUCKET, formFields, localFile);
    }

    /**
     * 上传文件到oss
     *
     * @param urlStr
     * @param formFields
     * @param localFile
     * @return
     * @throws Exception
     */
    private Result formUpload(String urlStr, Map<String, String> formFields, String localFile)
            throws Exception {
        File file = new File(localFile);
        if (!file.exists()) {
            return new Result(-1, "图片不存在");
        }
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        FileInputStream fileInputStream = new FileInputStream(file);
        int total = fileInputStream.available();
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT_CONNECT);
            conn.setReadTimeout(TIME_OUT_READ);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(METHOD_POST);
            conn.setRequestProperty(KEY_USER_AGENT, VALUE_USER_AGENT);
            conn.setRequestProperty(KEY_CONTENT_TYPE, String.format(VALUE_CONTENT_TYPE, boundary));
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // text
            StringBuffer strBuf = new StringBuffer();
            if (formFields != null) {
                Iterator<Map.Entry<String, String>> iter = formFields.entrySet().iterator();
                int i = 0;
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();

                    if (inputValue == null) {
                        continue;
                    }

                    if (i == 0) {
                        strBuf.append(LINE__).append(boundary).append(ENTER);
                        strBuf.append(VALUE_CONTENT_INPUT).append(QUOTES).append(inputName).append(QUOTES).append(ENTER).append(ENTER);
                        strBuf.append(inputValue);
                    } else {
                        strBuf.append(ENTER).append(LINE__).append(boundary).append(ENTER);
                        strBuf.append(VALUE_CONTENT_INPUT).append(QUOTES).append(inputName).append(QUOTES).append(ENTER).append(ENTER);
                        strBuf.append(inputValue);
                    }

                    i++;
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            String filename = file.getName();
            strBuf = new StringBuffer();
            strBuf.append(ENTER).append(LINE__).append(boundary).append(ENTER);
            strBuf.append(KEY_FILE_DATA + QUOTES + filename + QUOTES + ENTER);
            strBuf.append(VALUE_FILE_DATA + VALUE_OSM + ENTER + ENTER);
            out.write(strBuf.toString().getBytes());

            DataInputStream in = new DataInputStream(fileInputStream);
            int bytes = -1;
            int current = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
                current += bytes;
                publishProgress(total, current);
            }
            in.close();

            out.write((ENTER + LINE__ + boundary + LINE__ + ENTER).getBytes());
            out.flush();
            out.close();

            // 读取返回数据
            int resultCode = conn.getResponseCode();
            if (resultCode < 200 || resultCode > 300) {
                return new Result(-1, String.format(Locale.CHINA, "错误[%d]", resultCode));
            }
            // 取得该连接的输入流，以读取响应内容
            return new Result(1, FileUtils.inputStream2String(conn.getInputStream(), CHARSET));
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
    }

    private String computeSignature(String accessKeySecret, String encodePolicy)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // convert to UTF-8
        byte[] key = accessKeySecret.getBytes(CHARSET);
        byte[] data = encodePolicy.getBytes(CHARSET);

        // hmac-sha1
        Mac mac = Mac.getInstance(ENCODER);
        mac.init(new SecretKeySpec(key, ENCODER));
        byte[] sha = mac.doFinal(data);
        // base64
        return new String(Base64.encode(sha, Base64.NO_WRAP));
    }

    private static final String K_KEY = "key";
    private static final String K_CONTENT_DIS = "Content-Disposition";
    private static final String K_ASS_KEY = "OSSAccessKeyId";
    private static final String K_POLICY = "policy";
    private static final String K_SIGNATURE = "Signature";
    private static final String URL_ALI_BUCKET = "http://oss2.yicheku.com.cn";
    //    private static final String URL_ALI_BUCKET = "http://cheku-bucket.oss-cn-shenzhen.aliyuncs.com";
    private static final String V_ASS_KEY = "LTAIOFzbZKPIyDyP";
    private static final String V_ASS_SECRET = "O8tGkDmmazqH4fe5c7Aw4OFTcuxMXl";
    private static final String V_POLICY = "{\"expiration\": \"2120-01-01T12:00:00.000Z\",\"conditions\": [[\"content-length-range\", 0, 104857600]]}";
    private static final String V_ATTACHMENT = "attachment;filename=";

    /**
     * 签名请求内容
     *
     * @param localFilePath
     * @param outFileName
     * @return
     * @throws Exception
     */
    private Map<String, String> buildRequestParams(String localFilePath, String outFileName) throws Exception {
        // 表单域
        Map<String, String> formFields = new LinkedHashMap<String, String>();
        // key
        formFields.put(K_KEY, outFileName);
        // Content-Disposition
        formFields.put(K_CONTENT_DIS, V_ATTACHMENT + localFilePath);
        // OSSAccessKeyId
        formFields.put(K_ASS_KEY, V_ASS_KEY);
        // policy
//        String encodePolicy = new String(Base64.encode(V_POLICY.getBytes(), Base64.DEFAULT));
        String encodePolicy = new String(Base64.encode(V_POLICY.getBytes(), Base64.NO_WRAP));
        formFields.put(K_POLICY, encodePolicy);
        // Signature
        formFields.put(K_SIGNATURE, computeSignature(V_ASS_SECRET, encodePolicy));
        return formFields;
    }

    public static class Result {
        int status;
        String msg;

        protected Result(int state, String message) {
            this.status = state;
            this.msg = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }
    }
}
