package com.frxs.order.model;


import java.util.List;

/**
 * Post请求单行修改购物车商品实体 By Tiepier
 */
public class PostEditCart {

    public static final int EDIT_TYPE_ADD = 0;
    public static final int EDIT_TYPE_EDIT = 1;
    public static final int EDIT_TYPE_DELETE = 2;

    private int EditType; //修改类型 0：add 1:edit 2:delete
    private String OrderId;
    private String WID;
    private String DetailId;
    private String ShopID;
    private String UserId;
    private String UserName;
    private int WarehouseId;
    private PostInfo Cart;
    private CartGoodsDetail Detail;
    private PostPreGood BookProduct;// 修改预订商品备注需传预订商品
    private List<PostPreGood> Items;// 删除预订商品需传预订商品的集合
    private String ClientIP;// 客户端ip
    private String ClinetModelType;//　客户端设备应用信息

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

    public String getDetailId() {
        return DetailId;
    }

    public void setDetailId(String detailId) {
        DetailId = detailId;
    }

    public CartGoodsDetail getDetail() {
        return Detail;
    }

    public void setDetail(CartGoodsDetail detail) {
        Detail = detail;
    }

    public int getEditType() {
        return EditType;
    }

    public void setEditType(int editType) {
        EditType = editType;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
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

    public int getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        WarehouseId = warehouseId;
    }

    public PostInfo getCart() {
        return Cart;
    }

    public void setCart(PostInfo cart) {
        Cart = cart;
    }

    public PostPreGood getBookProduct() {
        return BookProduct;
    }

    public void setBookProduct(PostPreGood preGood) {
        this.BookProduct = preGood;
    }

    public List<PostPreGood> getItems() {
        return Items;
    }

    public void setItems(List<PostPreGood> items) {
        Items = items;
    }

    public String getClientIP() {
        return ClientIP;
    }

    public void setClientIP(String clientIP) {
        ClientIP = clientIP;
    }

    public String getClinetModelType() {
        return ClinetModelType;
    }

    public void setClinetModelType(String clinetModelType) {
        ClinetModelType = clinetModelType;
    }
}
