package com.frxs.order.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chentie on 2017/6/15.
 */

public class ApplySaleBackInfo implements Serializable{

    private OrderBean Order;//订单信息
    private List<TracksBean> Tracks;//订单流程
    private List<DetailBean> Detail;//商品详情

    public OrderBean getOrder() {
        return Order;
    }

    public void setOrder(OrderBean Order) {
        this.Order = Order;
    }

    public List<TracksBean> getTracks() {
        return Tracks;
    }

    public void setTracks(List<TracksBean> Tracks) {
        this.Tracks = Tracks;
    }

    public List<DetailBean> getDetail() {
        return Detail;
    }

    public void setDetail(List<DetailBean> Detail) {
        this.Detail = Detail;
    }

    public static class OrderBean implements Serializable{
        private String ApplyBackID;
        private String BackDate;
        private String ConfTime;
        private int ConfUserID;
        private String ConfUserName;
        private String CreateTime;
        private int CreateUserID;
        private String CreateUserName;
        private String ModifyTime;
        private int ModifyUserID;
        private String ModifyUserName;
        private double PayAmount;//新增退货申请单总金额
        private String ReceivedUserName;
        private String Remark;
        private String ShopCode;
        private int ShopID;
        private String ShopName;
        private String StartReceiveUserName;
        private int Status;
        private String StatusName;
        private String SubWCode;
        private int SubWID;
        private String SubWName;
        private int TakeBackCarOwnerType;
        private String TakeBackTime;
        private double TakeBackTotalQty = -1;
        private int TakeBackUserID;
        private String TakeBackUserName;
        private double TotalAddAmt;
        private double TotalBackAmt;
        private double TotalBackQty;//新增退货申请单总数量
        private double TotalBackTotalAmt = -1;
        private double TotalBasePoint;
        private String WCode;
        private int WID;
        private String WName;
        private double TotalPoint;//退货申请单退货积分

        public String getApplyBackID() {
            return ApplyBackID;
        }

        public void setApplyBackID(String ApplyBackID) {
            this.ApplyBackID = ApplyBackID;
        }

        public String getBackDate() {
            return BackDate;
        }

        public void setBackDate(String BackDate) {
            this.BackDate = BackDate;
        }

        public String getConfTime() {
            return ConfTime;
        }

        public void setConfTime(String ConfTime) {
            this.ConfTime = ConfTime;
        }

        public int getConfUserID() {
            return ConfUserID;
        }

        public void setConfUserID(int ConfUserID) {
            this.ConfUserID = ConfUserID;
        }

        public String getConfUserName() {
            return ConfUserName;
        }

