package com.frxs.order.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.BackApplyInfoAcitvity;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.ApplySaleBackOrder;
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
 * 退货申请单
 */

public class BackApplyOrderFragment extends MaterialStyleFragment {

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private ListView lvOrderList;

    private EmptyView emptyView;

    private Adapter<ApplySaleBackOrder.ApplyForSaleBackListBean> applyAdapter;// 退货申请单列表

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
    public void onResume() {
        super.onResume();
        reqApplySaleBackList();
    }

    @Override
    protected void initEvent() {
        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                // 请求数据
                reqApplySaleBackList();
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
                // 请求数据
                reqApplySaleBackList();
            }
        });
    }

    @Override
    protected void initData() {
        reqApplySaleBackList();

        applyAdapter = new Adapter<ApplySaleBackOrder.ApplyForSaleBackListBean>(mActivity, R.layout.item_return_order) {
            @Override
            protected void convert(AdapterHelper helper, final ApplySaleBackOrder.ApplyForSaleBackListBean item) {
                helper.setText(R.id.tv_order_time, item.getCreateTime());//订单时间
                helper.setText(R.id.tv_order_id, item.getApplyBackID());//订单ID
                helper.setVisible(R.id.tv_order_state, View.VISIBLE);
                helper.setVisible(R.id.tv_order_point, View.GONE);

                if (item.getTotalBackTotalAmt() > -1){
                    helper.setText(R.id.tv_order_amount, "退货金额:￥" + MathUtils.twolittercountString(item.getTotalBackTotalAmt()));
                } else {
                    helper.setText(R.id.tv_order_amount, "退货金额:￥" + MathUtils.twolittercountString(item.getPayAmount()));
                }
                /**
                 * 显示订单状态及取消订单按钮
                 * 1:等待确认;2:等待司机取货;5:司机已取货;6:仓库收货中;7:仓库收货完成;8:客户取消申请;9:后台取消申请（申请驳回，表示所有明细都没确认通过）
                 */
                switch (item.getStatus()){
                    case 1://1:等待确认;
                        helper.setText(R.id.tv_order_state, "等待确认");//订单状态
                        helper.setVisible(R.id.ll_choose, View.VISIBLE);
                        break;

                    case 2://2:等待带回;
                        helper.setText(R.id.tv_order_state, "等待取货");//订单状态
                        helper.setVisible(R.id.ll_choose, View.VISIBLE);
                        break;

                    case 5://5:等待收货;
                        helper.setText(R.id.tv_order_state, "等待收货");//订单状态
                        helper.setVisible(R.id.ll_choose, View.GONE);
                        break;

                    case 6://6:等待收货;
                        helper.setText(R.id.tv_order_state, "等待收货");//订单状态
                        helper.setVisible(R.id.ll_choose, View.GONE);
                        break;

                    case 7://7:已过账
                        helper.setText(R.id.tv_order_state, "已过账");//订单状态
                        helper.setVisible(R.id.ll_choose, View.GONE);
                        break;

                    case 8://8：退货取消
                        helper.setText(R.id.tv_order_state, "退货取消");//订单状态
                        helper.setVisible(R.id.ll_choose, View.GONE);
                        break;

                    case 9://9：退货取消
                        helper.setText(R.id.tv_order_state, "退货取消");//订单状态
                        helper.setVisible(R.id.ll_choose, View.GONE);
                        break;

                    default://未知状态
                        helper.setText(R.id.tv_order_state, "未知状态");//订单状态
                        helper.setVisible(R.id.ll_choose, View.GONE);
                        break;
                }

                /**
                 * 取消退货申请
                 */
                helper.setOnClickListener(R.id.tv_cancel_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mActivity.isShowingProgressDialog()) {
                            final MaterialDialog dialog = new MaterialDialog(mActivity);
                            dialog.setMessage("是否确认取消退货申请？");
                            dialog.setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            //删除该订单
                                            CancelApplySaleBackOrder(item.getApplyBackID());
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
                 * 查看申请详情
                 */
                helper.setOnClickListener(R.id.ll_check_info, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, BackApplyInfoAcitvity.class);
                        intent.putExtra("APPLY_BACK_ID", item.getApplyBackID());
                        startActivity(intent);
                    }
                });
            }
        };
        lvOrderList.setAdapter(applyAdapter);
    }

    /**
     * 门店退货申请取消
     * @param applyBackId
     */
    private void CancelApplySaleBackOrder(String applyBackId) {
        mActivity.showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("BackID", applyBackId);
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserId", getUserID());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());

        getService().CancelApplySaleBack(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(ApiResponse<Boolean> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")){
                    ToastUtils.show(mActivity, "退货申请单已取消");
                    reqApplySaleBackList();
                } else {
                    ToastUtils.show(mActivity, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtils.show(mActivity, t.getMessage());
                mActivity.dismissProgressDialog();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqApplySaleBackList();
                    }
                });
            }
        });
    }

    /**
     * 获取退货申请单分页数据
     */
    private void reqApplySaleBackList() {
        mActivity.showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        params.put("UserId", getUserID());

        getService().GetApplySaleBackList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ApplySaleBackOrder>>() {

            @Override
            public void onResponse(ApiResponse<ApplySaleBackOrder> result, int code, String msg) {
                refreshComplete();
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")){
                    ApplySaleBackOrder orders = result.getData();
                    if (orders != null){
                        if (orders.getApplyForSaleBackList() != null && orders.getApplyForSaleBackList().size() > 0){
                            emptyView.setVisibility(View.GONE);
                            List<ApplySaleBackOrder.ApplyForSaleBackListBean> applyForSaleBackList = orders.getApplyForSaleBackList();
                            applyAdapter.replaceAll(applyForSaleBackList);
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
            }

            @Override
            public void onFailure(Call<ApiResponse<ApplySaleBackOrder>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqApplySaleBackList();
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
                reqApplySaleBackList();
            }
        });
    }
}
