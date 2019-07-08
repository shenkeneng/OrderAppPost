package com.frxs.order.fragment;

import android.view.View;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.NoScrollListView;
import com.frxs.order.BackApplyInfoAcitvity;
import com.frxs.order.R;
import com.frxs.order.adapter.ApplyRoutlItemAdapter;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.ApplySaleBackInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import java.util.List;
import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/7.
 * 退货申请单退货信息页
 */

public class BackInformationFragment extends FrxsFragment {

    private TextView backChange;

    private TextView backIdTv;// 退货申请单ID

    private TextView backCountTv;// 退货商品总数量

    private TextView backAmtTv;// 退货总金额

    private NoScrollListView backTrackLv;

    private ApplyRoutlItemAdapter.OrderOptionListener mListener;

    private String applyBackId;

    private TextView backPointTv;// 退货积分

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_information;
    }

    @Override
    protected void initViews(View view) {
        // 隐藏正常订单信息栏
        view.findViewById(R.id.ll_order_info).setVisibility(View.GONE);
        view.findViewById(R.id.ll_order_choose).setVisibility(View.GONE);
        view.findViewById(R.id.ll_tel).setVisibility(View.GONE);
        // 显示退货订单信息栏
        view.findViewById(R.id.ll_return_info).setVisibility(View.VISIBLE);
        backChange = (TextView) view.findViewById(R.id.btn_back_change);
        backChange.setVisibility(View.VISIBLE);
        backIdTv = (TextView) view.findViewById(R.id.tv_back_id);
        backCountTv = (TextView) view.findViewById(R.id.tv_back_count);
        backAmtTv = (TextView) view.findViewById(R.id.tv_back_amt);
        backPointTv = (TextView) view.findViewById(R.id.tv_back_point);
        backTrackLv = (NoScrollListView) view.findViewById(R.id.lv_order_track);
    }

    @Override
    protected void initEvent() {
        backChange.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    /**
     * 设置订单信息
     */
    public void setData(final ApplySaleBackInfo applyInfo) {
        if (applyInfo != null) {
            if (applyInfo.getOrder() != null){
                ApplySaleBackInfo.OrderBean applyOrder = applyInfo.getOrder();
                applyBackId = applyOrder.getApplyBackID();
                backIdTv.setText(applyBackId);
                if (applyOrder.getTakeBackTotalQty() > -1) {
                    backCountTv.setText(String.valueOf(MathUtils.doubleTrans(MathUtils.round(applyOrder.getTakeBackTotalQty(), 2))));
                } else {
                    backCountTv.setText(String.valueOf(MathUtils.doubleTrans(MathUtils.round(applyOrder.getTotalBackQty(), 2))));
                }
                if (applyOrder.getTotalBackTotalAmt() > -1){
                    backAmtTv.setText("￥" + MathUtils.twolittercountString(applyOrder.getTotalBackTotalAmt()));
                } else {
                    backAmtTv.setText("￥" + MathUtils.twolittercountString(applyOrder.getPayAmount()));
                }
                //设置退货申请单退货积分
                backPointTv.setText(MathUtils.twolittercountString(applyOrder.getTotalPoint()));
                List<ApplySaleBackInfo.TracksBean> backTracks = applyInfo.getTracks();
                if (backTracks != null && backTracks.size() > 0) {
                    ApplyRoutlItemAdapter itemAdapter = new ApplyRoutlItemAdapter(mActivity, applyInfo.getTracks(), mListener);
                    itemAdapter.notifyDataSetChanged();
                    backTrackLv.setAdapter(itemAdapter);

                    //客户交易取消
                    if (applyOrder.getStatus() > 2) {
                        backChange.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back_change:// 取消退货申请
                if (!mActivity.isShowingProgressDialog()) {
                    final MaterialDialog dialog = new MaterialDialog(mActivity);
                    dialog.setMessage("是否确认取消退货申请？");
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    //删除该订单
                                    CancelApplySaleBackOrder();
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
                break;
        }
    }

    /**
     * 门店退货申请取消
     */
    private void CancelApplySaleBackOrder() {
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
                    ((BackApplyInfoAcitvity)mActivity).refresh();
                } else {
                    ToastUtils.show(mActivity, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtils.show(mActivity, t.getMessage());
                mActivity.dismissProgressDialog();
            }
        });
    }
}
