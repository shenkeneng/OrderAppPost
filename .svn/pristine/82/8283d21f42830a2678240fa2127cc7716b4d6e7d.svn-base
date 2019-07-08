package com.frxs.order;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ewu.core.utils.DateUtil;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.UserInfoStatus;
import com.frxs.order.model.Warehouse;
import com.frxs.order.model.WarehouseLine;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;

import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Chentie on 2017/6/8.
 */

public class DeliveryInfoActivity extends FrxsActivity {
    private TextView tvTitle;//门店名称

    private TextView tvUserTime;//下单时间

    //private TextView tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;//配送周期

    private TextView tvUserInfo;//配送周期 门店级别

    private TextView tvUserTel1, tvUserTel2, tvUserSaleTel, tvUserAccountTel;//仓库联系薄
    private TextView shopNameTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_delivery_info;
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("仓库配送信息");
        findViewById(R.id.tv_title_right).setVisibility(View.GONE);
        shopNameTv = (TextView) findViewById(R.id.tv_shop_name);
        tvUserTime = (TextView) findViewById(R.id.tv_user_time);
        tvUserInfo = (TextView) findViewById(R.id.tv_user_info);
        tvUserTel1 = (TextView) findViewById(R.id.tv_user_tel1);
        tvUserTel2 = (TextView) findViewById(R.id.tv_user_tel2);
        tvUserSaleTel = (TextView) findViewById(R.id.tv_user_saletel);
        tvUserAccountTel = (TextView) findViewById(R.id.tv_user_accounttel);


    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        showProgressDialog();
        String shopName = FrxsApplication.getInstance().getmCurrentShopInfo().getShopName();
        if (!TextUtils.isEmpty(shopName)) {
            shopNameTv.setText(shopName);
        }

        /**
         * 订单时间和配送周期
         */
        AjaxParams params = new AjaxParams();
        params.put("UserID", FrxsApplication.getInstance().getUserInfo().getUserId());
        params.put("WID", FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
        params.put("ShopID", FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
        getService().GetShopDeliveryCycle(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<WarehouseLine>>() {
            @Override
            public void onResponse(ApiResponse<WarehouseLine> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null) {
                        WarehouseLine data = result.getData();
                        setDeliverGoodsTimes(data);
                    } else {
                        ToastUtils.show(DeliveryInfoActivity.this, result.getInfo());
                        tvUserTime.setText("暂无订单");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<WarehouseLine>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });

        /**
         * 门店状态和级别
         */
        AjaxParams userParams = new AjaxParams();
        userParams.put("WID", getWID());
        userParams.put("ShopID", getShopID());
        getService().GetUserInfo(userParams.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<UserInfoStatus>>() {
            @Override
            public void onResponse(ApiResponse<UserInfoStatus> result, int code, String msg) {
                if (null != result) {
                    if (result.getFlag().equals("0") && null != result.getData()) {
                        if (!TextUtils.isEmpty(result.getData().getStatusStr()) && !TextUtils.isEmpty(result.getData().getCreditLevel())) {
                            tvUserInfo.setText("配送周期：" + result.getData().getStatusStr() + "        门店级别：" + result.getData().getCreditLevel());
                        } else {
                            tvUserInfo.setText("配送周期：        门店级别：");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserInfoStatus>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });

        /**
         * 仓库信息
         */
        AjaxParams wareHoaseParams = new AjaxParams();
        wareHoaseParams.put("UserID", getUserID());
        wareHoaseParams.put("WID", getWID());
        getService().WarehouseGet(wareHoaseParams.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Warehouse>>() {
            @Override
            public void onResponse(ApiResponse<Warehouse> result, int code, String msg) {
                dismissProgressDialog();
                if (null != result) {
                    if (result.getFlag().equals("0") && null != result.getData()) {
                        tvUserTel1.setText((TextUtils.isEmpty(result.getData().getYW1Tel())) ? "业务咨询电话1：" : ("业务咨询电话1：" + result.getData().getYW1Tel()));
                        tvUserTel2.setText((TextUtils.isEmpty(result.getData().getYW2Tel())) ? "业务咨询电话2：" : ("业务咨询电话2：" + result.getData().getYW2Tel()));
                        tvUserSaleTel.setText((TextUtils.isEmpty(result.getData().getTHBTel())) ? "退货部电话：" : ("退货部电话：" + result.getData().getTHBTel()));
                        tvUserAccountTel.setText((TextUtils.isEmpty(result.getData().getCWTel())) ? "核算办公室电话：" : ("核算办公室电话：" + result.getData().getCWTel()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Warehouse>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });

    }

    /**
     * 送货时间显示
     *
     * @param wl
     */
    private void setDeliverGoodsTimes(WarehouseLine wl) {
        if (!TextUtils.isEmpty(wl.getOrderEndTime())) {
            String time1 = "";
            if (!TextUtils.isEmpty(wl.getOrderEndTime())) {
                Date date1 = DateUtil.string2Date(wl.getOrderEndTime(), "HH:mm");
                time1 = DateUtil.format(date1, "HH:mm");
            }
            tvUserTime.setText(Html.fromHtml("<font color=red>" + ((TextUtils.isEmpty(time1)) ? "" : time1) + "</font>前"));
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
}
