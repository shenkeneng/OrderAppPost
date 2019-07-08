package com.frxs.order.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ewu.core.utils.CommonUtils;
import com.ewu.core.utils.EasyPermissionsEx;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.AccountBillActivity;
import com.frxs.order.LoginActivity;
import com.frxs.order.MyBizCircleShopsActivity;
import com.frxs.order.PointDetailActivity;
import com.frxs.order.PreSaleActivity;
import com.frxs.order.R;
import com.frxs.order.SalesBackActivity;
import com.frxs.order.ShopBillActivity;
import com.frxs.order.UserInfoActivity;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.ColumnSwitchSet;
import com.frxs.order.model.Warehouse;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.google.gson.JsonObject;
import retrofit2.Call;

/**
 * 关于我 by Tiepier
 */
public class MineFragment extends FrxsFragment {
    private TextView tvUserInfo;//用户信息
    //private TextView llAccountBill;//我的账户
    private TextView llBizCircle; //商圈门店
    private View llPreSale; //预售专场
    private LinearLayout llComplaintsHotline;//投诉热线
    private LinearLayout llUpdateVersion;//版本更新
    private TextView tvSignOut;//退出用户
    private TextView tvVersionNumber;
    private LinearLayout llUserInfo;
    private View llPoints;// 积分明细
    private View llReturnManage;// 退货管理
    private View llAccountSafety;// 账户安全
    private TextView llShopBill;// 门店帐单

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViews(View view) {
        /**
         * 实例化控件
         */
        tvUserInfo = (TextView) view.findViewById(R.id.tv_user_info);
        llPoints = view.findViewById(R.id.ll_points);
        llShopBill = (TextView) view.findViewById(R.id.ll_shopbill);
        llBizCircle = (TextView) view.findViewById(R.id.ll_biz_circle_store_layout);
        llPreSale = view.findViewById(R.id.ll_pre_sale_layout);
        llComplaintsHotline = (LinearLayout) view.findViewById(R.id.ll_complaints_hotline);
        llUpdateVersion = (LinearLayout) view.findViewById(R.id.ll_update_version);
        tvSignOut = (TextView) view.findViewById(R.id.tv_sign_out);
        tvVersionNumber = (TextView) view.findViewById(R.id.tv_version_number);
        llUserInfo = (LinearLayout) view.findViewById(R.id.ll_user_info);
        llReturnManage = view.findViewById(R.id.ll_return_manage);
        llAccountSafety = view.findViewById(R.id.ll_account_safety);
    }

    @Override
    protected void initEvent() {
        llPoints.setOnClickListener(this);//积分明细
        tvSignOut.setOnClickListener(this);//退出用户
        llUpdateVersion.setOnClickListener(this);//版本更新
        llComplaintsHotline.setOnClickListener(this);//投诉热线
        llBizCircle.setOnClickListener(this);
        llPreSale.setOnClickListener(this);
        llUserInfo.setOnClickListener(this);//门店信息
        llReturnManage.setOnClickListener(this);
        llAccountSafety.setOnClickListener(this);
        llShopBill.setOnClickListener(this);// 门店帐单
    }

