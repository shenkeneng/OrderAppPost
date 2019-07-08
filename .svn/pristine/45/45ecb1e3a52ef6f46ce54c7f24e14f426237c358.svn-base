package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/3/23.
 */
public class UserInfo implements Serializable{

    @SerializedName("UserId")
    private String UserId;

    @SerializedName("UserName")
    private String UserName;

    @SerializedName("ShopList")
    private List<ShopInfo> ShopList;

    @SerializedName("UserMobile")
    private String UserMobile;

    @SerializedName("UserAccount")
    private String UserAccount;

    private ShopInfo currenShopInfo;

    public List<ShopInfo> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopInfo> shopList) {
        ShopList = shopList;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public ShopInfo getCurrenShopInfo() {
        return currenShopInfo;
    }

    public void setCurrenShopInfo(ShopInfo currenShopInfo) {
        this.currenShopInfo = currenShopInfo;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String userAccount) {
        UserAccount = userAccount;
    }
}
