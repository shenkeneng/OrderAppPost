package com.frxs.order;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 下单成功 by Tiepier
 */
public class OrderSuccessActivity extends FrxsActivity {

    private TextView tvOrderExpectedTime;//预计送达时间

    private TextView tvOrderView;//点击查看

    private TextView tvOrderDliveryDate; //订单预计配送日期

    private LinearLayout llDeliveryDate;

    private TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_success;
    }

    @Override
    protected void initViews() {
        /**
         * 实例化控件
         */
        tvOrderView = (TextView) findViewById(R.id.tv_order_view);
        tvTitle = (TextView) findViewById(R.id.title_tv);
        tvOrderDliveryDate = (TextView) findViewById(R.id.tv_order_delivery_date);
        llDeliveryDate = (LinearLayout) findViewById(R.id.ll_delivery_date);

    }

    @Override
    protected void initData() {
        tvTitle.setText("下单成功");
        Intent intent = getIntent();
        if (intent != null) {
            String orderDeliveryDate =  intent.getStringExtra("order_delivery_date");
            if (!TextUtils.isEmpty(orderDeliveryDate)) {
                llDeliveryDate.setVisibility(View.VISIBLE);
                tvOrderDliveryDate.setText(orderDeliveryDate);
            }
        }
    }

    @Override
    protected void initEvent() {
        tvOrderView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_order_view: {
                Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
                intent.putExtra("TAB", 3);
                startActivity(intent);
                finish();
                break;
            }
            default:
                break;
        }
    }

}
