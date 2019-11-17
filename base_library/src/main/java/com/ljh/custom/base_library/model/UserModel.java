package com.ljh.custom.base_library.model;

import java.util.List;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 19:05
 */
public class UserModel {
    /**
     * address : string
     * areaId : 0
     * areaName : string
     * authCount : 0
     * avatar : string
     * cityId : 0
     * cityName : string
     * companyId : string
     * companyList : [{"companyId":"string","companyName":"string","positionDtoList":[{"positionId":0,"positionName":"string"}]}]
     * companyName : string
     * id : 0
     * name : string
     * nickName : string
     * phone : string
     * positionId : 0
     * positionName : string
     * provinceId : 0
     * provinceName : string
     * streetId : 0
     * streetName : string
     * token : string
     */
    private String address;
    private String areaId;
    private String areaName;
    private int authCount;
    private String avatar;
    private String cityId;
    private String cityName;
    private String companyId;
    private String companyName;
    private String id;
    private String name;
    private String nickName;
    private String phone;
    private String positionId;
    private String positionName;
    private String provinceId;
    private String provinceName;
    private String streetId;
    private String streetName;
    private String token;
    private List<CompanyModel> companyList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAuthCount() {
        return authCount;
    }

    public void setAuthCount(int authCount) {
        this.authCount = authCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CompanyModel> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyModel> companyList) {
        this.companyList = companyList;
    }
}
