package com.frxs.order.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.MyPreSaleProductListRsp;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.ClearEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by ewu on 2016/11/8.
 */

public class MyPreSaleGoodsFragment extends MaterialStyleFragment {
    private ListView preSaleGoodsRv;
    private Adapter<MyPreSaleProductListRsp.MyPreSaleProductBean> preSaleGoodsAdapter;
    private List<MyPreSaleProductListRsp.MyPreSaleProductBean> preSaleGoodsList = new ArrayList<>();
    private int mPageIndex = 1;
    private final int mPageSize = 10;
    private PtrFrameLayoutEx mPtrFrameLayoutEx;
    private View filterBtn;
    private ClearEditText searchInputEt;
    private TextView searchPromptTv;
    private String searchContent;
    private Integer filterCondition = null; //全部是空值，已处理是1，未处理是0

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_pre_sale_goods;
    }

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        searchInputEt = (ClearEditText) view.findViewById(R.id.et_search_order);
        searchPromptTv = (TextView) view.findViewById(R.id.search_prompt_tv);
        view.findViewById(R.id.search_btn).setOnClickListener(this);
        filterBtn = view.findViewById(R.id.filte_btn);
        filterBtn.setOnClickListener(this);

        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        preSaleGoodsRv = (ListView) view.findViewById(R.id.pre_sale_rv);
        preSaleGoodsAdapter = new Adapter<MyPreSaleProductListRsp.MyPreSaleProductBean>(mActivity, R.layout.item_presale_goods_view) {
            @Override
            protected void convert(AdapterHelper helper, final MyPreSaleProductListRsp.MyPreSaleProductBean item) {
                helper.setText(R.id.order_date_tv, item.getPreOrderDate().split(" ")[0]);
                if (item.getStatus() == 1) {
                    helper.setText(R.id.order_status_tv, R.string.status_done);
                    helper.setVisible(R.id.delete_btn, View.INVISIBLE);
                } else {
                    helper.setText(R.id.order_status_tv, R.string.status_pending);
                    helper.setVisible(R.id.delete_btn, View.VISIBLE);
                }
                helper.setText(R.id.product_name_tv, item.getProductName());
                helper.setText(R.id.goods_subtitle_tv, item.getProductName2());
                //helper.setText(R.id.code_bar_tv, item.getBarCode());
                helper.setText(R.id.package_num_tv, String.format(getResources().getString(R.string.package_num), item.getPackingQty()));
                helper.setText(R.id.unit_price_tv, String.format(getResources().getString(R.string.order_price), item.getDeliveryPrice(), item.getDeliveryUnit(), item.getPreOrderQty()));
                // 当前条码为空 显示无
                helper.setText(R.id.code_bar_tv, (TextUtils.isEmpty(item.getBarCode())) ?
                        String.format(getResources().getString(R.string.code_bar),"无") : (String.format(getResources().getString(R.string.code_bar), item.getBarCode().split(",")[0])));
                // 当前建议零售价为0 显示无
                helper.setText(R.id.suggest_retail_price_tv, (item.getMarketPrice() != 0) ?
                        (String.format(getResources().getString(R.string.suggest_retail_price), item.getMarketPrice(), item.getMarketUnit())) : "建议零售价：无");

                helper.setOnClickListener(R.id.delete_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog dialog = new MaterialDialog(mActivity);
                        dialog.setMessage("是否删除此商品？");
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        requestModifyOrDelPreSaleProduct(item.getPreSaleProductId());
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
                });
            }
        };

        preSaleGoodsRv.setAdapter(preSaleGoodsAdapter);
    }

    @Override
    protected void initEvent() {
        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                requestMyPreSaleGoodsList();
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, preSaleGoodsRv, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                requestMyPreSaleGoodsList();
            }
        });
    }

    @Override
    protected void initData() {
        mActivity.showProgressDialog();
        requestMyPreSaleGoodsList();
    }

    public void refreshData() {
        requestMyPreSaleGoodsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn: {
                mPageIndex = 1;
                searchContent = searchInputEt.getText().toString().trim();
                mActivity.showProgressDialog();
                requestMyPreSaleGoodsList();
                break;
            }
            case R.id.filte_btn: {
                showPopMenu();
                break;
            }
            default:
                break;
        }
    }

    private void showPopMenu() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.window_popup, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(filterBtn);
        contentView.findViewById(R.id.menu_all_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCondition = null;
                mActivity.showProgressDialog();
                requestMyPreSaleGoodsList();
                popupWindow.dismiss();
                filterBtn.setBackgroundResource(R.drawable.shape_button_red);
            }
        });
        contentView.findViewById(R.id.menu_done_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCondition = 1;
                mActivity.showProgressDialog();
                requestMyPreSaleGoodsList();
                popupWindow.dismiss();
                filterBtn.setBackgroundResource(R.drawable.shape_button_red_highlight);
            }
        });
        contentView.findViewById(R.id.menu_pending_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCondition = 0;
                mActivity.showProgressDialog();
                requestMyPreSaleGoodsList();
                popupWindow.dismiss();
                filterBtn.setBackgroundResource(R.drawable.shape_button_red_highlight);
            }
        });
    }

    private void requestModifyOrDelPreSaleProduct(final String productId) {
        mActivity.showProgressDialog();

        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("UserId", userInfo.getUserId());
        params.put("UserName", userInfo.getUserName());
        params.put("WarehouseId", String.valueOf(userInfo.getCurrenShopInfo().getWID()));
        params.put("ShopID", userInfo.getCurrenShopInfo().getShopID());
        params.put("ProductId", productId);
        params.put("Type", "1");

        getService().ModifyOrDelPreSaleProduct(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    for (MyPreSaleProductListRsp.MyPreSaleProductBean item : preSaleGoodsList) {
                        if (productId.equals(item.getPreSaleProductId())) {
                            preSaleGoodsList.remove(item);
                            preSaleGoodsAdapter.replaceAll(preSaleGoodsList);
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

    private void requestMyPreSaleGoodsList() {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();

        AjaxParams params = new AjaxParams();
        params.put("UserId", userInfo.getUserId());
        params.put("UserName", userInfo.getUserName());
        params.put("WarehouseId", String.valueOf(userInfo.getCurrenShopInfo().getWID()));
        params.put("ShopId", userInfo.getCurrenShopInfo().getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);
        if (!TextUtils.isEmpty(searchContent)) {
            params.put("Search", searchContent);
        }
        if (null != filterCondition && (filterCondition == 0 || filterCondition == 1)) {
            params.put("Status", filterCondition);
        }

        getService().GetMyPreSaleProductList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<MyPreSaleProductListRsp>>() {
            @Override
            public void onResponse(ApiResponse<MyPreSaleProductListRsp> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                refreshComplete();
                if (result.getFlag().equals("0")) {
                    MyPreSaleProductListRsp getPreSaleProductListRsp = result.getData();
                    if (null != getPreSaleProductListRsp) {
                        if (mPageIndex == 1) {
                            preSaleGoodsList.clear();
                        }
                        preSaleGoodsList.addAll(getPreSaleProductListRsp.getItemList());
                        preSaleGoodsAdapter.replaceAll(preSaleGoodsList);
                        if (!TextUtils.isEmpty(searchContent)) {
                            searchPromptTv.setVisibility(View.VISIBLE);
                            searchPromptTv.setText(String.format(getResources().getString(R.string.search_result), searchContent, getPreSaleProductListRsp.getTotalCount()));
                        } else {
                            searchPromptTv.setVisibility(View.GONE);
                        }

                        boolean hasMoreItems = (preSaleGoodsList.size() < getPreSaleProductListRsp.getTotalCount());
                        mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                    }
                } else {
                    preSaleGoodsList.clear();
                    preSaleGoodsAdapter.replaceAll(preSaleGoodsList);
                    if (!TextUtils.isEmpty(result.getInfo())) {
                        ToastUtils.show(mActivity, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MyPreSaleProductListRsp>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
                refreshComplete();
            }
        });
    }
}
