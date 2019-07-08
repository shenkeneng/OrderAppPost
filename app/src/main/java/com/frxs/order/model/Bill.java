package com.frxs.order.model;

import java.util.List;

/**
 * Created by Endoon on 2016/5/8.
 */
public class Bill {
    private int TotalRecords;//":90967,

    private List<AccountBill> ItemList;//:[

    public int getTotalRecords() {
        return TotalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        TotalRecords = totalRecords;
    }

    public List<AccountBill> getItemList() {
        return ItemList;
    }

    public void setItemList(List<AccountBill> itemList) {
        ItemList = itemList;
    }
}
