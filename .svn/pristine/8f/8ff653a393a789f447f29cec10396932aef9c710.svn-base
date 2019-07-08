package com.frxs.order;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.ewu.core.base.BaseActivity;
import com.ewu.core.utils.EasyPermissionsEx;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.model.PostPrePay;
import com.frxs.order.model.PrefectShopInfo;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.ApiService;
import com.frxs.order.rest.service.RequestListener;
import com.frxs.order.rest.service.SimpleCallback;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import retrofit2.Call;

/**
 * Created by ewu on 2016/3/24.
 */
public abstract class FrxsActivity extends BaseActivity {

    private IWXAPI msgApi;// 微信支付API

    private static final int MY_PERMISSIONS_REQUEST_WES = 2;// 请求文件存储权限的标识码

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;// 请求相机权限的标识码

    private Intent intent = null;

    private static final int CCB_PAY = 338;// 建行支付

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(this instanceof SplashActivity) && !(this instanceof BaseDialogActivity)) {
            // 判断当前用户是否允许此权限
            if (EasyPermissionsEx.hasPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                // 允许 - 执行更新方法
                if (FrxsApplication.getInstance().isNeedCheckUpgrade()) {
                    FrxsApplication.getInstance().prepare4Update(this, false);
                }
            } else {
                // 不允许 - 弹窗提示用户是否允许放开权限
                EasyPermissionsEx.executePermissionsRequest(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WES);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
            }

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.frxs_red);
        }

        msgApi = WXAPIFactory.createWXAPI(this, Config.getWXAppID());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public ApiService getService() {
        return FrxsApplication.getRestClient().getApiService();
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initEvent();

    protected abstract void initData();

    public void onBack(View view) {
        finish();
    }

    public String getUserID()
    {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        if (null != userInfo)
        {
            return userInfo.getUserId();
        }
        else
        {
            return "";
        }
    }

    public String getShopID()
    {
        ShopInfo shopInfo = FrxsApplication.getInstance().getmCurrentShopInfo();
        if (null != shopInfo)
        {
            return shopInfo.getShopID();
        }
        else
        {
            return "";
        }
    }

    public String getWID()
    {
        ShopInfo shopInfo = FrxsApplication.getInstance().getmCurrentShopInfo();
        if (null != shopInfo)
        {
            return String.valueOf(shopInfo.getWID());
        }
        else
        {
            return "";
        }
    }

    /**
     * 请求单个订单预支付信息
     * @param orderNo
     * @param payType
     */
    public void requestPrepayInfo(String orderNo, final int payType) {
        requestPrepayInfo(orderNo, null, payType);
    }

    /**
     * 请求多个订单预支付信息
     * @param cusVoucherIDList
     * @param payType
     */
    public void requestPrepayInfo(List<Long> cusVoucherIDList, final int payType) {
        requestPrepayInfo(null, cusVoucherIDList, payType);
    }

    /**
     * 请求预支付信息
     * @param orderNo
     * @param payType
     */
    private void requestPrepayInfo(String orderNo, List<Long> cusVoucherIDList, final int payType) {
        showProgressDialog();
        FrxsApplication.getInstance().setPayType(payType);
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        PostPrePay postPrePay = new PostPrePay();
        if (cusVoucherIDList != null && cusVoucherIDList.size() > 0) {
            postPrePay.setCusVoucherIDList(cusVoucherIDList);
        }
        if (!TextUtils.isEmpty(orderNo)){
            postPrePay.setOut_trade_no(orderNo);
        }
        postPrePay.setAttach("");
        postPrePay.setPayPlatform(2);
        postPrePay.setPayType(payType);
        postPrePay.setUserId(userInfo.getUserId());
        postPrePay.setShopID(userInfo.getCurrenShopInfo().getShopID());
        postPrePay.setUserName(userInfo.getUserName());
        postPrePay.setWID(getWID());

        getService().GetPrepayInfo(postPrePay).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result.isSuccessful()) {
                    if (!TextUtils.isEmpty(result.getData())) {
                        switch (payType) {
                            case 0: //微信支付
                                try {
                                    JSONObject dataResult = new JSONObject(result.getData());
                                    if (null != dataResult) {
                                        // 判断微信是否已经安装
                                        if (!msgApi.isWXAppInstalled()) {
                                            ToastUtils.show(FrxsActivity.this, getString(R.string.not_installed_wechat));
                                            return;
                                        }

                                        PayReq req = new PayReq();
                                        req.appId = dataResult.getString("appid");
                                        req.partnerId = dataResult.getString("partnerid");
                                        req.prepayId = dataResult.getString("prepayid");
                                        req.nonceStr = dataResult.getString("noncestr");
                                        req.timeStamp = dataResult.getString("timestamp");
                                        req.packageValue = dataResult.getString("package");
                                        req.sign = dataResult.getString("paySign");

                                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                        msgApi.registerApp(Config.getWXAppID());
                                        msgApi.sendReq(req);
                                    } else {
                                        ToastUtils.show(FrxsActivity.this, result.getInfo());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 1: //建行支付
                                Intent intent = new Intent(FrxsActivity.this, CommonWebViewActivity.class);
                                intent.putExtra("REQUSTPOINT", result.getData());
                                intent.putExtra("H5TITLE", "建行支付");
                                startActivityForResult(intent, CCB_PAY);
                                break;
                        }
                    }
                } else {
                    ToastUtils.show(FrxsActivity.this, result.getInfo());
                }
        }

        @Override
        public void onFailure (Call < ApiResponse < String >> call, Throwable t){
            super.onFailure(call, t);
            ToastUtils.show(FrxsActivity.this, "预支付信息生成失败");
            dismissProgressDialog();

        }
        });
    }

    public void reqIsPrefectShopInfo(final RequestListener listener) {
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        getService().IsPrefectShopInfo(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<PrefectShopInfo>>() {
            @Override
            public void onResponse(ApiResponse<PrefectShopInfo> result, int code, String msg) {
                if (null != listener) {
                    listener.handleRequestResponse(result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PrefectShopInfo>> call, Throwable t) {
                super.onFailure(call, t);
                if (null != listener) {
                    listener.handleExceptionResponse(t.getMessage());
                }
            }
        });
    }

    /**
     * 调用相机时判断是否有相机权限
     * @param intent
     */
    public void hasCameraPermissions(Intent intent, boolean isfinish){
        this.intent = intent;
        // 判断当前用户是否允许相机权限
        if (EasyPermissionsEx.hasPermissions(this, new String[]{Manifest.permission.CAMERA})) {
            // 允许 - 调起相机
            startActivity(intent);
            if (isfinish){
                finish();
            }
        } else {
            // 不允许 - 弹窗提示用户是否允许放开权限
            EasyPermissionsEx.executePermissionsRequest(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    /**
     * 请求用户是否放开权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 已获取权限 继续运行应用
                    if (FrxsApplication.getInstance().isNeedCheckUpgrade()) {
                        FrxsApplication.getInstance().prepare4Update(this, false);
                    }
                } else {
                    // 不允许放开权限后，提示用户可在去设置中跳转应用设置页面放开权限。
                    if (!EasyPermissionsEx.somePermissionPermanentlyDenied(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                        EasyPermissionsEx.goSettings2PermissionsDialog(this, "需要文件存储权限来下载更新的内容,但是该权限被禁止,你可以到设置中更改");
                    }
                }
                break;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA: {// 扫码相机权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 已获取权限 继续运行应用
                    startActivity(intent);
                } else {
                    ToastUtils.show(FrxsActivity.this, getString(R.string.hasCameraPermission));
                }
                break;
            }

        }
    }

    /**
     * 初始化并保存支付方式
     * @param view
     * @param rgPaySelector
     */
    public void initAndSavePayType(View view, RadioGroup rgPaySelector) {
        // 获取上次操作的支付方式
        final int payType = FrxsApplication.getInstance().getPayType();
        switch (payType){
            case 0:// 微信支付
                RadioButton payWXButton = (RadioButton) view.findViewById(R.id.pay_wx_button);
                payWXButton.setChecked(true);
                break;

            case 1:// 建行支付
                RadioButton payUPButton = (RadioButton) view.findViewById(R.id.pay_up_button);
                payUPButton.setChecked(true);
                break;
        }

        rgPaySelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.pay_wx_button:
                        FrxsApplication.getInstance().setPayType(0);
                        break;

                    case R.id.pay_up_button:
                        FrxsApplication.getInstance().setPayType(1);
                        break;
                }
            }
        });
    }

    public String getPhoneInfo() {
        String phontInfo = "brand:" + Build.BRAND + ",VERSION:" + Build.VERSION.RELEASE + ",versionName:" + getVersion();
        if (TextUtils.isEmpty(phontInfo)) {
            return "";
        } else {
            return phontInfo;
        }
    }

    /**
     * 获取系统版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }
}
