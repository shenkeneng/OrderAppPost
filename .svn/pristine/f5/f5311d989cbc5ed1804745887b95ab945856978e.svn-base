package com.frxs.order;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ewu.core.utils.DateUtil;
import com.ewu.core.utils.MD5Util;
import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.model.ShopApplyInfoStatus;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.UrlDecorator;

import java.util.Date;
import java.util.Random;

import retrofit2.Call;

/**
 * Created by Chentie on 2017/1/6.
 */
public class UserInfoActivity extends FrxsActivity {

    private String from;

    private View llUserInfo;// 用户信息

    private View llCredentialsInfo;// 证照信息

    private View llAccountInfo;// 帐户信息

    private View llDeliveryInfo;// 仓库配送信息

    private View llPswModify;// 修改密码

    private View llMobBundle;// 修改手机号

    private View llBankModify;// 修改银行卡

    private TextView shopInfoTv;//门店资料审核状态

    private TextView liceseInfoTv;//证照信息审核状态

    private TextView accountInfoTv;//账户信息审核状态

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initViews() {
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText("门店资料");
        from = getIntent().getStringExtra("FROM");
        if (!TextUtils.isEmpty(from) && from.equals("user")) {
            findViewById(R.id.ll_shop_info).setVisibility(View.VISIBLE);
            llUserInfo = findViewById(R.id.ll_user_info);
            llCredentialsInfo = findViewById(R.id.ll_credentials_info);
            llAccountInfo = findViewById(R.id.ll_account_info);
            llDeliveryInfo = findViewById(R.id.ll_delivery_info);
            shopInfoTv = (TextView) findViewById(R.id.tv_shop_info);
            liceseInfoTv = (TextView) findViewById(R.id.tv_license_info);
            accountInfoTv = (TextView) findViewById(R.id.tv_account_info);
        } else if (!TextUtils.isEmpty(from) && from.equals("safety")) {
            findViewById(R.id.ll_safety_setting).setVisibility(View.VISIBLE);
            llPswModify = findViewById(R.id.ll_password_modify);
            llMobBundle = findViewById(R.id.ll_mob_bundle);
            llBankModify = findViewById(R.id.ll_bank_modify);
        }
    }

    @Override
    protected void initEvent() {
        if (!TextUtils.isEmpty(from) && from.equals("user")) {
            llUserInfo.setOnClickListener(this);
            llCredentialsInfo.setOnClickListener(this);
            llAccountInfo.setOnClickListener(this);
            llDeliveryInfo.setOnClickListener(this);
        } else if (!TextUtils.isEmpty(from) && from.equals("safety")) {
            llPswModify.setOnClickListener(this);
            llMobBundle.setOnClickListener(this);
            llBankModify.setOnClickListener(this);
        }
    }

