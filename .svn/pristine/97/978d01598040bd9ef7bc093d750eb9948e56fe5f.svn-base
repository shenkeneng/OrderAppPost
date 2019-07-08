package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Endoon on 2016/5/9.
 */
public class WarehouseLine implements Serializable{
    /**
     * ListSendTypeCode : [1]
     * OrderEndTime : 00:00:00
     * SendMode : 1
     */

    private String OrderEndTime;// 订单确认时间
    private int SendMode;// 0:单双日  1:星期
    private List<Integer> ListSendTypeCode;// 配送周期（1-7：周一至周日，8：单日，9：双日）
    private String ShippingRemark; //订单确认及到货时间说明

    public String getOrderEndTime() {
        return OrderEndTime;
    }

    public void setOrderEndTime(String OrderEndTime) {
        this.OrderEndTime = OrderEndTime;
    }

    public int getSendMode() {
        return SendMode;
    }

    public void setSendMode(int SendMode) {
        this.SendMode = SendMode;
    }

    public List<Integer> getListSendTypeCode() {
        return ListSendTypeCode;
    }

    public void setListSendTypeCode(List<Integer> ListSendTypeCode) {
        this.ListSendTypeCode = ListSendTypeCode;
    }

    public String getShippingRemark() {
        return ShippingRemark;
    }

    public void setShippingRemark(String shippingRemark) {
        ShippingRemark = shippingRemark;
    }
}
