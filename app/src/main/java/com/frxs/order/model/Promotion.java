package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * 促销活动列表 By Tiepier
 */

public class Promotion implements Serializable {
    //促销编码
    private String PromotionID;
    //促销名称
    private String PromotionName;
    //促销开始时间
    private String BeginTime;
    //促销结束时间
    private String EndTime;
    //促销商品
    private List<PromotionProduct> Items;

    public String getPromotionID() {
        return PromotionID;
    }

    public void setPromotionID(String promotionID) {
        PromotionID = promotionID;
    }

    public String getPromotionName() {
        return PromotionName;
    }

    public void setPromotionName(String promotionName) {
        PromotionName = promotionName;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public List<PromotionProduct> getItems() {
        return Items;
    }

    public void setItems(List<PromotionProduct> items) {
        Items = items;
    }
}
