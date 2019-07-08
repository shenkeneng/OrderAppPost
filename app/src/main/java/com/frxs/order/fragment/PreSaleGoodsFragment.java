package com.frxs.order.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewu.core.utils.ImageLoader;
import com.ewu.core.utils.ImeUtils;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.PreSaleGoodsPhotoViewActivity;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.GetPreSaleProductListRsp;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.ClearEditText;
import com.frxs.order.widget.CountEditText;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.frxs.order.R.id.tv_goods_buy;

/**
 * Created by ewu on 2016/11/8.
 */

public class PreSaleGoodsFragment extends FrxsFragment {
    private RecyclerView preSaleGoodsRv;
    private RecyclerAdapter<GetPreSaleProductListRsp.PreSaleProductBean> preSaleGoodsAdapter;
    private List<GetPreSaleProductListRsp.PreSaleProductBean> preSaleGoodsList = new ArrayList<>();
    private List<GetPreSaleProductListRsp.PreSaleProductBean> searchResultList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ClearEditText searchInputEt;
    private int mPageIndex = 1;
    private final int mPageSize = Integer.MAX_VALUE;
    private String searchContent;
    private TextView searchPromptTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pre_sale_goods;
    }

    @Override
    protected void initViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        swipeRefreshLayout.setColorSchemeColors(colors);

        view.findViewById(R.id.search_btn).setOnClickListener(this);
        searchInputEt = (ClearEditText) view.findViewById(R.id.et_search_order);
        searchPromptTv = (TextView) view.findViewById(R.id.search_prompt_tv);

        preSaleGoodsRv = (RecyclerView) view.findViewById(R.id.pre_sale_rv);
        preSaleGoodsRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
