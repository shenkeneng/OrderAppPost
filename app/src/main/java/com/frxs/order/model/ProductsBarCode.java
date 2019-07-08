package com.frxs.order.model;

import java.io.Serializable;

/**
 * Created by Endoon on 2016/5/16.
 */
public class ProductsBarCode implements Serializable{
    private String ProductId;//: 70001,
    private String BarCode;//2201000003178",

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }
}
