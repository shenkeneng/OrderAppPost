package com.frxs.order.model;

import java.util.List;

/**
 * Created by Chentie on 2017/3/10.
 */

public class ShopPointIncome {

    private double PointQtyTotay;// 本月可用积分
    private int Total;
    private List<ItemListBean> ItemList;

    public double getPointQtyTotay() {
        return PointQtyTotay;
    }

    public void setPointQtyTotay(double PointQtyTotay) {
        this.PointQtyTotay = PointQtyTotay;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<ItemListBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<ItemListBean> ItemList) {
        this.ItemList = ItemList;
    }

    public static class ItemListBean {

        private String BillNO;// 订单号
        private double PointQty;// 积分
        private String PointTime;// 时间
        private String ProductImageUrl200;// 商品图片
        private String ProductName;// 商品名称
        private double Qty;// 数量
        private String Remark;// 备注
        private String SKU;// 商品编号
        private int PointType;// 4：增值积分

        public String getBillNO() {
            return BillNO;
        }

        public void setBillNO(String BillNO) {
            this.BillNO = BillNO;
        }

        public double getPointQty() {
            return PointQty;
        }

        public void setPointQty(double PointQty) {
            this.PointQty = PointQty;
        }

        public String getPointTime() {
            return PointTime;
        }

        public void setPointTime(String PointTime) {
            this.PointTime = PointTime;
        }

        public String getProductImageUrl200() {
            return ProductImageUrl200;
        }

        public void setProductImageUrl200(String ProductImageUrl200) {
            this.ProductImageUrl200 = ProductImageUrl200;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public double getQty() {
            return Qty;
        }

        public void setQty(double Qty) {
            this.Qty = Qty;
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

        public int getPointType() {
            return PointType;
        }

        public void setPointType(int pointType) {
            PointType = pointType;
        }
    }
}
