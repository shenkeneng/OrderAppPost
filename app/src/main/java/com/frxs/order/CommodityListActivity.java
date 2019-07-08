package com.frxs.order;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.utils.DensityUtils;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 结算-->商品清单 by Tiepier
 */
public class CommodityListActivity extends FrxsActivity {

    private TextView tvTitle;
    private ListView lvGoodsList;//商品清单
    private Adapter<CartGoodsDetail> quickAdapter;

    List<CartGoodsDetail> mGoodsList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void initViews() {
        /**
         * 实例化控件
         */
        tvTitle = (TextView) findViewById(R.id.title_tv);
        lvGoodsList = (ListView) findViewById(R.id.lv_goods_list);
    }

    @Override
    protected void initData() {
        tvTitle.setText("商品清单");

//        Intent intent = getIntent();
//        if (intent != null) {
//            List<CartGoodsDetail> cartGoodsList = (List<CartGoodsDetail>) intent.getSerializableExtra("GOODSLIST");
//            if (cartGoodsList != null && cartGoodsList.size() > 0) {
//                mGoodsList.addAll(cartGoodsList);
//            }
//        }
        List<CartGoodsDetail> cartGoodsList = FrxsApplication.getInstance().getStoreCart();
        if (cartGoodsList != null && cartGoodsList.size() > 0) {
            mGoodsList.addAll(cartGoodsList);
        }

        if (mGoodsList != null && mGoodsList.size() > 0) {
            quickAdapter = new Adapter<CartGoodsDetail>(this, mGoodsList, R.layout.item_goods_list) {
                @Override
                protected void convert(AdapterHelper helper, CartGoodsDetail item) {
                    //赠品业务处理（0：非赠品；1：赠品; 2:搭售）
                    TextView tvGoodsGift = helper.getView(R.id.tv_goods_gift);//赠品标识
                    int isGift = item.getIsGift();
                    if (isGift == 1) {
                        tvGoodsGift.setVisibility(View.VISIBLE);
                        tvGoodsGift.setText(getString(R.string.activity_gift));
                    } else if(isGift == 2) {
                        tvGoodsGift.setVisibility(View.VISIBLE);
                        tvGoodsGift.setText(getString(R.string.activity_reduce));
                    }else {
                        // 参加促销活动和有积分的商品显示“促”字
                        if (!TextUtils.isEmpty(item.getGiftPromotionID()) || item.getGoodsShopPoint() > 0){
                            tvGoodsGift.setVisibility(View.VISIBLE);
                            tvGoodsGift.setText(getString(R.string.activity_promotion));
                        } else {
                            tvGoodsGift.setVisibility(View.GONE);
                        }
                    }
                    //商品名称（0：有库存；1：无库存）
                    if (item.getIsNoStock() == 0) {
                        helper.setText(R.id.tv_goods_describe, item.getProductName());
                    } else if (item.getIsNoStock() == 1){
                        helper.setText(R.id.tv_goods_describe, Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                    }
                    //helper.setText(R.id.tv_goods_describe, item.getProductName());//商品描述
                    double deliveryPrice = item.getSalePrice()*(1+item.getShopAddPerc());
                    helper.setText(R.id.tv_goods_price, "￥" + MathUtils.twolittercountString(deliveryPrice)+"/"+item.getSaleUnit());//商品价格
                    helper.setText(R.id.tv_goods_count, "x " + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getPreQty())));//商品数量
                    helper.setText(R.id.tv_goods_remark, item.getRemark());//商品备注
                }
            };
            lvGoodsList.setAdapter(quickAdapter);
        }
    }

    @Override
    protected void initEvent() {
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:
                break;
            default:
                break;
        }
    }
}
