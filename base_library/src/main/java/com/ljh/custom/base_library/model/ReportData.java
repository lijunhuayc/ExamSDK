package com.ljh.custom.base_library.model;

import android.text.TextUtils;

import com.ljh.custom.base_library.data_report.ReportDataService;
import com.ljh.custom.base_library.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/5/23.
 * Function:数据上报model
 *
 * @author Zhipeng.Fan
 */
public class ReportData implements Serializable {
    private static final int FROM_TYPE_CHEKU = 0;
    private static final int FROM_TYPE_FWS = 1;

    public ReportData() {
        session_id = ReportDataService.getInstance().getUuid();
        if (TextUtils.isEmpty(this.account)) {
            this.account = "unlogin";
        }
        this.event_time = DateUtils.getFormatTimeStr(System.currentTimeMillis());
        this.from = FROM_TYPE_FWS;
    }

    private static final long serialVersionUID = 5309435954714846902L;
    /*
     * session_id:本参数为会话ID,格式：UUID,会话ID应贯穿整个会话过程。每一次一级页面的展示开始，都认为是一个新会话，因些在一级页面展示时，需重置Session_id
     * event:事件ID，定义参见ck_report_meta_event
     * event_time:事件产生的时间，格式：yyyy-MM-dd hh:mm:ss
     * name:事件主体的名称，各终端需统一约定，用来表示主体，例如：首页
     * type:事件主体的类型，定义参见ck_report_meta_type
     * account:事件生产者账号，未登录时传：unlogin
     */
    private String session_id;
    /**
     * 事件类型。进入页面还是退出页面{@link //com.renxin.cheku.logic.service.ReportDataService.EVENT_PAGE_IN}
     * {@link //com.renxin.cheku.logic.service.ReportDataService.EVENT_PAGE_OUT}
     */
    private int event;
    private String event_time;
    private String name;
    /**
     * 时间触发类型
     * {@link //com.renxin.cheku.logic.service.ReportDataService.EVENT_TYPE_PAGE}跳转
     * {@link //com.renxin.cheku.logic.service.ReportDataService.EVENT_TYPE_CLICK}按钮点击
     * {@link //com.renxin.cheku.logic.service.ReportDataService.EVENT_TYPE_VIEW}view显示
     */
    private int type;
    private String account;
    private String target;
    private String uCompany;
    private List<ContentBean> content;
    private int from = FROM_TYPE_FWS;//来源 from  0车酷APP端  1车酷服务商app端

    public int getFrom() {
        return from;
    }

    public void setFrom(int pFrom) {
        from = pFrom;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public String getuCompany() {
        return uCompany;
    }

    public void setuCompany(String uCompany) {
        this.uCompany = uCompany;
    }

    public ReportData setContent(List<ContentBean> content) {
        this.content = content;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public ReportData setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getEvent() {
        return event;
    }

    public ReportData setEvent(int event) {
        this.event = event;
        return this;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getName() {
        return name;
    }

    public ReportData setName(String name) {
        this.name = name;
        return this;
    }

    public int getType() {
        return type;
    }

    public ReportData setType(int type) {
        this.type = type;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ReportData addContent(ContentBean contentBean) {
        if (contentBean == null) return this;
        if (content == null) {
            content = new ArrayList<>();
        }
        content.add(contentBean);
        return this;
    }

    public static class ContentBean implements Serializable {
        private static final long serialVersionUID = 5135370875140439423L;
        private String key;
        private String value;
        private String desc;

        public ContentBean() {
        }

        public ContentBean(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public ContentBean(String key, String value, String desc) {
            this.key = key;
            this.value = value;
            this.desc = desc;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
