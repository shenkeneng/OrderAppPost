package com.frxs.order.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.ewu.core.utils.FileUtil;
import com.ewu.core.utils.JsonParser;
import com.ewu.core.utils.SerializableUtil;
import com.ewu.core.utils.SharedPreferencesHelper;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.R;
import com.frxs.order.comms.Config;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.AppVersionGetRespData;
import com.frxs.order.model.BaseCartGoodsInfo;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.ColumnSwitchSet;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.UserInfo;
import com.frxs.order.receiver.NetStateReceiver;
import com.frxs.order.rest.RestClient;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.service.apkUpdate.DownloadService;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

/**
 * Created by ewu on 2016/2/18.
 */
public class FrxsApplication extends Application {
    private static FrxsApplication mInstance;
    public static Context context;
    private static RestClient restClient;
    private UserInfo mUserInfo;// 用户信息
    private int shopCartCount = 0; //购物车商品的数量
    private boolean needCheckUpgrade = true; // 是否需要检测更新
    private List<BaseCartGoodsInfo> cartGoodsInfoList = new ArrayList<BaseCartGoodsInfo>();
    private ColumnSwitchSet columnSwitchSet; //保存模块显示开关参数
    private List<CartGoodsDetail> mCartGoodsList; // 保存购物车数据（及时清空）

