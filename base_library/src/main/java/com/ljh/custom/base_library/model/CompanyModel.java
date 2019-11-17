package com.ljh.custom.base_library.model;

import java.util.List;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 19:05
 */
public class CompanyModel {
    /**
     * companyId : string
     * companyName : string
     * positionDtoList : [{"positionId":0,"positionName":"string"}]
     */
    private String companyId;
    private String companyName;
    private List<PositionModel> positionDtoList;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<PositionModel> getPositionDtoList() {
        return positionDtoList;
    }

    public void setPositionDtoList(List<PositionModel> positionDtoList) {
        this.positionDtoList = positionDtoList;
    }
}
