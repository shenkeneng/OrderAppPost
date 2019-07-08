package com.frxs.order.model;

import java.io.Serializable;

/**
 * 商品清单 By Tiepier
 */
public class CommodityList implements Serializable {

    private static final long serialVersionUID = 1115998136887560518L;

    private String GoodsImg;

    private String GoodsDescribe;

    private double GoodsPrice;

    private int ShopIntegral;

    private int PlatformRate;

    private int GoodsCount;

    private String GoodsRemark;

    public String getGoodsImg() {
        return GoodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        GoodsImg = goodsImg;
    }

    public String getGoodsDescribe() {
        return GoodsDescribe;
    }

    public void setGoodsDescribe(String goodsDescribe) {
        GoodsDescribe = goodsDescribe;
    }

    public double getGoodsPrice() {
        return GoodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        GoodsPrice = goodsPrice;
    }

    public int getShopIntegral() {
        return ShopIntegral;
    }

    public void setShopIntegral(int shopIntegral) {
        ShopIntegral = shopIntegral;
    }

    public int getPlatformRate() {
        return PlatformRate;
    }

    public void setPlatformRate(int platformRate) {
        PlatformRate = platformRate;
    }

    public int getGoodsCount() {
        return GoodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        GoodsCount = goodsCount;
    }

    public String getGoodsRemark() {
        return GoodsRemark;
    }

    public void setGoodsRemark(String goodsRemark) {
        GoodsRemark = goodsRemark;
    }
}
