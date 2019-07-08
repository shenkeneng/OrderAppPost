package com.frxs.order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.SaleBackCart;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/14.
 * 提交退货申请单
 */

public class SubmitBackOrderActivity extends FrxsActivity {

    private TextView goodCountTv;// 商品数量
    private TextView goodAmountTv;// 商品总额
    private TextView orderSubmitTv;//提交退货申请单提交退货申请单
    private TextView orderGoodPoint;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_submit_back;
    }

    @Override
    protected void initViews() {
        TextView titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText("退货申请单提交");
        findViewById(R.id.tv_title_right).setVisibility(View.GONE);
        goodCountTv = (TextView) findViewById(R.id.tv_good_count);
        goodAmountTv = (TextView) findViewById(R.id.tv_good_amount);
        orderSubmitTv = (TextView) findViewById(R.id.tv_order_submit);
        orderGoodPoint = (TextView) findViewById(R.id.tv_good_point);
    }

    @Override
    protected void initEvent() {
        orderSubmitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsRepeatGoodsInBackApplyOrder();
            }
        });
    }

    @Override
    protected void initData() {
        SaleBackCart saleBackOrder = (SaleBackCart) getIntent().getSerializableExtra("ORDER");
        if (saleBackOrder != null && saleBackOrder.getSaleBackCart().size() > 0){
            List<SaleBackCart.SaleBackCartBean> saleBackGoods = saleBackOrder.getSaleBackCart();
            double amount = 0.0;// 提交订单总金额
            double count = 0.0;// 提交商品总数量
            double point = 0.0;// 提交商品退货总积分
            for (SaleBackCart.SaleBackCartBean good : saleBackGoods ){
                amount = MathUtils.add(MathUtils.mul(good.getPrice(), good.getQty()), amount);
                count = MathUtils.add(good.getQty(), count);
                point = MathUtils.add(MathUtils.mul(good.getShopPoint(), good.getQty()), point);
            }
            goodAmountTv.setText("￥" + MathUtils.twolittercountString(amount));
            goodCountTv.setText(String.valueOf(MathUtils.doubleTrans(MathUtils.round(count, 2))));
            if (point == 0) {
                orderGoodPoint.setText(MathUtils.twolittercountString(point));
            } else {
                orderGoodPoint.setText("-" + MathUtils.twolittercountString(point));
            }
        } else {
            ToastUtils.show(this, "当前无退货商品可提交");
        }
    }

    /**
     * 退货申请单中是否有提交商品
     */
    private void IsRepeatGoodsInBackApplyOrder(){
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", getShopID());
        params.put("WarehouseId", getWID());
        params.put("UserId", userInfo.getUserId());
        params.put("UserName", userInfo.getUserName());

        getService().IsRepeatGoodsInBackApplyOrder(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {

            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                if (result.getFlag().equals("0")){
                    submitBackOrder();
                } else {
                    String[] split = result.getInfo().split(";");
                    List<String> backGoodsList = Arrays.asList(split);
                    if (backGoodsList.size() > 0) {
                        showRepeatGoodsDialog(backGoodsList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtils.show(SubmitBackOrderActivity.this, "提交失败：" + t.getMessage());
            }
        });
    }

    /**
     * 显示已存在的商品
     * @param backGoodsList
     */
    private void showRepeatGoodsDialog (List<String> backGoodsList) {
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
        goodsRv.setAdapter(new RecyclerAdapter<String>(this, backGoodsList, R.layout.item_text_view) {
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
                submitBackOrder();
            }
        });
        dialog.show();
    }

    /**
     * 提交退货申请单
     */
    private void submitBackOrder() {
        showProgressDialog();
        AjaxParams param = new AjaxParams();
        param.put("WarehouseId", getWID());
        param.put("ShopID", getShopID());
        param.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        param.put("UserId", getUserID());
        param.put("WID", getWID());

        getService().SubmitApplyForSaleBack(param.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Boolean>>() {

            @Override
            public void onResponse(ApiResponse<Boolean> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")){
                    ToastUtils.show(SubmitBackOrderActivity.this, "退货申请单提交成功！");
                    NewBackOrderActivity.instance.finish();
                    finish();
                } else {
                    ToastUtils.show(SubmitBackOrderActivity.this, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(SubmitBackOrderActivity.this, t.getMessage());
            }
        });
    }
}
