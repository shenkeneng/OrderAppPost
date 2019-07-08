package com.frxs.order.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ewu.core.adpter.RefreshableFragPagerAdapter;
import com.frxs.order.fragment.CategoryGoodsFragment;

import java.util.List;

/**
 * Created by ewu on 2016/4/25.
 */
public class TabPagerAdapter extends RefreshableFragPagerAdapter {

    private int currentPosition = 0;

    /**
     * @param fm
     */
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param fm
     */
    public TabPagerAdapter(FragmentManager fm, List<Fragment> frgs) {
        super(fm, frgs);
    }

    public void setmCurrentFragment(int position) {
        currentPosition = position;
    }

    public int getCurrentFragmentPosition() {
        return currentPosition;
    }

    public CategoryGoodsFragment getCurrentFragment() {
        return (CategoryGoodsFragment) getFragment(currentPosition);
    }

    public boolean checkCanDoRefresh() {
        CategoryGoodsFragment currentFragment = getCurrentFragment();
        if (currentFragment == null) {
            return true;
        }
        return currentFragment.checkCanDoRefresh();
    }
}

