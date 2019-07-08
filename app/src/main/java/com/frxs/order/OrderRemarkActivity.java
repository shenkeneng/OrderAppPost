package com.frxs.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.OrderPreProducts;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.PostPreGood;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.IpAdressUtils;
import retrofit2.Call;


/**
 * 备注 by Tiepier
 */
public class OrderRemarkActivity extends FrxsActivity {

    private ImageView btnBack;
    private TextView tvTitle;
    private Button btnConfirm;
    private EditText editReamrk;
    private String remark = "";
    private boolean isCart;
    private CartGoodsDetail editCartGoodsDetail;
    private OrderPreProducts.ItemsBean preGood;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_remark;
    }

    @Override
    protected void initViews() {
        /**
         * 实例化控件
         */
        btnBack = (ImageView) findViewById(R.id.left_btn);
        tvTitle = (TextView) findViewById(R.id.title_tv);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        editReamrk = (EditText) findViewById(R.id.order_remark);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            remark = getIntent().getStringExtra("REMARK");
            isCart = getIntent().getBooleanExtra("CART", false);
            if (isCart) {
                editCartGoodsDetail = (CartGoodsDetail) getIntent().getSerializableExtra("CART_GOODS");
                preGood = (OrderPreProducts.ItemsBean) getIntent().getSerializableExtra("PRE_GOOD");
            }
        }
        if (isCart) {
            tvTitle.setText("备注");
        } else {
            tvTitle.setText("整单备注");
        }
        if (!TextUtils.isEmpty(remark)) {
            editReamrk.setText(remark);
            editReamrk.setSelection(remark.length());
        }

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        btnConfirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (isCart) {
                    if (null != editCartGoodsDetail) {
                        if (editCartGoodsDetail.isFromModifyOrder()) {
//                            requestOrderEditRemark();
                            //修改备注直接在提交修改的订单里边修改
                            String remark = editReamrk.getText().toString().trim();
                            editCartGoodsDetail.setRemark(remark);
                            Intent intent = new Intent();
                            intent.putExtra("CART_GOODS", editCartGoodsDetail);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            requestEditSingleGoods();
                        }
                    } else if (null != preGood) {
                        requestEditOrderPreGoods();
                    }else {
                        finish();
                    }
                } else {// 修改订单的整单备注
                    Intent intent = new Intent();
                    intent.putExtra("REMARK", editReamrk.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 修改预订商品的信息
     */
    private void requestEditOrderPreGoods() {
        showProgressDialog();
        PostPreGood postPreGood = new PostPreGood();
        postPreGood.setBookOrderId(preGood.getBookOrderId());
        postPreGood.setID(preGood.getID());
        postPreGood.setQty(preGood.getSaleQty());
        postPreGood.setRemark(editReamrk.getText().toString().trim());
        PostEditCart postEditCart = new PostEditCart();
        postEditCart.setEditType(1);
        postEditCart.setShopID(getShopID());
        postEditCart.setUserId(FrxsApplication.getInstance().getUserInfo().getUserId());
        postEditCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        postEditCart.setWID(getWID());
        postEditCart.setBookProduct(postPreGood);

        getService().EditSaleOrderPreProducts(postEditCart).enqueue(new SimpleCallback<ApiResponse<Object>>() {
            @Override
            public void onResponse(ApiResponse<Object> result, int code, String msg) {
                dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        ToastUtils.show(OrderRemarkActivity.this, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(OrderRemarkActivity.this, R.string.network_request_failed + t.getMessage());
            }
        });
    }

    /**
     * 单行修改购物车商品
     */
    private void requestEditSingleGoods() {
        showProgressDialog();

//        Map<String, Object> cartGoods = new HashMap<>();
//        cartGoods.put("ID", editCartGoodsDetail.getID());
//        cartGoods.put("ProductID", editCartGoodsDetail.getProductId());
//        cartGoods.put("PreQty", editCartGoodsDetail.getPreQty());
//        cartGoods.put("WID", info.getWID());
//        cartGoods.put("WarehouseId", info.getWID());
//        cartGoods.put("Remark", editReamrk.getText().toString().trim());
//
//        AjaxParams params = new AjaxParams();
//        params.put("EditType", 1);// 修改类型 0：add 1:edit 2:delete
//        params.put("ShopID", info.getShopID());
//        params.put("UserId", FrxsApplication.getInstance().getUserInfo().getUserId());
//        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
//        params.put("WarehouseId", info.getWID());
//        params.put("Cart", cartGoods);
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();
        PostInfo postInfo = new PostInfo();
        if (editCartGoodsDetail != null) {
            postInfo.setID(editCartGoodsDetail.getID());
            postInfo.setProductID(editCartGoodsDetail.getProductId());
            postInfo.setPreQty((int) editCartGoodsDetail.getPreQty());
        }
        postInfo.setWID(info.getWID());
        postInfo.setWarehouseId(info.getWID());
        postInfo.setRemark(editReamrk.getText().toString().trim());

        PostEditCart editCart = new PostEditCart();
        editCart.setEditType(1);
        editCart.setShopID(info.getShopID());
        editCart.setUserId(FrxsApplication.getInstance().getUserInfo().getUserId());
        editCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        editCart.setWarehouseId(info.getWID());
        editCart.setCart(postInfo);

        getService().SaleCartEditSingle(editCart).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        finish();
                    } else {
                        ToastUtils.show(OrderRemarkActivity.this, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(OrderRemarkActivity.this, R.string.network_request_failed);//网络请求失败，请重试
            }
        });
    }

    /**
     * 编辑订单备注
     */
    private void requestOrderEditRemark() {
        showProgressDialog();
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();
//        AjaxParams params = new AjaxParams();
//        params.put("OrderId", editCartGoodsDetail.getOrderID());
//        params.put("WID", getWID());
//        params.put("DetailId", editCartGoodsDetail.getID());
//        params.put("Detail", editCartGoodsDetail);
//        params.put("EditType", 1);
//        params.put("ShopId",getShopID());
//        params.put("UserId", FrxsApplication.getInstance().getUserInfo().getUserId());
//        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
//        params.put("WarehouseId", info.getWID());

        PostEditCart postEditCart = new PostEditCart();
        postEditCart.setOrderId(editCartGoodsDetail.getOrderID());
        postEditCart.setDetailId(editCartGoodsDetail.getID());
        postEditCart.setEditType(1);
        postEditCart.setShopID(getShopID());
        postEditCart.setUserId(FrxsApplication.getInstance().getUserInfo().getUserId());
        postEditCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        postEditCart.setWarehouseId(info.getWID());
        postEditCart.setClientIP(IpAdressUtils.getIpAdress2(this));
        postEditCart.setClinetModelType(getPhoneInfo());

        getService().OrderShopEditRemark(postEditCart).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        //TODO 编辑订单商品备注
                        String remark = editReamrk.getText().toString().trim();
                        editCartGoodsDetail.setRemark(remark);
                        Intent intent = new Intent();
                        intent.putExtra("CART_GOODS", editCartGoodsDetail);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtils.show(OrderRemarkActivity.this, result.getInfo());
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



}
