package com.frxs.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.fragment.CommodityListFragment;
import com.frxs.order.fragment.OrderInformationFragment;
import com.frxs.order.model.OrderShopGetRespData;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.PagerSlidingTabStrip;

import retrofit2.Call;


/**
 * 订单详情 by Tiepier
 */
public class OrderDetailActivity extends FrxsActivity {

    private PagerSlidingTabStrip mTabs;

    private ViewPager mPager;

    private OrderInformationFragment OIFragment;//订单信息

    private CommodityListFragment CLFragment;//商品清单

    private String OrderID;//订单编号

    private String shippingDate;//订单配送完成时间

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initViews() {

        /**
         * 实例化控件
         */
        mTabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);// 标题
        mPager = (ViewPager) this.findViewById(R.id.pager);//
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);
        setTabsValue();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            OrderID = getIntent().getStringExtra("ORDERID");
            shippingDate = getIntent().getStringExtra("SHIPPING_DATE");
        }

    }

    @Override
    protected void initData() {
        reqOrderDetail();
    }

    @Override
    protected void initEvent() {

    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();

        mTabs.setShouldExpand(true);
        mTabs.setDividerColor(Color.parseColor("#e6e6e6"));
        mTabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        mTabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        mTabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        mTabs.setIndicatorColor(Color.parseColor("#DB251F"));
        mTabs.setSelectedTextColor(Color.parseColor("#DB251F"));
        mTabs.setTabBackground(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = {"订单信息", "商品清单"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return getCurrentFragment(position);
        }
    }

    private Fragment getCurrentFragment(int index) {
        switch (index) {
            case 0:
                if (OIFragment == null) {
                    OIFragment = new OrderInformationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("ORDERID", OrderID);//传递订单详情-->订单详情所需参数：订单ID
                    bundle.putString("SHIPPING_DATE", shippingDate);
                    OIFragment.setArguments(bundle);
                }
                return OIFragment;
            case 1:
                if (CLFragment == null) {
                    CLFragment = new CommodityListFragment();
                }
                return CLFragment;


            default:
                return null;
        }
    }

    /**
     * 当应用被强行关闭后（通过第三方软件手动强关，或系统为节省内存自动关闭应用）, Activity虽然被回收，但Fragment对象仍然保持，当再次打开应用时，activity被重建，Activity中Fragment对象的成员变量
     * 也会被实例化，老的Fragment也会被attach到新的Activity, 这样就出现了tab页面的UI重叠的问题
     * 解决的办法就是在Activity的onAttachFragment回调中把老的Fragment赋值给新的Activity的Fragment对象，这样就不会重新新建Fragment,从而去除UI重叠的问题
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (OIFragment == null && fragment instanceof OrderInformationFragment) {
            OIFragment = (OrderInformationFragment) fragment;
        }
        if (CLFragment == null && fragment instanceof CommodityListFragment) {
            CLFragment = (CommodityListFragment) fragment;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {

            default:
                break;
        }
    }

    /**
     * 订单详情数据请求
     */
    public void reqOrderDetail() {
        showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("OrderId", OrderID);
        params.put("ShopID", getShopID());
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserName", userInfo.getUserName());
        getService().OrderGetAction(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<OrderShopGetRespData>>() {
            @Override
            public void onResponse(ApiResponse<OrderShopGetRespData> result, int code, String msg) {
                dismissProgressDialog();
                OrderShopGetRespData orderDetails = result.getData();
                if (orderDetails != null) {
                    if (OIFragment == null) {
                        OIFragment = new OrderInformationFragment();
                    }
                    if (CLFragment == null) {
                        CLFragment = new CommodityListFragment();
                    }
                    OIFragment.setData(orderDetails);
                    CLFragment.setData(orderDetails);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderShopGetRespData>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(OrderDetailActivity.this, R.string.network_request_failed);//网络请求失败，请重试
            }
        });

    }

}