    public static FrxsApplication getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Not yet initialized");
        }

        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mInstance != null) {
            throw new IllegalStateException("Not a singleton");
        }

        context = getApplicationContext();

        mInstance = this;

        initData();

        initRestClient();

        MobclickAgent.updateOnlineConfig(getApplicationContext());

        NetStateReceiver.registerNetworkStateReceiver(this);
    }

    private void initRestClient() {
        restClient = new RestClient(Config.getBaseUrl(getEnvironment()));
    }

    public static RestClient getRestClient() {
        return restClient;
    }

    private void initData() {
        // Get the user Info
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.PREFS_NAME);
        String userStr = helper.getString(Config.KEY_USER, "");
        if (!TextUtils.isEmpty(userStr)) {
            Object object = null;
            try {
                object = SerializableUtil.str2Obj(userStr);
                if (null != object) {
                    mUserInfo = (UserInfo) object;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //Get the Goods information of the sale cart
        String saleCartProductStr = FileUtil.readInternalStoragePrivate(this, GlobelDefines.SALE_CART_FILE_NAME);
        if (null != saleCartProductStr && saleCartProductStr.trim().length() > 0) {
            Type listType = new TypeToken<List<BaseCartGoodsInfo>>() { }.getType();
            List<BaseCartGoodsInfo> productList = JsonParser .fromJson(saleCartProductStr, listType);
            if (null != productList && productList.size() > 0) {
                cartGoodsInfoList.addAll(productList);
            }
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;

        if (null != mUserInfo) {
            List<ShopInfo> shopList = mUserInfo.getShopList();
            //如果有且只有一个店铺，则当前店铺就是这个店铺
            if (null != shopList && shopList.size() == 1) {
                mUserInfo.setCurrenShopInfo(shopList.get(0));
            }
        }

        String userStr = "";
        try {
            userStr = SerializableUtil.obj2Str(userInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.PREFS_NAME);
        helper.putValue(Config.KEY_USER, userStr);
    }

    public UserInfo getUserInfo() {
        if (null == mUserInfo) {
            initData();
        }

        return mUserInfo;
    }

    public void setStoreCart(List<CartGoodsDetail> cartGoodsList){
        this.mCartGoodsList = cartGoodsList;
    }

    public List<CartGoodsDetail> getStoreCart(){
        return mCartGoodsList;
    }

    public ShopInfo getmCurrentShopInfo() {
        return mUserInfo == null ? null : mUserInfo.getCurrenShopInfo();
    }

    public void setmCurrentShopInfo(ShopInfo mCurrentShopInfo) {
        mUserInfo.setCurrenShopInfo(mCurrentShopInfo);

        String userStr = "";
        try {
            userStr = SerializableUtil.obj2Str(mUserInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.PREFS_NAME);
        helper.putValue(Config.KEY_USER, userStr);
    }

    public String getUserAccount() {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        return helper.getString(GlobelDefines.KEY_USER_ACCOUNT, "");
    }

    public void setUserAccount(String userAccount) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        helper.putValue(GlobelDefines.KEY_USER_ACCOUNT, userAccount);
    }

    public void setEnvironment(int environmentId) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        helper.putValue(GlobelDefines.KEY_ENVIRONMENT, environmentId);

        restClient = new RestClient(Config.getBaseUrl(environmentId));
    }

    public int getEnvironment() {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        return helper.getInt(GlobelDefines.KEY_ENVIRONMENT, Config.networkEnv);
    }

    /**
     * 保存支付方式
     * @param payType
     */
    public void setPayType(int payType) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        helper.putValue(GlobelDefines.KEY_PAY_TYPE, payType);
    }

    /**
     * 获取支付方式
     * @return
     */
    public int getPayType(){
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        return helper.getInt(GlobelDefines.KEY_PAY_TYPE, 0);
    }

    /**
     * 保存门支付方式
     * @param isDisplayShopBill
     */
    public void setShopBillState(boolean isDisplayShopBill) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        helper.putValue(GlobelDefines.KEY_SHOP_BILL, isDisplayShopBill);
    }

    /**
     * 获取支付方式
     * @return
     */
    public boolean getShopBillState(){
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        return helper.getBoolean(GlobelDefines.KEY_SHOP_BILL, false);
    }

    public int getShopCartCount() {
        return shopCartCount;
    }

    public void setShopCartCount(int shopCartCount) {
        this.shopCartCount = shopCartCount;
        EventBus.getDefault().post(shopCartCount);
    }

    public void addShopCartCount(int number) {
        this.shopCartCount += number;
        EventBus.getDefault().post(shopCartCount);
    }

    public boolean isNeedCheckUpgrade() {
        return needCheckUpgrade;
    }


    public void setColumnSwitchSet (ColumnSwitchSet switchSet) {
        this.columnSwitchSet = switchSet;
    }

    public ColumnSwitchSet getColumnSwitchSet() {
        return columnSwitchSet;
    }

    public double getSaleCartProductCount(String productId) {
        if (!TextUtils.isEmpty(productId)) {
            for (BaseCartGoodsInfo item : cartGoodsInfoList) {
                if (productId.equals(item.getProductId())) {
                    return item.getPreQty();
                }
            }
        }

        return 0;
    }

    /**
     * 更新本地缓存的购物车商品数量
     *
     * @param productId 商品ID
     * @param count 增加/减少的商品数量
     */
    public void updateSaleCartProduct(String productId, int count) {
        if (TextUtils.isEmpty(productId)) {
            return;
        }

        for (BaseCartGoodsInfo item : cartGoodsInfoList) {
            //如果已经在购物车当中则跟新数量，但如果数量小于等于0则，从购车中删除
            if (productId.equals(item.getProductId())) {
                count += item.getPreQty();

                if (count <= 0) {
                    cartGoodsInfoList.remove(item);
                } else {
                    item.setPreQty(count);
                }
                String fileContent = JsonParser.toJson(cartGoodsInfoList);
                FileUtil.writeInternalStoragePrivate(this, GlobelDefines.SALE_CART_FILE_NAME, fileContent);
                return;
            }
        }

        //如果不在购物车中，则新增到购物车
        if (count > 0) {
            BaseCartGoodsInfo cartGoodsInfo = new BaseCartGoodsInfo();
            cartGoodsInfo.setProductId(productId);
            cartGoodsInfo.setPreQty(count);
            cartGoodsInfoList.add(cartGoodsInfo);
            String fileContent = JsonParser.toJson(cartGoodsInfoList);
            FileUtil.writeInternalStoragePrivate(this, GlobelDefines.SALE_CART_FILE_NAME, fileContent);
        }
    }

    public void saveSaleCartProducts(List<BaseCartGoodsInfo> products) {
        cartGoodsInfoList.clear();

        if (null != products) {
            cartGoodsInfoList.addAll(products);
        }

        String fileContent = JsonParser.toJson(cartGoodsInfoList);
        FileUtil.writeInternalStoragePrivate(this, GlobelDefines.SALE_CART_FILE_NAME, fileContent);
    }

    /**
     * 更新版本的网路请求
     *
     * @param activity
     */
    public void prepare4Update(final Activity activity, final boolean isShow) {
        //开始检测了升级之后，设置标志位为不再检测升级
        if (needCheckUpgrade) {
            needCheckUpgrade = false;
        } else {
            return;
        }

        AjaxParams params = new AjaxParams();
//        params.put("Sign", MD5.ToMD5("GetAppVersion"));
        params.put("SysType", "0"); // 0:android;1:ios
        params.put("AppType", "0"); // 软件类型(0:订货平台;1:拣货APP;2:配送APP;3:装箱APP;4:采购APP)
        FrxsApplication.getRestClient().getApiService().AppVersionUpdateGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<AppVersionGetRespData>>() {
            @Override
            public void onResponse(ApiResponse<AppVersionGetRespData> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    AppVersionGetRespData respData = result.getData();
                    if (null != respData) {
                        int versionCode = Integer.valueOf(SystemUtils.getVersionCode(getApplicationContext()));

                        String curVersion = respData.getCurVersion();
                        int curCode = respData.getCurCode();

                        if (versionCode < curCode) {
                            int updateFlag = respData.getUpdateFlag();
                            String updateRemark = respData.getUpdateRemark();
                            String downloadUrl = respData.getDownUrl();
                            switch (updateFlag) {
                                case 0: // 0:不需要
                                    break;
                                case 1: // 1:建议升级
                                    showUpdateDialog(activity, false, downloadUrl, curVersion, updateRemark);
                                    break;
                                case 2: // 2：强制升级
                                    showUpdateDialog(activity, true, downloadUrl, curVersion, updateRemark);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            ToastUtils.show(activity, "没有发现更新");
                        }
                    } else {
                        // 在每次登陆页面检测是否需要更新，如果没有发现新版本则不需要提示
                        if (isShow) {
                            ToastUtils.show(activity, "没有发现更新");
                        }
                    }
                } else {
                    ToastUtils.show(activity, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AppVersionGetRespData>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /**
     * 弹出更新的dialog
     *
     * @param activity
     * @param isForceUpdate 是否强制更新 true是，false否
     * @param downloadUrl   下载链接
     * @param curVersion    最新版本
     * @param updateRemark  更新内容
     * @description
     */
    private void showUpdateDialog(final Activity activity, final boolean isForceUpdate, final String downloadUrl, String curVersion, String updateRemark) {
        final MaterialDialog updateDialog = new MaterialDialog(activity);
        updateDialog.setTitle(R.string.update_title);
        updateDialog.setMessage(String.format(activity.getResources().getString(R.string.updade_content), curVersion,
                updateRemark));
        updateDialog.setCanceledOnTouchOutside(false);// 对话框外点击无效

        // 立即更新
        updateDialog.setPositiveButton("立即更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
                //Local download for the apk
               /* UpdateService updateService = new UpdateService(activity, downloadUrl, "", isForceUpdate);
                updateService.downFile();*/

                DownloadService downLoadService = new DownloadService(activity, downloadUrl, isForceUpdate);
                downLoadService.execute();
                if (!isForceUpdate) {
                    ToastUtils.showShortToast(activity, "程序在后台下载，请稍等...");
                }
            }
        });

        // 稍后更新（强制更新）:点击稍后更新也要更新
        updateDialog.setNegativeButton("稍后更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
                if (isForceUpdate) {
                    activity.finish();
                    System.exit(0);
                }
            }
        });
        updateDialog.show();
    }

    /**
     * 退出登录
     * 在这里做，如：清空用户信息，禁止接收消息之类的操作
     */
    public void logout() {
        // 清空用户信息
        setUserInfo(null);
        cartGoodsInfoList.clear();
        // 清空首页商品缓存
        String fileContent = JsonParser.toJson(cartGoodsInfoList);
        FileUtil.writeInternalStoragePrivate(this, GlobelDefines.SALE_CART_FILE_NAME, fileContent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
    }
}
