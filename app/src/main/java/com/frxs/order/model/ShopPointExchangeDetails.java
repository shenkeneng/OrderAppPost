package com.frxs.order.model;

import java.util.List;

/**
 * Created by Chentie on 2017/3/13.
 */

public class ShopPointExchangeDetails {

    private int Total;
    private List<ItemListBean> ItemList;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<ItemListBean> getItemList() {
        return ItemList;
    }

    public void setItemList(List<ItemListBean> ItemList) {
        this.ItemList = ItemList;
    }

    public static class ItemListBean {

        private double ExchAmt;// 已兑金额
        private String PostingTime;//时间
        private String Remark;// 备注
        private int SettleFlag;// 积分状态
        private String ExchEndTime;// 有效时间
        private double ExchPoint;// 积分

        public double getExchAmt() {
            return ExchAmt;
        }

        public void setExchAmt(double ExchAmt) {
            this.ExchAmt = ExchAmt;
        }

        public String getPostingTime() {
            return PostingTime;
        }

        public void setPostingTime(String PostingTime) {
            this.PostingTime = PostingTime;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public int getSettleFlag() {
            return SettleFlag;
        }

        public void setSettleFlag(int SettleFlag) {
            this.SettleFlag = SettleFlag;
        }

        public String getExchEndTime() {
            return ExchEndTime;
        }

        public void setExchEndTime(String exchEndTime) {
            ExchEndTime = exchEndTime;
        }

        public double getExchPoint() {
            return ExchPoint;
        }

        public void setExchPoint(double exchPoint) {
            ExchPoint = exchPoint;
        }
    }
}
