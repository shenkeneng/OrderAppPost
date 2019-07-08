package com.frxs.order.model;

import java.io.Serializable;

/**
 * OrderShop By Tiepier
 */
public class OrderShop implements Serializable {
    private String OrderId;
    private String WID;
    private String SubWID;
    private int OrderType;
    private String WarehouseId;
    private String UserId;
    private String UserName;

    private String Remark;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getSubWID() {
        return SubWID;
    }

    public void setSubWID(String subWID) {
        SubWID = subWID;
    }

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int orderType) {
        OrderType = orderType;
    }
}
