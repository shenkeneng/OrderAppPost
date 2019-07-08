package com.frxs.order.fragment;

import android.view.View;

import com.ewu.core.base.BaseFragment;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.service.ApiService;
import com.frxs.order.application.FrxsApplication;

public abstract class FrxsFragment extends BaseFragment
{
	public ApiService getService() {
		return FrxsApplication.getRestClient().getApiService();
	}

	protected abstract int getLayoutId();
	protected abstract void initViews(View view);
	protected abstract void initEvent();
	protected abstract void initData();

	public String getUserID()
	{
		UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
		if (null != userInfo)
		{
			return userInfo.getUserId();
		}
		else
		{
			return "";
		}
	}

	public String getShopID()
	{
		ShopInfo shopInfo = FrxsApplication.getInstance().getmCurrentShopInfo();
		if (null != shopInfo)
		{
			return shopInfo.getShopID();
		}
		else
		{
			return "";
		}
	}

	public String getWID()
	{
		ShopInfo shopInfo = FrxsApplication.getInstance().getmCurrentShopInfo();
		if (null != shopInfo)
		{
			return String.valueOf(shopInfo.getWID());
		}
		else
		{
			return "";
		}
	}

}
