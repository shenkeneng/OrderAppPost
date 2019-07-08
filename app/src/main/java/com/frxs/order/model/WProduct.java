package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ewu on 2016/5/6.
 */
public class WProduct implements Serializable {

    @SerializedName("SalePrice")
    private double SalePrice;

    @SerializedName("ShopPoint")
    private double ShopPoint;

    @SerializedName("SaleBackFlag")
    private int SaleBackFlag;

    @SerializedName("BigUnit")
    private String BigUnit;

    private double BigPackingQty;

    private String MarketUnit;

    private double ShopAddPerc;

    private String Unit;//: "包"

    private double MarketPrice;//: 20,

    private String ProductName2;//商品副标题

    private int IsNoStock;//有无库存商品  0：无库存商品,1：无库存商品

    public String getProductName2() {
        return ProductName2;
    }

    public void setProductName2(String productName2) {
        ProductName2 = productName2;
    }

    public double getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(double salePrice) {
        SalePrice = salePrice;
    }

    public double getShopPoint() {
        return ShopPoint;
    }

    public void setShopPoint(double shopPoint) {
        ShopPoint = shopPoint;
    }

    public int getSaleBackFlag() {
        return SaleBackFlag;
    }

    public void setSaleBackFlag(int saleBackFlag) {
        SaleBackFlag = saleBackFlag;
    }

    public String getBigUnit() {
        return BigUnit;
    }

    public void setBigUnit(String bigUnit) {
        BigUnit = bigUnit;
    }

    public double getBigPackingQty() {
        return BigPackingQty;
    }

    public void setBigPackingQty(double bigPackingQty) {
        BigPackingQty = bigPackingQty;
    }

    public String getMarketUnit() {
        return MarketUnit;
    }

    public void setMarketUnit(String marketUnit) {
        MarketUnit = marketUnit;
    }

    public double getShopAddPerc() {
        return ShopAddPerc;
    }

    public void setShopAddPerc(double shopAddPerc) {
        ShopAddPerc = shopAddPerc;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public double getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        MarketPrice = marketPrice;
    }

    public int getIsNoStock() {
        return IsNoStock;
    }

    public void setIsNoStock(int isNoStock) {
        IsNoStock = isNoStock;
    }
}
