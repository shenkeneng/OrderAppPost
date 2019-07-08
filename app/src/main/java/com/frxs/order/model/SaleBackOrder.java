package com.frxs.order.model;

import java.util.List;

/**
 * Created by shenpei on 2017/6/15.
 * 退货单
 */

public class SaleBackOrder {

    private double TotalBackAmt;
    private double TotalBackAmtPage;
    private int TotalRecords;
    private List<SaleBackOrderList> ItemList;

    public double getTotalBackAmt() {
        return TotalBackAmt;
    }

    public void setTotalBackAmt(double TotalBackAmt) {
        this.TotalBackAmt = TotalBackAmt;
    }

    public double getTotalBackAmtPage() {
        return TotalBackAmtPage;
    }

    public void setTotalBackAmtPage(double TotalBackAmtPage) {
        this.TotalBackAmtPage = TotalBackAmtPage;
    }

    public int getTotalRecords() {
        return TotalRecords;
    }

    public void setTotalRecords(int TotalRecords) {
        this.TotalRecords = TotalRecords;
    }

    public List<SaleBackOrderList> getItemList() {
        return ItemList;
    }

    public void setItemList(List<SaleBackOrderList> ItemList) {
        this.ItemList = ItemList;
    }

}
