package com.frxs.order.model;

import java.io.Serializable;

/**
 * 订单跟踪 By Tiepier
 */
public class OrderTrack implements Serializable {

    private static final long serialVersionUID = -4003312013250465399L;

    private String OrderID;

    private String CreateTime;

    private String Remark;

    private int OrderStatus;

    private String OrderStatusName;

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return OrderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        OrderStatusName = orderStatusName;
    }
}
