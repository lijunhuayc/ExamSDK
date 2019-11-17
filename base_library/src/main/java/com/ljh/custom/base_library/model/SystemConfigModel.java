package com.ljh.custom.base_library.model;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2018/07/30 16:57
 */
public class SystemConfigModel {
    private String configKey;//: "report_event_count",
    private String note;//: "前端上报数据的数量要求",
    private int configValue;//: "6",
    private int delFlag;//: 0

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String pConfigKey) {
        configKey = pConfigKey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String pNote) {
        note = pNote;
    }

    public int getConfigValue() {
        return configValue;
    }

    public void setConfigValue(int pConfigValue) {
        configValue = pConfigValue;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int pDelFlag) {
        delFlag = pDelFlag;
    }
}