        public void setConfUserName(String ConfUserName) {
            this.ConfUserName = ConfUserName;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public int getCreateUserID() {
            return CreateUserID;
        }

        public void setCreateUserID(int CreateUserID) {
            this.CreateUserID = CreateUserID;
        }

        public String getCreateUserName() {
            return CreateUserName;
        }

        public void setCreateUserName(String CreateUserName) {
            this.CreateUserName = CreateUserName;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public int getModifyUserID() {
            return ModifyUserID;
        }

        public void setModifyUserID(int ModifyUserID) {
            this.ModifyUserID = ModifyUserID;
        }

        public String getModifyUserName() {
            return ModifyUserName;
        }

        public void setModifyUserName(String ModifyUserName) {
            this.ModifyUserName = ModifyUserName;
        }

        public double getPayAmount() {
            return PayAmount;
        }

        public void setPayAmount(double PayAmount) {
            this.PayAmount = PayAmount;
        }

        public String getReceivedUserName() {
            return ReceivedUserName;
        }

        public void setReceivedUserName(String ReceivedUserName) {
            this.ReceivedUserName = ReceivedUserName;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getShopCode() {
            return ShopCode;
        }

        public void setShopCode(String ShopCode) {
            this.ShopCode = ShopCode;
        }

        public int getShopID() {
            return ShopID;
        }

        public void setShopID(int ShopID) {
            this.ShopID = ShopID;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getStartReceiveUserName() {
            return StartReceiveUserName;
        }

        public void setStartReceiveUserName(String StartReceiveUserName) {
            this.StartReceiveUserName = StartReceiveUserName;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public String getStatusName() {
            return StatusName;
        }

        public void setStatusName(String StatusName) {
            this.StatusName = StatusName;
        }

        public String getSubWCode() {
            return SubWCode;
        }

        public void setSubWCode(String SubWCode) {
            this.SubWCode = SubWCode;
        }

        public int getSubWID() {
            return SubWID;
        }

        public void setSubWID(int SubWID) {
            this.SubWID = SubWID;
        }

        public String getSubWName() {
            return SubWName;
        }

        public void setSubWName(String SubWName) {
            this.SubWName = SubWName;
        }

        public int getTakeBackCarOwnerType() {
            return TakeBackCarOwnerType;
        }

        public void setTakeBackCarOwnerType(int TakeBackCarOwnerType) {
            this.TakeBackCarOwnerType = TakeBackCarOwnerType;
        }

        public String getTakeBackTime() {
            return TakeBackTime;
        }

        public void setTakeBackTime(String TakeBackTime) {
            this.TakeBackTime = TakeBackTime;
        }

        public double getTakeBackTotalQty() {
            return TakeBackTotalQty;
        }

        public void setTakeBackTotalQty(double TakeBackTotalQty) {
            this.TakeBackTotalQty = TakeBackTotalQty;
        }

        public int getTakeBackUserID() {
            return TakeBackUserID;
        }

        public void setTakeBackUserID(int TakeBackUserID) {
            this.TakeBackUserID = TakeBackUserID;
        }

        public String getTakeBackUserName() {
            return TakeBackUserName;
        }

        public void setTakeBackUserName(String TakeBackUserName) {
            this.TakeBackUserName = TakeBackUserName;
        }

        public double getTotalAddAmt() {
            return TotalAddAmt;
        }

        public void setTotalAddAmt(double TotalAddAmt) {
            this.TotalAddAmt = TotalAddAmt;
        }

        public double getTotalBackAmt() {
            return TotalBackAmt;
        }

        public void setTotalBackAmt(double TotalBackAmt) {
            this.TotalBackAmt = TotalBackAmt;
        }

        public double getTotalBackQty() {
            return TotalBackQty;
        }

        public void setTotalBackQty(double TotalBackQty) {
            this.TotalBackQty = TotalBackQty;
        }

        public double getTotalBackTotalAmt() {
            return TotalBackTotalAmt;
        }

        public void setTotalBackTotalAmt(double TotalBackTotalAmt) {
            this.TotalBackTotalAmt = TotalBackTotalAmt;
        }

        public double getTotalBasePoint() {
            return TotalBasePoint;
        }

        public void setTotalBasePoint(double TotalBasePoint) {
            this.TotalBasePoint = TotalBasePoint;
        }

        public String getWCode() {
            return WCode;
        }

        public void setWCode(String WCode) {
            this.WCode = WCode;
        }

        public int getWID() {
            return WID;
        }

        public void setWID(int WID) {
            this.WID = WID;
        }

        public String getWName() {
            return WName;
        }

        public void setWName(String WName) {
            this.WName = WName;
        }

        public double getTotalPoint() {
            return TotalPoint;
        }

        public void setTotalPoint(double totalPoint) {
            TotalPoint = totalPoint;
        }
    }

    public static class TracksBean implements Serializable{

        private String ID;
        private String ApplyBackID;
        private int WID;
        private String Remark;
        private int IsDisplayUser;
        private int ApplyStatus;
        private String ApplyStatusName;
        private String CreateTime;
        private int CreateUserID;
        private String CreateUserName;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getApplyBackID() {
            return ApplyBackID;
        }

        public void setApplyBackID(String ApplyBackID) {
            this.ApplyBackID = ApplyBackID;
        }

        public int getWID() {
            return WID;
        }

        public void setWID(int WID) {
            this.WID = WID;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public int getIsDisplayUser() {
            return IsDisplayUser;
        }

        public void setIsDisplayUser(int IsDisplayUser) {
            this.IsDisplayUser = IsDisplayUser;
        }

        public int getApplyStatus() {
            return ApplyStatus;
        }

        public void setApplyStatus(int ApplyStatus) {
            this.ApplyStatus = ApplyStatus;
        }

        public String getApplyStatusName() {
            return ApplyStatusName;
        }

        public void setApplyStatusName(String ApplyStatusName) {
            this.ApplyStatusName = ApplyStatusName;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public int getCreateUserID() {
            return CreateUserID;
        }

        public void setCreateUserID(int CreateUserID) {
            this.CreateUserID = CreateUserID;
        }

        public String getCreateUserName() {
            return CreateUserName;
        }

        public void setCreateUserName(String CreateUserName) {
            this.CreateUserName = CreateUserName;
        }
    }

    public static class DetailBean  implements Serializable{

        private String ApplyBackID;
        private double BackPackingQty;
        private double BackPrice;
        private double BackQty;// 退货申请单商品退货数量
        private String BackReasonCode;
        private String BackReasonDes;
        private String BackReasonName;
        private String BackUnit;
        private String BarCode;
        private double BasePoint;
        private double ConfBackPackingQty;
        private double ConfBackQty;
        private String ConfBackUnit;
        private String ConfRemark;
        private int ConfStatus;
        private String ConfStatusName;
        private String ConfTime;
        private int ConfUserID;
        private String ConfUserName;
        private String ID;
        private String ModifyTime;
        private int ModifyUserID;
        private String ModifyUserName;
        private int ProductId;
        private String ProductImageUrl200;
        private String ProductImageUrl400;
        private String ProductName;
        private String SKU;
        private int SerialNumber;
        private double ShopAddPerc;
        private String ShopCode;
        private int ShopID;
        private String ShopName;
        private String Spec;
        private int Status;
        private String StatusName;
        private double SubAddAmt;
        private double SubAmt;
        private double SubBasePoint;
        private double SubVendor1Amt;
        private double SubVendor2Amt;
        private int TakeBackCarOwnerType;
        private double TakeBackPackingQty;
        private double TakeBackQty;// 退货申请单单个商品总数量
        private String TakeBackTime;
        private String TakeBackUnit;
        private int TakeBackUserID;
        private String TakeBackUserName;
        private String Unit;
        private double UnitPrice;
        private double UnitQty;
        private double VendorPerc1;
        private double VendorPerc2;
        private int WID;
        private double SalePrice;// 商品配送价
        private double SubPoint;// 商品小计退货积分

        public String getApplyBackID() {
            return ApplyBackID;
        }

        public void setApplyBackID(String ApplyBackID) {
            this.ApplyBackID = ApplyBackID;
        }

        public double getBackPackingQty() {
            return BackPackingQty;
        }

        public void setBackPackingQty(double BackPackingQty) {
            this.BackPackingQty = BackPackingQty;
        }

        public double getBackPrice() {
            return BackPrice;
        }

        public void setBackPrice(double BackPrice) {
            this.BackPrice = BackPrice;
        }

        public double getBackQty() {
            return BackQty;
        }

        public void setBackQty(double BackQty) {
            this.BackQty = BackQty;
        }

        public String getBackReasonCode() {
            return BackReasonCode;
        }

        public void setBackReasonCode(String BackReasonCode) {
            this.BackReasonCode = BackReasonCode;
        }

        public String getBackReasonDes() {
            return BackReasonDes;
        }

        public void setBackReasonDes(String BackReasonDes) {
            this.BackReasonDes = BackReasonDes;
        }

        public String getBackReasonName() {
            return BackReasonName;
        }

        public void setBackReasonName(String BackReasonName) {
            this.BackReasonName = BackReasonName;
        }

        public String getBackUnit() {
            return BackUnit;
        }

        public void setBackUnit(String BackUnit) {
            this.BackUnit = BackUnit;
        }

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String BarCode) {
            this.BarCode = BarCode;
        }

        public double getBasePoint() {
            return BasePoint;
        }

        public void setBasePoint(double BasePoint) {
            this.BasePoint = BasePoint;
        }

        public double getConfBackPackingQty() {
            return ConfBackPackingQty;
        }

        public void setConfBackPackingQty(double ConfBackPackingQty) {
            this.ConfBackPackingQty = ConfBackPackingQty;
        }

        public double getConfBackQty() {
            return ConfBackQty;
        }

        public void setConfBackQty(double ConfBackQty) {
            this.ConfBackQty = ConfBackQty;
        }

        public String getConfBackUnit() {
            return ConfBackUnit;
        }

        public void setConfBackUnit(String ConfBackUnit) {
            this.ConfBackUnit = ConfBackUnit;
        }

        public String getConfRemark() {
            return ConfRemark;
        }

        public void setConfRemark(String ConfRemark) {
            this.ConfRemark = ConfRemark;
        }

        public int getConfStatus() {
            return ConfStatus;
        }

        public void setConfStatus(int ConfStatus) {
            this.ConfStatus = ConfStatus;
        }

        public String getConfStatusName() {
            return ConfStatusName;
        }

        public void setConfStatusName(String ConfStatusName) {
            this.ConfStatusName = ConfStatusName;
        }

        public String getConfTime() {
            return ConfTime;
        }

        public void setConfTime(String ConfTime) {
            this.ConfTime = ConfTime;
        }

        public int getConfUserID() {
            return ConfUserID;
        }

        public void setConfUserID(int ConfUserID) {
            this.ConfUserID = ConfUserID;
        }

        public String getConfUserName() {
            return ConfUserName;
        }

        public void setConfUserName(String ConfUserName) {
            this.ConfUserName = ConfUserName;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public int getModifyUserID() {
            return ModifyUserID;
        }

        public void setModifyUserID(int ModifyUserID) {
            this.ModifyUserID = ModifyUserID;
        }

        public String getModifyUserName() {
            return ModifyUserName;
        }

        public void setModifyUserName(String ModifyUserName) {
            this.ModifyUserName = ModifyUserName;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public String getProductImageUrl200() {
            return ProductImageUrl200;
        }

        public void setProductImageUrl200(String ProductImageUrl200) {
            this.ProductImageUrl200 = ProductImageUrl200;
        }

        public String getProductImageUrl400() {
            return ProductImageUrl400;
        }

        public void setProductImageUrl400(String ProductImageUrl400) {
            this.ProductImageUrl400 = ProductImageUrl400;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getSKU() {
            return SKU;
        }

        public void setSKU(String SKU) {
            this.SKU = SKU;
        }

        public int getSerialNumber() {
            return SerialNumber;
        }

        public void setSerialNumber(int SerialNumber) {
            this.SerialNumber = SerialNumber;
        }

        public double getShopAddPerc() {
            return ShopAddPerc;
        }

        public void setShopAddPerc(double ShopAddPerc) {
            this.ShopAddPerc = ShopAddPerc;
        }

        public String getShopCode() {
            return ShopCode;
        }

        public void setShopCode(String ShopCode) {
            this.ShopCode = ShopCode;
        }

        public int getShopID() {
            return ShopID;
        }

        public void setShopID(int ShopID) {
            this.ShopID = ShopID;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getSpec() {
            return Spec;
        }

        public void setSpec(String Spec) {
            this.Spec = Spec;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public String getStatusName() {
            return StatusName;
        }

        public void setStatusName(String StatusName) {
            this.StatusName = StatusName;
        }

        public double getSubAddAmt() {
            return SubAddAmt;
        }

        public void setSubAddAmt(double SubAddAmt) {
            this.SubAddAmt = SubAddAmt;
        }

        public double getSubAmt() {
            return SubAmt;
        }

        public void setSubAmt(double SubAmt) {
            this.SubAmt = SubAmt;
        }

        public double getSubBasePoint() {
            return SubBasePoint;
        }

        public void setSubBasePoint(double SubBasePoint) {
            this.SubBasePoint = SubBasePoint;
        }

        public double getSubVendor1Amt() {
            return SubVendor1Amt;
        }

        public void setSubVendor1Amt(double SubVendor1Amt) {
            this.SubVendor1Amt = SubVendor1Amt;
        }

        public double getSubVendor2Amt() {
            return SubVendor2Amt;
        }

        public void setSubVendor2Amt(double SubVendor2Amt) {
            this.SubVendor2Amt = SubVendor2Amt;
        }

        public int getTakeBackCarOwnerType() {
            return TakeBackCarOwnerType;
        }

        public void setTakeBackCarOwnerType(int TakeBackCarOwnerType) {
            this.TakeBackCarOwnerType = TakeBackCarOwnerType;
        }

        public double getTakeBackPackingQty() {
            return TakeBackPackingQty;
        }

        public void setTakeBackPackingQty(double TakeBackPackingQty) {
            this.TakeBackPackingQty = TakeBackPackingQty;
        }

        public double getTakeBackQty() {
            return TakeBackQty;
        }

        public void setTakeBackQty(double TakeBackQty) {
            this.TakeBackQty = TakeBackQty;
        }

        public String getTakeBackTime() {
            return TakeBackTime;
        }

        public void setTakeBackTime(String TakeBackTime) {
            this.TakeBackTime = TakeBackTime;
        }

        public String getTakeBackUnit() {
            return TakeBackUnit;
        }

        public void setTakeBackUnit(String TakeBackUnit) {
            this.TakeBackUnit = TakeBackUnit;
        }

        public int getTakeBackUserID() {
            return TakeBackUserID;
        }

        public void setTakeBackUserID(int TakeBackUserID) {
            this.TakeBackUserID = TakeBackUserID;
        }

        public String getTakeBackUserName() {
            return TakeBackUserName;
        }

        public void setTakeBackUserName(String TakeBackUserName) {
            this.TakeBackUserName = TakeBackUserName;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }

        public double getUnitPrice() {
            return UnitPrice;
        }

        public void setUnitPrice(double UnitPrice) {
            this.UnitPrice = UnitPrice;
        }

        public double getUnitQty() {
            return UnitQty;
        }

        public void setUnitQty(double UnitQty) {
            this.UnitQty = UnitQty;
        }

        public double getVendorPerc1() {
            return VendorPerc1;
        }

        public void setVendorPerc1(double VendorPerc1) {
            this.VendorPerc1 = VendorPerc1;
        }

        public double getVendorPerc2() {
            return VendorPerc2;
        }

        public void setVendorPerc2(double VendorPerc2) {
            this.VendorPerc2 = VendorPerc2;
        }

        public int getWID() {
            return WID;
        }

        public void setWID(int WID) {
            this.WID = WID;
        }

        public double getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(double salePrice) {
            SalePrice = salePrice;
        }

        public double getSubPoint() {
            return SubPoint;
        }

        public void setSubPoint(double subPoint) {
            SubPoint = subPoint;
        }
    }
}
