package com.frxs.order.model;


import java.util.List;

/**
 * Post请求订单整单修改参数传递 By Tiepier
 */
public class PostOrderEditAll {

    private String ShopId;
    private String SubWID;
    private String WarehouseId;
    private boolean bDeleteOld;
    private String UserID;
    private String OrderId;
    private boolean bFromCart;
    private String Remark;
    private OrderShop OrderShop;
    private List<CartGoodsDetail> Details;
    private String UserName;
    private String ClientIP;
    private String ClientModelType;


    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getSubWID() {
        return SubWID;
    }

    public void setSubWID(String subWID) {
        SubWID = subWID;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        WarehouseId = warehouseId;
    }

    public boolean isbDeleteOld() {
        return bDeleteOld;
    }

    public void setbDeleteOld(boolean bDeleteOld) {
        this.bDeleteOld = bDeleteOld;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public boolean isbFromCart() {
        return bFromCart;
    }

    public void setbFromCart(boolean bFromCart) {
        this.bFromCart = bFromCart;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public com.frxs.order.model.OrderShop getOrderShop() {
        return OrderShop;
    }

    public void setOrderShop(com.frxs.order.model.OrderShop orderShop) {
        OrderShop = orderShop;
    }

    public List<CartGoodsDetail> getDetails() {
        return Details;
    }

    public void setDetails(List<CartGoodsDetail> details) {
        Details = details;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getClientIP() {
        return ClientIP;
    }

    public void setClientIP(String clientIP) {
        ClientIP = clientIP;
    }

    public String getClientModelType() {
        return ClientModelType;
    }

    public void setClientModelType(String clientModelType) {
        ClientModelType = clientModelType;
    }
}
