package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ewu on 2016/4/29.
 */
public class WProductExt implements Serializable {

    @SerializedName("WProductID")
    private int WProductID;

    @SerializedName("ProductName")
    private String ProductName;

    private String WID;//:76505,// 仓库ID
    private String ProductId;//:76505,//总部商品ID
    private String ProductName2;//:"演示数据",// 仓库商品副标题(加入时，为空，由仓库自行定行, 商品详情如果仓库有时，显示自己的，没有时显示总部的)
    private int WStatus;//:76505,// 仓库商品状态(0:已移除1:正常;2:淘汰;3:冻结;) ;淘汰商品和冻结商品不能销售;加入或重新加入时为正常；移除后再加入时为正常
    private String Unit;//:"演示数据",// 库存单位
    private double SalePrice;//:100.23,// 库存单位销售价格
    private double BigPackingQty;//:76505,// 大(配送)单位包装数(冗余设计 选中配送单位时,同步该表,没有时该值为库存单位)
    private String BigUnit;//:"件",// 大(配送)单位(冗余设计 选中配送单位时,同步该表,没有时该值为1)
    private double ShopAddPerc;//:100.23,// 门店库存单位提点率(%)
    private double ShopPoint;//:100.23,// 门店库存单位积分
    private double BigSalePrice;//:100.23,// 配送销售总价（销售配送价 * 包装数）
    private String ImageUrl200x200;//:"http://api.com/imgs/ce1a1213f42348b299ffa2d094bd6483.jpg",
    private double BigShopPoint;//:100.23,// 配送积分兑物总价（销售配送价 * 包装数）
    private String SKU;//:"演示数据",
    private String BarCode;//:"695865326954",
    private double PromotionShopPoint;//:100.23// 促销积分
    private int IsGift;//促销商品标识符
    private Double MinPreQty;//最小起定量
    private Double MaxaPreQty;//最大起定量
    private int IsNoStock;//有无库存商品  0：无库存商品,1：无库存商品

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

    private String CategoryName3;

    private int CategoryId3;

    public int getIsGift() {
        return IsGift;
    }

    public void setIsGift(int isGift) {
        IsGift = isGift;
    }

    public int getWProductID() {
        return WProductID;
    }

    public void setWProductID(int WProductID) {
        this.WProductID = WProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName2() {
        return ProductName2;
    }

    public void setProductName2(String productName2) {
        ProductName2 = productName2;
    }

    public int getWStatus() {
        return WStatus;
    }

    public void setWStatus(int WStatus) {
        this.WStatus = WStatus;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public double getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(double salePrice) {
        SalePrice = salePrice;
    }

    public double getBigPackingQty() {
        return BigPackingQty;
    }

    public void setBigPackingQty(double bigPackingQty) {
        BigPackingQty = bigPackingQty;
    }

    public String getBigUnit() {
        return BigUnit;
    }

    public void setBigUnit(String bigUnit) {
        BigUnit = bigUnit;
    }

    public double getShopAddPerc() {
        return ShopAddPerc;
    }

    public void setShopAddPerc(double shopAddPerc) {
        ShopAddPerc = shopAddPerc;
    }

    public double getShopPoint() {
        return ShopPoint;
    }

    public void setShopPoint(double shopPoint) {
        ShopPoint = shopPoint;
    }

    public double getBigSalePrice() {
        return BigSalePrice;
    }

    public void setBigSalePrice(double bigSalePrice) {
        BigSalePrice = bigSalePrice;
    }

    public double getBigShopPoint() {
        return BigShopPoint;
    }

    public void setBigShopPoint(double bigShopPoint) {
        BigShopPoint = bigShopPoint;
    }

    public String getImageUrl200x200() {
        return ImageUrl200x200;
    }

    public void setImageUrl200x200(String imageUrl200x200) {
        ImageUrl200x200 = imageUrl200x200;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getCategoryName3() {
        return CategoryName3;
    }

    public void setCategoryName3(String categoryName3) {
        CategoryName3 = categoryName3;
    }

    public int getCategoryId3() {
        return CategoryId3;
    }

    public void setCategoryId3(int categoryId3) {
        CategoryId3 = categoryId3;
    }

    public double getProPoints() {
        if (PromotionShopPoint > 0) {
            return PromotionShopPoint;
        } else {
            return BigShopPoint;
        }
    }

    public int getIsNoStock() {
        return IsNoStock;
    }

    public void setIsNoStock(int isNoStock) {
        IsNoStock = isNoStock;
    }
}
