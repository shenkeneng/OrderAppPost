package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/5/6.
 */
public class ProductInfo implements Serializable {

    private int IsGift;//促销商品标识符(0：非促销商品；1：促销商品)

    @SerializedName("Product")
    private Product Product;

    @SerializedName("WProduct")
    private WProduct WProduct;

    @SerializedName("ProductAttributes")
    private List<ProductAttribute> ProductAttributes;

    @SerializedName("PictureDetails")
    private List<PictureDetails> PictureDetails;

    @SerializedName("AttributePicture")
    private AttributePicture AttributePicture;

    private List<ProductsBarCode> ProductsBarCodes;

    private Promotion WPromotion;//促销信息

    private Double MinPreQty;//最小起定量

    private Double MaxaPreQty;//最大起定量

    private String PromotionID;//促销活动ID

    public String getPromotionID() {
        return PromotionID;
    }

    public void setPromotionID(String promotionID) {
        PromotionID = promotionID;
    }

    public Double getMinPreQty() {
        return MinPreQty;
    }

    public void setMinPreQty(Double minPreQty) {
        MinPreQty = minPreQty;
    }

    public Double getMaxaPreQty() {
        return MaxaPreQty;
    }

    public void setMaxaPreQty(Double maxaPreQty) {
        MaxaPreQty = maxaPreQty;
    }

    public Promotion getWPromotion() {
        return WPromotion;
    }

    public void setWPromotion(Promotion WPromotion) {
        this.WPromotion = WPromotion;
    }

    public int getIsGift() {
        return IsGift;
    }

    public void setIsGift(int isGift) {
        IsGift = isGift;
    }

    public com.frxs.order.model.Product getProduct() {
        return Product;
    }

    public void setProduct(com.frxs.order.model.Product product) {
        Product = product;
    }

    public com.frxs.order.model.WProduct getWProduct() {
        return WProduct;
    }

    public void setWProduct(com.frxs.order.model.WProduct WProduct) {
        this.WProduct = WProduct;
    }

    public List<com.frxs.order.model.PictureDetails> getPictureDetails() {
        return PictureDetails;
    }

    public void setPictureDetails(List<com.frxs.order.model.PictureDetails> pictureDetails) {
        PictureDetails = pictureDetails;
    }

    public com.frxs.order.model.AttributePicture getAttributePicture() {
        return AttributePicture;
    }

    public void setAttributePicture(com.frxs.order.model.AttributePicture attributePicture) {
        AttributePicture = attributePicture;
    }

    public List<ProductAttribute> getProductAttributes() {
        return ProductAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        ProductAttributes = productAttributes;
    }

    public List<ProductsBarCode> getProductsBarCodes() {
        return ProductsBarCodes;
    }

    public void setProductsBarCodes(List<ProductsBarCode> productsBarCodes) {
        ProductsBarCodes = productsBarCodes;
    }
}
