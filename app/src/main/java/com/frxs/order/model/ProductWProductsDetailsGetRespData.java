package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/5/6.
 */
public class ProductWProductsDetailsGetRespData implements Serializable {

    @SerializedName("CurrentProduct")
    private ProductInfo CurrentProduct;

    @SerializedName("Attributes")
    private List<Attribute> Attributes;

    @SerializedName("SubProducts")
    private List<ProductInfo> SubProducts;

    public List<ProductInfo> getSubProducts() {
        return SubProducts;
    }

    public void setSubProducts(List<ProductInfo> subProducts) {
        SubProducts = subProducts;
    }

    public List<Attribute> getAttributes() {
        return Attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        Attributes = attributes;
    }

    public ProductInfo getCurrentProduct() {
        return CurrentProduct;
    }

    public void setCurrentProduct(ProductInfo currentProduct) {
        CurrentProduct = currentProduct;
    }
}
