package com.frxs.order.model;

import java.io.Serializable;

/**
 * Created by ewu on 2016/9/13.
 */
public class PromotionActivityGroupInfo implements Serializable {


    /**
     * GroupCode : A
     * ConditionQty : 2
     */

    private String GroupCode;
    private int ConditionQty;

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String GroupCode) {
        this.GroupCode = GroupCode;
    }

    public int getConditionQty() {
        return ConditionQty;
    }

    public void setConditionQty(int ConditionQty) {
        this.ConditionQty = ConditionQty;
    }
}
