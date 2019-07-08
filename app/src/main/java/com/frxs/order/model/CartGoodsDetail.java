package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ewu on 2016/5/3.
 */
public class CartGoodsDetail extends BaseCartGoodsInfo implements Serializable {

    @SerializedName("ID")
    private String ID;

    @SerializedName("ProductName")
    private String ProductName;

    @SerializedName("Remark")
    private String Remark;

    @SerializedName("ShopPoint")
    private double ShopPoint;

    @SerializedName("PromotionShopPoint")
    private double PromotionShopPoint;

    @SerializedName("SalePrice")
    private double SalePrice;

    @SerializedName("ShopAddPerc")
    private double ShopAddPerc;

    @SerializedName("SalePackingQty")
    private double SalePackingQty;

    @SerializedName("IsGift")
    private int IsGift; //0表示商品 1表示赠品 2表示搭售？

    @SerializedName("GiftPromotionID")
    private String GiftPromotionID;

    @SerializedName("GiftPromotionName")
    private String GiftPromotionName;

    private boolean fromModifyOrder = false; //表示是否是修改订单

    private String OrderID;

    private String WID;

    private String WarehouseId;

    private String UserId;

    private String UserName;

    private String SaleUnit;

    private Double MinPreQty;//最小起定量

    private Double MaxaPreQty;//最大起定量

    private String CategoryName1;//一级分类

    private int IsNoStock; //0：有库存   1：无库存

    public Double getMinPreQty() {
        return MinPreQty;
    }

    public void setMinPreQty(Double minPreQty) {
        MinPreQty = minPreQty;
    }

    public Double getMaxaPreQty() {
        return MaxaPreQty;
    }

    public void setMaxaPreQty(Double maxaPreQty) {
        MaxaPreQty = maxaPreQty;
    }

    public String getSaleUnit() {
        return SaleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        SaleUnit = saleUnit;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        WarehouseId = warehouseId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public boolean isFromModifyOrder() {
        return fromModifyOrder;
    }

    public void setFromModifyOrder(boolean fromModifyOrder) {
        this.fromModifyOrder = fromModifyOrder;
    }

    public double getSalePackingQty() {
        return SalePackingQty;
    }

    public void setSalePackingQty(double salePackingQty) {
        SalePackingQty = salePackingQty;
    }

    private int editType = -1;  //修改类型 0：add 1:edit 2:delete

    private double newPreQty = 0; //表示需要修改的商品预定数量

    private boolean needDelete;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public double getShopPoint() {
        return ShopPoint;
    }

    public void setShopPoint(double shopPoint) {
        ShopPoint = shopPoint;
    }

    public double getPromotionShopPoint() {
        return PromotionShopPoint;
    }

    public void setPromotionShopPoint(double promotionShopPoint) {
        PromotionShopPoint = promotionShopPoint;
    }

    public double getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(double salePrice) {
        SalePrice = salePrice;
    }

    public double getShopAddPerc() {
        return ShopAddPerc;
    }

    public void setShopAddPerc(double shopAddPerc) {
        ShopAddPerc = shopAddPerc;
    }

    public double getNewPreQty() {
        return newPreQty;
    }

    public void setNewPreQty(double newPreQty) {
        this.newPreQty = newPreQty;
    }

    public boolean isNeedDelete() {
        return needDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        this.needDelete = needDelete;
    }

    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

    public double getGoodsShopPoint() {
        return PromotionShopPoint > 0 ? PromotionShopPoint : ShopPoint;
    }

    public int getIsGift() {
        return IsGift;
    }

    public void setIsGift(int isGift) {
        IsGift = isGift;
    }

    public String getGiftPromotionID() {
        return GiftPromotionID;
    }

    public void setGiftPromotionID(String giftPromotionID) {
        GiftPromotionID = giftPromotionID;
    }

    public String getGiftPromotionName() {
        return GiftPromotionName;
    }

    public void setGiftPromotionName(String giftPromotionName) {
        GiftPromotionName = giftPromotionName;
    }

    public String getCategoryName1() {
        return CategoryName1;
    }

    public void setCategoryName1(String categoryName1) {
        CategoryName1 = categoryName1;
    }

    public int getIsNoStock() {
        return IsNoStock;
    }

    public void setIsNoStock(int isNoStock) {
        IsNoStock = isNoStock;
    }
}