//        preSaleGoodsRv.addItemDecoration(new HorizontalItemDecoration
//                .Builder(getContext())
//                .sizeResId(R.dimen.height_explore_divider)
//                .showLastDivider()
//                .build());
        preSaleGoodsAdapter = new RecyclerAdapter<GetPreSaleProductListRsp.PreSaleProductBean>(mActivity, R.layout.item_presale_goods_recyleview) {

            @Override
            protected void convert(RecyclerAdapterHelper helper, final GetPreSaleProductListRsp.PreSaleProductBean item) {
                ImageLoader.loadImage(mActivity, item.getImageExt1(), (ImageView) helper.getView(R.id.goods_pics_iv));
                helper.setText(R.id.goods_desc_tv, item.getProductName());
                helper.setText(R.id.goods_subtitle_tv, item.getProductName2());
                helper.setText(R.id.package_num_tv, String.format(getResources().getString(R.string.package_num), item.getDeliveryPackingQty()));
                helper.setText(R.id.unit_price_tv, String.format(getResources().getString(R.string.unit_price), item.getDeliveryPrice(), item.getDeliveryUnit()));
                // 当前条码为空 显示无
                helper.setText(R.id.code_bar_tv, (TextUtils.isEmpty(item.getBarCode())) ?
                        String.format(getResources().getString(R.string.code_bar),"无") : (String.format(getResources().getString(R.string.code_bar), item.getBarCode().split(",")[0])));
                // 当前建议零售价为0 显示无
                helper.setText(R.id.suggest_retail_price_tv, (item.getMarketPrice() != 0) ?
                        (String.format(getResources().getString(R.string.suggest_retail_price), item.getMarketPrice(), item.getMarketUnit())) : "建议零售价：无");

                if (item.getShopMySaleQty() > 0) {
                    helper.setText(R.id.tv_goods_buy, MathUtils.doubleTrans(item.getShopMySaleQty()));
                } else {
                    helper.setText(R.id.tv_goods_buy, "");
                }
                final TextView tvGoodsBuy = helper.getView(R.id.tv_goods_buy);
                final CountEditText countEditText = helper.getView(R.id.count_edit_text);//商品加减
                // 点击加入购物车
                helper.setOnClickListener(tv_goods_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestAddPreSaleProduct(item.getProductId(), countEditText.getCount(), tvGoodsBuy);
                    }
                });

                // 点击查看图片详情
                helper.setOnClickListener(R.id.goods_pics_iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // 获取图片集合
                        ArrayList<String> imageExtList = new ArrayList<String>();
                        if (!TextUtils.isEmpty(item.getImageExt1())) {
                            imageExtList.add(item.getImageExt1());
                        }
                        if (!TextUtils.isEmpty(item.getImageExt2())) {
                            imageExtList.add(item.getImageExt2());
                        }
                        if (!TextUtils.isEmpty(item.getImageExt3())) {
                            imageExtList.add(item.getImageExt3());
                        }

                        if (imageExtList.size() > 0) {// 展示集合中的图片
                            Intent intent = new Intent(FrxsApplication.context, PreSaleGoodsPhotoViewActivity.class);
                            intent.putStringArrayListExtra("imageExtList", imageExtList);
                            startActivity(intent);
                        }else{
                            ToastUtils.show(mActivity, "当前商品没有图片");
                        }
                    }
                });
            }
        };

        preSaleGoodsRv.setAdapter(preSaleGoodsAdapter);
    }

    @Override
    protected void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchInputEt.setText("");
                searchContent = "";
                requestPreSaleGoodsList();
            }
        });
    }

    @Override
    protected void initData() {
        mActivity.showProgressDialog();
        requestPreSaleGoodsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                searchResultList.clear();
                searchContent = searchInputEt.getText().toString().trim();
                if (!TextUtils.isEmpty(searchContent)) {
                    for (GetPreSaleProductListRsp.PreSaleProductBean item : preSaleGoodsList) {
                        if ((item.getProductName() != null && item.getProductName().contains(searchContent))
                                || (item.getProductName2() != null && item.getProductName2().contains(searchContent))
                                || (item.getBarCode() != null && item.getBarCode().contains(searchContent))
                                || (item.getSKU() != null && item.getSKU().contains(searchContent))) {
                            searchResultList.add(item);
                        }
                    }

                    preSaleGoodsAdapter.replaceAll(searchResultList);
                    if (!TextUtils.isEmpty(searchContent)) {
                        searchPromptTv.setVisibility(View.VISIBLE);
                        searchPromptTv.setText(String.format(getResources().getString(R.string.search_result), searchContent, searchResultList.size()));
                    } else {
                        searchPromptTv.setVisibility(View.GONE);
                    }

                    ImeUtils.hideSoftKeyboard(searchInputEt);
                } else {
                    mActivity.showProgressDialog();
                    requestPreSaleGoodsList();
                }
                break;
            default:
                break;
        }
    }

    private void requestAddPreSaleProduct(final String productId, final int addCount, final TextView tvGoodsBuy) {
        mActivity.showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("UserId", userInfo.getUserId());
        params.put("UserName", userInfo.getUserName());
        params.put("WarehouseId", String.valueOf(userInfo.getCurrenShopInfo().getWID()));
        params.put("ShopID", userInfo.getCurrenShopInfo().getShopID());
        params.put("ProductId", productId);
        params.put("PreOrderQty", addCount);

        getService().AddPreSaleProduct(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    ToastUtils.show(mActivity, "代购商品成功");
                    List<GetPreSaleProductListRsp.PreSaleProductBean> preSaleProductBeanList = preSaleGoodsAdapter.getAll();
                    for (GetPreSaleProductListRsp.PreSaleProductBean item: preSaleProductBeanList) {
                        if (productId.equals(item.getProductId())) {
                            String strCount = tvGoodsBuy.getText().toString().trim();
                            double count = addCount;
                            if (!TextUtils.isEmpty(strCount)) {
                                count += Double.valueOf(strCount);
                            }
                            tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                            break;
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(result.getInfo())) {
                        ToastUtils.show(mActivity, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    private void requestPreSaleGoodsList() {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();

        AjaxParams params = new AjaxParams();
        params.put("UserId", userInfo.getUserId());
        params.put("UserName", userInfo.getUserName());
        params.put("WarehouseId", String.valueOf(userInfo.getCurrenShopInfo().getWID()));
        params.put("ShopID", userInfo.getCurrenShopInfo().getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);

        getService().GetPreSaleProductList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<GetPreSaleProductListRsp>>() {
            @Override
            public void onResponse(ApiResponse<GetPreSaleProductListRsp> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (result.getFlag().equals("0")) {
                    GetPreSaleProductListRsp getPreSaleProductListRsp = result.getData();
                    if (null != getPreSaleProductListRsp) {
                        List<GetPreSaleProductListRsp.PreSaleProductBean> preSaleProductBeanList = getPreSaleProductListRsp.getItemList();
                        if (mPageIndex == 1) {
                            preSaleGoodsList.clear();
                            preSaleGoodsList.addAll(preSaleProductBeanList);
                            preSaleGoodsAdapter.replaceAll(preSaleGoodsList);
                        } else {
                            preSaleGoodsList.addAll(preSaleProductBeanList);
                            preSaleGoodsAdapter.addAll(preSaleProductBeanList);
                        }

                        if (!TextUtils.isEmpty(searchContent)) {
                            searchPromptTv.setVisibility(View.VISIBLE);
                            searchPromptTv.setText(String.format(getResources().getString(R.string.search_result), searchContent, getPreSaleProductListRsp.getTotalCount()));
                        } else {
                            searchPromptTv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GetPreSaleProductListRsp>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
