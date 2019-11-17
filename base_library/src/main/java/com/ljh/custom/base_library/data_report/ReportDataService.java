package com.ljh.custom.base_library.data_report;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ljh.custom.base_library.data_source.net.BaseLibraryAPIService;
import com.ljh.custom.base_library.data_source.net.retrofit.RetrofitUtils;
import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.ReportData;
import com.ljh.custom.base_library.model.SystemConfigModel;
import com.ljh.custom.base_library.utils.DateUtils;
import com.ljh.custom.base_library.utils.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;

/**
 * Created on 2017/5/23.
 * Function:数据上报业务
 *
 * @author Zhipeng.Fan
 */
public class ReportDataService {
    public static final int EVENT_PAGE_IN = 1;//页面进入
    public static final int EVENT_PAGE_OUT = 2;//页面退出
    public static final int EVENT_TYPE_PAGE = 1;//事件类型，page
    public static final int EVENT_TYPE_CLICK = 2;//事件类型，click
    public static final int EVENT_TYPE_VIEW = 3;//view的显示隐藏
    private String mUuid = UUID.randomUUID().toString();
    //    @Autowired(name = RouterTable.PROVIDER_USER_INFO_PROVIDER_IMPL)
//    UserInfoProvider mUserInfoProvider;
    private ReportWorkThread mReportWorkThread = new ReportWorkThread();
    private Gson mGson = new Gson();
    private static ReportDataService mInstance = new ReportDataService();

    private ReportDataService() {
//        ARouter.getInstance().inject(this);
        mReportWorkThread.setMinCount(5);
    }

    public void init() {
        Timber.d("ReportDataService init");
        RetrofitUtils.getInstance().request(BaseLibraryAPIService.class, new RetrofitUtils.RetrofitCallback<BaseLibraryAPIService, SystemConfigModel>() {
            @Override
            public void onSuccess(SystemConfigModel model) {
                Timber.d("ReportDataService getSystemConfig.onSuccess: ReportWorkThread.maxCount = %s", mReportWorkThread.getMaxCount());
                if (model.getConfigValue() > 0) {
                    mReportWorkThread.setMaxCount(model.getConfigValue());
                }
            }

            @Override
            public void onError(int pStatus, String pMessage) {
                Timber.d("ReportDataService getSystemConfig.onError: %s", pMessage);
            }

            @Override
            public Call<BaseResult<SystemConfigModel>> getAPI(BaseLibraryAPIService pT) {
                return pT.getSystemConfig("report_event_count");
            }

            @Override
            public void onFinish() {
            }
        });
        mReportWorkThread.start();
    }

    public void stop() {
        mReportWorkThread.stopThread();
    }

    public static ReportDataService getInstance() {
        return mInstance;
    }

    /**
     * 每次重现一级页面(MainTabActivity)时重置
     */
    public void updateUuid() {
        mUuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return mUuid;
    }

    /**
     * 添加数据进行上报
     *
     * @param data 数据model
     */
    public void report(ReportData data) {
        Timber.d("ReportDataService.report: data = %s", mGson.toJson(data));
        data.setSession_id(ReportDataService.getInstance().getUuid());
//        String account = mUserInfoProvider.getLoginName();
        String account = "";
        if (TextUtils.isEmpty(account)) {
            account = "unlogin";
        }
        data.setAccount(account);
        data.setEvent_time(DateUtils.getFormatTimeStr(System.currentTimeMillis()));
        data.setuCompany(/*mUserInfoProvider.getCompanyName()*/"");
        mReportWorkThread.putData(data);
    }

    /**
     * 页面进入事件
     */
    public void pageInEvent(String pTarget, String pPageName) {
        Timber.d("ReportDataService.pageInEvent: pTarget = %s, pPageName = %s", pTarget, pPageName);
        ReportData data = new ReportData();
        data.setEvent(EVENT_PAGE_IN);
        data.setType(EVENT_TYPE_PAGE);
        data.setName(pPageName);
        data.setTarget(pTarget);
        report(data);
    }

    /**
     * 页面退出事件
     */
    public void pageOutEvent(String pTarget, String pPageName) {
        Timber.d("ReportDataService.pageOutEvent: pTarget = %s, pPageName = %s", pTarget, pPageName);
        ReportData data = new ReportData();
        data.setEvent(EVENT_PAGE_OUT);
        data.setType(EVENT_TYPE_PAGE);
        data.setName(pPageName);
        data.setTarget(pTarget);
        report(data);
    }

    /**
     * 上报用户登录事件
     */
    public void reportLoginEvent(String target) {
        Timber.d("ReportDataService reportLoginEvent target:" + target);
        ReportData data = new ReportData();
        data.setType(EVENT_TYPE_PAGE);
        data.setEvent(EVENT_PAGE_IN);
        data.setName("登录app");
        data.setTarget(target);
        data.addContent(new ReportData.ContentBean("account", /*mUserInfoProvider.getLoginName()*/"", "用户登录账号"));
        report(data);
    }

    /**
     * 上报用户退出app事件
     */
    public void reportExitEvent(String target) {
        Timber.d("ReportDataService reportExitEvent target:" + target);
        ReportData data = new ReportData();
        data.setType(EVENT_TYPE_PAGE);
        data.setEvent(EVENT_PAGE_OUT);
        data.setName("退出APP");
        data.setTarget(target);
        data.addContent(new ReportData.ContentBean("account", /*mUserInfoProvider.getLoginName()*/"", "用户退出登录账号"));
        report(data);
    }

    /**
     * 拨打电话的事件上报
     *
     * @param tel 电话号码
     */
    public void callPhoneEventReport(String tel, String target) {
        Timber.d("ReportDataService callPhoneEventReport tel:" + tel + ",target:" + target);
        ReportData data = new ReportData();
        data.setType(EVENT_TYPE_CLICK);
        data.setEvent(EVENT_PAGE_IN);
        data.setName("致电客服");
        data.setTarget(target);
        data.addContent(new ReportData.ContentBean("mobile", tel, "电话号码"));
        report(data);
    }

    /**
     * 统一点击事件上报
     *
     * @param target 点击打开目标页面类名(不打开页面则不传)
     * @param name
     */
    public void reportClickEvent(String target, String name) {
        reportClickEvent(target, name, new ArrayList<>());
    }

    /**
     * 统一点击事件上报
     *
     * @param target       点击打开目标页面类名(不打开页面则不传)
     * @param name
     * @param pContentBean 拓展信息
     */
    public void reportClickEvent(String target, String name, ReportData.ContentBean pContentBean) {
        List<ReportData.ContentBean> content = new ArrayList<>();
        if (pContentBean != null) {
            content.add(pContentBean);
        }
        reportClickEvent(target, name, content);
    }

    /**
     * 统一点击事件上报
     *
     * @param target  点击打开目标页面类名(不打开页面则不传)
     * @param name    事件名称
     * @param content 拓展信息
     */
    public void reportClickEvent(String target, String name, List<ReportData.ContentBean> content) {
        report(new ReportData()
                .setEvent(ReportDataService.EVENT_PAGE_IN)
                .setType(ReportDataService.EVENT_TYPE_CLICK)
                .setName(name)
                .setTarget(target)
                .setContent(content));
    }
}
