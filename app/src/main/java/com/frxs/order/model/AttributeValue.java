package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ewu on 2016/5/6.
 */
public class AttributeValue implements Serializable {

    @SerializedName("ValuesId")
    private int ValuesId;

    @SerializedName("ValueStr")
    private String ValueStr;

    public int getValuesId() {
        return ValuesId;
    }

    public void setValuesId(int valuesId) {
        ValuesId = valuesId;
    }

    public String getValueStr() {
        return ValueStr;
    }

    public void setValueStr(String valueStr) {
        ValueStr = valueStr;
    }
}
