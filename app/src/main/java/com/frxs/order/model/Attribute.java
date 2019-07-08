package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/5/6.
 */
public class Attribute implements Serializable {

    @SerializedName("AttributeId")
    private int AttributeId;

    @SerializedName("AttributeName")
    private String AttributeName;

    @SerializedName("Values")
    private List<AttributeValue> Values;

    public int getAttributeId() {
        return AttributeId;
    }

    public void setAttributeId(int attributeId) {
        AttributeId = attributeId;
    }

    public String getAttributeName() {
        return AttributeName;
    }

    public void setAttributeName(String attributeName) {
        AttributeName = attributeName;
    }

    public List<AttributeValue> getValues() {
        return Values;
    }

    public void setValues(List<AttributeValue> values) {
        Values = values;
    }
}
