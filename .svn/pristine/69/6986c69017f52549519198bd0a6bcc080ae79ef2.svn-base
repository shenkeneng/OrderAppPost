package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ewu on 2016/4/29.
 */
public class ShopInfo implements Serializable {

    @SerializedName("ShopID")
    private String ShopID;

    @SerializedName("ShopName")
    private String ShopName;

    @SerializedName("WID")
    private int WID;// 子仓库ID

    @SerializedName("Status")
    private int Status;

    @SerializedName("MininumAmt")
    private double MininumAmt;

    @SerializedName("ShopType")
    private int ShopType; //门店类型(0:加盟店;1:签约店;2:网络店)

    public double getMininumAmt() {
        return MininumAmt;
    }

    public void setMininumAmt(double mininumAmt) {
        MininumAmt = mininumAmt;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public int getWID() {
        return WID;
    }

    public void setWID(int WID) {
        this.WID = WID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getShopType() {
        return ShopType;
    }

    public void setShopType(int shopType) {
        ShopType = shopType;
    }
}
