package com.frxs.order.model;

import java.io.Serializable;

/**
 * Created by ewu on 2016/11/29.
 */

public class BaseCartGoodsInfo implements Serializable {

    private String ProductId;

    private double PreQty;

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public double getPreQty() {
        return PreQty;
    }

    public void setPreQty(double preQty) {
        PreQty = preQty;
    }
}
