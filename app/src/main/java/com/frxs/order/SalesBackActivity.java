package com.frxs.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.frxs.order.fragment.BackOrderFragment;
import com.frxs.order.widget.PagerSlidingTabStrip;
import com.frxs.order.fragment.BackApplyOrderFragment;

/**
 * Created by shenpei on 2017/6/7.
 * 退货管理
 */

public class SalesBackActivity extends FrxsActivity {

    private PagerSlidingTabStrip salesTablayout;

    private ViewPager salesViewPager;

    private BackApplyOrderFragment backApplyOrderFragment;// 退货申请单

    private BackOrderFragment backOrderFragment;// 退货单

    protected static SalesBackActivity instance;

    private TextView rightTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sales_return;
    }

    @Override
    protected void initViews() {
        rightTv = (TextView) findViewById(R.id.tv_title_right);
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_add);
        rightTv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        rightTv.setText("退货申请");
        TextView titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText("退货管理");
        salesTablayout = (PagerSlidingTabStrip) findViewById(R.id.sales_tablayout);
        salesViewPager = (ViewPager) findViewById(R.id.sales_pager);
        salesViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        salesTablayout.setViewPager(salesViewPager);
        instance = this;
        setTabsValue();
    }

    public void setCurrentOrder(int position) {
        salesViewPager.setCurrentItem(position);
    }

    @Override
    protected void initEvent() {
        rightTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        int tabIndex = getIntent().getIntExtra("TAB_INDEX", -1);
        if (tabIndex > -1){
            setCurrentOrder(tabIndex);
        }
    }

    private void setTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        salesTablayout.setShouldExpand(true);
        salesTablayout.setDividerColor(Color.parseColor("#e6e6e6"));
        salesTablayout.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        salesTablayout.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        salesTablayout.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        salesTablayout.setIndicatorColor(Color.parseColor("#DB251F"));
        salesTablayout.setSelectedTextColor(Color.parseColor("#DB251F"));
        salesTablayout.setTabBackground(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[]{getResources().getString(R.string.apply_order), getResources().getString(R.string.sales_return_order)};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    if (null == backApplyOrderFragment) {
                        backApplyOrderFragment = new BackApplyOrderFragment();
                    }
                    return backApplyOrderFragment;
                }

                case 1: {
                    if (null == backOrderFragment) {
                        backOrderFragment = new BackOrderFragment();
                    }
                    return backOrderFragment;
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_title_right:// 跳转新增退货申请页面
                Intent intent = new Intent(this, NewBackOrderActivity.class);
                startActivity(intent);
                break;
        }
    }

}
