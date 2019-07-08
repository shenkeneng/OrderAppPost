package com.frxs.order.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ewu.core.widget.slidingtabs.SlidingTabLayout;
import com.frxs.order.R;
import com.frxs.order.adapter.CartPagerAdapter;
import com.frxs.order.model.CartGoodsDetail;

import java.util.List;

/**
 * Created by Chentie on 2017/5/9.
 */

public class CartCategoryFragment extends FrxsFragment implements SlidingTabLayout.TabAdapter {


    private SlidingTabLayout homeTabLayout;

    private ViewPager tabViewPager;

    protected CartPagerAdapter cartPagerAdapter;

    private List<List<CartGoodsDetail>> preProductList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cart_category;
    }

    @Override
    protected void initViews(View view) {
        homeTabLayout = (SlidingTabLayout) view.findViewById(R.id.cart_tab_layout);
        tabViewPager = (ViewPager) view.findViewById(R.id.tab_cart_pager);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && null != cartPagerAdapter && null != cartPagerAdapter.getCurrentFragment()) {
            cartPagerAdapter.getCurrentFragment().notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != cartPagerAdapter) {
            notifyDataSetChanged();
        }
    }

    protected void notifyDataSetChanged() {
        PreGoodsFragment currentFrg = cartPagerAdapter.getCurrentFragment();
        if (null != currentFrg) {
            currentFrg.notifyDataSetChanged();
        }
    }

    @Override
    protected void initEvent() {
        homeTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cartPagerAdapter.setmCurrentFragment(position);
                notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {

        final int[] colors={0xFFFFFFFF,0xFF654321,0xFF336699};
        Bundle bundle = getArguments();
        if (bundle != null) {
            //preProductList = (ArrayList<CartGoodsDetail>) bundle.getSerializable("product_list");
            preProductList = (List<List<CartGoodsDetail>>) bundle.getSerializable("product_list");
        }
        cartPagerAdapter = new CartPagerAdapter(getChildFragmentManager(), preProductList);
//        tabViewPager.setOffscreenPageLimit(1);
        tabViewPager.setAdapter(cartPagerAdapter);
        homeTabLayout.setCustomTabView(R.layout.view_sliding_tab_item, R.id.textview);
        homeTabLayout.setDistributeEvenly(true);
        homeTabLayout.setTabAdapter(this);
        homeTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.red));
        homeTabLayout.setViewPager(tabViewPager);
        homeTabLayout.setSelectedIndicatorHeight(3);
        updateAdvertisements(preProductList);
    }

    @Override
    public void onClick(View v) {

    }

    protected void updateAdvertisements(List<List<CartGoodsDetail>> productList) {
        if (null != productList || productList.size() > 0) {
            preProductList = productList;
            // 商品只有一类时，不显示分类tab
            if (productList.size() == 1) {
                homeTabLayout.setVisibility(View.GONE);
            } else {
                homeTabLayout.setVisibility(View.VISIBLE);
            }

            cartPagerAdapter.setPagerItems(productList);
            homeTabLayout.notifyDataSetChanged();
            cartPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public CharSequence getTitle(int position) {
        if (preProductList.size() > position) {
            if (position == 0) {;
                return getString(R.string.all_goods);
            } else {
                return preProductList.get(position).get(0).getCategoryName1();
            }
        } else {
            return "";
        }
    }

    @Override
    public int getImageId(int position) {
        return R.mipmap.ic_launcher;
    }

    @Override
    public int getTabWidth(int position) {
        return 0;
    }

    @Override
    public void loadBitmap(View imageView, int position, boolean isSelect) {

    }

}