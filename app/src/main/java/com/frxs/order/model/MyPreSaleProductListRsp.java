package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/11/14.
 */

public class MyPreSaleProductListRsp implements Serializable {

    /**
     * TotalCount : 24
     * ItemList : [{"PID":52,"WID":200,"PreSaleProductId":4,"ShopId":46,"DeliveryPrice":2323,"PreOrderQty":1,"PackingQty":2332,"PreOrderDate":"2016-11-11 10:22:03","TotalQty":2332,"TotalAmt":2323,"CreateTime":"2016-11-11 10:22:03","CreateUserId":0,"CreateUserName":"","IsDeleted":0,"Status":0,"ShopName":"(00015070)*2长沙远大","ShopCode":"00015070","LineId":1,"LineName":"通用线路","ProductName":"323","ProductName2":"2323","BarCode":"233223","DeliveryUnit":"233","SKU":""}]
     */

    private int TotalCount;
    /**
     * PID : 52
     * WID : 200
     * PreSaleProductId : 4
     * ShopId : 46
     * DeliveryPrice : 2323
     * PreOrderQty : 1
     * PackingQty : 2332
     * PreOrderDate : 2016-11-11 10:22:03
     * TotalQty : 2332
     * TotalAmt : 2323
     * CreateTime : 2016-11-11 10:22:03
     * CreateUserId : 0
     * CreateUserName :
     * IsDeleted : 0
     * Status : 0
     * ShopName : (00015070)*2长沙远大
     * ShopCode : 00015070
     * LineId : 1
     * LineName : 通用线路
     * ProductName : 323
     * ProductName2 : 2323
     * BarCode : 233223
     * DeliveryUnit : 233
     * SKU :
     */

    private List<MyPreSaleProductBean> ItemList;

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int TotalCount) {
        this.TotalCount = TotalCount;
    }

    public List<MyPreSaleProductBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<MyPreSaleProductBean> ItemList) {
        this.ItemList = ItemList;
    }

    public static class MyPreSaleProductBean {
        private int PID;
        private int WID;
        private String PreSaleProductId;
        private int ShopId;
        private double DeliveryPrice;
        private double PreOrderQty;
        private double PackingQty;
        private String PreOrderDate;
        private double TotalQty;
        private double TotalAmt;
        private String CreateTime;
        private int CreateUserId;
        private String CreateUserName;
        private int IsDeleted;
        private int Status; //1：已处理 0：未处理
        private String ShopName;
        private String ShopCode;
        private int LineId;
        private String LineName;
        private String ProductName;
        private String ProductName2;
        private String BarCode;
        private String DeliveryUnit;
        private String SKU;
        private String MarketUnit;
        private double MarketPrice;

        public int getPID() {
            return PID;
        }

        public void setPID(int PID) {
            this.PID = PID;
        }

        public int getWID() {
            return WID;
        }

        public void setWID(int WID) {
            this.WID = WID;
        }

        public String getPreSaleProductId() {
            return PreSaleProductId;
        }

        public void setPreSaleProductId(String PreSaleProductId) {
            this.PreSaleProductId = PreSaleProductId;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
            this.ShopId = ShopId;
        }

        public double getDeliveryPrice() {
            return DeliveryPrice;
        }

        public void setDeliveryPrice(int DeliveryPrice) {
            this.DeliveryPrice = DeliveryPrice;
        }

        public double getPreOrderQty() {
            return PreOrderQty;
        }

        public void setPreOrderQty(int PreOrderQty) {
            this.PreOrderQty = PreOrderQty;
        }

        public double getPackingQty() {
            return PackingQty;
        }

        public void setPackingQty(int PackingQty) {
            this.PackingQty = PackingQty;
        }

        public String getPreOrderDate() {
            return PreOrderDate;
        }

        public void setPreOrderDate(String PreOrderDate) {
            this.PreOrderDate = PreOrderDate;
        }

        public double getTotalQty() {
            return TotalQty;
        }

        public void setTotalQty(int TotalQty) {
            this.TotalQty = TotalQty;
        }

        public double getTotalAmt() {
            return TotalAmt;
        }

        public void setTotalAmt(int TotalAmt) {
            this.TotalAmt = TotalAmt;
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

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getShopCode() {
            return ShopCode;
        }

        public void setShopCode(String ShopCode) {
            this.ShopCode = ShopCode;
        }

        public int getLineId() {
            return LineId;
        }

        public void setLineId(int LineId) {
            this.LineId = LineId;
        }

        public String getLineName() {
            return LineName;
        }

        public void setLineName(String LineName) {
            this.LineName = LineName;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getProductName2() {
            return ProductName2;
        }

        public void setProductName2(String ProductName2) {
            this.ProductName2 = ProductName2;
        }

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String BarCode) {
            this.BarCode = BarCode;
        }

        public String getDeliveryUnit() {
            return DeliveryUnit;
        }

        public void setDeliveryUnit(String DeliveryUnit) {
            this.DeliveryUnit = DeliveryUnit;
        }

        public String getSKU() {
            return SKU;
        }

        public void setSKU(String SKU) {
            this.SKU = SKU;
        }

        public double getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            MarketPrice = marketPrice;
        }

        public String getMarketUnit() {
            return MarketUnit;
        }

        public void setMarketUnit(String marketUnit) {
            MarketUnit = marketUnit;
        }
    }
}
