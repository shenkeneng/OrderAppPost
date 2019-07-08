package com.frxs.order;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.fragment.PointExchangeFragment;
import com.frxs.order.fragment.PointIncomeFragment;
import com.frxs.order.widget.PagerSlidingTabStrip;

/**
 * Created by Endoon on 2016/4/28.
 */
public class PointDetailActivity extends FrxsActivity{

    private PagerSlidingTabStrip pointTablayout;

    private ViewPager pointsViewPager;

    private PointIncomeFragment pointIncomeFragment;// 积分收入

    private PointExchangeFragment pointExchangeFragment;// 兑换明细

    private TextView rightTv;//查看积分规则

    @Override
    protected int getLayoutId() {
        return R.layout.activity_point_detail;
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.title_tv)).setText(getResources().getString(R.string.point_detail));
        rightTv = (TextView) findViewById(R.id.right_tv);
        rightTv.setTextSize(18);
        pointTablayout = (PagerSlidingTabStrip) findViewById(R.id.points_tablayout);
        pointsViewPager = (ViewPager) findViewById(R.id.points_pager);
        pointsViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pointTablayout.setViewPager(pointsViewPager);
        setTabsValue();
    }

    private void setTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();

        pointTablayout.setShouldExpand(true);
        pointTablayout.setDividerColor(Color.parseColor("#e6e6e6"));
        pointTablayout.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        pointTablayout.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        pointTablayout.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        pointTablayout.setIndicatorColor(Color.parseColor("#DB251F"));
        pointTablayout.setSelectedTextColor(Color.parseColor("#DB251F"));
        pointTablayout.setTabBackground(0);
    }

    @Override
    protected void initEvent() {
        rightTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[]{getResources().getString(R.string.point_income), getResources().getString(R.string.point_exchange)};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    if (null == pointIncomeFragment) {
                        pointIncomeFragment = new PointIncomeFragment();
                    }
                    return pointIncomeFragment;
                }
                case 1: {
                    if (null == pointExchangeFragment) {
                        pointExchangeFragment = new PointExchangeFragment();
                    }
                    return pointExchangeFragment;
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

    /**
     * 当应用被强行关闭后（通过第三方软件手动强关，或系统为节省内存自动关闭应用）, Activity虽然被回收，但Fragment对象仍然保持，当再次打开应用时，activity被重建，Activity中Fragment对象的成员变量
     * 也会被实例化，老的Fragment也会被attach到新的Activity, 这样就出现了tab页面的UI重叠的问题
     * 解决的办法就是在Activity的onAttachFragment回调中把老的Fragment赋值给新的Activity的Fragment对象，这样就不会重新新建Fragment,从而去除UI重叠的问题
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (pointIncomeFragment == null && fragment instanceof PointIncomeFragment) {
            pointIncomeFragment = (PointIncomeFragment) fragment;
        }
        if (pointExchangeFragment == null && fragment instanceof PointExchangeFragment) {
            pointExchangeFragment = (PointExchangeFragment) fragment;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.right_tv:{
                int shopType = FrxsApplication.getInstance().getmCurrentShopInfo().getShopType();
                Intent intent = new Intent(this, CommonWebViewActivity.class);
                String baseUrl = Config.getBaseUrl();// 手动切换环境后 通过getBaseUrl获取的地址值始终是初始化地址值
                String pointRuleUrl = baseUrl + "AppH5/PointRule?" + "wId=" + getWID() + "&shopType=" + shopType;
                intent.putExtra("REQUSTPOINT", pointRuleUrl);
                intent.putExtra("H5TITLE", "积分规则");
                startActivity(intent);
            }
        }
    }
}
