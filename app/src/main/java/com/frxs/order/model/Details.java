package com.frxs.order.model;

import java.io.Serializable;

/**
 * 商品清单 By Tiepier
 */

public class Details implements Serializable {

    private int ProductId;
    private String ProductName;
    private String ProductImageUrl200;
    private double SalePrice;
    private Double ShopPoint;
    private Double SaleQty; //实发数量
    private Double PreQty; //预定数量
    private String SaleUnit;
    private double ShopAddPerc;
    private String Remark;
    private Double PromotionShopPoint;
    private double SalePackingQty;
    private String OrderID;
    private String ID;
    private String WID;
    private int IsGift;
    private String GiftPromotionID;
    private int IsNoStock; //0：有库存商品   1：无库存商品

    public int getIsGift() {
        return IsGift;
    }

    public void setIsGift(int isGift) {
        IsGift = isGift;
    }

    public String getSaleUnit() {
        return SaleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        SaleUnit = saleUnit;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public double getSalePackingQty() {
        return SalePackingQty;
    }

    public void setSalePackingQty(double salePackingQty) {
        SalePackingQty = salePackingQty;
    }

    public Double getPromotionShopPoint() {
        return PromotionShopPoint == null ? 0 : PromotionShopPoint;
    }

    public void setPromotionShopPoint(Double promotionShopPoint) {
        PromotionShopPoint = promotionShopPoint;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImageUrl200() {
        return ProductImageUrl200;
    }

    public void setProductImageUrl200(String productImageUrl200) {
        ProductImageUrl200 = productImageUrl200;
    }

    public double getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(double salePrice) {
        SalePrice = salePrice;
    }

    public Double getShopPoint() {
        return ShopPoint;
    }

    public void setShopPoint(Double shopPoint) {
        ShopPoint = shopPoint;
    }

    public Double getSaleQty() {
        return SaleQty;
    }

    public void setSaleQty(Double saleQty) {
        SaleQty = saleQty;
    }

    public Double getPreQty() {
        return PreQty;
    }

    public void setPreQty(Double preQty) {
        PreQty = preQty;
    }

    public double getShopAddPerc() {
        return ShopAddPerc;
    }

    public void setShopAddPerc(double shopAddPerc) {
        ShopAddPerc = shopAddPerc;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getGiftPromotionID() {
        return GiftPromotionID;
    }

    public void setGiftPromotionID(String giftPromotionID) {
        GiftPromotionID = giftPromotionID;
    }

    public int getIsNoStock() {
        return IsNoStock;
    }

    public void setIsNoStock(int isNoStock) {
        IsNoStock = isNoStock;
    }
}
