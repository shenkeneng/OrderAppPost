package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/11/14.
 */

public class GetPreSaleProductListRsp implements Serializable {

    private int TotalCount;

    /**
     * ProductId : 7
     * WID : 200
     * ProductName : 商品13335522
     * BarCode : 1234568977
     * ProductName2 : 水电费郭德纲的法国队
     * DeliveryUnit : 个
     * DeliveryPrice : 15.0
     * DeliveryPackingQty : 40.0
     * MarketUnit : 箱
     * MarketPrice : 123.0
     * MarketPackingQty : 20.0
     * SKU : 123
     * ImageExt1 : http://images.erp.frxs.cn/Product/2016/10/29/0d5fafe3-e169-4cea-9585-7414551f2ed5_400x400.Jpeg
     * ImageExt2 : 123
     * ImageExt3 : 123
     * Remak :
     * CreateTime : 2016-11-10 12:57:55
     * CreateUserId : 68
     * CreateUserName : admin
     * ModifyTime : null
     * ModifyUserID : null
     * ModifyUserName :
     * IsDeleted : 0
     * Status : 1
     * TotalCount : 4.0
     * TotalAmt : 60.0
     * ShopMySaleQty : 0.0
     */

    private List<PreSaleProductBean> ItemList;

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public List<PreSaleProductBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<PreSaleProductBean> ItemList) {
        this.ItemList = ItemList;
    }

    public static class PreSaleProductBean {
        private String ProductId;
        private int WID;
        private String ProductName;
        private String BarCode;
        private String ProductName2;
        private String DeliveryUnit;
        private double DeliveryPrice;
        private double DeliveryPackingQty;
        private String MarketUnit;
        private double MarketPrice;
        private double MarketPackingQty;
        private String SKU;
        private String ImageExt1;
        private String ImageExt2;
        private String ImageExt3;
        private String Remak;
        private String CreateTime;
        private int CreateUserId;
        private String CreateUserName;
        private Object ModifyTime;
        private Object ModifyUserID;
        private String ModifyUserName;
        private int IsDeleted;
        private int Status;
        private double TotalCount;
        private double TotalAmt;
        private double ShopMySaleQty;

        public String getProductId() {
            return ProductId;
        }

        public void setProductId(String ProductId) {
            this.ProductId = ProductId;
        }

        public int getWID() {
            return WID;
        }

        public void setWID(int WID) {
            this.WID = WID;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String BarCode) {
            this.BarCode = BarCode;
        }

        public String getProductName2() {
            return ProductName2;
        }

        public void setProductName2(String ProductName2) {
            this.ProductName2 = ProductName2;
        }

        public String getDeliveryUnit() {
            return DeliveryUnit;
        }

        public void setDeliveryUnit(String DeliveryUnit) {
            this.DeliveryUnit = DeliveryUnit;
        }

        public double getDeliveryPrice() {
            return DeliveryPrice;
        }

        public void setDeliveryPrice(double DeliveryPrice) {
            this.DeliveryPrice = DeliveryPrice;
        }

        public double getDeliveryPackingQty() {
            return DeliveryPackingQty;
        }

        public void setDeliveryPackingQty(double DeliveryPackingQty) {
            this.DeliveryPackingQty = DeliveryPackingQty;
        }

        public String getMarketUnit() {
            return MarketUnit;
        }

        public void setMarketUnit(String MarketUnit) {
            this.MarketUnit = MarketUnit;
        }

        public double getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(double MarketPrice) {
            this.MarketPrice = MarketPrice;
        }

        public double getMarketPackingQty() {
            return MarketPackingQty;
        }

        public void setMarketPackingQty(double MarketPackingQty) {
            this.MarketPackingQty = MarketPackingQty;
        }

        public String getSKU() {
            return SKU;
        }

        public void setSKU(String SKU) {
            this.SKU = SKU;
        }

        public String getImageExt1() {
            return ImageExt1;
        }

        public void setImageExt1(String ImageExt1) {
            this.ImageExt1 = ImageExt1;
        }

        public String getImageExt2() {
            return ImageExt2;
        }

        public void setImageExt2(String ImageExt2) {
            this.ImageExt2 = ImageExt2;
        }

        public String getImageExt3() {
            return ImageExt3;
        }

        public void setImageExt3(String ImageExt3) {
            this.ImageExt3 = ImageExt3;
        }

        public String getRemak() {
            return Remak;
        }

        public void setRemak(String Remak) {
            this.Remak = Remak;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public int getCreateUserId() {
            return CreateUserId;
        }

        public void setCreateUserId(int CreateUserId) {
            this.CreateUserId = CreateUserId;
        }

        public String getCreateUserName() {
            return CreateUserName;
        }

        public void setCreateUserName(String CreateUserName) {
            this.CreateUserName = CreateUserName;
        }

        public Object getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(Object ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public Object getModifyUserID() {
            return ModifyUserID;
        }

        public void setModifyUserID(Object ModifyUserID) {
            this.ModifyUserID = ModifyUserID;
        }

        public String getModifyUserName() {
            return ModifyUserName;
        }

        public void setModifyUserName(String ModifyUserName) {
            this.ModifyUserName = ModifyUserName;
        }

        public int getIsDeleted() {
            return IsDeleted;
        }

        public void setIsDeleted(int IsDeleted) {
            this.IsDeleted = IsDeleted;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public double getTotalCount() {
            return TotalCount;
        }

        public void setTotalCount(double TotalCount) {
            this.TotalCount = TotalCount;
        }

        public double getTotalAmt() {
            return TotalAmt;
        }

        public void setTotalAmt(double TotalAmt) {
            this.TotalAmt = TotalAmt;
        }

        public double getShopMySaleQty() {
            return ShopMySaleQty;
        }

        public void setShopMySaleQty(double ShopMySaleQty) {
            this.ShopMySaleQty = ShopMySaleQty;
        }
    }
}
