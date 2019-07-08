package com.frxs.order.model;

/**
 * Created by Chentie on 2017/5/16.
 */

public class PostPreGood {

    private String BookOrderId;

    private String ID;

    private double Qty;

    private String Remark;

    public String getBookOrderId() {
        return BookOrderId;
    }

    public void setBookOrderId(String bookOrderId) {
        BookOrderId = bookOrderId;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        Qty = qty;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
