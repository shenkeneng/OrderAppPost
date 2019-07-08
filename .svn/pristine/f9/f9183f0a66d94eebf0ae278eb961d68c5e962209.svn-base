package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chentie on 2017/5/15.
 */

public class OrderPreProducts {

    /**
     * Items : [{"BookOrderId":"c4d3d5e4-a5eb-4f02-9163-e0853af87e47","CreateTime":"2017-05-12 15:42:28","ID":"20064107f86-0aae-46b8-a66d-51ee63b96b93","OrderDate":"2017-05-12 15:42:28","ProductId":70102,"ProductName":"YYJ","Remark":"","SKU":"453443","SaleAmt":1980,"SalePackingQty":90,"SalePrice":990,"SaleQty":2,"SaleUnit":"打","ShopPoint":2.7,"Status":2,"StatusName":"备货完成","TotalPoint":0}]
     * Rows : 4
     */

    private int Rows;
    private List<ItemsBean> Items;

    public int getRows() {
        return Rows;
    }

    public void setRows(int Rows) {
        this.Rows = Rows;
    }

    public List<ItemsBean> getItems() {
        return Items;
    }

    public void setItems(List<ItemsBean> Items) {
        this.Items = Items;
    }

    public static class ItemsBean implements Serializable{
        /**
         * BookOrderId : c4d3d5e4-a5eb-4f02-9163-e0853af87e47
         * CreateTime : 2017-05-12 15:42:28
         * ID : 20064107f86-0aae-46b8-a66d-51ee63b96b93
         * OrderDate : 2017-05-12 15:42:28
         * ProductId : 70102
         * ProductName : YYJ
         * Remark :
         * SKU : 453443
         * SaleAmt : 1980
         * SalePackingQty : 90
         * SalePrice : 990
         * SaleQty : 2
         * SaleUnit : 打
         * ShopPoint : 2.7
         * Status : 2
         * StatusName : 备货完成
         * TotalPoint : 0
         */

        private String BookOrderId;
        private String CreateTime;//下单时间
        private String ID;
        private String OrderDate;//订单时间
        private String ProductId;//商品ID
        private String ProductName;//商品名称
        private String Remark;//备注
        private String SKU;//编码
        private double SaleAmt;//订单总金额
        private double SalePrice;//商品价格
        private double SaleQty;//数量
        private String SaleUnit;//单位
        private double ShopPoint;//商品积分
        private int Status;//0:录单状态;1.已确认;2:备货完成;3:已转订单;8:交易取消;9:交易关闭
        private double TotalPoint;
        private int editType = -1;  //修改类型 0：add 1:edit 2:delete
        private boolean needDelete;
        //private boolean fromModifyOrder = false; //表示是否是修改订单

        public String getBookOrderId() {
            return BookOrderId;
        }

        public void setBookOrderId(String BookOrderId) {
            this.BookOrderId = BookOrderId;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getOrderDate() {
            return OrderDate;
        }

        public void setOrderDate(String OrderDate) {
            this.OrderDate = OrderDate;
        }

        public String getProductId() {
            return ProductId;
        }

        public void setProductId(String ProductId) {
            this.ProductId = ProductId;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getSKU() {
            return SKU;
        }

        public void setSKU(String SKU) {
            this.SKU = SKU;
        }

        public double getSaleAmt() {
            return SaleAmt;
        }

        public void setSaleAmt(double SaleAmt) {
            this.SaleAmt = SaleAmt;
        }

        public double getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(double SalePrice) {
            this.SalePrice = SalePrice;
        }

        public double getSaleQty() {
            return SaleQty;
        }

        public void setSaleQty(double SaleQty) {
            this.SaleQty = SaleQty;
        }

        public String getSaleUnit() {
            return SaleUnit;
        }

        public void setSaleUnit(String SaleUnit) {
            this.SaleUnit = SaleUnit;
        }

        public double getShopPoint() {
            return ShopPoint;
        }

        public void setShopPoint(double ShopPoint) {
            this.ShopPoint = ShopPoint;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public double getTotalPoint() {
            return TotalPoint;
        }

        public void setTotalPoint(double TotalPoint) {
            this.TotalPoint = TotalPoint;
        }

        public int getEditType() {
            return editType;
        }

        public void setEditType(int editType) {
            this.editType = editType;
        }

        public boolean isNeedDelete() {
            return needDelete;
        }

        public void setNeedDelete(boolean needDelete) {
            this.needDelete = needDelete;
        }
    }
}
