package com.frxs.order.model;

/**
 * Created by Endoon on 2016/5/16.
 */
public class SettleDetail {
    private String BillTypeStr;

    private double BillAddAmt;//: 9.6,

    private double BillPayAmt;//": 329.6,

    private String BillDate;

    private String BillID;

    public String getBillTypeStr() {
        return BillTypeStr;
    }

    public void setBillTypeStr(String billTypeStr) {
        BillTypeStr = billTypeStr;
    }

    public double getBillAddAmt() {
        return BillAddAmt;
    }

    public void setBillAddAmt(double billAddAmt) {
        BillAddAmt = billAddAmt;
    }

    public double getBillPayAmt() {
        return BillPayAmt;
    }

    public void setBillPayAmt(double billPayAmt) {
        BillPayAmt = billPayAmt;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getBillID() {
        return BillID;
    }

    public void setBillID(String billID) {
        BillID = billID;
    }
}