    @Override
    protected void initData() {
        String shopName = FrxsApplication.getInstance().getmCurrentShopInfo().getShopName();
        if (!TextUtils.isEmpty(shopName)) {
            tvUserInfo.setText(shopName);//用户信息
        }
        tvVersionNumber.setText(getActivity().getResources().getString(R.string.tv_version_id, getVersion()));

        initColumns();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initColumns();
        }
    }

    private void initColumns() {
        initPreSaleItem();
        initBizCircleItem();
        initPointsItem();
        initBuyBackItem();
    }

    /**
     * 初始化预售专场入口
     */
    private void initPreSaleItem() {
        ColumnSwitchSet switchSet = FrxsApplication.getInstance().getColumnSwitchSet();
        if (null != switchSet && switchSet.isDisplayPreScale()) {
            llPreSale.setVisibility(View.VISIBLE);
        } else {
            llPreSale.setVisibility(View.GONE);
        }
    }

    private void initBizCircleItem() {
        ColumnSwitchSet switchSet = FrxsApplication.getInstance().getColumnSwitchSet();
        if (null != switchSet && switchSet.isDisplayCircle()) {
            llBizCircle.setVisibility(View.VISIBLE);
        } else {
            llBizCircle.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化积分明细入口
     */
    private void initPointsItem() {
        ColumnSwitchSet switchSet = FrxsApplication.getInstance().getColumnSwitchSet();
        if (null != switchSet && switchSet.isDisplayPoint()) {
            llPoints.setVisibility(View.VISIBLE);
        } else {
            llPoints.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化退货管理入口
     */
    private void initBuyBackItem() {
        ColumnSwitchSet switchSet = FrxsApplication.getInstance().getColumnSwitchSet();
        if (null != switchSet && switchSet.isDisplayBuyBack()) {
            llReturnManage.setVisibility(View.VISIBLE);
        } else {
            llReturnManage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (CommonUtils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            //退出用户
            case R.id.tv_sign_out: {
                logOutDialog();
                break;
            }
            //版本更新
            case R.id.ll_update_version: {
                if (SystemUtils.isNetworkAvailable(mActivity)) {
                    checkUpdate();
                } else {
                    ToastUtils.show(mActivity, "网络异常，请检查网络是否连接");
                }
                break;
            }
            //投诉电话
            case R.id.ll_complaints_hotline: {
                reqWarehouse();
                break;
            }
            //门店账单
            case R.id.ll_shopbill: {
                requestNeedShowShopBill();
                break;
            }
            //商圈门店
            case R.id.ll_biz_circle_store_layout: {
                Intent intent = new Intent(mActivity, MyBizCircleShopsActivity.class);
                startActivity(intent);
                break;
            }
            //代购专场
            case R.id.ll_pre_sale_layout: {
                Intent intent = new Intent(mActivity, PreSaleActivity.class);
                startActivity(intent);
                break;
            }
            //门店资料
            case R.id.ll_user_info: {
                Intent userInfo = new Intent(mActivity, UserInfoActivity.class);
                userInfo.putExtra("FROM", "user");
                startActivity(userInfo);
                break;
            }
            //积分明细
            case R.id.ll_points: {
                Intent intent = new Intent(mActivity, PointDetailActivity.class);
                startActivity(intent);
                break;
            }
            //退货管理
            case R.id.ll_return_manage: {
                Intent intent = new Intent(mActivity, SalesBackActivity.class);
                startActivity(intent);
                break;
            }
            //账户安全
            case R.id.ll_account_safety: {
                Intent safetyIntent = new Intent(mActivity, UserInfoActivity.class);
                safetyIntent.putExtra("FROM", "safety");
                startActivity(safetyIntent);
                break;
            }
            default:
                break;
        }
    }

    private void logOutDialog() {
        final MaterialDialog mDialog = new MaterialDialog(mActivity);
        mDialog.setMessage("确认退出登录？");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrxsApplication.getInstance().logout();
                Intent outLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(outLogin);
                mDialog.dismiss();
                getActivity().finish();
            }
        });
        mDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    /**
     * 仓库信息数据请求
     */
    public void reqWarehouse() {
        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("WID", getWID());
        getService().WarehouseGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Warehouse>>() {
            @Override
            public void onResponse(ApiResponse<Warehouse> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                Warehouse respData = result.getData();
                if (null != respData) {
                    final String strTelWarehouse = respData.getWCustomerServiceTel();
                    if (null != strTelWarehouse && !strTelWarehouse.equals("")) {
                        final MaterialDialog materialDialog = new MaterialDialog(mActivity);
                        materialDialog.setMessage("拨打投诉电话：" + strTelWarehouse);
                        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        materialDialog.dismiss();
                                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            EasyPermissionsEx.requestPermissions(mActivity, "需要开启拨打电话的权限", 1, Manifest.permission.CALL_PHONE);
                                            return;
                                        }
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strTelWarehouse));
                                        mActivity.startActivity(intent);
                                    }
                                }
                        );
                        materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                materialDialog.dismiss();
                            }
                        });
                        materialDialog.show();
                    } else {
                        ToastUtils.show(mActivity, "无投诉电话");
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Warehouse>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });

    }

    /**
     * 获取系统版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

    public void requestNeedShowShopBill() {
        mActivity.showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());

        getService().GetShopMergePaySwitch(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<JsonObject>>() {
            @Override
            public void onResponse(ApiResponse<JsonObject> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    JsonObject jsonData = result.getData();
                    if (jsonData != null) {
                        Intent toBill = null;
                        String switchValue = jsonData.get("SwitchValue").getAsString();
                        String vExt1 = jsonData.get("VExt1").getAsString();
                        if (switchValue.equals("1") && vExt1.equals("1")) {
                            FrxsApplication.getInstance().setShopBillState(true); // 记录当前门店支付方式
                            toBill = new Intent(mActivity, ShopBillActivity.class);
                        } else {
                            FrxsApplication.getInstance().setShopBillState(false); // 记录当前门店支付方式
                            toBill = new Intent(mActivity, AccountBillActivity.class);
                        }

                        if (toBill != null) {
                            startActivity(toBill);
                        }
                    } else {
                        ToastUtils.show(mActivity, "获取当前门店账单状态失败!");
                    }
                } else {
                    if (!TextUtils.isEmpty(result.getInfo())) {
                        ToastUtils.show(mActivity, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<JsonObject>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        FrxsApplication.getInstance().prepare4Update(mActivity, true);
    }

//    UmengUpdateListener listener = new UmengUpdateListener() {
//
//        @Override
//        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//            switch (updateStatus) {
//                case UpdateStatus.Yes:
//                    ToastUtils.show(mActivity,"发现更新");
//                    break;
//                case UpdateStatus.No:
//                    ToastUtils.show(mActivity,"没有发现更新");
//                    break;
//                case UpdateStatus.NoneWifi:
//                    // Toast.makeText(getActivity(), "没有wifi", Toast.LENGTH_SHORT)
//                    // .show();
//                    break;
//                case UpdateStatus.Timeout:
//                    ToastUtils.show(mActivity,"检查更新超时");
//                    break;
//            }
//        }
//
//    };
}
