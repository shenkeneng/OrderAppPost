package com.frxs.order;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frxs.order.application.FrxsApplication;

/**
 * Created by Chentie on 2016/12/13.
 */
public class PaySuccessActivity extends FrxsActivity{

    private TextView tvShop;
    private TextView tvOrder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void initViews() {
        TextView tvTitle = (TextView) findViewById(R.id.title_tv);
        tvTitle.setText("支付成功");
        tvTitle.setTextColor(getResources().getColor(R.color.black_de));

        ImageView btnLeft = (ImageView) findViewById(R.id.left_btn);
        btnLeft.setVisibility(View.GONE);

        tvShop = (TextView) findViewById(R.id.tv_shop);
        tvOrder = (TextView) findViewById(R.id.tv_order);
    }

    @Override
    protected void initEvent() {

        tvShop.setOnClickListener(this);
        tvOrder.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.tv_shop:// 跳转到首页
                intent =  new Intent(this, HomeActivity.class);
                intent.putExtra("TAB", 0);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_order:// 根据当前门店账单状态跳转不同的门店帐单页面
                if (FrxsApplication.getInstance().getShopBillState()){
                    intent = new Intent(this, ShopBillActivity.class);
                } else {
                    intent = new Intent(this, AccountBillActivity.class);
                }
                startActivity(intent);
                finish();
                break;
        }
        super.onClick(view);
    }
}
