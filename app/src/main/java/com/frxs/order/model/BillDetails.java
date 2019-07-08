package com.frxs.order.model;

import java.util.List;

/**
 * Created by Endoon on 2016/5/16.
 */
public class BillDetails {
    private SaleSettle SaleSettle;
    private List<SettleDetail> SaleSettleDetailList;

    public SaleSettle getSaleSettle() {
        return SaleSettle;
    }

    public void setSaleSettle(SaleSettle saleSettle) {
        SaleSettle = saleSettle;
    }

    public List<SettleDetail> getSaleSettleDetailList() {
        return SaleSettleDetailList;
    }

    public void setSaleSettleDetailList(List<SettleDetail> saleSettleDetailList) {
        SaleSettleDetailList = saleSettleDetailList;
    }
}
