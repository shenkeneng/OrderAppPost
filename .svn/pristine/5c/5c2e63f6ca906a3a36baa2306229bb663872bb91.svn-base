package com.frxs.order.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.PromotionDetailActivity;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.PromotionProduct;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.frxs.order.widget.CountEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.List;

import retrofit2.Call;

/**
 * Created by ewu on 2016/9/10.
 */
public class ActivityGoodsFragment extends FrxsFragment {

    private PromotionDetailActivity parentActivity;
    private ListView promotionProductLv;
    private ListView promotionGiftLv;
    private View giftLayout;
    private int index = 0; //当前tab

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_goods;
    }

    @Override
    protected void initViews(View view) {
        promotionProductLv = (ListView) view.findViewById(R.id.goods_list_view);
        giftLayout = view.findViewById(R.id.gift_group_list_layout);
        promotionGiftLv = (ListView) view.findViewById(R.id.gift_list_view);
        parentActivity = (PromotionDetailActivity) mActivity;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        boolean isTabFrag = getArguments().getBoolean("is_tab", false);
        index = getArguments().getInt("index", 0);
        List<PromotionProduct> productList = (List<PromotionProduct>) getArguments().getSerializable("product_list");
        if (null != productList) {
            Adapter adapter = new Adapter<PromotionProduct>(mActivity, R.layout.item_promotion_product) {
                @Override
                protected void convert(AdapterHelper helper, final PromotionProduct item) {
                    date2view(helper, item);

                }
            };
            adapter.addAll(productList);
            promotionProductLv.setAdapter(adapter);
        }

        if (!isTabFrag) {
            List<PromotionProduct> giftList = (List<PromotionProduct>) getArguments().getSerializable("gift_list");
            if (null != giftList && giftList.size() > 0) {
                giftLayout.setVisibility(View.VISIBLE);

                String groupName = "";
                if (giftList.get(0).getIsGift() == 1) {
                    groupName = getString(R.string.activity_gift_goup_name);
                } else {
                    groupName = getString(R.string.activity_match_goup_name);
                }

                ((TextView)giftLayout.findViewById(R.id.tv_promotion_name)).setText(groupName);
                Adapter adapter = new Adapter<PromotionProduct>(mActivity, R.layout.item_promotion_product) {
                    @Override
                    protected void convert(AdapterHelper helper, final PromotionProduct item) {
                        date2view(helper, item);
                    }
                };
                adapter.addAll(giftList);
                promotionGiftLv.setAdapter(adapter);
            }
        }
    }

    /**
     * 促销活动详情数据
     */
    private void date2view(AdapterHelper helper, final PromotionProduct item) {
        //赠品标识(0：非赠品；1：赠品；2：搭售)
        TextView tvGfitFlag = helper.getView(R.id.tv_goods_gift);
        int isGift = item.getIsGift();
        if (isGift == 1 || isGift == 2) {
            tvGfitFlag.setVisibility(View.VISIBLE);
            //商品编辑模块
            RelativeLayout rlGoodsEdit = helper.getView(R.id.rl_goods_edit);
            rlGoodsEdit.setVisibility(View.GONE);
            //赠品数量业务处理
            if (item.getGiftsQty() != null) {
                helper.setText(R.id.tv_goods_gift_count, "x " + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getGiftsQty())));
                helper.setVisible(R.id.tv_goods_gift_count, View.VISIBLE);
            } else {
                helper.setVisible(R.id.tv_goods_gift_count, View.INVISIBLE);
            }

            if (1 == isGift) {
                tvGfitFlag.setText(getString(R.string.activity_gift));
            } else {
                tvGfitFlag.setText(getString(R.string.activity_reduce));
            }
        } else {
            helper.setVisible(R.id.tv_goods_gift_count, View.INVISIBLE);
            tvGfitFlag.setVisibility(View.GONE);
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
        //商品标题
        helper.setText(R.id.tv_goods_title, item.getProductName());
        //商品副标题
        TextView tvSubTitle = helper.getView(R.id.tv_goods_subtitle);
        if (!TextUtils.isEmpty(item.getProductName2())) {
            tvSubTitle.setVisibility(View.VISIBLE);
            tvSubTitle.setText(item.getProductName2());
        } else {
            tvSubTitle.setVisibility(View.GONE);
        }
        //积分
        TextView tvIntegral = helper.getView(R.id.tv_goods_integral);
        if (item.getBigShopPoint() > 0) {
            tvIntegral.setVisibility(View.VISIBLE);
            tvIntegral.setText(String.format(getResources().getString(R.string.points), item.getBigShopPoint()));
        } else {
            tvIntegral.setVisibility(View.GONE);
        }
        //商品价格
        double deliveryPrice = item.getSalePrice()*(1+item.getShopAddPerc());
        helper.setText(R.id.tv_goods_price, MathUtils.twolittercountString(deliveryPrice) + "/" + item.getSaleUnit());
        //数量加减
        final CountEditText countEditText = helper.getView(R.id.count_edit_text);
        countEditText.setCount(1);
        //商品冻结业务处理
        TextView tvGoodsBuy = helper.getView(R.id.tv_goods_buy);
        if (item.getWStatus() == 3) {
            tvGoodsBuy.setBackgroundResource(R.mipmap.add_disable_btn);
            tvGoodsBuy.setEnabled(false);
        } else {
            tvGoodsBuy.setBackgroundResource(R.mipmap.add_cart_circle_btn);
            tvGoodsBuy.setEnabled(true);
            tvGoodsBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reqBuyGoods(item, countEditText.getCount());
                }
            });
        }

        if (item.getCartQty() > 0) {
            tvGoodsBuy.setText(String.valueOf(item.getCartQty()));
            tvGoodsBuy.setTextSize(10);
        }
    }


    /**
     * 商品购买
     */
    private void reqBuyGoods(PromotionProduct ext, int count) {
        mActivity.showProgressDialog();
        // 处理单次购买商品数据
            ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();
            final int addCount = count;
            PostInfo postInfo = new PostInfo();
            final int productId = ext.getProductId();
            postInfo.setProductID(String.valueOf(productId));
            postInfo.setPreQty(count);
            postInfo.setWID(info.getWID());
            postInfo.setWarehouseId(info.getWID());
            postInfo.setIsGift(0);

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
                            FrxsApplication.getInstance().addShopCartCount(addCount);
                            parentActivity.updapteGroupCartCount(index, productId, addCount);

                            ((Adapter) promotionProductLv.getAdapter()).notifyDataSetChanged();
                        } else {
                            ToastUtils.show(mActivity, result.getInfo());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    super.onFailure(call, t);
                    mActivity.dismissProgressDialog();
                }
            });

    }

    @Override
    public void onClick(View v) {

    }
}

