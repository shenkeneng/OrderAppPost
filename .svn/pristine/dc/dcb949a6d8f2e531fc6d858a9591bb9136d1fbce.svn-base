package com.frxs.order.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ewu.core.utils.DateUtil;
import com.ewu.core.utils.EasyPermissionsEx;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.HomeActivity;
import com.frxs.order.ModifyOrderActivity;
import com.frxs.order.OrderDetailActivity;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.OrderShopGetRespData;
import com.frxs.order.model.OrderShopQueryRespData;
import com.frxs.order.model.Orders;
import com.frxs.order.model.PostOrderCancel;
import com.frxs.order.model.UserInfo;
import com.frxs.order.model.Warehouse;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by Chentie on 2017/5/15.
 */

public class OrderManageFragment extends MaterialStyleFragment {

    private EmptyView emptyView;

    private ListView lvOrderList;

    private Adapter<Orders> orderAdapter;

    private int mPageIndex = 1;

    private final int mPageSize = 100;

    private int type = 0;//订单状态

    private String strTelWarehouse;//投诉电话

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_pre;
    }

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
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
                reqOrdersList();
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
                reqOrdersList();
            }
        });

    }

    @Override
    protected void initData() {
        reqOrdersList();
        orderAdapter = new Adapter<Orders>(getActivity(), R.layout.item_order_list) {
            @Override
            protected void convert(AdapterHelper helper, final Orders item) {
                helper.setText(R.id.tv_order_id, "订单编号：" + item.getOrderId());//订单编号
                /**
                 * 下单日期处理：2016-05-07 10:00:00.033000 --> 2016/05/07 10:00
                 */
                String strOrderDate = item.getOrderDate();
                strOrderDate = strOrderDate.substring(0, strOrderDate.lastIndexOf(":")).replace("-", "/");
                helper.setText(R.id.tv_order_time, strOrderDate);//下单时间
                helper.setText(R.id.tv_order_count, String.valueOf(item.getTotalProductCount()));//订单数量
                helper.setText(R.id.tv_order_integral, MathUtils.twolittercountString(item.getTotalPoint()));//积分
                helper.setText(R.id.tv_order_amount, "￥" + MathUtils.twolittercountString(item.getPayAmount()));//订单总金额

                /**
                 * 订单状态(0:草稿(代客下单才有);1:等待确认;2:等待拣货;3:正在拣货;4:拣货完成;5:打印完成;6:正在配送中;7:交易完成;8:客户交易取消;9:客服交易关闭）
                 */
                switch (item.getStatus()) {
                    case 1:
                        helper.setText(R.id.tv_order_state, "等待确认");
                        helper.setVisible(R.id.btn_order_close, View.VISIBLE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.VISIBLE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.GONE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                    case 2:
                        helper.setText(R.id.tv_order_state, "交易进行中");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                    case 3:
                        helper.setText(R.id.tv_order_state, "交易进行中");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                    case 4:
                        helper.setText(R.id.tv_order_state, "交易进行中");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                    case 5:
                        helper.setText(R.id.tv_order_state, "交易进行中");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                    case 6:
                        helper.setText(R.id.tv_order_state, "交易进行中");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        if (!TextUtils.isEmpty(item.getShippingEndDate())) {// 配送时间
                            helper.setVisible(R.id.btn_order_confirm, View.VISIBLE);//确认收货
                        } else {
                            helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        }
                        break;
                    case 7:
                        helper.setText(R.id.tv_order_state, "交易完成");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                    case 8:
                        helper.setText(R.id.tv_order_state, "交易关闭");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                    case 9:
                        helper.setText(R.id.tv_order_state, "交易关闭");
                        helper.setVisible(R.id.btn_order_close, View.GONE);//订单关闭
                        helper.setVisible(R.id.btn_order_change, View.GONE);//修改订单
                        helper.setVisible(R.id.btn_order_complaint, View.VISIBLE);//投诉订单
                        helper.setVisible(R.id.btn_order_confirm, View.GONE);//确认收货
                        break;
                }

                /**
                 * 订单投诉
                 */
                helper.setOnClickListener(R.id.btn_order_complaint, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reqWarehouse();
                    }
                });
                /**
                 * 作废订单
                 */
                helper.setOnClickListener(R.id.btn_order_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                        materialDialog.setMessage("确定作废订单：" + item.getOrderId());
                        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                List<String> orderCancelList = new ArrayList<>();
                                orderCancelList.add(item.getOrderId());
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
                /**
                 * 修改订单
                 */
                helper.setOnClickListener(R.id.btn_order_change, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                        materialDialog.setMessage("确定修改订单？");
                        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reqOrderDetail(item.getOrderId());
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
                /**
                 * 确认收货
                 */
                helper.setOnClickListener(R.id.btn_order_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                        materialDialog.setMessage("是否确认收货？");
                        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reqOrderFinish(item.getOrderId());
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
                /**
                 * 查看订单详情
                 */
                helper.setOnClickListener(R.id.rl_order_info, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                        intent.putExtra("ORDERID", item.getOrderId());
                        intent.putExtra("SHIPPING_DATE", item.getShippingEndDate());
                        mActivity.startActivity(intent);
                    }
                });

            }
        };
        lvOrderList.setAdapter(orderAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }


    /**
     * 订单列表数据请求
     */
    public void reqOrdersList() {
        mActivity.showProgressDialog();
        //获取系统当前时间和过去第七天时间
        Date date = new Date();
        String strOrderEnd = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        String strOrderBegin = DateUtil.format(DateUtil.addDateDays(date, -6), "yyyy-MM-dd 00:00:00");
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(0);
        integers.add(1);
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("OrderDateBegin", strOrderBegin);
        params.put("OrderDateEnd", strOrderEnd);
        params.put("UserID", getUserID());
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("FilterStatus", integers);
        params.put("UserName", userInfo.getUserName());
        params.put("ShopID", getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);
        if (type != 0) {
            params.put("Status", String.valueOf(type));
        }
        getService().OrderQuery(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<OrderShopQueryRespData>>() {
            @Override
            public void onResponse(ApiResponse<OrderShopQueryRespData> result, int code, String msg) {
                refreshComplete();
                mActivity.dismissProgressDialog();
                OrderShopQueryRespData respData = result.getData();
                if (null != respData) {
                    List<Orders> ordersList = respData.getOrders();
                    if (ordersList != null && ordersList.size() > 0) {
                        emptyView.setVisibility(View.GONE);
                        if (mPageIndex == 1) {
                            orderAdapter.replaceAll(ordersList);
                        } else {
                            orderAdapter.addAll(ordersList);
                        }
                    } else {
                        if (1 == mPageIndex) {
                            initNoData();
                        } else {
                            ToastUtils.show(mActivity, R.string.tips_pageending);
                        }
                    }

                    boolean hasMoreItems = (orderAdapter.getCount() < respData.getTotalCount());
                    mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                } else {
                    initNoData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderShopQueryRespData>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                boolean hasMoreItems = !(orderAdapter.getCount() < mPageIndex * mPageSize);
                mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqOrdersList();
                    }
                });
                mActivity.dismissProgressDialog();
            }
        });

    }

    private void initNoData() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_NODATA);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqOrdersList();
            }
        });
    }

    /**
     * 确认收货数据请求
     */
    private void reqOrderFinish(String strOrderID) {
        mActivity.showProgressDialog();
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
                if (result.getFlag().equals("0")) {
                    reqOrdersList();
                } else {
                    ToastUtils.show(mActivity, result.getInfo());
                    reqOrdersList();
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
    private void reqOrderReBuy(String strOrderID) {
        mActivity.showProgressDialog();
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
                    mActivity.dismissProgressDialog();
                    ToastUtils.show(mActivity, "加入购物车成功");

                    //请求更新购物车商品数量显示
                    ((HomeActivity)mActivity).requestGetSaleCartCount();
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
     * 仓库信息数据请求
     */
    public void reqWarehouse() {
        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("WID", getWID());
        getService().WarehouseGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Warehouse>>() {
            @Override
            public void onResponse(ApiResponse<Warehouse> result, int code, String msg) {
                refreshComplete();
                mActivity.dismissProgressDialog();
                Warehouse respData = result.getData();
                if (null != respData) {
                    strTelWarehouse = respData.getWCustomerServiceTel();
                    if (!TextUtils.isEmpty(strTelWarehouse)) {
                        final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                        materialDialog.setMessage("拨打投诉电话：" + strTelWarehouse);
                        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        materialDialog.dismiss();

                                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            EasyPermissionsEx.requestPermissions(mActivity, "需要开启拨打电话的权限", 1, Manifest.permission.CALL_PHONE);
                                            return;
                                        }
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strTelWarehouse));
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
                    } else {
                        ToastUtils.show(mActivity, "无投诉电话");
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Warehouse>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                mActivity.dismissProgressDialog();
            }
        });

    }

    /**
     * 作废订单订单(调用后台取消订单接口)
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
                    reqOrdersList();
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
     * 订单详情数据请求
     */
    public void reqOrderDetail(String strOrderID) {
        mActivity.showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("OrderId", strOrderID);
        params.put("ShopID", getShopID());
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserName", userInfo.getUserName());
        getService().OrderGetAction(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<OrderShopGetRespData>>() {
            @Override
            public void onResponse(ApiResponse<OrderShopGetRespData> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                OrderShopGetRespData orderDetails = result.getData();
                if (orderDetails != null) {
                    List<com.frxs.order.model.Details> detailsList = orderDetails.getDetails();
                    Intent intent = new Intent(mActivity, ModifyOrderActivity.class);
                    intent.putExtra("ORDER", (Serializable) detailsList);
                    intent.putExtra("ORDERID", orderDetails.getOrder().getOrderId());
                    intent.putExtra("REMARK", orderDetails.getOrder().getRemark());
                    intent.putExtra("DATE", orderDetails.getOrder().getOrderDate());
                    mActivity.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderShopGetRespData>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });

    }

    public void setPosition(int position) {
        mPageIndex = 1;
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            reqOrdersList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment currentFragment = ((HomeActivity) mActivity).getCurrentFragment();
        if (currentFragment instanceof OrdersFragment) {
            if (!emptyView.isShown()) {
                mActivity.showProgressDialog();
            }
            reqOrdersList();
        }
    }
}
