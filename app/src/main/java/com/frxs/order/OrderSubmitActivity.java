package com.frxs.order;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ewu.core.utils.DateUtil;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.OrderShop;
import com.frxs.order.model.PostOrderEditAll;
import com.frxs.order.model.PrefectShopInfo;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.WarehouseLine;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.RequestListener;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.IpAdressUtils;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import retrofit2.Call;


/**
 * 订单提交 by Tiepier
 */
public class OrderSubmitActivity extends FrxsActivity {

    private TextView tvTitle;
    private TextView tvOrderTime;
    private TextView tvOrderCount;
    private TextView tvOrderRemark;
    private TextView tvOrderIntegral;
    private TextView tvOrderTotal;
    private Button btnOrderSubmit;
    private LinearLayout rlGoodsCountBrock;

    private String remark = "";// 整单备注

    List<CartGoodsDetail> mGoodsList = new ArrayList<>();

    private OrderShop orderShop;

    private boolean isModifyOrder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_submit;
    }

    @Override
    protected void initViews() {
        /**
         * 实例化控件
         */
        tvTitle = (TextView) findViewById(R.id.title_tv);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvOrderCount = (TextView) findViewById(R.id.tv_order_count);
        tvOrderRemark = (TextView) findViewById(R.id.tv_order_remark);
        tvOrderIntegral = (TextView) findViewById(R.id.tv_order_integral);
        tvOrderTotal = (TextView) findViewById(R.id.tv_order_total);
        btnOrderSubmit = (Button) findViewById(R.id.btn_order_submit);
        rlGoodsCountBrock = (LinearLayout) findViewById(R.id.rl_goods_count_brock);
    }

    @Override
    protected void initData() {
        tvTitle.setText("结算");
        // 获取购物车数据
        List<CartGoodsDetail> cartGoodsList = FrxsApplication.getInstance().getStoreCart();
        if (cartGoodsList != null && cartGoodsList.size() > 0) {
            mGoodsList.addAll(cartGoodsList);
        }

        Intent intent = getIntent();
        if (intent != null) {
            //List<CartGoodsDetail> cartGoodsList = (List<CartGoodsDetail>) intent.getSerializableExtra("GOODSLIST");
            orderShop = (OrderShop) intent.getSerializableExtra("ORDER");
            isModifyOrder = intent.getBooleanExtra("MODIFY", false);
//            strOrderID = intent.getStringExtra("ORDERID");
            remark = intent.getStringExtra("REMARK");
            tvOrderRemark.setText(remark);
            tvOrderRemark.setGravity(Gravity.RIGHT);// 留言内容显示居右边
        }

        initOrderData();

        ShopInfo shopInfo = FrxsApplication.getInstance().getmCurrentShopInfo();
        if (shopInfo != null) {
            getDeliverGoodsTimes();
        }
    }

    @Override
    protected void initEvent() {
        rlGoodsCountBrock.setOnClickListener(this);
        tvOrderRemark.setOnClickListener(this);
        btnOrderSubmit.setOnClickListener(this);
    }

    private void initOrderData() {
        double orderPoints = 0;
        double orderAmount = 0.0;
        double orderFee = 0.0;
        int count = 0;

        if (null != mGoodsList && mGoodsList.size() > 0) {
            for (CartGoodsDetail item : mGoodsList) {
                orderAmount += (item.getSalePrice() * item.getPreQty());
                count += item.getPreQty();
                orderFee += ((item.getSalePrice() * item.getPreQty()) * item.getShopAddPerc());// 平台费用=商品金额x商品数量x平台费率
                double point = item.getPromotionShopPoint() > 0 ? item.getPromotionShopPoint() : item.getShopPoint();
                point = point * item.getSalePackingQty();
                orderPoints += (item.getPreQty() * point);
            }
        }
        tvOrderTotal.setText("￥" + MathUtils.twolittercountString(orderAmount + orderFee));// 合计
        tvOrderCount.setText(String.valueOf(count)); // 商品数量
        tvOrderIntegral.setText(String.format("%1$.2f", orderPoints)); // 积分
    }

    private void getDeliverGoodsTimes() {
        AjaxParams params = new AjaxParams();
        params.put("UserID", FrxsApplication.getInstance().getUserInfo().getUserId());
        params.put("WID", FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
        params.put("ShopID", FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
        getService().GetShopDeliveryCycle(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<WarehouseLine>>() {
            @Override
            public void onResponse(ApiResponse<WarehouseLine> result, int code, String msg) {
                if (result.getFlag().equals("0")){
                    if (result.getData() != null){
                        WarehouseLine data = result.getData();
                        setDeliverGoodsTimes(data);
                    }else{
                        ToastUtils.show(OrderSubmitActivity.this, result.getInfo());
                    }
                }

            }
        });
    }

    /**
     * 送货时间显示
     *
     * @param wl
     */
    private void setDeliverGoodsTimes(WarehouseLine wl) {
        if (!TextUtils.isEmpty(wl.getShippingRemark())) {
            String orderDesc = getString(R.string.order_delivery_desc) + "<font color=\"#ff564c\">" + wl.getShippingRemark()+ "</font>";
            tvOrderTime.setText(Html.fromHtml(orderDesc));
        } else {
            String orderEndTime = "";
            if (!TextUtils.isEmpty(wl.getOrderEndTime())) {
                Date orderDate = DateUtil.string2Date(wl.getOrderEndTime(), "HH:mm");
                if (orderDate != null) {
                    orderEndTime = DateUtil.format(orderDate, "HH:mm");
                }
            }

            if (!TextUtils.isEmpty(orderEndTime)) {
                String orderDesc = getString(R.string.order_confirm_desc) + "<font color=\"#ff564c\">" + ((TextUtils.isEmpty(orderEndTime)) ? "" : orderEndTime) + "</font>";
                tvOrderTime.setText(Html.fromHtml(orderDesc));
            }
        }

        List<Integer> listSendTypeCode = wl.getListSendTypeCode();
        if (listSendTypeCode == null && listSendTypeCode.size() <= 0) {
            return;
        }

        if (wl.getSendMode() == 0){
            TextView tvSingleDay = (TextView) findViewById(R.id.tv_single_day);// 单日
            TextView tvBothDay = (TextView) findViewById(R.id.tv_both_day);// 双日
            for (Integer sendTypeCode : listSendTypeCode){
                switch (sendTypeCode){
                    case 8:{
                        tvSingleDay.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 9:{
                        tvBothDay.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        }else if (wl.getSendMode() == 1){
            TextView tvDeliveryTime = (TextView) findViewById(R.id.tv_delivery_time);
            tvDeliveryTime.setVisibility(View.VISIBLE);
            TextView tvMonday = (TextView) findViewById(R.id.tv_monday);
            TextView tvTuesday = (TextView) findViewById(R.id.tv_tuesday);
            TextView tvWednesday = (TextView) findViewById(R.id.tv_wednesday);
            TextView tvThursday = (TextView) findViewById(R.id.tv_thursday);
            TextView tvFriday = (TextView) findViewById(R.id.tv_friday);
            TextView tvSaturday = (TextView) findViewById(R.id.tv_saturday);
            TextView tvSunday = (TextView) findViewById(R.id.tv_sunday);

            for (Integer sendTypeCode : listSendTypeCode){
                switch (sendTypeCode){
                    case 1:{
                        tvMonday.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2:{
                        tvTuesday.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 3:{
                        tvWednesday.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 4:{
                        tvThursday.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 5:{
                        tvFriday.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 6:{
                        tvSaturday.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 7:{
                        tvSunday.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_goods_count_brock:
                intent = new Intent(OrderSubmitActivity.this, CommodityListActivity.class);
                //intent.putExtra("GOODSLIST", (Serializable) mGoodsList);
                startActivity(intent);
                break;
            case R.id.tv_order_remark:
                intent = new Intent(OrderSubmitActivity.this, OrderRemarkActivity.class);
                intent.putExtra("REMARK", remark);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_order_submit:
                if (isModifyOrder) {
                    reqOrderEditAll(orderShop, mGoodsList);
                } else {
                    reqNeedPerfectShopInfo();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断当前店铺是否冻结 -> 冻结跳转完善资料页面并提示 门店已冻结无法下单，请联系管理员!
     *      - 门店未冻结
     *          - 通过审核 -> 提交订单
     *          - 没通过审核 (账户信息状态为-1 跳转完善资料页面    账户信息状态为0 停留当前页面提示账户信息审核中，无法下单)
     */
    private void reqNeedPerfectShopInfo() {
        showProgressDialog();
        reqIsPrefectShopInfo(new RequestListener() {

            @Override
            public void handleRequestResponse(ApiResponse result) {
                dismissProgressDialog();
                PrefectShopInfo data = (PrefectShopInfo) result.getData();
                // 门店是否冻结
                if (data != null) {
                    if (data.getStatus() == 0) {
                        ToastUtils.show(OrderSubmitActivity.this, "门店已冻结无法下单，请联系管理员!");
                        return;
                    }

                    // 信息是否审核通过
                    if (!result.isSuccessful()) {
                        if (data != null) {
                            if (data.getShopErrInfo() != null) {
                                // 暂不跳转完善资料页面  只提示该门店资料未完善或暂未通过审核  并且可以提交订单
                                showIncompleteDataDialog(data.getShopErrInfo());
                            }
                        }
                    } else {
                        // 门店状态正常 信息审核通过后 可以提交购物车商品
                        reqIsExistUnConfirmOrder();
                    }
                }
            }

            @Override
            public void handleExceptionResponse(String errMsg) {
                dismissProgressDialog();
            }
        });
    }

    private void showIncompleteDataDialog(final List<String> shopErrInfo) {
        final MaterialDialog dialog = new MaterialDialog(OrderSubmitActivity.this);
        View contentView = LayoutInflater.from(OrderSubmitActivity.this).inflate(R.layout.dialog_repeat_goods_comfirm, null);
        contentView.findViewById(R.id.close_btn).setVisibility(View.GONE);
        RecyclerView goodsRv = (RecyclerView) contentView.findViewById(R.id.repeat_goods_rv);
        goodsRv.setLayoutManager(new LinearLayoutManager(OrderSubmitActivity.this, LinearLayoutManager.VERTICAL, false));
        goodsRv.setAdapter(new RecyclerAdapter<String>(OrderSubmitActivity.this, shopErrInfo, R.layout.item_text_view) {
            @Override
            protected void convert(RecyclerAdapterHelper helper, String item) {
                helper.setText(R.id.text_view, item);
            }
        });
        dialog.setContentView(contentView);
        dialog.setPositiveButton(R.string.dialog_confirm_submit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                reqIsExistUnConfirmOrder();
            }
        });
        dialog.show();
    }

    private void reqOrderSubmit(boolean isCover, String mUnConfirmOrderId) {
        showProgressDialog();
        String remark = tvOrderRemark.getText().toString().trim();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
        params.put("bFromCart", "true");
        params.put("bDeleteOld", isCover);// true覆盖订单，false合并订单
        params.put("Remark", remark);
        params.put("WarehouseId", FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
        params.put("UserId", FrxsApplication.getInstance().getUserInfo().getUserId());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        params.put("OrderId", mUnConfirmOrderId);// 被合并的订单ID，没有则为null
        params.put("ClientIP", IpAdressUtils.getIpAdress2(this));
        params.put("ClientModelType", getPhoneInfo());getPhoneInfo();
        getService().OrderShopCreateBusiness(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        FrxsApplication.getInstance().setStoreCart(null);//提交订单成功后 清空本地缓存购物车数据
                        ToastUtils.show(OrderSubmitActivity.this, "提交成功");
                        FrxsApplication.getInstance().saveSaleCartProducts(null);//清空本地缓存的购物车商品
                        FrxsApplication.getInstance().setShopCartCount(0);//设置购物车的商品数量
                        Intent intent = new Intent(OrderSubmitActivity.this, OrderSuccessActivity.class);
                        if (!TextUtils.isEmpty(result.getInfo())) {
                            String[] orders = result.getInfo().split(",");
                            if (orders != null && orders.length > 1) {
                                intent.putExtra("order_delivery_date", orders[1]);
                            }
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(OrderSubmitActivity.this, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

    private void reqIsExistUnConfirmOrder() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
        params.put("WID", FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
        params.put("WarehouseId", FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
        params.put("UserId", FrxsApplication.getInstance().getUserInfo().getUserId());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        getService().OrderShopExistUnConfirmOrder(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        String resultData = result.getData();
                        String orderId = result.getInfo();
                        if (!TextUtils.isEmpty(resultData)) {
                            dismissProgressDialog();
                            String[] repeatGoods = resultData.split(";");
                            showRepeatGoodsDialog(orderId, Arrays.asList(repeatGoods));
                        } else {
                            reqOrderSubmit(false, orderId);// 没有重复商品直接合并
                        }
                    } else {
                        reqOrderSubmit(false, null);
                    }
                } else {
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

    private void showRepeatGoodsDialog (final String orderId, List<String> goodsList) {
        final MaterialDialog dialog = new MaterialDialog(this);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_repeat_goods_comfirm, null);
        contentView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView goodsRv = (RecyclerView) contentView.findViewById(R.id.repeat_goods_rv);
        goodsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        goodsRv.setAdapter(new RecyclerAdapter<String>(this, goodsList, R.layout.item_text_view) {
            @Override
            protected void convert(RecyclerAdapterHelper helper, String item) {
                helper.setText(R.id.text_view, item);
            }
        });
        dialog.setContentView(contentView);
        dialog.setNegativeButton(R.string.dialog_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(R.string.dialog_confirm_submit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                reqOrderSubmit(false, orderId);
            }
        });
        dialog.show();
    }

    // 备注：需求修改了，如果有未确认订单直接合并。没有才覆盖（新订单）
//    private void showConfirmOrderDialog(final String orderId)
//    {
//        // 不管合并还是覆盖，这里都需要把订单ID传递给提交订单接口
//        final MaterialDialog dialog = new MaterialDialog(OrderSubmitActivity.this);
//        dialog.setMessage("您有未确认订单，是否与其合并？");
//        dialog.setPositiveButton("确定", new View.OnClickListener() {// 表示需要合并订单
//            @Override
//            public void onClick(View v) {
//                reqOrderSubmit(false,orderId);
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setNegativeButton("取消", new View.OnClickListener() {// 表示不合并，则覆盖订单
//            @Override
//            public void onClick(View v) {
//                reqOrderSubmit(true,orderId);
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    /**
     * 订单编辑（整单编辑）
     */
    private void reqOrderEditAll(OrderShop orderShop, List<CartGoodsDetail> cartGoodsList) {
        showProgressDialog();
        String remark = tvOrderRemark.getText().toString().trim();
//        AjaxParams params = new AjaxParams();
//        params.put("ShopId", FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
//        params.put("SubWID", FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
//        params.put("WarehouseId", FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
//        params.put("bDeleteOld", true);
//        params.put("UserId", getUserID());
//        params.put("OrderId", strOrderID);
//        params.put("bFromCart", false);
//        params.put("Remark", remark);
//        params.put("OrderShop", orderShop);
//        params.put("Details", cartGoodsList);
//        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());

        PostOrderEditAll orderEditAll = new PostOrderEditAll();
        orderEditAll.setShopId(FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
        orderEditAll.setSubWID(String.valueOf(FrxsApplication.getInstance().getmCurrentShopInfo().getWID()));
        orderEditAll.setWarehouseId(String.valueOf(FrxsApplication.getInstance().getmCurrentShopInfo().getWID()));
        orderEditAll.setbDeleteOld(true);
        orderEditAll.setUserID(getUserID());
        orderEditAll.setbFromCart(false);
        orderEditAll.setRemark(remark);
        orderEditAll.setOrderShop(orderShop);
        orderEditAll.setDetails(cartGoodsList);
        orderEditAll.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        orderEditAll.setOrderId(orderShop.getOrderId());
        orderEditAll.setClientIP(IpAdressUtils.getIpAdress2(this));
        orderEditAll.setClientModelType(getPhoneInfo());

        getService().OrderShopEditAll(orderEditAll).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    Intent intent = new Intent(OrderSubmitActivity.this, HomeActivity.class);
                    intent.putExtra("TAB", 3);
                    startActivity(intent);
                    ToastUtils.show(OrderSubmitActivity.this, "订单修改成功");
                    finish();
                } else {
                    ToastUtils.show(OrderSubmitActivity.this, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(OrderSubmitActivity.this, R.string.network_request_failed);//网络请求失败，请重试
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (intent != null) {
                        remark = intent.getStringExtra("REMARK");
                        tvOrderRemark.setText(remark);
                        if (!TextUtils.isEmpty(remark)) {
                            tvOrderRemark.setGravity(Gravity.RIGHT);// 留言内容显示居右边
                        }
                    }
                    break;
            }
        }
    }
}
