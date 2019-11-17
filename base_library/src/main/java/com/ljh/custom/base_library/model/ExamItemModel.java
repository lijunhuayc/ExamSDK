package com.ljh.custom.base_library.model;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 20:01
 */
public class ExamItemModel {
    private String id;  //考试ID
    private String title;
    private String backgroundUrl;
    private boolean isShop;//是否需要企业
    private String positionId;//岗位ID
    private String starTime;
    private String endTime;

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        id = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String pBackgroundUrl) {
        backgroundUrl = pBackgroundUrl;
    }

    public boolean isShop() {
        return isShop;
    }

    public void setShop(boolean pShop) {
        isShop = pShop;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String pPositionId) {
        positionId = pPositionId;
    }

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String pStarTime) {
        starTime = pStarTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String pEndTime) {
        endTime = pEndTime;
    }

    @Override
    public String toString() {
        return "ExamItemModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", backgroundUrl='" + backgroundUrl + '\'' +
                ", isShop=" + isShop +
                ", positionId='" + positionId + '\'' +
                ", starTime='" + starTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
