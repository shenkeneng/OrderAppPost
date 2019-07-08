package com.frxs.order;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.SaleBackDetails;
import com.frxs.order.model.SaleBackOrderList;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.List;

import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/8.
 * 退货单商品详情页
 */

public class BackOrderInfoActivity extends FrxsActivity {

    private ListView goodsLv;// 商品列表

    private TextView orderIdTv;// 订单时间

    private TextView orderCountTv;// 商品数量

    private TextView orderAmountTv;// 订单金额

    private Adapter<SaleBackDetails> adapter;

    private String backId;

    private int backStatus;// 退货单状态

    private String backQty;

    private TextView backPointTv;// 退货单退货积分

    private ImageView ivStatusOrder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_info;
    }

    @Override
    protected void initViews() {
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText("退货单详情");
        goodsLv = (ListView) findViewById(R.id.lv_goods_list);
        orderIdTv = (TextView) findViewById(R.id.tv_order_id);
        orderCountTv = (TextView) findViewById(R.id.tv_order_count);
        orderAmountTv = (TextView) findViewById(R.id.tv_order_amount);
        backPointTv = (TextView) findViewById(R.id.tv_order_point);
        ivStatusOrder = (ImageView) findViewById(R.id.iv_status_order);

        backId = getIntent().getStringExtra("ORDERID");
        backStatus = getIntent().getIntExtra("BACK_STATUS", 0);
        backQty = getIntent().getStringExtra("BACK_QTY");
    }

    @Override
    protected void initEvent() {

        adapter = new Adapter<SaleBackDetails>(this, R.layout.item_return_info) {
            @Override
            protected void convert(AdapterHelper helper, SaleBackDetails item) {
                //商品名称
                helper.setText(R.id.tv_good_name, item.getProductName());
                //商品编码
                helper.setText(R.id.tv_good_sku, TextUtils.isEmpty(item.getSKU()) ? "编码:" : "编码:" + item.getSKU());
                //商品条码 （多条只显示一条）
                String barCode = item.getBarCode().split(",")[0];
                helper.setText(R.id.tv_bar_code, TextUtils.isEmpty(barCode) ? "条码:" : "条码:" + barCode);
                //退货价格
                helper.setText(R.id.tv_back_price, "退货价格:" + MathUtils.twolittercountString(item.getBackPrice()) + "/" + item.getBackUnit());
                //退货数量
                helper.setText(R.id.tv_back_count, "退货数量:" + MathUtils.doubleTrans(MathUtils.round(item.getBackQty(), 2)) + item.getBackUnit());
                //退货总金额
                helper.setText(R.id.tv_back_amt, "退货金额:" + MathUtils.twolittercountString(item.getSubAmt()));
                //退货积分
                helper.setText(R.id.tv_back_point, "小计退货积分:" + MathUtils.twolittercountString(item.getSubPoint()));
                //商品备注 （没有则不显示）
                if (!TextUtils.isEmpty(item.getRemark())){
                    helper.setVisible(R.id.tv_good_remark, View.VISIBLE);
                    helper.setText(R.id.tv_good_remark, "备注：" + item.getRemark());
                }
            }
        };

        goodsLv.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        reqSaleBackInfoDate();
    }

    private void reqSaleBackInfoDate() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("BackID", backId);
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("UserId", getUserID());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());

        getService().GetSaleBackInfo(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<SaleBackOrderList>>() {
            @Override
            public void onResponse(ApiResponse<SaleBackOrderList> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")){
                    SaleBackOrderList data = result.getData();
                    if (data != null){
                        orderIdTv.setText(data.getBackID());
                        if (backStatus == 2){
                            ivStatusOrder.setImageResource(R.mipmap.ic_state_no);
                        } else {
                            ivStatusOrder.setImageResource(R.mipmap.ic_state_yes);
                        }
                        orderCountTv.setText("数量：" + backQty);
                        orderAmountTv.setText("金额：" + MathUtils.twolittercountString(data.getPayAmount()));
                        backPointTv.setText("退货积分：" + MathUtils.twolittercountString(data.getTotalPoint()));//设置退货单退货积分
                        List<SaleBackDetails> backdetails = data.getBackdetails();
                        if (backdetails != null && backdetails.size() > 0){
                            adapter.replaceAll(backdetails);
                        } else {
                            ToastUtils.show(BackOrderInfoActivity.this, "未找到当前退货单商品列表");
                        }
                    } else {
                        ToastUtils.show(BackOrderInfoActivity.this, result.getInfo());
                    }
                } else {
                    ToastUtils.show(BackOrderInfoActivity.this, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SaleBackOrderList>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(BackOrderInfoActivity.this, t.getMessage());
            }
        });
    }
}