    @Override
    protected void initData() {
        reqGetShopApplyInfoStatus(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reqGetShopApplyInfoStatus(false);
    }

    private void reqGetShopApplyInfoStatus(final boolean startBankInfo) {
        showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", getShopID());
        params.put("ShopAccount", userInfo.getUserAccount());
        params.put("UserId", getUserID());
        params.put("UserName", userInfo.getUserName());

        getService().GetShopApplyInfoStatus(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopApplyInfoStatus>>() {
            @Override
            public void onResponse(ApiResponse<ShopApplyInfoStatus> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null) { // 状态(0:待审核,1:通过审核;2:驳回审核)
                        ShopApplyInfoStatus info = result.getData();
                        if (!TextUtils.isEmpty(from) && from.equals("user")) {
                            int shopStatus = info.getApplyStatus4ShopInfo();
                            shopInfoTv.setText((shopStatus == 0) ? "未审核" : (shopStatus == 1 ? "审核通过" : (shopStatus == 2) ? "审核未通过" : ""));
                            int licenseStatus = info.getApplyStatus4LicenseInfo();
                            liceseInfoTv.setText((licenseStatus == 0) ? "未审核" : (licenseStatus == 1 ? "审核通过" : (licenseStatus == 2) ? "审核未通过" : ""));
                            int accountStatus = info.getApplyStatus4AccountInfo();
                            accountInfoTv.setText((accountStatus == 0) ? "未审核" : (accountStatus == 1 ? "审核通过" : (accountStatus == 2) ? "审核未通过" : ""));
                        } else {
                            llBankModify.setVisibility(info.getBankInfoPassedFlag() == 0 ? View.GONE : View.VISIBLE);
                            if (startBankInfo) {
                                isStartBankInfo(info);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShopApplyInfoStatus>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

    /**
     * 是否开启银行信息入口
     * @param info
     */
    private void isStartBankInfo(ShopApplyInfoStatus info) {
        if (info.getBankInfoPassedFlag() == 0) {
            final MaterialDialog materialDialog = new MaterialDialog(UserInfoActivity.this);
            materialDialog.setMessage("信息待审核中，暂时无法修改!");
            materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialDialog.dismiss();
                }
            });
            materialDialog.show();
        } else {
            Intent toBank = new Intent(UserInfoActivity.this, UpdatePswActivity.class);
            toBank.putExtra("FROM", "BANK");
            toBank.putExtra("BANK_INFO", info);
            startActivity(toBank);
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_user_info: {// 门店信息
                Intent intent = new Intent(UserInfoActivity.this, CommMyWebViewActivity.class);
                UrlDecorator urlDecorator = new UrlDecorator(Config.getBaseUrl() + "AppData/ShopModify");
                urlDecorator.add("shopId", getShopID());
                String content = getShopID() + DateUtil.format(new Date(), "yyyyMMdd") + "frxs";
                urlDecorator.add("secret", MD5Util.MD5Encode(content, ""));
                intent.putExtra("H5_URL", urlDecorator.toString() + "&a=" + new Random().nextInt());
                intent.putExtra("Title", getString(R.string.shop_info));
                startActivity(intent);
            }
            break;

            case R.id.ll_credentials_info: {// 证照信息
                Intent intent = new Intent(UserInfoActivity.this, CommMyWebViewActivity.class);
                UrlDecorator urlDecorator = new UrlDecorator(Config.getBaseUrl() + "AppData/LicModify");
                urlDecorator.add("shopId", getShopID());
                String content = getShopID() + DateUtil.format(new Date(), "yyyyMMdd") + "frxs";
                urlDecorator.add("secret", MD5Util.MD5Encode(content, ""));
                intent.putExtra("H5_URL", urlDecorator.toString() + "&a=" + new Random().nextInt());
                intent.putExtra("Title", getString(R.string.credentials_info));
                startActivity(intent);
            }
            break;

            case R.id.ll_account_info: {// 账户信息
                Intent intent = new Intent(UserInfoActivity.this, CommMyWebViewActivity.class);
                UrlDecorator urlDecorator = new UrlDecorator(Config.getBaseUrl() + "AppData/BankModify");
                urlDecorator.add("shopId", getShopID());
                String content = getShopID() + DateUtil.format(new Date(), "yyyyMMdd") + "frxs";
                urlDecorator.add("secret", MD5Util.MD5Encode(content, ""));
                intent.putExtra("H5_URL", urlDecorator.toString() + "&a=" + new Random().nextInt());
                intent.putExtra("Title", getString(R.string.account_info));
                startActivity(intent);
            }
            break;

            case R.id.ll_delivery_info://　仓库配送信息
                Intent intent = new Intent(this, DeliveryInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_password_modify:// 修改密码
                Intent toPassWord = new Intent(this, UpdatePswActivity.class);
                toPassWord.putExtra("FROM", "PSW");
                startActivity(toPassWord);
                break;

            case R.id.ll_mob_bundle:// 修改手机号
                Intent toMob = new Intent(this, UpdatePswActivity.class);
                toMob.putExtra("FROM", "MOB");
                startActivity(toMob);
                break;

            case R.id.ll_bank_modify:// 修改银行卡
                reqGetShopApplyInfoStatus(true);
                break;

            default:
                break;
        }
    }
}
