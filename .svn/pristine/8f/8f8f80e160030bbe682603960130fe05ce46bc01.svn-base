package com.frxs.order;


import android.content.Intent;

import com.frxs.order.fragment.StoreCartFragment;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.utils.ListToGroup;

import java.util.List;


public class StoreCartActivity extends FrxsActivity {

    private StoreCartFragment mCartFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_storecart;
    }

    @Override
    protected void initViews() {
        mCartFragment = new StoreCartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mCartFragment).commit();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    public StoreCartFragment getFragment(){
        return mCartFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<CartGoodsDetail> cartGoodsList = mCartFragment.getCartGoodsList();
        if (resultCode == RESULT_OK && null != cartGoodsList) {
            CartGoodsDetail resultItem = (CartGoodsDetail) data.getSerializableExtra("CART_GOODS");
            if (null != resultItem) {
                for (CartGoodsDetail item : cartGoodsList) {
                    if (item.getProductId().equals(resultItem.getProductId())) {
                        item.setRemark(resultItem.getRemark());
                        break;
                    }
                }
                // 将全部商品分组
                mCartFragment.setCartgoryGroupList(ListToGroup.subArray(cartGoodsList));
                // 更新购物车商品
                mCartFragment.updateStoreCartGoods();
            }
        }
    }
}
