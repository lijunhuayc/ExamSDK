package com.ljh.custom.base_library.data_report;

import android.text.TextUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ljh.custom.base_library.BuildConfig;
import com.ljh.custom.base_library.data_source.net.BaseLibraryAPIService;
import com.ljh.custom.base_library.data_source.net.WebAPI;
import com.ljh.custom.base_library.data_source.net.retrofit.RetrofitUtils;
import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.ReportData;
import com.ljh.custom.base_library.receiver.LibProvider;
import com.ljh.custom.base_library.utils.DESUtils;
import com.ljh.custom.base_library.utils.FileUtils;
import com.ljh.custom.base_library.utils.NetworkUtils;
import com.ljh.custom.base_library.utils.Timber;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;

import static com.ljh.custom.base_library.model.BaseResult.STATUS_INTERNAL_ERROR;
import static com.ljh.custom.base_library.model.BaseResult.STATUS_REPORT_CUSTOM_ERROR;

/**
 * Created by youdong.shen on 2018/4/18.
 */
public class ReportWorkThread extends Thread {
//    @Autowired(name = RouterTable.PROVIDER_USER_INFO_PROVIDER_IMPL)
//    UserInfoProvider mUserInfoProvider;
    /**
     * 本地文件名
     */
    private static final String FILE_NAME = "report_data_cache_01.obj";
    private boolean mQuit;
    private Gson mGson = new Gson();
    private final List<ReportData> mDataQueue = Collections.synchronizedList(new ArrayList<>());
    private int maxCount = 10;
    private int minCount = 5;

    public ReportWorkThread() {
//        ARouter.getInstance().inject(this);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public void putData(ReportData data) {
        if (data == null) {
            return;
        }
        mDataQueue.add(data);
        writeToLocal();
    }

    private List<ReportData> takeDatas() {
        List<ReportData> result = new ArrayList<>();
        if (mDataQueue.size() < minCount) {
            return result;
        }
        synchronized (mDataQueue) {
            for (int i = 0; i < maxCount && mDataQueue.size() > 0; i++) {
                result.add(mDataQueue.remove(0));
            }
        }
        return result;
    }

    public void clear() {
        mDataQueue.clear();
    }

    public void stopThread() {
        mQuit = true;
        interrupt();
    }

    ExecutorService mExecutors = Executors.newSingleThreadExecutor();

    /**
     * 刷新本地数据
     */
    private void writeToLocal() {
        mExecutors.execute(() -> {
            String filePath = FileUtils.getExternalFileDirChildPath(FileUtils.FILE_CACHE_DIR) + File.separator + FILE_NAME;
            String dataStr;
            synchronized (mDataQueue) {
                dataStr = mGson.toJson(mDataQueue);
            }
            if (FileUtils.writeFile(filePath, dataStr)) {
                Timber.e("ReportWorkThread.writeToLocal: 缓存成功");
            } else {
                Timber.e("ReportWorkThread.writeToLocal: 缓存失败");
            }
        });
    }

    /**
     * 读取本地数据
     */
    private void readFromLocal() {
        Timber.d(" ReportWorkThread.readFromLocal: start");
        String filePath = FileUtils.getExternalFileDirChildPath(FileUtils.FILE_CACHE_DIR) + File.separator + FILE_NAME;
        StringBuilder content = FileUtils.readFile(filePath, "UTF-8");
        if (null != content) {
            List<ReportData> result = mGson.fromJson(content.toString(), new TypeToken<List<ReportData>>() {
            }.getType());
            Timber.d(" ReportWorkThread.readFromLocal: size = %s", result.size());
            if (null != result && !result.isEmpty()) {
                mDataQueue.addAll(result);
            }
        } else {
            Timber.d(" ReportWorkThread.readFromLocal: not content");
        }
    }

    private int sleepTime = 1000;
    private boolean isReport = false;

    @Override
    public void run() {
        readFromLocal();
        final List<ReportData> reportData = new ArrayList<>();
        while (!mQuit) {
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (reportData.size() == 0) {
                reportData.addAll(takeDatas());
            }
            if (reportData.size() == 0 || isReport || !NetworkUtils.isNetworkAvailable(LibProvider.getLibContext())) {
                continue;
            }
            isReport = true;
            String content = mGson.toJson(reportData);

            //以下部分暂时以此方式发起网络请求上报
            RetrofitUtils.getInstance().request(BaseLibraryAPIService.class, new RetrofitUtils.RetrofitCallback<BaseLibraryAPIService, String>() {
                @Override
                public void onSuccess(String model) {
                    Timber.d("ReportDataService postDataReport.onSuccess: 原始数据: %s", model);
                    try {
                        String decode = DESUtils.decode(model);
                        Timber.d("ReportDataService postDataReport.onSuccess: 解码数据: %s", URLDecoder.decode(decode, "UTF-8"));
                        if (TextUtils.isEmpty(decode)) {
                            onError(STATUS_REPORT_CUSTOM_ERROR, "解码错误");
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(decode);
                        int status = jsonObject.getInt("status");
                        if (status != 1) {
                            onError(status, jsonObject.getString("msg"));
                        } else {
                            Timber.d("ReportDataService postDataReport.onSuccess: 上报成功");
                            handlerSuccess();
                        }
                    } catch (UnsupportedEncodingException pE) {
                        pE.printStackTrace();
                        onError(STATUS_INTERNAL_ERROR, "URLDecoder解码错误");
                    } catch (Exception pE) {
                        pE.printStackTrace();
                        onError(STATUS_INTERNAL_ERROR, "JSON解析错误");
                    }
                }

                private void handlerSuccess() {
                    writeToLocal();
                    sleepTime = 1000;
                    reportData.clear();
                    isReport = false;
                }

                @Override
                public void onError(int pStatus, String pMessage) {
                    Timber.d("ReportDataService postDataReport.onError: %s", pMessage);
                    sleepTime = Math.min(sleepTime * 2, 6000);
                    if (pStatus == STATUS_REPORT_CUSTOM_ERROR) {//接口正确返回,解码错误时忽略错误,看着上报成功
                        handlerSuccess();
                    } else {
                        isReport = false;
                    }
                }

                @Override
                public Call<BaseResult<String>> getAPI(BaseLibraryAPIService pT) {
                    String bodyContent = DESUtils.encode(content);
                    Timber.d("ReportDataService postDataReport.getAPI: bodyContent = %s", bodyContent);
                    if (BuildConfig.DEBUG) {
                        switch (WebAPI.HOST_IP) {
                            case WebAPI.DOMAIN_TEST_210:
                                return pT.postDataReportTest210(bodyContent);//210上报210服务器
                            case WebAPI.DOMAIN_TEST_RELEASE:
                                return pT.postDataReportRelease(bodyContent);//线上测试上报线上服务器
                            default:
                                return pT.postDataReportTest200(bodyContent);//其他上报200服务器
                        }
                    } else {
                        return pT.postDataReportRelease(bodyContent);//发布包上报线上服务器
                    }

                    /**
                     * 添加out=json接口返回的原始数据结构为"{"data":"3th02psZaknU2W4oJ0zn9+D8nKcFnjdco4P/ExeaoK2oCukWODPAPq==","status":1}"
                     * 走正常流程读取data字段(data解码后格式为"{"msg":"操作成功","status":1}"),读取status状态判断
                     * @see com.ljh.custom.base_library.data_source.net.retrofit.converter.CustomResponseBodyConverter#convert(ResponseBody)
                     */
                }

                @Override
                public void onFinish() {
                }
            });
        }
    }
}
