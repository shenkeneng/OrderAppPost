package com.frxs.order.model;

import java.io.Serializable;

/**
 * 正在配送实体 by Tiepier
 */
public class DeliveryBeing implements Serializable {
    private static final long serialVersionUID = -7067445788191676563L;
    private int OrderID;
    private String StoreName;
    private int OrderCount;
    private String StoreBossName;
    private String StoreBossPhone;
    private String PaymentName;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public String getStoreBossName() {
        return StoreBossName;
    }

    public void setStoreBossName(String storeBossName) {
        StoreBossName = storeBossName;
    }

    public String getStoreBossPhone() {
        return StoreBossPhone;
    }

    public void setStoreBossPhone(String storeBossPhone) {
        StoreBossPhone = storeBossPhone;
    }

    public String getPaymentName() {
        return PaymentName;
    }

    public void setPaymentName(String paymentName) {
        PaymentName = paymentName;
    }
}
