package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情 By Tiepier
 */

public class OrderDetail implements Serializable {

    private static final long serialVersionUID = -7137669026034051920L;

    private String OrderId; // 订单编号

    private String OrderDate; // 订单时间

    private String Telephone; // 订单提交：客服电话 ；订单详情：收货人电话

    private String OrderType; // 订单类型 1（1小时达），2（预订送达 DeliveryTime：预订送达时间）

    private String DeliveryTime; // 预定送达开始时间

    private String DeliveryEndTime;//预定送达结束时间

    private boolean IsComplain; // 是否可投诉（ture、可投诉 false、不可投诉）

    private List<OrderTrack> OrderTrack;// 订单跟踪

    private int OrderStatus; // 订单状态 1:等待确认; 11:正在拣货;  15:等待配送; 2:正在配送;
    // 3: 交易完成;4:已关闭; 5:已拒收

    private String OrderStatusName; // 订单状态中文名字

    private String ShipTo;// 收货人

    private double OrderTotal; // 订单金额（商品金额+运费）

    private String ShippingRegion;

    private String Address; // //街道地址

    private double FreeSum; // 订单优惠金额

    private int CouponNumId; // 优惠券ID

    private double CouponAmount; // 优惠劵金额

    private String Remark; // 备注

    private int ComplainState; // 投诉状态（相对于已投诉 1、投诉处理中 2、投诉完成）

    private double Freight; // 邮费

    private double AdjustedFreight;//夜间配送费

    private List<OrderItemsIList> OrderItemsIList;

    private int OrderComplain; // 是否已投诉（0、未投诉 大于0、已投诉）

    private int IsCanPay;// 是否能继续付款（1：是、0：否）

    private double Amount;// 商品总额

    public double getAdjustedFreight() {
        return AdjustedFreight;
    }

    public void setAdjustedFreight(double adjustedFreight) {
        AdjustedFreight = adjustedFreight;
    }

    public String getDeliveryEndTime() {
        return DeliveryEndTime;
    }

    public void setDeliveryEndTime(String deliveryEndTime) {
        DeliveryEndTime = deliveryEndTime;
    }

    public double getAmount() {
        return Amount;
    }


    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getShipTo() {
        return ShipTo;
    }

    public void setShipTo(String shipTo) {
        ShipTo = shipTo;
    }

    public double getOrderTotal() {
        return OrderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        OrderTotal = orderTotal;
    }

    public String getOrderStatusName() {
        return OrderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        OrderStatusName = orderStatusName;
    }

    public int getCouponNumId() {
        return CouponNumId;
    }

    public void setCouponNumId(int couponNumId) {
        CouponNumId = couponNumId;
    }

    public List<OrderTrack> getOrderTrack() {
        return OrderTrack;
    }

    public void setOrderTrack(List<OrderTrack> orderTrack) {
        OrderTrack = orderTrack;
    }

    public int getOrderComplain() {
        return OrderComplain;
    }

    public void setOrderComplain(int orderComplain) {
        OrderComplain = orderComplain;
    }

    public List<OrderItemsIList> getOrderItemsIList() {
        return OrderItemsIList;
    }

    public void setOrderItemsIList(List<OrderItemsIList> orderItemsIList) {
        OrderItemsIList = orderItemsIList;
    }

    public double getFreight() {
        return Freight;
    }

    public void setFreight(double freight) {
        Freight = freight;
    }

    public int getComplainState() {
        return ComplainState;
    }

    public void setComplainState(int complainState) {
        ComplainState = complainState;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public double getFreeSum() {
        return FreeSum;
    }

    public void setFreeSum(double freeSum) {
        FreeSum = freeSum;
    }

    public double getCouponAmount() {
        return CouponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        CouponAmount = couponAmount;
    }

    public String getShippingRegion() {
        return ShippingRegion;
    }

    public void setShippingRegion(String shippingRegion) {
        ShippingRegion = shippingRegion;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public boolean isIsComplain() {
        return IsComplain;
    }

    public void setIsComplain(boolean isComplain) {
        IsComplain = isComplain;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public int getIsCanPay() {
        return IsCanPay;
    }

    public void setIsCanPay(int isCanPay) {
        IsCanPay = isCanPay;
    }

    public class OrderItemsIList implements Serializable {
        private static final long serialVersionUID = -5827226849521315366L;

        private int KId;// 门店商品表编号

        private String OrderId; // 订单编号

        private int ProductId; // 商品编号

        private String SKU; // sku编号

        private String ItemDescription;

        private String ThumbnailsUrl; // 缩略图地址

        private double ItemListPrice;// 商品原价

        private double ItemAdjustedPrice;// 商品购买价

        private int Quantity; // 数量

        public int getKId() {
            return KId;
        }

        public void setKId(int kId) {
            KId = kId;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public String getSKU() {
            return SKU;
        }

        public void setSKU(String sKU) {
            SKU = sKU;
        }

        public String getItemDescription() {
            return ItemDescription;
        }

        public void setItemDescription(String itemDescription) {
            ItemDescription = itemDescription;
        }

        public String getThumbnailsUrl() {
            return ThumbnailsUrl;
        }

        public void setThumbnailsUrl(String thumbnailsUrl) {
            ThumbnailsUrl = thumbnailsUrl;
        }

        public double getItemListPrice() {
            return ItemListPrice;
        }

        public void setItemListPrice(double itemListPrice) {
            ItemListPrice = itemListPrice;
        }

        public double getItemAdjustedPrice() {
            return ItemAdjustedPrice;
        }

        public void setItemAdjustedPrice(double itemAdjustedPrice) {
            ItemAdjustedPrice = itemAdjustedPrice;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }

    }
}
