package com.frxs.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.NetWorkUtil;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.BadgeView;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.NoReloadFragmentTabHost;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.fragment.CategoryNewFragment;
import com.frxs.order.fragment.HomeFragment;
import com.frxs.order.fragment.MineFragment;
import com.frxs.order.fragment.OrderPreGoodsFragment;
import com.frxs.order.fragment.OrdersFragment;
import com.frxs.order.fragment.StoreCartFragment;
import com.frxs.order.model.BaseCartGoodsInfo;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.DataSynEvent;
import com.frxs.order.model.SaleCartGetRespData;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.UnreadMessag;
import com.frxs.order.model.UserInfo;
import com.frxs.order.model.WXNoPayQuery;
import com.frxs.order.receiver.NetChangeObserver;
import com.frxs.order.receiver.NetStateReceiver;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.google.gson.JsonObject;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;


/**
 * 首页 by Tiepier
 */

public class HomeActivity extends FrxsActivity {


    private NoReloadFragmentTabHost mTabHost;

    private BadgeView badgeView;

    // 定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class, CategoryNewFragment.class, StoreCartFragment.class,
            OrdersFragment.class, MineFragment.class};

    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_icon_selector,
            R.drawable.tab_category_icon_selector, R.drawable.tab_cart_icon_selector, R.drawable.tab_order_icon_selector, R.drawable.tab_mine_icon_selector};

    // Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "订购", "购物车", "订单", "我的"};

    private ShopInfo shopInfo;

    private boolean needSyncSaleCartGoods = false; //是否需要同步购物车商品数量，首次登陆同步购物车数量

    //用来控制应用前后台切换的标识
    private boolean applicationBroughtToBackground = true;

    private BottomSheetDialog dialog;// 未支付提示弹框

    //网络观察者
    protected NetChangeObserver mNetChangeObserver = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        mTabHost = (NoReloadFragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tab_content_layout);

        // 得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i)
//                    .setBackgroundResource(R.drawable.main_tab_item_bg);
        }

