package com.frxs.order.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewu.core.utils.EasyPermissionsEx;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.NoScrollListView;
import com.frxs.order.ModifyOrderActivity;
import com.frxs.order.R;
import com.frxs.order.adapter.RouteItemAdapter;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.Details;
import com.frxs.order.model.OrderShopGetRespData;
import com.frxs.order.model.OrderTrack;
import com.frxs.order.model.Orders;
import com.frxs.order.model.PostOrderCancel;
import com.frxs.order.model.SaleOrderGetTrackRespData;
import com.frxs.order.model.UserInfo;
import com.frxs.order.model.Warehouse;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * 订单信息 by Tiepier
 */
public class OrderInformationFragment extends FrxsFragment {

    private TextView tvOrderID;//订单编号

    private TextView tvOrderCount;//商品总数

    private TextView tvShopIntegral;//门店积分

    private TextView tvOrderAmount;//订单总金额

    private TextView tvOrderRemark;//整单备注

//    private TextView btnOrderBuy;//再次购买

    private TextView btnOrderConfirm;//确认收货

    private TextView btnOrderComplaint;//订单投诉

    private TextView btnOrderClose;//订单关闭

    private TextView btnOrderChange;//订单修改

    private NoScrollListView lvOrderTrack;//订单跟踪

    private RouteItemAdapter.OrderOptionListener mListener;

    private String strOrderID;

    private OrderShopGetRespData orderDetails;

    private TextView tvOrderAccount;//结算方式

    private TextView tvOrderPay;

    private String servicePhone = "400600200";// 客服电话

    private String shippingDate;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_information;
    }

    @Override
    protected void initViews(View view) {
        /**
         * 实例化控件
         */
        tvOrderID = (TextView) view.findViewById(R.id.tv_order_id);
        tvOrderCount = (TextView) view.findViewById(R.id.tv_order_count);
        tvShopIntegral = (TextView) view.findViewById(R.id.tv_shop_integral);
        tvOrderAmount = (TextView) view.findViewById(R.id.tv_order_amount);
        tvOrderRemark = (TextView) view.findViewById(R.id.tv_order_remark);
        btnOrderComplaint = (TextView) view.findViewById(R.id.btn_order_complaint);
        btnOrderClose = (TextView) view.findViewById(R.id.btn_order_close);
        btnOrderChange = (TextView) view.findViewById(R.id.btn_order_change);
//        btnOrderBuy = (TextView) view.findViewById(R.id.btn_order_buy);
        btnOrderConfirm = (TextView) view.findViewById(R.id.btn_order_confirm);
        lvOrderTrack = (NoScrollListView) view.findViewById(R.id.lv_order_track);
        //TODO:根据接口返回的数据显示订单的结算方式
        //tvOrderAccount = (TextView) view.findViewById(R.id.tv_order_account);
        //TODO:根据接口返回的数据显示订单的付款状态
        //tvOrderPay = (TextView) view.findViewById(R.id.tv_order_pay);
        //弹跳拨打电话的对话框
        LinearLayout llTel = (LinearLayout) view.findViewById(R.id.ll_tel);
        llTel.setOnClickListener(this);
        lvOrderTrack.setFocusable(false);
        /**
         * 获取订单ID
         */
        Bundle bundle = getArguments();
        if (bundle != null) {
            strOrderID = bundle.getString("ORDERID");
            shippingDate = bundle.getString("SHIPPING_DATE");
        }
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        reqOrderTrack();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tel:
                ShowDialogTel(servicePhone);
                break;
        }

    }

    /**
     * 设置订单信息
     */
    public void setData(final OrderShopGetRespData orderDetails) {
        if (orderDetails != null) {
            final Orders orders = orderDetails.getOrder();
            final List<Details> detailses = orderDetails.getDetails();
            tvOrderID.setText(orders.getOrderId());//订单编号
            tvOrderCount.setText(String.valueOf(orders.getTotalProductCount()));//商品数量
            tvShopIntegral.setText(MathUtils.twolittercountString(orders.getTotalPoint()));//门店积分
            tvOrderAmount.setText("￥" + MathUtils.twolittercountString(orders.getPayAmount()));//订单总金额
            if (!TextUtils.isEmpty(orders.getRemark())) {
                tvOrderRemark.setText(Html.fromHtml(orders.getRemark()));
            }

            /**
             * 订单状态操作 订单状态(0:草稿(代客下单才有);1:等待确认;2:等待拣货;3:正在拣货;4:拣货完成;5:打印完成;6:正在配送中;7:交易完成;8:客户交易取消;9:客服交易关闭）
             */
            switch (orders.getStatus()) {
                case 1:
                    btnOrderClose.setVisibility(View.VISIBLE);
//                    btnOrderBuy.setVisibility(View.GONE);
                    btnOrderChange.setVisibility(View.VISIBLE);
                    btnOrderComplaint.setVisibility(View.GONE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;
                case 2:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;
                case 3:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;
                case 4:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;
                case 5:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;
                case 6:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(shippingDate)) {// 配送时间
                        btnOrderConfirm.setVisibility(View.VISIBLE);//确认收货
                    } else {
                        btnOrderConfirm.setVisibility(View.GONE);//确认收货
                    }
                    break;
                case 7:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;
                case 8:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;
                case 9:
                    btnOrderClose.setVisibility(View.GONE);
//                    btnOrderBuy.setVisibility(View.VISIBLE);
                    btnOrderChange.setVisibility(View.GONE);
                    btnOrderComplaint.setVisibility(View.VISIBLE);
                    btnOrderConfirm.setVisibility(View.GONE);
                    break;

            }
            /**
             * 订单操作事件处理
             */
            //作废订单
            btnOrderClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                    materialDialog.setMessage("确定作废订单：" + orders.getOrderId());
                    materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<String> orderCancelList = new ArrayList<>();
                            orderCancelList.add(orders.getOrderId());
                            reqOrderCancel(orderCancelList);
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.show();
                }
            });
