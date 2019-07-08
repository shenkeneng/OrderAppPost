package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/5/4.
 */
public class WarehouseMessageShopGetListRespData implements Serializable {

    @SerializedName("ItemList")
    private List<WarehouseMessage> ItemList;

    public List<WarehouseMessage> getItemList() {
        return ItemList;
    }

    public void setItemList(List<WarehouseMessage> itemList) {
        ItemList = itemList;
    }
}
