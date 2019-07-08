package com.frxs.order.fragment;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ewu.core.utils.LogUtils;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.frxs.order.HomeActivity;
import com.frxs.order.ProductDetailActivity;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.WProductExt;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.List;

import retrofit2.Call;

/**
 * Created by ewu on 2016/4/25.
 */
public class CategoryGoodsFragment extends FrxsFragment {
    private GridView gvGoodsGrid;

    private Adapter<WProductExt> goodsAdapter;

    private EmptyView emptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_goods;
    }

    public void notifyDataSetChanged() {
        if (null != goodsAdapter) {
            goodsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initViews(View view) {
        gvGoodsGrid = (GridView) view.findViewById(R.id.gv_goods_grid);
        emptyView = (EmptyView) view.findViewById(R.id.emptyview);
    }

    @Override
    protected void initEvent() {
        gvGoodsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WProductExt item = (WProductExt) parent.getAdapter().getItem(position);
                if (null != item) {
                    Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                    intent.putExtra("product", item);
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        HomeFragment homeFragment = (HomeFragment) ((HomeActivity) mActivity).getFragment(0);
        int index = getArguments().getInt("index", 0);
        List<WProductExt> wProductExts = homeFragment.getFragmentProductList(index);

        if (wProductExts != null && wProductExts.size() > 0) {
            emptyView.setVisibility(View.GONE);
            goodsAdapter = new Adapter<WProductExt>(mActivity, wProductExts, R.layout.item_goods_grid) {
                @Override
                protected void convert(AdapterHelper helper, final WProductExt item) {
;                   //商品名称（0：有库存；1：无库存）
                    if (item.getIsNoStock() == 0) {
                        helper.setText(R.id.goods_desc_tv, item.getProductName());
                    } else if (item.getIsNoStock() == 1){
                        helper.setText(R.id.goods_desc_tv, Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                    }
                    //起定量业务处理
                    TextView tvTipsQty = helper.getView(R.id.tv_tips_preqty);
                    /*
                    * 在布局中多个控件同时使用一个资源的时候，这些控件会共用一个状态，例如ColorState，如果你改变了一个控件的状态，其他的控件都会接收到相同的通知。
                    * 这时我们可以使用mutate()方法使该控件状态不定，这样不定状态的控件就不会共享自己的状态了
                     */
                    //tvTipsQty.getBackground().setAlpha(50);
                    tvTipsQty.getBackground().mutate().setAlpha(60);
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
                    //副标题
                    TextView tvSubtitle = helper.getView(R.id.goods_subtitle_tv);
                    if (!TextUtils.isEmpty(item.getProductName2())) {
                        tvSubtitle.setVisibility(View.VISIBLE);
                        tvSubtitle.setText(item.getProductName2());
                    } else {
                        tvSubtitle.setVisibility(View.INVISIBLE);
                    }
                    if (!TextUtils.isEmpty(item.getImageUrl200x200())) {
                        helper.setImageUrl(R.id.goods_pics_iv, item.getImageUrl200x200());
                    } else {
                        helper.setImageResource(R.id.goods_pics_iv, R.mipmap.showcase_product_default);
                    }
                    final TextView tvGoodsBuy = helper.getView(R.id.good_buy_btn);
                    //商品冻结业务处理
                    if (item.getWStatus() == 3) {
                        helper.setVisible(R.id.sold_out_img, View.VISIBLE);
                        tvGoodsBuy.setBackgroundResource(R.mipmap.add_disable_btn);
                        tvGoodsBuy.setEnabled(false);
                    } else {
                        helper.setVisible(R.id.sold_out_img, View.GONE);
                        tvGoodsBuy.setBackgroundResource(R.mipmap.add_cart_circle_btn);
                        tvGoodsBuy.setEnabled(true);
                    }

                    double count = FrxsApplication.getInstance().getSaleCartProductCount(item.getProductId());
                    if (count > 0) {
                        tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                    } else {
                        tvGoodsBuy.setText("");
                    }

                    double deliveryPrice = item.getBigSalePrice()*(1+item.getShopAddPerc());
                    helper.setText(R.id.good_price_tv, "￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getBigUnit());
                    TextView tvIntegral = helper.getView(R.id.tv_goods_integral);
                    if (item.getBigShopPoint() > 0) {
                        tvIntegral.setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_goods_integral, String.format(getResources().getString(R.string.points), item.getBigShopPoint()));
                    } else {
                        helper.setVisible(R.id.tv_goods_integral, View.GONE);
                        tvIntegral.setVisibility(View.GONE);

                    }
                    //促销商品业务处理（0：非促销商品；1：促销商品）
                    if (item.getIsGift() == 1 || item.getProPoints() > 0 ) {
                        helper.setVisible(R.id.tv_good_discount, View.VISIBLE);
                    } else {
                        helper.setVisible(R.id.tv_good_discount, View.GONE);
                    }

                    tvGoodsBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reqBuyGoods(item, tvGoodsBuy);
                        }
                    });
                }
            };
            gvGoodsGrid.setAdapter(goodsAdapter);
        } else {
            emptyView.setNoDataView(R.mipmap.icon_noservice, "暂无推荐商品");
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void reqBuyGoods(final WProductExt ext, final TextView tvGoodsBuy) {
        mActivity.showProgressDialog();
        // 处理单次购买商品数据
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();

        PostInfo postInfo = new PostInfo();
        postInfo.setProductID(ext.getProductId());
        postInfo.setPreQty(1);
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
                        FrxsApplication.getInstance().addShopCartCount(1);
                        FrxsApplication.getInstance().updateSaleCartProduct(ext.getProductId(), 1);
                        double count = FrxsApplication.getInstance().getSaleCartProductCount(ext.getProductId());
                        //容错处理
                        if (count <= 0) {
                            count = 1;
                        }
                        tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                        ToastUtils.showShortToast(mActivity, "加入购物车成功");
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

    public boolean checkCanDoRefresh() {
        if (null == gvGoodsGrid.getChildAt(0)) {
            LogUtils.d("null == gvGoodsGrid.getChildAt(0)");
        }
        if (goodsAdapter == null || goodsAdapter.getCount() == 0 || gvGoodsGrid == null || null == gvGoodsGrid.getChildAt(0)) {
            return true;
        }

        LogUtils.d(String.format("checkCanDoRefresh: %s %s", gvGoodsGrid.getFirstVisiblePosition(), gvGoodsGrid.getChildAt(0).getTop()));
        return gvGoodsGrid.getFirstVisiblePosition() == 0 && gvGoodsGrid.getChildAt(0).getTop() == 0;
    }
}
