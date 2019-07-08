package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/9/13.
 */
public class PromotiaonActivityDetail implements Serializable {

    /**
     * PromotionID : sample string 1
     * PromotionName : sample string 2
     * PromotionRules : sample string 3
     * BeginTime : 2016-09-13 14:05:04
     * EndTime : 2016-09-13 14:05:04
     */

    private String PromotionID;
    private String PromotionName;
    private String PromotionRules;
    private String BeginTime;
    private String EndTime;

    private List<PromotionActivityGroup> WPromotionModelGroupList;

    private List<PromotionProduct> WPromotionGiftProductsList;

    public String getPromotionID() {
        return PromotionID;
    }

    public void setPromotionID(String PromotionID) {
        this.PromotionID = PromotionID;
    }

    public String getPromotionName() {
        return PromotionName;
    }

    public void setPromotionName(String PromotionName) {
        this.PromotionName = PromotionName;
    }

    public String getPromotionRules() {
        return PromotionRules;
    }

    public void setPromotionRules(String PromotionRules) {
        this.PromotionRules = PromotionRules;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String BeginTime) {
        this.BeginTime = BeginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public List<PromotionActivityGroup> getWPromotionModelGroupList() {
        return WPromotionModelGroupList;
    }

    public void setWPromotionModelGroupList(List<PromotionActivityGroup> WPromotionModelGroupList) {
        this.WPromotionModelGroupList = WPromotionModelGroupList;
    }

    public List<PromotionProduct> getWPromotionGiftProductsList() {
        return WPromotionGiftProductsList;
    }

    public void setWPromotionGiftProductsList(List<PromotionProduct> WPromotionGiftProductsList) {
        this.WPromotionGiftProductsList = WPromotionGiftProductsList;
    }
}
