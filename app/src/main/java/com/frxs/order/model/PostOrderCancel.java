package com.frxs.order.model;


import java.util.List;

/**
 * Post请求订单取消参数传递 By Tiepier
 */
public class PostOrderCancel {

    private String UserID;

    private String WarehouseId;

    private String Status;

    private String UserName;

    private String CloseReason;

    private List<String> OrderIdList;

    public String getCloseReason() {
        return CloseReason;
    }

    public void setCloseReason(String closeReason) {
        CloseReason = closeReason;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        WarehouseId = warehouseId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public List<String> getOrderIdList() {
        return OrderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        OrderIdList = orderIdList;
    }
}
