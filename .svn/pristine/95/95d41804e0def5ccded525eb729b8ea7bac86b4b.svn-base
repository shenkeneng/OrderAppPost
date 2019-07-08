package com.frxs.order.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.utils.CheckUtils;
import com.ewu.core.utils.LogUtils;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.HomeActivity;
import com.frxs.order.OrderRemarkActivity;
import com.frxs.order.R;
import com.frxs.order.StoreCartActivity;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.utils.DensityUtils;
import com.frxs.order.widget.CountEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chentie on 2017/5/9.
 */

public class PreGoodsFragment extends FrxsFragment {

    private List<CartGoodsDetail> preProductList;

    private ListView preProductLv;

    protected Adapter<CartGoodsDetail> preProductAdapter;

    private StoreCartFragment storeCartFragment;

    public Adapter getPreProductAdapter() {
        return preProductAdapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pre_goods;
    }

    @Override
    protected void initViews(View view) {

        preProductLv = (ListView) view.findViewById(R.id.lv_commodity_list);
        if (mActivity instanceof HomeActivity) {
            storeCartFragment = (StoreCartFragment) ((HomeActivity) mActivity).getFragment(2);
        } else if (mActivity instanceof StoreCartActivity){
            storeCartFragment = ((StoreCartActivity) mActivity).getFragment();
        }

        /**
        * 获取全部商品
        */
        Bundle bundle = getArguments();
        if (bundle != null) {
            preProductList = (ArrayList<CartGoodsDetail>) bundle.getSerializable("product_list");
            int position = bundle.getInt("position");

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            preProductAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        preProductAdapter = new Adapter<CartGoodsDetail>(mActivity, R.layout.view_store_cart_item) {

            @Override
            protected void convert(final AdapterHelper helper, final CartGoodsDetail item) {

                double deliveryPrice = item.getSalePrice() * (1 + item.getShopAddPerc());
                helper.setText(R.id.tv_goods_price, "￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getSaleUnit());
                /***
                 * 积分为"0"处理
                 */
                if (item.getGoodsShopPoint() == 0) {
                    helper.setVisible(R.id.tv_goods_integral, View.GONE);
                } else {
                    helper.setVisible(R.id.tv_goods_integral, View.VISIBLE);

                    /**
                     * 积分计算公式 ((PromotionShopPoint!=null && PromotionShopPoint>0)?PromotionShopPoint:ShopPoint)*SalePackingQty
                     */

                    double point = (String.valueOf(item.getPromotionShopPoint()) != null && item.getPromotionShopPoint() > 0) ? item.getPromotionShopPoint() : item.getShopPoint();
                    point = point * item.getSalePackingQty();
                    helper.setText(R.id.tv_goods_integral, String.format(getResources().getString(R.string.points), point));
                }
                final CountEditText countEditText = helper.getView(R.id.count_edit_text);
                countEditText.setCount(item.getNewPreQty() > 0 ? (int) item.getNewPreQty() : (int) item.getPreQty());
                /**
                 * 修改数量 隐藏显示确定按钮
                 */

                if (countEditText.getCount() == item.getPreQty()) {
                    helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                } else {
                    helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                }
                countEditText.setEditTextClickale(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog dialog = new MaterialDialog(mActivity);
                        LayoutInflater inflater = LayoutInflater.from(mActivity);
                        final View view = inflater.inflate(R.layout.dialog_modify_num, null);
                        dialog.setContentView(view);
                        ((TextView) view.findViewById(R.id.my_title_tv)).setText("修改购买数量");
                        final EditText countEt = (EditText) view.findViewById(R.id.count_edit_et);
                        countEt.setText(String.valueOf(countEditText.getCount()));
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String strCount = countEt.getText().toString().trim();
                                int count = 1;
                                if (CheckUtils.strIsNumber(strCount)) {
                                    count = Integer.valueOf(strCount);
                                    if (count < 1) {
                                        count = 1;
                                    }
                                }
                                countEditText.setCount(count);

                                if (count == item.getPreQty()) {
                                    helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                                    item.setNewPreQty(count);
                                } else {
                                    helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                                    item.setNewPreQty(count);
                                }

                                dialog.dismiss();
                            }
                        });
                        dialog.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                countEditText.setOnCountChangeListener(new CountEditText.onCountChangeListener() {
                    @Override
                    public void onCountAdd(int count) {
                        if (!mActivity.isShowingProgressDialog()) {
                            helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                            int cartCount = countEditText.getCount();
                            if (cartCount == item.getPreQty()) {
                                helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                                item.setNewPreQty(cartCount);
                            } else {
                                item.setNewPreQty(cartCount);
                            }
                        }
                    }

                    @Override
                    public void onCountSub(int count) {
                        if (!mActivity.isShowingProgressDialog()) {
                            helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                            int cartCount = countEditText.getCount();
                            if (cartCount == item.getPreQty()) {
                                helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                                item.setNewPreQty(cartCount);
                            } else {
                                item.setNewPreQty(cartCount);
                            }
                        }
                    }
                });
                helper.setOnClickListener(R.id.tv_cart_confirm, new View.OnClickListener() {// 确定修改商品数量 刷新数据
                    @Override
                    public void onClick(View view) {
                        int cartCount = countEditText.getCount();
                        item.setNewPreQty(cartCount);
                        item.setEditType(1);
                        storeCartFragment.requestEditSingleGoods(PostEditCart.EDIT_TYPE_EDIT, cartCount, item);
                    }
                });

                helper.setOnClickListener(R.id.delete_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mActivity.isShowingProgressDialog()) {
                            final MaterialDialog dialog = new MaterialDialog(mActivity);
                            dialog.setMessage("是否删除此商品？");
                            dialog.setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            item.setEditType(2);
                                            item.setNeedDelete(true);
                                            storeCartFragment.requestEditSingleGoods(PostEditCart.EDIT_TYPE_DELETE, 0, item);
                                        }
                                    }
                            );
                            dialog.setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }
                });
                /**
                 * 购物车备注
                 */
                helper.setOnClickListener(R.id.remark_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, OrderRemarkActivity.class);
                        boolean isCart = true;
                        intent.putExtra("CART", isCart);
                        intent.putExtra("REMARK", item.getRemark());
                        intent.putExtra("CART_GOODS", item);
                        startActivityForResult(intent, GlobelDefines.REQ_CODE_STORE_CART);
                    }
                });

                /**
                 * 促销商品业务处理(0：非促销商品；1：促销商品; 2：搭售商品)
                 */
                if (0 == item.getIsGift()) {
                    helper.setVisible(R.id.edit_layout, View.VISIBLE);
                    helper.setVisible(R.id.tv_promotion_count, View.GONE);
                    helper.setVisible(R.id.tv_promotion_flag, View.GONE);
                    //商品名称（0：有库存；1：无库存）
                    if (item.getIsNoStock() == 0) {
                        helper.setText(R.id.tv_goods_describe, item.getProductName());
                    } else if (item.getIsNoStock() == 1){
                        helper.setText(R.id.tv_goods_describe, Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                    }

                    //参加促销活动的商品显示“促”字
                    if (!TextUtils.isEmpty(item.getGiftPromotionID()) || item.getGoodsShopPoint() > 0) {
                        helper.setVisible(R.id.tv_promotion_flag, View.VISIBLE);//促标识
                        helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_promotion));
                        //商品名称（0：有库存；1：无库存）
                        if (item.getIsNoStock() == 0) {
                            //helper.setVisible(R.id.tv_goods_pre, View.GONE);
                            helper.setText(R.id.tv_goods_describe, "      " + item.getProductName());
                        } else if (item.getIsNoStock() == 1){
                            helper.setText(R.id.tv_goods_describe,  Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;" + getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                        }
                    }
                } else {
                    helper.setVisible(R.id.edit_layout, View.GONE);//商品数量编辑模块
                    helper.setVisible(R.id.tv_promotion_count, View.VISIBLE);//赠品数量
                    helper.setText(R.id.tv_promotion_count, "x" + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getPreQty())));
                    helper.setVisible(R.id.tv_promotion_flag, View.VISIBLE);//赠品标识
                    //商品名称（0：有库存；1：无库存）
                    if (item.getIsNoStock() == 0) {
                        //helper.setVisible(R.id.tv_goods_pre, View.GONE);
                        helper.setText(R.id.tv_goods_describe, "          " + item.getProductName());
                    } else if (item.getIsNoStock() == 1){
                        helper.setText(R.id.tv_goods_describe,  Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                    }

                    if (1 == item.getIsGift()) {
                        helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_gift));
                    } else if (2 == item.getIsGift()) {
                        helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_reduce));
                    }
                }

                if (TextUtils.isEmpty(item.getRemark())) {
                    helper.setVisible(R.id.remark_layout, View.GONE);
                } else {
                    helper.setVisible(R.id.remark_layout, View.VISIBLE);
                    TextView remarkTv = helper.getView(R.id.tv_goods_remark);
                    remarkTv.setText(Html.fromHtml(item.getRemark()));
                }
                //起订量业务处理
                TextView tvTipsQty = helper.getView(R.id.tv_tips_preqty);
                if (item.getMinPreQty() != null && item.getMaxaPreQty() != null) {
                    tvTipsQty.setVisibility(View.VISIBLE);
                    if (item.getMinPreQty() == 0) {
                        String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMaxaPreQty()));//最大起订量
                        tvTipsQty.setText("最大订购量：" + strMaxQty);
                    } else if (item.getMaxaPreQty() == 0) {
                        String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMinPreQty()));//最小起订量
                        tvTipsQty.setText("最小起订量：" + strMinQty);
                    } else if (item.getMinPreQty() == 0 && item.getMaxaPreQty() == 0) {
                        tvTipsQty.setVisibility(View.VISIBLE);
                    } else {
                        String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMinPreQty()));//最小起订量
                        String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMaxaPreQty()));//最大起订量
                        tvTipsQty.setText(strMinQty + "≤订购量≤" + strMaxQty);
                    }
                } else if (item.getMinPreQty() != null && item.getMaxaPreQty() == null) {
                    String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMinPreQty()));//最小起订量
                    tvTipsQty.setText("最小起订量：" + strMinQty);
                } else if (item.getMaxaPreQty() != null && item.getMinPreQty() == null) {
                    String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMaxaPreQty()));//最大起订量
                    tvTipsQty.setText("最大订购量：" + strMaxQty);
                } else {
                    tvTipsQty.setVisibility(View.GONE);
                }
            }
        };
        preProductLv.setAdapter(preProductAdapter);
        if (preProductList != null && preProductList.size() > 0){
            preProductAdapter.replaceAll(preProductList);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public boolean checkCanDoRefresh() {
        if (null == preProductLv.getChildAt(0)) {
            LogUtils.d("null == gvGoodsGrid.getChildAt(0)");
        }
        if (preProductAdapter == null || preProductAdapter.getCount() == 0 || preProductAdapter == null || null == preProductLv.getChildAt(0)) {
            return true;
        }

        LogUtils.d(String.format("checkCanDoRefresh: %s %s", preProductLv.getFirstVisiblePosition(), preProductLv.getChildAt(0).getTop()));
        return preProductLv.getFirstVisiblePosition() == 0 && preProductLv.getChildAt(0).getTop() == 0;
    }

    public void notifyDataSetChanged() {
        if (null != preProductAdapter) {
            preProductAdapter.notifyDataSetChanged();
        }
    }


}
