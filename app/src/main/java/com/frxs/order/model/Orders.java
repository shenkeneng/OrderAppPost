package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * 订单信息 By Tiepier
 */

public class Orders implements Serializable {

    private String OrderId;//订单编号

    private int Status;//订单状态

    private int TotalProductCount;//商品数量

    private String OrderDate;//下单日期

    private double TotalPoint;//订单积分

    private double PayAmount;//订单总金额

    private String Remark;//整单备注

    private String ShippingEndDate;// 配送时间

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getTotalProductCount() {
        return TotalProductCount;
    }

    public void setTotalProductCount(int totalProductCount) {
        TotalProductCount = totalProductCount;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public double getTotalPoint() {
        return TotalPoint;
    }

    public void setTotalPoint(double totalPoint) {
        TotalPoint = totalPoint;
    }

    public double getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(double payAmount) {
        PayAmount = payAmount;
    }

    public String getShippingEndDate() {
        return ShippingEndDate;
    }

    public void setShippingEndDate(String shippingEndDate) {
        ShippingEndDate = shippingEndDate;
    }
}
