package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chentie on 2016/12/12.
 */
public class WXNoPayQuery {

    /**
     * PayOutTime : 24.0
     * RedirectType : 2
     * SettleDetailList : [{"BillID":"200000001425","BillPayAmt":513.53,"BillTypeStr":"订单"}]
     * SettleInfo : {"SettleAmt":514,"SettleID":"200000000617"}
     * UserId : 0
     */

    private double PayOutTime;
    private int RedirectType;
    /**
     * SettleAmt : 514.0
     * SettleID : 200000000617
     */

    private SettleInfoBean SettleInfo;
    private int UserId;
    private double ShouldPayAmt;// 应付合计
    /**
     * BillID : 200000001425
     * BillPayAmt : 513.53
     * BillTypeStr : 订单
     */

    private List<SettleListBean> SettleList; //开启对账单时 返回的账单列表
    private List<SettleDetailListBean> SettleDetailList;//关闭对账单时 返回的账单列表

    public double getPayOutTime() {
        return PayOutTime;
    }

    public void setPayOutTime(double PayOutTime) {
        this.PayOutTime = PayOutTime;
    }

    public int getRedirectType() {
        return RedirectType;
    }

    public void setRedirectType(int RedirectType) {
        this.RedirectType = RedirectType;
    }

    public SettleInfoBean getSettleInfo() {
        return SettleInfo;
    }

    public void setSettleInfo(SettleInfoBean SettleInfo) {
        this.SettleInfo = SettleInfo;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public double getShouldPayAmt() {
        return ShouldPayAmt;
    }

    public void setShouldPayAmt(double shouldPayAmt) {
        ShouldPayAmt = shouldPayAmt;
    }

    public List<SettleDetailListBean> getSettleDetailList() {
        return SettleDetailList;
    }

    public void setSettleDetailList(List<SettleDetailListBean> SettleDetailList) {
        this.SettleDetailList = SettleDetailList;
    }

    public List<SettleListBean> getSettleList() {
        return SettleList;
    }

    public void setSettleList(List<SettleListBean> settleList) {
        SettleList = settleList;
    }

    public static class SettleInfoBean {
        private double SettleAmt;
        private String SettleID;

        public double getSettleAmt() {
            return SettleAmt;
        }

        public void setSettleAmt(double SettleAmt) {
            this.SettleAmt = SettleAmt;
        }

        public String getSettleID() {
            return SettleID;
        }

        public void setSettleID(String SettleID) {
            this.SettleID = SettleID;
        }
    }

    public static class SettleDetailListBean {
        private String BillID;
        private double BillPayAmt;
        private String BillTypeStr;

        public String getBillID() {
            return BillID;
        }

        public void setBillID(String BillID) {
            this.BillID = BillID;
        }

        public double getBillPayAmt() {
            return BillPayAmt;
        }

        public void setBillPayAmt(double BillPayAmt) {
            this.BillPayAmt = BillPayAmt;
        }

        public String getBillTypeStr() {
            return BillTypeStr;
        }

        public void setBillTypeStr(String BillTypeStr) {
            this.BillTypeStr = BillTypeStr;
        }
    }

    public static class SettleListBean {
        private long CusVoucherID;
        private double SettleAmt;
        private String SettleID;
        private int Ref_BillType;// 账单类型 0：结算单   1：付款单

        public long getCusVoucherID() {
            return CusVoucherID;
        }

        public void setCusVoucherID(long cusVoucherID) {
            CusVoucherID = cusVoucherID;
        }

        public double getSettleAmt() {
            return SettleAmt;
        }

        public void setSettleAmt(double settleAmt) {
            SettleAmt = settleAmt;
        }

        public String getSettleID() {
            return SettleID;
        }

        public void setSettleID(String settleID) {
            SettleID = settleID;
        }

        public int getRef_BillType() {
            return Ref_BillType;
        }

        public void setRef_BillType(int ref_BillType) {
            Ref_BillType = ref_BillType;
        }
    }
}
