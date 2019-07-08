package com.frxs.order.fragment;


import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.Details;
import com.frxs.order.model.OrderShopGetRespData;
import com.frxs.order.model.Orders;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.List;

import retrofit2.Call;


/**
 * 商品清单 by Tipier
 */
public class CommodityListFragment extends FrxsFragment {


    private ListView lvCommodityList;//商品清单

    private Adapter<Details> quickAdapter;

    private int orderStatus = 0; //1:等待确认 订单等待确认之前不能再次购买

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commodity_list;
    }

    @Override
    protected void initViews(View view) {
        /**
         * 实例化控件
         */

        lvCommodityList = (ListView) view.findViewById(R.id.lv_commodity_list);

    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        quickAdapter = new Adapter<Details>(getActivity(), R.layout.item_commodity_list) {
            @Override
            protected void convert(AdapterHelper helper, final Details item) {
                TextView tvGoodsBuy = helper.getView(R.id.tv_goods_buy);//再次购买

                //赠品业务处理（0：非赠品；1：赠品；2：搭售）
                if (0 == item.getIsGift()){
                    if (1 == orderStatus) {
                        tvGoodsBuy.setVisibility(View.INVISIBLE);
                    } else {
                        tvGoodsBuy.setVisibility(View.VISIBLE);
                    }
                    helper.setVisible(R.id.tv_goods_gift, View.GONE);
                    //参加促销活动和有积分的商品显示“促”字
                    if (!TextUtils.isEmpty(item.getGiftPromotionID()) || item.getPromotionShopPoint() > 0 || item.getShopPoint() > 0){
                        helper.setVisible(R.id.tv_good_discount, View.VISIBLE);
                    } else {
                        helper.setVisible(R.id.tv_good_discount, View.GONE);
                    }
                }else{
                    helper.setVisible(R.id.tv_goods_gift, View.VISIBLE);
                    if (1 == item.getIsGift()){
                        tvGoodsBuy.setVisibility(View.INVISIBLE);
                        helper.setText(R.id.tv_goods_gift, getString(R.string.activity_gift));
                    }else{
                        if (1 == orderStatus) {
                            tvGoodsBuy.setVisibility(View.INVISIBLE);
                        } else {
                            tvGoodsBuy.setVisibility(View.VISIBLE);
                        }
                        helper.setText(R.id.tv_goods_gift, getString(R.string.activity_reduce));
                    }
                }

                //商品名称（0：有库存；1：无库存）
                if (item.getIsNoStock() == 0) {
                    helper.setText(R.id.tv_goods_describe, (0 == item.getIsGift()) ? item.getProductName() : "         " + item.getProductName());
                } else if (item.getIsNoStock() == 1){
                    helper.setText(R.id.tv_goods_describe, (0 == item.getIsGift()) ? Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName())
                    : "         " + Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                }

                helper.setImageUrl(R.id.img_goods, item.getProductImageUrl200());//商品图片
                double deliveryPrice = item.getSalePrice()*(1+item.getShopAddPerc());
                helper.setText(R.id.tv_goods_price, "￥" + MathUtils.twolittercountString(deliveryPrice)+"/"+item.getSaleUnit());//商品价格

                double saleQty = null != item.getSaleQty() ? item.getSaleQty() : 0;
                double preQty = null != item.getPreQty() ? item.getPreQty() : 0;
                if (saleQty < preQty) {
                    helper.getItemView().setBackgroundColor(Color.parseColor("#c2d2fc"));
                    helper.setVisible(R.id.lack_tag_view, View.VISIBLE);
                } else {
                    helper.getItemView().setBackgroundColor(Color.parseColor("#ffffff"));
                    helper.setVisible(R.id.lack_tag_view, View.GONE);
                }

                helper.setText(R.id.tv_goods_count, "x " + DensityUtils.subZeroAndDot(String.valueOf(saleQty)));//商品数量
                helper.setText(R.id.tv_pre_qty,"订：x " + DensityUtils.subZeroAndDot(String.valueOf(preQty)));

                helper.setVisible(R.id.tv_goods_integral, View.VISIBLE);
                double point = item.getPromotionShopPoint() > 0 ? item.getPromotionShopPoint() : item.getShopPoint();
                point = point * item.getSalePackingQty();
                helper.setText(R.id.tv_goods_integral, String.format(getResources().getString(R.string.points), point));//商品积分

                if (point <= 0) {
                    helper.setVisible(R.id.tv_goods_integral, View.GONE);
                }

                if (TextUtils.isEmpty(item.getRemark())) {
                    helper.setVisible(R.id.remark_layout, View.GONE);
                } else {
                    helper.setVisible(R.id.remark_layout, View.VISIBLE);
                    TextView remarkTv = helper.getView(R.id.tv_goods_remark);
                    remarkTv.setText(Html.fromHtml(item.getRemark()));
                }
                tvGoodsBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reqBuyGoods(item);
                    }
                });
            }
        };
        lvCommodityList.setAdapter(quickAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }

    public void setData(OrderShopGetRespData orderDetails) {
        Orders order = orderDetails.getOrder();
        if (null != order) {
            orderStatus = order.getStatus();
        }

        List<Details> detailsList = orderDetails.getDetails();
        if (detailsList != null && detailsList.size() > 0) {
            quickAdapter.replaceAll(detailsList);
        }
    }

    private void reqBuyGoods(Details details) {
        mActivity.showProgressDialog();
        // 处理单次购买商品数据
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();

        PostInfo postInfo = new PostInfo();
        postInfo.setProductID(String.valueOf(details.getProductId()));
        postInfo.setPreQty(1);
        postInfo.setWID(info.getWID());
        postInfo.setWarehouseId(info.getWID());
        postInfo.setIsGift(0); //不管是否搭售商品,此处必须要填写0才能再次购买添加购物车成功 2016/12/04

        PostEditCart editCart = new PostEditCart();
        editCart.setEditType(0);
        editCart.setShopID(info.getShopID());
        editCart.setUserId(FrxsApplication.getInstance().getUserInfo().getUserId());
        editCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        editCart.setWarehouseId(info.getWID());
        editCart.setCart(postInfo);

        getService().SaleCartEditSingle(editCart).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        ToastUtils.show(mActivity, "加入购物车成功");
                        FrxsApplication.getInstance().addShopCartCount(1);
                    } else {
                        ToastUtils.show(mActivity, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
                ToastUtils.show(mActivity, R.string.network_request_failed);//网络请求失败，请重试
            }
        });
    }
}