//        badgeView = new BadgeView(this, mTabHost.getTabWidget().getChildAt(2));
//        badgeView.setTextSize(10);
//        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
    }

    @Override
    protected void initData() {
        shopInfo = FrxsApplication.getInstance().getmCurrentShopInfo();
        if (null != shopInfo) {
            Intent intent = getIntent();
            if (null != intent) {
                needSyncSaleCartGoods = intent.getBooleanExtra("sync_sale_cart", false);
            }
            //请求更新购物车商品数量显示
            if (needSyncSaleCartGoods) {
                requestGetSaleCartGoods();
            } else {
                requestGetSaleCartCount();
            }
        }

        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetWorkUtil.NetType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                onNetworkDisConnected();
            }
        };
        //开启广播去监听网络改变事件
        NetStateReceiver.registerObserver(mNetChangeObserver);
    }

    /**
     * 网络连接状态
     *
     * @param type 网络状态
     */
    protected void onNetworkConnected(NetWorkUtil.NetType type) {
        Fragment homeFrag = getFragment(0);
        if (null != homeFrag && homeFrag instanceof HomeFragment) {
            ((HomeFragment) homeFrag).syncData();
        }
    }

    /**
     * 网络断开的时候调用
     */
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
    }

    @Override
    protected void initEvent() {
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
    }

    public Fragment getCurrentFragment() {
        int currentTab = mTabHost.getCurrentTab();
        return getFragment(currentTab);
    }

    public Fragment getFragment(int tabIndex) {
        return getSupportFragmentManager().findFragmentByTag(mTextviewArray[tabIndex]);
    }

    @Subscribe
    public void onEventMainThread(Integer busEvent) {
        showBadgeView(busEvent);
    }

    private void showBadgeView(int count) {
        if (count > 0) {
            if (count > 100) {
                badgeView.setText("99+");
            } else {
                badgeView.setText(String.valueOf(count));
            }
            badgeView.show();
        } else {
            badgeView.hide();
        }
    }

    public void requestGetSaleCartGoods() {
        // 获取商品列表
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("ShopID", getShopID());
        params.put("WID", String.valueOf(getWID()));
        params.put("WarehouseId", String.valueOf(getWID()));
        params.put("UserId", userInfo.getUserId());
        params.put("UserName", userInfo.getUserName());
        getService().SaleCartGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<SaleCartGetRespData>>() {
            @Override
            public void onResponse(ApiResponse<SaleCartGetRespData> result, int code, String msg) {
                if (result != null) {
                    SaleCartGetRespData resultData = result.getData();
                    if (null != resultData) {
                        List<CartGoodsDetail> goodsList = resultData.getDetails();
                        if (null != goodsList && goodsList.size() > 0) {
                            int count = 0;
                            List<BaseCartGoodsInfo> cartGoodsInfos = new ArrayList<BaseCartGoodsInfo>();
                            for (CartGoodsDetail item : goodsList) {
                                // 将正常商品的商品信息放入集合中
                                if (item.getIsGift() == 0) {
                                    count += item.getPreQty();
                                    BaseCartGoodsInfo info = new BaseCartGoodsInfo();
                                    info.setProductId(item.getProductId());
                                    info.setPreQty(item.getPreQty());
                                    cartGoodsInfos.add(info);
                                }
                            }
                            FrxsApplication.getInstance().setShopCartCount(count);
                            //二级缓存购物车商品数量
                            FrxsApplication.getInstance().saveSaleCartProducts(cartGoodsInfos);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SaleCartGetRespData>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });

    }

    public void requestGetSaleCartCount() {
        AjaxParams params = new AjaxParams();
        params.put("ShopID", shopInfo.getShopID());
        params.put("WarehouseId", String.valueOf(shopInfo.getWID()));
        params.put("UserId", getUserID());

        getService().SaleCartCount(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(ApiResponse<Integer> result, int code, String msg) {
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        if (result.getData() != null) {
                            int count = result.getData();
                            if (count > 0) {
                                FrxsApplication.getInstance().setShopCartCount(count);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void setCurrentTab(int index) {
        mTabHost.setCurrentTab(index);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int index = intent.getIntExtra("TAB", -1);
            if (index != -1) {
                setCurrentTab(index);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (applicationBroughtToBackground) {
            applicationBroughtToBackground = false;
            //LogUtils.d(">>>>>>>>>>>>>>>>>>>HomeActivity 切到前台");
            requestNeedShowShopBill();
        }

    }

    /**
     * 请求门店账单数据
     */
    private void requestOrderPay(final boolean isNeedShowShopBill) {
        AjaxParams params = new AjaxParams();
        params.put("WID", String.valueOf(getWID()));
        params.put("ShopID", String.valueOf(getShopID()));
        if (isNeedShowShopBill) {// 当开启门店账单允许合并支付功能时，上传\MergePay\字段，标识允许查询金额为负数的结算单；
            params.put("MergePay", 1);
        }
        getService().GetNoPayQuery(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<WXNoPayQuery>>() {

            @Override
            public void onResponse(ApiResponse<WXNoPayQuery> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    WXNoPayQuery wxNoPayQuery = result.getData();
                    if (null != wxNoPayQuery) {
                        double settleAmt = 0.0;
                        /**
                         * 判断是否开启门店账单
                         * 获取不同订单列表
                         */
                        if (isNeedShowShopBill) {
                            List<WXNoPayQuery.SettleListBean> settleList = wxNoPayQuery.getSettleList();
                            if (settleList != null && settleList.size() > 0) {
                                for (WXNoPayQuery.SettleListBean item : settleList) {
                                    settleAmt += item.getSettleAmt();
                                }
                                if (settleAmt > 0) {
                                    showNeedShopBillDialog(wxNoPayQuery, settleAmt, wxNoPayQuery.getShouldPayAmt(), settleList);
                                } else {
                                    getMessage();
                                }
                            } else {
                                ToastUtils.show(HomeActivity.this, "获取支付信息失败");
                                getMessage();
                            }
                        } else {
                            WXNoPayQuery.SettleInfoBean settleInfo = wxNoPayQuery.getSettleInfo();
                            if (settleInfo != null) {
                                settleAmt = settleInfo.getSettleAmt();
                                String payOrderId = settleInfo.getSettleID();
                                List<WXNoPayQuery.SettleDetailListBean> itemList = wxNoPayQuery.getSettleDetailList();
                                if (settleAmt > 0) {
                                    showNotNeedShopBillDialog(wxNoPayQuery, settleAmt, payOrderId, itemList);
                                } else {
                                    getMessage();
                                }
                            } else {
                                ToastUtils.show(HomeActivity.this, "获取支付信息失败");
                                getMessage();
                            }
                        }
                    }
                } else {
                    getMessage();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<WXNoPayQuery>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /**
     * 弹出不开启门店账单的弹窗
     *
     * @param wxNoPayQuery
     * @param settleAmt
     * @param payOrderId
     */
    private void showNotNeedShopBillDialog(WXNoPayQuery wxNoPayQuery, double settleAmt, final String payOrderId, List<WXNoPayQuery.SettleDetailListBean> itemList) {
        if (dialog == null) {
            dialog = new BottomSheetDialog(HomeActivity.this);
        }
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_bottom_pay, null);
        final RadioGroup rgPaySelector = (RadioGroup) view.findViewById(R.id.rg_pay_selector);
        TextView tvOrderAmt = (TextView) view.findViewById(R.id.tv_order_amt);
        TextView tvContent = (TextView) view.findViewById(R.id.content_tv);
        // 动态设置逾期时间
        tvContent.setText(String.format(getResources().getString(R.string.str_dialog_pay), MathUtils.integerString(wxNoPayQuery.getPayOutTime())));
        ListView activitiesLv = (ListView) view.findViewById(R.id.order_listview);
        LinearLayout dialogTel = (LinearLayout) view.findViewById(R.id.dialog_linlay_tel);
        tvOrderAmt.setText(MathUtils.twolittercountString(settleAmt));

        /**
         * 初始化并保存支付方式
         */
        initAndSavePayType(view, rgPaySelector);

        /**
         * 帐户是否冻结
         */
        int redirectType = wxNoPayQuery.getRedirectType();
        if (redirectType == 2) {// 已逾期，冻结账户
            dialogTel.setVisibility(View.VISIBLE);
            tvContent.setText(String.format(getResources().getString(R.string.str_dialog_freeze), MathUtils.integerString(wxNoPayQuery.getPayOutTime())));
            tvContent.setTextColor(getResources().getColor(R.color.black_de));
        }

        /**
         * 底部对话框点击消失交互
         */
        final TextView tvFinish = (TextView) view.findViewById(R.id.img_close_pay);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getResources().getDrawable(R.mipmap.detail_unfold);
                tvFinish.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                dialog.dismiss();
            }
        });

        /**
         * 设置订单条目
         */

        Adapter adapter = new Adapter<WXNoPayQuery.SettleDetailListBean>(HomeActivity.this, R.layout.item_order_price) {
            @Override
            protected void convert(AdapterHelper helper, WXNoPayQuery.SettleDetailListBean item) {
                TextView tvOrderName = helper.getView(R.id.tv_order_name);
                tvOrderName.setCompoundDrawables(null, null, null, null);
                helper.setText(R.id.tv_order_name, item.getBillTypeStr());// 单据类型
                helper.setText(R.id.tv_order_num, " : " + item.getBillID());// 单据编号
                helper.setText(R.id.tv_order_price, MathUtils.twolittercountString(item.getBillPayAmt()));// 单据金额
            }
        };

        /**
         * 查看微信限额说明
         */
        view.findViewById(R.id.tv_wx_explain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CommonWebViewActivity.class);
                String baseUrl = Config.getBaseUrl();
                intent.putExtra("REQUSTPOINT", baseUrl + "h5/limitWX.html");
                intent.putExtra("H5TITLE", "微信支付限额说明");
                startActivity(intent);
            }
        });

        /**
         * 查询当前订单明细并选择支付方式
         */
        view.findViewById(R.id.order_pay_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPrepayInfo(payOrderId, FrxsApplication.getInstance().getPayType());
                dialog.dismiss();
            }
        });


        adapter.replaceAll(itemList);
        activitiesLv.setAdapter(adapter);
        dialog.hide();
        dialog.setContentView(view);
        View parent = (View) view.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        behavior.setPeekHeight(DisplayUtil.getScreenHeight(HomeActivity.this) / 1);
        dialog.show();
    }

    /**
     * 弹出需要门店账单的弹窗
     *
     * @param wxNoPayQuery
     * @param settleAmt
     */
    private void showNeedShopBillDialog(WXNoPayQuery wxNoPayQuery, final double settleAmt, double ShouldPayAmt, List<WXNoPayQuery.SettleListBean> settleList) {
        if (dialog == null) {
            dialog = new BottomSheetDialog(HomeActivity.this);
        }
        final List<WXNoPayQuery.SettleListBean> payOrderList = new ArrayList<WXNoPayQuery.SettleListBean>();// 存储当前选中订单集合
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_shop_pay, null);
        final RadioGroup rgPaySelector = (RadioGroup) view.findViewById(R.id.rg_pay_selector);
        final TextView tvOrderAmt = (TextView) view.findViewById(R.id.tv_order_amt);
        final TextView tvPayBillAmt = (TextView) view.findViewById(R.id.tv_bill_amt);
        TextView tvContent = (TextView) view.findViewById(R.id.content_tv);
        final TextView orderPayTv = (TextView) view.findViewById(R.id.order_pay_tv);
        // 动态设置逾期时间
        tvContent.setText(String.format(getResources().getString(R.string.str_dialog_pay), MathUtils.integerString(wxNoPayQuery.getPayOutTime())));
        RecyclerView activitiesLv = (RecyclerView) view.findViewById(R.id.order_listview);
        activitiesLv.setLayoutManager(new LinearLayoutManager(this));
        LinearLayout dialogTel = (LinearLayout) view.findViewById(R.id.dialog_linlay_tel);
        tvOrderAmt.setText(MathUtils.twolittercountString(settleAmt));
        tvPayBillAmt.setText(String.format(getString(R.string.dialog_payamt), MathUtils.twolittercountString(ShouldPayAmt)));

        /**
         * 初始化并保存支付方式
         */
        initAndSavePayType(view, rgPaySelector);

        /**
         * 帐户是否冻结
         */
        int redirectType = wxNoPayQuery.getRedirectType();
        if (redirectType == 2) {// 已逾期，冻结账户
            dialogTel.setVisibility(View.VISIBLE);
            tvContent.setText(String.format(getResources().getString(R.string.str_dialog_freeze), MathUtils.integerString(wxNoPayQuery.getPayOutTime())));
            tvContent.setTextColor(getResources().getColor(R.color.black_de));
        }

        /**
         * 底部对话框点击消失交互
         */
        final TextView tvFinish = (TextView) view.findViewById(R.id.img_close_pay);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getResources().getDrawable(R.mipmap.detail_unfold);
                tvFinish.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                dialog.dismiss();
            }
        });

        /**
         * 设置订单条目
         */
        RecyclerAdapter<WXNoPayQuery.SettleListBean> adapter = new RecyclerAdapter<WXNoPayQuery.SettleListBean>(HomeActivity.this, R.layout.item_order_price) {
            @Override
            protected void convert(final RecyclerAdapterHelper helper, final WXNoPayQuery.SettleListBean item) {
                final TextView tvOrderName = helper.getView(R.id.tv_order_name);
                tvOrderName.setSelected(payOrderList.contains(item) ? true : false);
                helper.setText(R.id.tv_order_name, (item.getRef_BillType() == 0) ? R.string.bill_type_0 : R.string.bill_type_1);// 订单类型
                helper.setText(R.id.tv_order_num, " : " + item.getSettleID());// 单据编号
                helper.setText(R.id.tv_order_price, MathUtils.twolittercountString(item.getSettleAmt()));// 单据金额
                helper.setOnClickListener(R.id.ll_pay_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (payOrderList.contains(item)) {
                            payOrderList.remove(item);
                            tvOrderName.setSelected(false);
                        } else {
                            payOrderList.add(item);
                            tvOrderName.setSelected(true);
                        }

                        double payAmt = 0.0;
                        for (WXNoPayQuery.SettleListBean order : payOrderList) {
                            payAmt += order.getSettleAmt();
                        }
                        tvOrderAmt.setText(MathUtils.twolittercountString(payAmt));
                        orderPayTv.setSelected(payAmt <= 0 ? true : false);
                    }
                });
            }
        };

        /**
         * 查看微信限额说明
         */
        view.findViewById(R.id.tv_wx_explain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CommonWebViewActivity.class);
                String baseUrl = Config.getBaseUrl();
                intent.putExtra("REQUSTPOINT", baseUrl + "h5/limitWX.html");
                intent.putExtra("H5TITLE", "微信支付限额说明");
                startActivity(intent);
            }
        });

        /**
         * 查询当前订单明细并选择支付方式
         */
        view.findViewById(R.id.order_pay_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double payOrderAmt = 0.0;
                List<Long> CusVoucherIDList = new ArrayList<Long>();
                for (WXNoPayQuery.SettleListBean order : payOrderList) {
                    CusVoucherIDList.add(order.getCusVoucherID());
                    payOrderAmt += order.getSettleAmt();
                }
                if (payOrderAmt > 0) {
                    requestPrepayInfo(CusVoucherIDList, FrxsApplication.getInstance().getPayType());
                    dialog.dismiss();
                } else {
                    ToastUtils.show(HomeActivity.this, R.string.str_dialog_payamt);
                }
            }
        });

        payOrderList.addAll(settleList);
        adapter.replaceAll(settleList);
        activitiesLv.setAdapter(adapter);
        dialog.hide();
        dialog.setContentView(view);
        View parent = (View) view.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        behavior.setPeekHeight(DisplayUtil.getScreenHeight(HomeActivity.this) / 1);
        dialog.show();
    }

    /**
     * 请求是否需要门店对账单
     */
    public void requestNeedShowShopBill() {
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());

        getService().GetShopMergePaySwitch(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<JsonObject>>() {
            @Override
            public void onResponse(ApiResponse<JsonObject> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    JsonObject jsonData = result.getData();
                    if (jsonData != null) {
                        String switchValue = jsonData.get("SwitchValue").getAsString();
                        String vExt1 = jsonData.get("VExt1").getAsString();
                        if (switchValue.equals("1") && vExt1.equals("1")) {
                            FrxsApplication.getInstance().setShopBillState(true); // 记录当前门店支付方式
                            requestOrderPay(true);
                        } else {
                            FrxsApplication.getInstance().setShopBillState(false); // 记录当前门店支付方式
                            requestOrderPay(false);
                        }
                    } else {
                        ToastUtils.show(HomeActivity.this, "获取当前门店账单状态失败!");
                    }
                } else {
                    if (!TextUtils.isEmpty(result.getInfo())) {
                        ToastUtils.show(HomeActivity.this, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<JsonObject>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }


    /**
     * 微信支付
     * - 失败弹出提示对话框
     * - 成功刷新账单数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(DataSynEvent event) {
        if (null != event.getResp()) {
            if (0 != event.getResp().errCode) {
                final MaterialDialog materialDialog = new MaterialDialog(HomeActivity.this);
                View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_warn_wx, null);
                materialDialog.setView(view);
                view.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                    }
                });
                materialDialog.show();
            } else {
                // 支付成功跳转支付成功页面
                Intent intent = new Intent(this, PaySuccessActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 获取当前门店的未读消息
     */
    private void getMessage() {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("UserID", userInfo.getUserId());
        params.put("WarehouseId", String.valueOf(getWID()));
        params.put("WID", getWID());
        params.put("UserName", userInfo.getUserName());
        params.put("ShopID", String.valueOf(getShopID()));
        getService().GetUserUnreadMessage(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<UnreadMessag>>() {

            @Override
            public void onResponse(ApiResponse<UnreadMessag> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    UnreadMessag data = result.getData();
                    if (null != data) {
                        final Dialog messageDialog = new Dialog(HomeActivity.this, R.style.Dialog_Fullscreen);
                        View unreadMsg = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_unread_message, null);
                        ImageView ivClose = (ImageView) unreadMsg.findViewById(R.id.iv_close);
                        TextView tvMsgTitle = (TextView) unreadMsg.findViewById(R.id.tv_message_title);
                        WebView tvMsgContent = (WebView) unreadMsg.findViewById(R.id.tv_message_content);
                        TextView tvMsgMore = (TextView) unreadMsg.findViewById(R.id.tv_message_more);
                        // 设置标题内容
                        tvMsgTitle.setText(data.getTitle());
                        tvMsgContent.loadDataWithBaseURL(null, data.getMessage(), "text/html", "utf-8", null);
                        // 点击关闭按钮
                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                messageDialog.dismiss();
                            }
                        });
                        // 点击查看更多跳转消息页面
                        tvMsgMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                messageDialog.dismiss();
                                Intent intent = new Intent(HomeActivity.this, MessageListActivity.class);
                                startActivity(intent);
                            }
                        });

                        messageDialog.setContentView(unreadMsg);
                        messageDialog.show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UnreadMessag>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /**
     * 获取当前时间的毫秒值
     *
     * @param settleTime
     */
    private long getDateMS(String settleTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long millionSeconds = 0;//毫秒
        try {
            millionSeconds = sdf.parse(settleTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;
    }

    @Override
    protected void onStop() {
        super.onStop();
        applicationBroughtToBackground = SystemUtils.isApplicationBroughtToBackground(this);
/*        if (applicationBroughtToBackground) {
           LogUtils.d(">>>>>>>>>>>>>>>>>>>切到后台");
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        int shopCartCount = FrxsApplication.getInstance().getShopCartCount();
        showBadgeView(shopCartCount);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 移除粘性事件
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_tab_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        //购物车TAB处显示数量
        if (2 == index) {
            badgeView = new BadgeView(this, imageView);
            badgeView.setTextSize(10);
            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        }

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_title_right: {
                break;
            }
            default:
                break;
        }
    }

    private void showDialog() {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setMessage("是否退出应用？");
        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                        finish();
                        System.exit(0);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();// 应用程序退出对话框
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 338) { // 支付后返回主页面 更新弹窗请求
            requestNeedShowShopBill();
        } else if (requestCode == OrderPreGoodsFragment.REQ_CODE && resultCode == Activity.RESULT_OK) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment instanceof OrdersFragment) {
                OrderPreGoodsFragment orderPreGoodsFragment = ((OrdersFragment) currentFragment).getOrderPreGoodsFragment();
                if (null != orderPreGoodsFragment) {
                    orderPreGoodsFragment.refreshData();
                }
            }
        }
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}
