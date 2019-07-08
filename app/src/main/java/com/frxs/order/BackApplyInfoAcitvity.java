package com.frxs.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.fragment.BackCommodityListFragment;
import com.frxs.order.fragment.BackInformationFragment;
import com.frxs.order.model.ApplySaleBackInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.PagerSlidingTabStrip;

import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/7.
 * 退货申请单详情页
 */

public class BackApplyInfoAcitvity extends FrxsActivity {

    private PagerSlidingTabStrip mTabs;

    private ViewPager mPager;

    private BackInformationFragment OIFragment;//订单信息

    private BackCommodityListFragment CLFragment;//商品清单

    private ApplySaleBackInfo applyInfo;

    private String applyBackID;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initViews() {
        mTabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);// 标题
        mPager = (ViewPager) this.findViewById(R.id.pager);//
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);
        setTabsValue();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //退货申请单编号
            applyBackID = getIntent().getStringExtra("APPLY_BACK_ID");
        }

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        refresh();
    }

    public void refresh(){
        reqApplyBackOrderInfo(applyBackID);
    }

    /**
     * 获取退货申请单详细
     */
    private void reqApplyBackOrderInfo(String applyBackID) {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("BackID", applyBackID);
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserId", getUserID());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());

        getService().GetApplySaleBackInfo(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ApplySaleBackInfo>>() {

            @Override
            public void onResponse(ApiResponse<ApplySaleBackInfo> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null) {
                        applyInfo = result.getData();
                        if (OIFragment == null) {
                            OIFragment = new BackInformationFragment();
                        }
                        if (CLFragment == null) {
                            CLFragment = new BackCommodityListFragment();
                        }
                        OIFragment.setData(applyInfo);
                        CLFragment.setData(applyInfo);
                    }
                } else {
                    ToastUtils.show(BackApplyInfoAcitvity.this, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(BackApplyInfoAcitvity.this, t.getMessage());
            }
        });
    }

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

        private final String[] titles = {"退货申请", "退货商品"};

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
                    OIFragment = new BackInformationFragment();
                }
                return OIFragment;
            case 1:
                if (CLFragment == null) {
                    CLFragment = new BackCommodityListFragment();
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

        if (OIFragment == null && fragment instanceof BackInformationFragment) {
            OIFragment = (BackInformationFragment) fragment;
        }
        if (CLFragment == null && fragment instanceof BackInformationFragment) {
            CLFragment = (BackCommodityListFragment) fragment;
        }
    }
}
