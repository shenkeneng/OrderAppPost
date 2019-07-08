package com.frxs.order.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.ewu.core.adpter.RefreshableFragPagerAdapterEx;
import com.frxs.order.fragment.PreGoodsFragment;
import com.frxs.order.model.CartGoodsDetail;
import com.pacific.adapter.Adapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chentie on 2017/5/10.
 */

public class CartPagerAdapter extends RefreshableFragPagerAdapterEx {

    private int currentPosition = 0;

    public CartPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected Fragment newFragmentInstance(int position, List dataList) {
        PreGoodsFragment preGoodsFragment = new PreGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product_list", (Serializable) dataList);
        bundle.putInt("position", position);
        preGoodsFragment.setArguments(bundle);
        return preGoodsFragment;
    }

    /**
     * @param fm
     */
    public CartPagerAdapter(FragmentManager fm, List<List<CartGoodsDetail>> frgs) {
        super(fm, frgs);
    }

    public void setmCurrentFragment(int position) {
        currentPosition = position;
    }

    public int getCurrentFragmentPosition() {
        return currentPosition;
    }

    public PreGoodsFragment getCurrentFragment() {
        return (PreGoodsFragment) getFragment(currentPosition);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PreGoodsFragment pagerFrag = (PreGoodsFragment) super.instantiateItem(container, position);
        if (pagerFrag != null) {
            if (position < dataArrayList.size()) {
                List<CartGoodsDetail> productList = (List<CartGoodsDetail>) dataArrayList.get(position);
                if (null != productList) {
                    //Update the data value of the fragment
                    Bundle bundle = pagerFrag.getArguments();
                    bundle.putSerializable("product_list", (Serializable) productList);
                    bundle.putInt("position", position);
                    Adapter<CartGoodsDetail> preProductAdapter = pagerFrag.getPreProductAdapter();
                    if (preProductAdapter != null && productList != null) {
                        preProductAdapter.replaceAll(productList);
                    }
                }
            }
        }

        return pagerFrag;
    }

    public boolean checkCanDoRefresh() {
        PreGoodsFragment preFragment = getCurrentFragment();
        if (preFragment == null) {
            return true;
        }
        return preFragment.checkCanDoRefresh();
    }

}
