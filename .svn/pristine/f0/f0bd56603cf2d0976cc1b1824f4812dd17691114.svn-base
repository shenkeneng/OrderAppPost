package com.frxs.order;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.BillDetails;
import com.frxs.order.model.SaleSettle;
import com.frxs.order.model.SettleDetail;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Endoon on 2016/5/16.
 */
public class AccountBillDetailsActivity extends FrxsActivity{
    private String mSettleId;

    private Adapter<SettleDetail> settleDetailAdapter;

    private TextView tvSaleSettleDate;

    private TextView tvSaleSettleId;

    private ListView lvSettleList;

    private TextView tvBillMoney;

    private TextView tvTitle;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_accountbill_details;
    }

    @Override
    protected void initViews() {
        tvSaleSettleDate = (TextView) findViewById(R.id.tv_salesettle_date);
        tvSaleSettleId = (TextView) findViewById(R.id.tv_salesettle_id);
        lvSettleList = (ListView) findViewById(R.id.lv_settle_list);
        tvBillMoney = (TextView) findViewById(R.id.tv_bill_money);
        tvTitle = (TextView) findViewById(R.id.title_tv);
    }

    @Override
    protected void initData() {
        tvTitle.setText("账单详情");

        Intent intent = getIntent();
        if (intent != null)
        {
            mSettleId = intent.getStringExtra("SETTLEID");
        }

        if (!TextUtils.isEmpty(mSettleId))
        {
            reqBillDatails();
        }

        settleDetailAdapter = new Adapter<SettleDetail>(this,R.layout.item_accountbill_details) {
            @Override
            protected void convert(AdapterHelper helper, SettleDetail item) {
                String time = item.getBillDate();
                String settleTime = time.substring(0,time.indexOf(" "));
                helper.setText(R.id.tv_settle_date,settleTime);
                helper.setText(R.id.tv_bill_id,"单据号："+item.getBillID());
                helper.setText(R.id.tv_bill_type,item.getBillTypeStr());
                helper.setText(R.id.tv_bill_amt,"￥"+MathUtils.twolittercountString(item.getBillPayAmt()));
            }
        };
        lvSettleList.setAdapter(settleDetailAdapter);
    }

    @Override
    protected void initEvent() {
    }

    private void reqBillDatails()
    {
        AjaxParams params = new AjaxParams();
        params.put("UserID", FrxsApplication.getInstance().getUserInfo().getUserId());
        params.put("WID",FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
        params.put("ShopID",FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
        params.put("SettleID",mSettleId);
        getService().SaleSettleGetListActionGetModel(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<BillDetails>>() {
            @Override
            public void onResponse(ApiResponse<BillDetails> result, int code, String msg) {
                if (result.getFlag().equals("0"))
                {
                    BillDetails bd = result.getData();
                    if (bd != null)
                    {
                        SaleSettle currentSettle = bd.getSaleSettle();
                        if (currentSettle != null)
                        {
                            String time = currentSettle.getSettleTime();
                            String settleTime = time.substring(0,time.indexOf(" "));
                            tvSaleSettleDate.setText(settleTime);
                            tvSaleSettleId.setText("结算单号："+currentSettle.getSettleID());
                            tvBillMoney.setText("结款：￥"+MathUtils.twolittercountString(currentSettle.getSettleAmt()));
                        }

                        List<SettleDetail> details = bd.getSaleSettleDetailList();
                        if (details != null && details.size() > 0)
                        {
                            settleDetailAdapter.replaceAll(details);
                            settleDetailAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BillDetails>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
}
