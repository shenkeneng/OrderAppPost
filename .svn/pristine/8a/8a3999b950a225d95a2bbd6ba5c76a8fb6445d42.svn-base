package com.frxs.order.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frxs.order.R;
import com.frxs.order.widget.PagerSlidingTabStrip;

/**
 * 订单 By Tiepier
 */
public class OrdersFragment extends FrxsFragment {

    private PagerSlidingTabStrip orderTablayout;

    private ViewPager orderViewPager;

    private OrderManageFragment orderManageFragment;// 订单管理

    private OrderPreGoodsFragment orderPreGoodsFragment;// 预订商品

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_orders;
    }

    @Override
    protected void initViews(View view) {
        ((TextView) view.findViewById(R.id.title_tv)).setText("全部订单");
        ImageView imgBack = (ImageView) view.findViewById(R.id.left_btn);
        imgBack.setVisibility(View.GONE);
        orderTablayout = (PagerSlidingTabStrip) view.findViewById(R.id.points_tablayout);
        orderViewPager = (ViewPager) view.findViewById(R.id.points_pager);
        orderViewPager.setAdapter(new MyPagerAdapter(mActivity.getSupportFragmentManager()));
        orderTablayout.setViewPager(orderViewPager);
        setTabsValue();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    public OrderPreGoodsFragment getOrderPreGoodsFragment() {
        return orderPreGoodsFragment;
    }

    private void setTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        orderTablayout.setShouldExpand(true);
        orderTablayout.setDividerColor(Color.parseColor("#e6e6e6"));
        orderTablayout.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        orderTablayout.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        orderTablayout.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        orderTablayout.setIndicatorColor(Color.parseColor("#DB251F"));
        orderTablayout.setSelectedTextColor(Color.parseColor("#DB251F"));
        orderTablayout.setTabBackground(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[]{getResources().getString(R.string.order_manager), getResources().getString(R.string.order_pre_goods)};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    if (null == orderManageFragment) {
                        orderManageFragment = new OrderManageFragment();
                    }
                    return orderManageFragment;
                }
                case 1: {
                    if (null == orderPreGoodsFragment) {
                        orderPreGoodsFragment = new OrderPreGoodsFragment();
                    }
                    return orderPreGoodsFragment;
                }
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
