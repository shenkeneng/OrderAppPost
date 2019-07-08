package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/5/4.
 */
public class ProductWProductsGetToB2BRespData implements Serializable {

    @SerializedName("TotalRecords")
    private int TotalRecords;

    @SerializedName("ItemList")
    private List<WProductExt> ItemList;

    public int getTotalRecords() {
        return TotalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        TotalRecords = totalRecords;
    }

    public List<WProductExt> getItemList() {
        return ItemList;
    }

    public void setItemList(List<WProductExt> itemList) {
        ItemList = itemList;
    }
}
