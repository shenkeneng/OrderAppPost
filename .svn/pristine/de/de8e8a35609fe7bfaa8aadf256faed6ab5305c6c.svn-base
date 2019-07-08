package com.frxs.order.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.BackOrderInfoActivity;
import com.frxs.order.R;
import com.frxs.order.model.SaleBackOrder;
import com.frxs.order.model.SaleBackOrderList;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import java.util.List;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/10.
 * 退货单
 */

public class BackOrderFragment extends MaterialStyleFragment {

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private ListView lvOrderList;

    private EmptyView emptyView;

    private Adapter<SaleBackOrderList> applyAdapter;// 退货申请单列表

    private int mPageIndex = 1;

    private final int mPageSize = 30;

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_pre;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        emptyView = (EmptyView) view.findViewById(R.id.emptyview);
        lvOrderList = (ListView) view.findViewById(R.id.lv_order_list);
    }

    @Override
    protected void initEvent() {
        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                // 请求数据
                reqSaleBackData();
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, lvOrderList, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                 mPageIndex = 1;
                // 请求数据
                reqSaleBackData();
            }
        });
    }

    @Override
    protected void initData() {
        reqSaleBackData();
        applyAdapter = new Adapter<SaleBackOrderList>(mActivity, R.layout.item_return_order) {
            @Override
            protected void convert(AdapterHelper helper, final SaleBackOrderList item) {
                helper.setVisible(R.id.ll_choose, View.GONE);
                helper.setText(R.id.tv_order_time, item.getPostingTime());
                helper.setText(R.id.tv_order_id, item.getBackID());
                helper.setText(R.id.tv_order_amount, "退货金额：" + MathUtils.twolittercountString(item.getPayAmount()));
                helper.setVisible(R.id.tv_order_point, View.VISIBLE);
                //设置退货单退货积分(单位积分 * 退货数量) 显示退货积分字段
                helper.setText(R.id.tv_order_point, "退货积分：" + MathUtils.twolittercountString(item.getTotalPoint()));
                TextView tvStateImg = helper.getView(R.id.tv_order_state);
                if (item.getStatus() == 2){
                    tvStateImg.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_state_no, 0, R.drawable.selector_right, 0);
                } else {
                    tvStateImg.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_state_yes, 0, R.drawable.selector_right, 0);
                }
                helper.setOnClickListener(R.id.ll_check_info, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, BackOrderInfoActivity.class);
                        intent.putExtra("ORDERID", item.getBackID());
                        intent.putExtra("BACK_QTY", MathUtils.doubleTrans(MathUtils.round(item.getTotalBackQty(), 2)));
                        intent.putExtra("BACK_STATUS", item.getStatus());
                        startActivity(intent);
                    }
                });
            }
        };
        lvOrderList.setAdapter(applyAdapter);
    }

    private void reqSaleBackData() {
        mActivity.showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("UserId", getUserID());
        params.put("StatusShop", 1);
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);

        getService().GetSaleBackList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<SaleBackOrder>>() {
            @Override
            public void onResponse(ApiResponse<SaleBackOrder> result, int code, String msg) {
                refreshComplete();
                if (result.getFlag().equals("0")){
                    SaleBackOrder saleBackOrder = result.getData();
                    if (saleBackOrder != null) {
                        List<SaleBackOrderList> itemList = saleBackOrder.getItemList();
                        if (itemList != null && itemList.size() > 0){
                            emptyView.setVisibility(View.GONE);
                            if (mPageIndex == 1) {
                                applyAdapter.replaceAll(itemList);
                            } else {
                                applyAdapter.addAll(itemList);
                            }

                            boolean hasMoreItems = (applyAdapter.getCount() < saleBackOrder.getTotalRecords());
                            mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                        } else {
                            initNoData();
                        }
                    } else {
                        initNoData();
                    }

                } else {
                    initNoData();
                    ToastUtils.show(mActivity, result.getInfo());
                }
                mActivity.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ApiResponse<SaleBackOrder>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqSaleBackData();
                    }
                });
                mActivity.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void initNoData() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_NODATA);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqSaleBackData();
            }
        });
    }
}
