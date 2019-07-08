package com.frxs.order;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.frxs.order.fragment.MyPreSaleGoodsFragment;
import com.frxs.order.fragment.PreSaleGoodsFragment;
import com.frxs.order.widget.PagerSlidingTabStrip;

/**
 * Created by ewu on 2016/11/8.
 */

public class PreSaleActivity extends FrxsActivity {
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private PreSaleGoodsFragment preSaleGoodsFragment; //预售商品
    private MyPreSaleGoodsFragment myPreSaleGoodsFragment; //我的预售商品列表

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pre_sale;
    }

    @Override
    protected void initViews() {
        mTabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);// 标题
        mPager = (ViewPager) this.findViewById(R.id.pager);//
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);
        mTabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (1 == position) {
                    if (null != myPreSaleGoodsFragment) {
                        myPreSaleGoodsFragment.refreshData();
                    }
                }
            }
        });
        setTabsValue();
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

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = {"代购商品", "我的代购商品"};

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
                if (preSaleGoodsFragment == null) {
                    preSaleGoodsFragment = new PreSaleGoodsFragment();
                }
                return preSaleGoodsFragment;
            case 1:
                if (myPreSaleGoodsFragment == null) {
                    myPreSaleGoodsFragment = new MyPreSaleGoodsFragment();
                }
                return myPreSaleGoodsFragment;
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

        if (preSaleGoodsFragment == null && fragment instanceof PreSaleGoodsFragment) {
            preSaleGoodsFragment = (PreSaleGoodsFragment) fragment;
        }
        if (myPreSaleGoodsFragment == null && fragment instanceof MyPreSaleGoodsFragment) {
            myPreSaleGoodsFragment = (MyPreSaleGoodsFragment) fragment;
        }
    }
}
