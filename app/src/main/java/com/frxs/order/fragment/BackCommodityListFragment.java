package com.frxs.order.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.PreSaleGoodsPhotoViewActivity;
import com.frxs.order.R;
import com.frxs.order.model.ApplySaleBackInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/7.
 * 退货申请单商品详情页
 */

public class BackCommodityListFragment extends FrxsFragment {

    private ListView commodityList;

    private Adapter<ApplySaleBackInfo.DetailBean> adapter;
    private ApplySaleBackInfo.OrderBean order;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commodity_list;
    }

    @Override
    protected void initViews(View view) {
        commodityList = (ListView) view.findViewById(R.id.lv_commodity_list);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        adapter = new Adapter<ApplySaleBackInfo.DetailBean>(mActivity, R.layout.item_apply_goods) {
            @Override
            protected void convert(final AdapterHelper helper, final ApplySaleBackInfo.DetailBean item) {
                helper.setText(R.id.tv_goods_name, item.getProductName());
                helper.setText(R.id.tv_goods_sku, "编码:" + item.getSKU());
                helper.setText(R.id.tv_good_code, "条码:" + item.getBarCode().split(",")[0]);
                helper.setText(R.id.tv_good_amt, "配送价:￥" + MathUtils.twolittercountString(item.getSalePrice()) + "/" + item.getBackUnit());
                //设置退货商品退货积分
                helper.setText(R.id.tv_good_point, "小计退货积分:" + MathUtils.twolittercountString(item.getSubPoint()));
                helper.setText(R.id.tv_good_count, "申请数量:" + MathUtils.doubleTrans(MathUtils.round(item.getBackQty(), 2)) + item.getBackUnit());

                /**
                 * 显示隐藏字段
                 */
                helper.setVisible(R.id.tv_reason_name, (TextUtils.isEmpty(item.getBackReasonName()) ? View.GONE : View.VISIBLE));
                helper.setVisible(R.id.tv_reason_des, (TextUtils.isEmpty(item.getBackReasonDes()) ? View.GONE : View.VISIBLE));
                helper.setVisible(R.id.tv_conf_remark, (TextUtils.isEmpty(item.getConfRemark()) ? View.GONE : View.VISIBLE));

                /**
                 * 给条目赋值
                 */
                if (item.getConfStatus() == 1 && (order.getStatus() == 5 || order.getStatus() == 6)){//当前退货申请单状态为等待收货时显示商品收货数量
                    helper.setText(R.id.tv_good_status, "收货数量:" + MathUtils.doubleTrans(MathUtils.round(item.getTakeBackQty(), 2)) + item.getTakeBackUnit());
                } else {
                    helper.setText(R.id.tv_good_status, "确认结果:" + item.getConfStatusName());
                    helper.setVisible(R.id.tv_good_status, (TextUtils.isEmpty(item.getConfStatusName()) ? View.GONE : View.VISIBLE));
                }
                helper.setText(R.id.tv_reason_des, "问题描述:" + item.getBackReasonDes());
                helper.setText(R.id.tv_reason_name, "申请原因:" + item.getBackReasonName());
                helper.setText(R.id.tv_conf_remark, "确认原因:" + (item.getConfRemark() != null ? item.getConfRemark() : ""));


                /**
                 * 查看更多 
                 */
                helper.setOnClickListener(R.id.tv_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView view = helper.getView(R.id.tv_more);
                        boolean isExpanded = helper.getView(R.id.ll_more_info).isShown();
                        if (isExpanded) {
                            helper.setVisible(R.id.ll_more_info, View.GONE);
                        } else {
                            helper.setVisible(R.id.ll_more_info, View.VISIBLE);
                        }
                        view.setSelected(!isExpanded);
                    }
                });

                /**
                 * 查看附件
                 */
                helper.setOnClickListener(R.id.tv_check_file, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqGetDetailsAttsByApplyBackID(item);
                    }
                });
            }
        };

        commodityList.setAdapter(adapter);
    }

    private void reqGetDetailsAttsByApplyBackID(ApplySaleBackInfo.DetailBean item) {
        mActivity.showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("ApplyBackID", item.getApplyBackID());
        params.put("ApplyBackDetailID", item.getID());
        params.put("WarehouseId", getWID());
        getService().GetDetailsAttsByApplyBackID(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ArrayList<String>>>() {
            @Override
            public void onResponse(ApiResponse<ArrayList<String>> result, int code, String msg) {
                if (result.isSuccessful()) {
                    ArrayList<String> attachedPicList = result.getData();
                    if (null != attachedPicList && attachedPicList.size() > 0) {
                        Intent intent = new Intent(mActivity, PreSaleGoodsPhotoViewActivity.class);
                        intent.putStringArrayListExtra("imageExtList", attachedPicList);
                        startActivity(intent);
                    } else {
                        ToastUtils.show(mActivity, "没有上传附件");
                    }
                } else {
                    ToastUtils.show(mActivity, "请求数据失败：" + result.getInfo());
                }

                mActivity.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ApiResponse<ArrayList<String>>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtils.show(mActivity, "请求数据出错：" + t.getMessage());
                mActivity.dismissProgressDialog();
            }
        });
    }

    /**
     * 设置订单信息
     */
    public void setData(final ApplySaleBackInfo applyInfo) {
        if (applyInfo != null) {
            List<ApplySaleBackInfo.DetailBean> backDetail = applyInfo.getDetail();
            order = applyInfo.getOrder();
            if (backDetail != null && backDetail.size() > 0){
                adapter.replaceAll(backDetail);
            } else {
                ToastUtils.show(mActivity, "未找到该退货申请单的商品数据...");
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