//            //再次购买
//            btnOrderBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final MaterialDialog materialDialog = new MaterialDialog(mActivity);
//                    materialDialog.setMessage("确定再次购买？");
//                    materialDialog.setPositiveButton("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            reqOrderReBuy(orders.getOrderId(), detailses);
//                            materialDialog.dismiss();
//                        }
//                    });
//                    materialDialog.setNegativeButton("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            materialDialog.dismiss();
//                        }
//                    });
//                    materialDialog.show();
//                }
//            });
            //修改订单
            btnOrderChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                    materialDialog.setMessage("确定修改订单？");
                    materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<Details> detailsList = orderDetails.getDetails();
                            Intent intent = new Intent(mActivity, ModifyOrderActivity.class);
                            intent.putExtra("ORDER", (Serializable) detailsList);
                            intent.putExtra("ORDERID", strOrderID);
                            if (!TextUtils.isEmpty(orders.getRemark())) {
                                intent.putExtra("REMARK", orders.getRemark().toString());
                            }
                            if(!TextUtils.isEmpty(orders.getOrderDate())){
                                intent.putExtra("DATE", orders.getOrderDate());
                            }
                            startActivity(intent);
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.show();

                }
            });
            //投诉订单
            btnOrderComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reqWarehouse();
                }
            });
            //确认收货
            btnOrderConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                    materialDialog.setMessage("是否确认收货？");
                    materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reqOrderFinish(orders.getOrderId());
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.show();
                }
            });
        }
    }


    /**
     * 确认收货数据请求
     */
    private void reqOrderFinish(String strOrderID) {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("OrderId", strOrderID);
        params.put("ShopId", getShopID());
        params.put("SubWID", "");
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserID", getUserID());
        params.put("UserName", userInfo.getUserName());
        getService().OrderPreFinished(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    btnOrderConfirm.setVisibility(View.GONE);
                    reqOrderTrack();
                } else {
                    ToastUtils.show(mActivity, result.getInfo());
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

    /**
     * 关闭订单(调用后台取消订单接口)
     */
    public void reqOrderCancel(List<String> OrderIdList) {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
//        AjaxParams params = new AjaxParams();
//        params.put("UserID", getUserID());
//        params.put("WarehouseId", getWID());
//        params.put("Status", "8");//订单状态 8或9
//        params.put("UserName", userInfo.getUserName());
//        params.put("OrderIdList", OrderIdList);
        PostOrderCancel orderCancel = new PostOrderCancel();
        orderCancel.setUserID(getUserID());
        orderCancel.setWarehouseId(getWID());
        orderCancel.setStatus("8");
        orderCancel.setCloseReason("");
        orderCancel.setUserName(userInfo.getUserName());
        orderCancel.setOrderIdList(OrderIdList);

        getService().OrderShopCancel(orderCancel).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    reqOrderTrack();
                } else {
                    ToastUtils.show(mActivity, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });

    }

    /**
     * 再次购买数据请求
     */
    private void reqOrderReBuy(String strOrderID, final List<Details> detailses) {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("OrderId", strOrderID);
        params.put("ShopId", getShopID());
        params.put("SubWID", "");
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserID", getUserID());
        params.put("UserName", userInfo.getUserName());
        getService().OrderReBuy(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    ToastUtils.show(mActivity, "加入购物车成功");
                    int count = 0;
                    if (null != detailses && detailses.size() > 0) {
                        for (Details item : detailses) {
                            count += item.getSaleQty();
                        }
                    }
                    FrxsApplication.getInstance().addShopCartCount(count);
                } else {
                    ToastUtils.show(mActivity, result.getInfo());
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    /**
     * 订单跟踪数据请求
     */
    public void reqOrderTrack() {
        mActivity.showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("OrderId", strOrderID);
        params.put("ShopID", getShopID());
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserName", userInfo.getUserName());
        getService().SaleOrderGetTrack(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<SaleOrderGetTrackRespData>>() {
            @Override
            public void onResponse(ApiResponse<SaleOrderGetTrackRespData> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                SaleOrderGetTrackRespData trackRespData = result.getData();
                if (trackRespData != null) {
                    final List<OrderTrack> orderTracks = trackRespData.getTracks();
                    if (orderTracks != null && orderTracks.size() > 0) {
                        RouteItemAdapter itemAdapter = new RouteItemAdapter(mActivity, trackRespData, mListener);
                        itemAdapter.notifyDataSetChanged();
                        // 添加脚布局
                        /*View footView = View.inflate(mActivity, R.layout.item_foot_tel, null);
                        lvOrderTrack.addFooterView(footView);*/
                        lvOrderTrack.setAdapter(itemAdapter);

                        int orderStatus = orderTracks.get(0).getOrderStatus();
                        //客户交易取消
                        if (orderStatus == 8) {
                            btnOrderChange.setEnabled(false);
                            btnOrderClose.setEnabled(false);
                            btnOrderChange.setTextColor(Color.GRAY);
                            btnOrderClose.setTextColor(Color.GRAY);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SaleOrderGetTrackRespData>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    /**
     * 仓库信息数据请求
     */
    public void reqWarehouse() {
        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("WID", getWID());
        getService().WarehouseGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Warehouse>>() {
            @Override
            public void onResponse(ApiResponse<Warehouse> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                Warehouse respData = result.getData();
                if (null != respData) {
                    String strTelWarehouse = respData.getWCustomerServiceTel();
                    if (!strTelWarehouse.equals("")) {
                        ShowDialogTel(strTelWarehouse);
                    } else {
                        ToastUtils.show(mActivity, "无投诉电话");
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Warehouse>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });

    }

    private void ShowDialogTel(final String tel) {
        final MaterialDialog materialDialog = new MaterialDialog(mActivity);
        materialDialog.setMessage("拨打投诉电话：" + tel);
        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            EasyPermissionsEx.requestPermissions(mActivity, "需要开启拨打电话的权限", 1, Manifest.permission.CALL_PHONE);
                            return;
                        }
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                        mActivity.startActivity(intent);
                    }
                }
        );
        materialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }
}
