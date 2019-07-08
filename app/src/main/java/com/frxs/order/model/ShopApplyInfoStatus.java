package com.frxs.order.model;

import java.io.Serializable;

/**
 * Created by Chentie on 2017/6/20.
 */

public class ShopApplyInfoStatus implements Serializable{

    private int ApplyStatus4ShopInfo;// 门店信息 状态(0:待审核,1:通过审核;2:驳回审核)
    private int ApplyStatus4LicenseInfo;// 证照信息 状态(0:待审核,1:通过审核;2:驳回审核)
    private int ApplyStatus4AccountInfo;// 帐号信息 状态(0:待审核,1:通过审核;2:驳回审核)
    private int BankInfoPassedFlag;// 0:不显示修改银行卡页面 1:则显示（账户信息的修改页面的银行卡信息（3个字段）做只读效果）
    private String BankAccount;// 银行账号
    private String BankAccountName;// 开户名
    private String BankType;// 开启行
    private String LegalPerson;// 企业法人

    public int getApplyStatus4ShopInfo() {
        return ApplyStatus4ShopInfo;
    }

    public void setApplyStatus4ShopInfo(int ApplyStatus4ShopInfo) {
        this.ApplyStatus4ShopInfo = ApplyStatus4ShopInfo;
    }

    public int getApplyStatus4LicenseInfo() {
        return ApplyStatus4LicenseInfo;
    }

    public void setApplyStatus4LicenseInfo(int ApplyStatus4LicenseInfo) {
        this.ApplyStatus4LicenseInfo = ApplyStatus4LicenseInfo;
    }

    public int getApplyStatus4AccountInfo() {
        return ApplyStatus4AccountInfo;
    }

    public void setApplyStatus4AccountInfo(int ApplyStatus4AccountInfo) {
        this.ApplyStatus4AccountInfo = ApplyStatus4AccountInfo;
    }

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String BankAccount) {
        this.BankAccount = BankAccount;
    }

    public String getBankAccountName() {
        return BankAccountName;
    }

    public void setBankAccountName(String BankAccountName) {
        this.BankAccountName = BankAccountName;
    }

    public String getBankType() {
        return BankType;
    }

    public void setBankType(String BankType) {
        this.BankType = BankType;
    }

    public int getBankInfoPassedFlag() {
        return BankInfoPassedFlag;
    }

    public void setBankInfoPassedFlag(int bankInfoPassedFlag) {
        BankInfoPassedFlag = bankInfoPassedFlag;
    }

    public String getLegalPerson() {
        return LegalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        LegalPerson = legalPerson;
    }
}
