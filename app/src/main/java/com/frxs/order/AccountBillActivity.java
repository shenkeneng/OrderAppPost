package com.frxs.order;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.model.AccountBill;
import com.frxs.order.model.Bill;
import com.frxs.order.model.BillDetails;
import com.frxs.order.model.DataSynEvent;
import com.frxs.order.model.SaleSettle;
import com.frxs.order.model.SettleDetail;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by Endoon on 2016/4/28.
 */
public class AccountBillActivity extends MaterialStyleActivity{
    private ListView lvAccountBillList;

    private TextView tvTitle;

    private EmptyView emptyView;

    private Adapter<AccountBill> accountAdapter;

    private int mPageIndex = 1;

    private final int mPageSize = 15;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private BottomSheetDialog dialog;// 未支付弹窗

    private BillDetails bd;

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_bill;
    }

    @Override
    protected void initViews() {
        super.initViews();
        lvAccountBillList = (ListView) findViewById(R.id.lv_accountbill_list);
        tvTitle = (TextView) findViewById(R.id.title_tv);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
    }

    @Override
    protected void initData() {
        tvTitle.setText("门店账单");

        accountAdapter = new Adapter<AccountBill>(this,R.layout.item_account_bill) {
            @Override
            protected void convert(AdapterHelper helper, final AccountBill item) {
                helper.setText(R.id.tv_order_id,"结算单号:"+item.getSettleID());
                String time = item.getSettleTime();
                String settleTime = time.substring(0,time.indexOf(" "));
                helper.setText(R.id.tv_order_time,settleTime);
                helper.setText(R.id.tv_order_money, MathUtils.twolittercountString(item.getSettleAmt()));
                // 查看门店账单详情
                helper.setOnClickListener(R.id.select_order_msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(AccountBillActivity.this,AccountBillDetailsActivity.class);
                        intent.putExtra("SETTLEID", item.getSettleID());
                        startActivity(intent);
                    }
                });

                /**
                 * 根据订单状态和结款金额判断是否显示去付款按钮 或者显示已付款按钮
                 * 付款状态(LinePayStatus - 0：不显示付款状态，1：未付款，2：已付款)
                 * 付款方式(SettleType - "9"：线上支付)
                 */
                TextView payBtn = helper.getView(R.id.tv_order_pay);
                if (item.getSettleAmt() > 0 && !TextUtils.isEmpty(item.getSettleType()) && item.getSettleType().equals("9")) {
                    helper.setVisible(R.id.tv_order_pay, View.VISIBLE);
                    //根据付款状态判断显示已付款或未付款
                    if (1 == item.getLinePayStatus()) {
                        payBtn.setEnabled(true);
                        // 1未付款：设置按钮和付款状态颜色为红色
                        helper.setVisible(R.id.tv_order_account, View.VISIBLE);
                        helper.setText(R.id.tv_order_pay, R.string.status_pay);// 去付款按钮
                        helper.setText(R.id.tv_order_account, R.string.status_non_pay);// 未付款状态
                        helper.setTextColorRes(R.id.tv_order_pay, R.color.red);
                        helper.setTextColorRes(R.id.tv_order_account, R.color.red);
                    } else if (2 == item.getLinePayStatus()) {
                        payBtn.setEnabled(false);
                        // 2已付款：设置按钮和付款状态颜色为黑色
                        helper.setText(R.id.tv_order_pay, R.string.status_paid);// 已付款按钮
                        helper.setVisible(R.id.tv_order_account, View.VISIBLE);
                        helper.setText(R.id.tv_order_account, R.string.status_paid);// 已付款状态
                        helper.setTextColorRes(R.id.tv_order_pay, R.color.gray);
                        helper.setTextColorRes(R.id.tv_order_account, R.color.darkgray);
                    } else {
                        // 隐藏按钮和付款状态
                        helper.setVisible(R.id.tv_order_pay, View.INVISIBLE);
                        helper.setVisible(R.id.tv_order_account, View.GONE);
                    }
                } else {
                    // 隐藏按钮和付款状态
                    helper.setVisible(R.id.tv_order_pay, View.INVISIBLE);
                    helper.setVisible(R.id.tv_order_account, View.GONE);
                }

                /**
                 * 点击获取订单明细并选择支付方式
                 */
                helper.setOnClickListener(R.id.tv_order_pay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqBillDatails(item.getSettleID());
                    }
                });
            }
        };
        lvAccountBillList.setAdapter(accountAdapter);
        reqSaleSettleList();
    }

    @Override
    protected void initEvent() {
        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                reqSaleSettleList();
            }
        });

        /*lvAccountBillList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(AccountBillActivity.this,AccountBillDetailsActivity.class);
                intent.putExtra("SETTLEID",accountAdapter.get(position).getSettleID());
                startActivity(intent);
            }
        });*/

        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                reqSaleSettleList();
            }
        });
    }

    /**
     * 微信支付
     * - 失败弹出提示对话框
     * - 成功刷新账单数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(DataSynEvent event) {
        if (null != event.getResp()) {
            if (0 != event.getResp().errCode) {
                final MaterialDialog materialDialog = new MaterialDialog(AccountBillActivity.this);
                View view = LayoutInflater.from(AccountBillActivity.this).inflate(R.layout.dialog_warn_wx, null);
                materialDialog.setView(view);
                view.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                    }
                });
                materialDialog.show();
            }else{
                // 支付成功跳转支付成功页面
                Intent intent = new Intent(this, PaySuccessActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 请求账单数据
     */
    private void reqSaleSettleList(){
        showProgressDialog();
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();
        AjaxParams params = new AjaxParams();
        params.put("UserID",FrxsApplication.getInstance().getUserInfo().getUserId()+"");
        params.put("WID", String.valueOf(info.getWID()));
        params.put("ShopID",String.valueOf(info.getShopID()));
        params.put("PageIndex",mPageIndex);
        params.put("PageSize",mPageSize);
        getService().SaleSettleGetList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Bill>>() {
            @Override
            public void onResponse(ApiResponse<Bill> result, int code, String msg) {
                refreshComplete();
                dismissProgressDialog();
                if (result != null)
                {
                    if (result.getFlag().equals("0"))
                    {
                        Bill bill = result.getData();
                        if (bill != null) {
                            List<AccountBill> ab = bill.getItemList();
                            if (ab != null && ab.size() > 0) {
                                emptyView.setVisibility(View.GONE);

                                if (mPageIndex == 1) {
                                    accountAdapter.replaceAll(ab);
                                } else {
                                    accountAdapter.addAll(ab);
                                }

                                accountAdapter.notifyDataSetChanged();

                                boolean hasMoreItems = (accountAdapter.getCount() < bill.getTotalRecords());
                                mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                            }else
                            {
                                emptyView.setNoDataView(R.mipmap.icon_noservice,"亲，没有账单哦~");
                            }
                        }
                    }else
                    {
                        ToastUtils.show(AccountBillActivity.this,result.getInfo());
                        emptyView.setNoDataView(R.mipmap.icon_noservice,"亲，没有账单哦~");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Bill>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                dismissProgressDialog();
                emptyView.setNoDataView(R.mipmap.icon_noservice,"亲，没有账单哦~");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
     * 查询当前订单明细并选择支付方式
     * @param mSettleId
     */
    private void reqBillDatails(final String mSettleId) {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("UserID", FrxsApplication.getInstance().getUserInfo().getUserId());
        params.put("WID",FrxsApplication.getInstance().getmCurrentShopInfo().getWID());
        params.put("ShopID",FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
        params.put("SettleID",mSettleId);
        getService().SaleSettleGetListActionGetModel(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<BillDetails>>() {
            @Override
            public void onResponse(ApiResponse<BillDetails> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    bd = result.getData();
                    if (bd != null) {
                        if (dialog == null) {
                            dialog = new BottomSheetDialog(AccountBillActivity.this);
                        }
                        View view = LayoutInflater.from(AccountBillActivity.this).inflate(R.layout.dialog_bottom_pay, null);
                        final RadioGroup rgPaySelector = (RadioGroup) view.findViewById(R.id.rg_pay_selector);
                        TextView tvOrderAmt = (TextView) view.findViewById(R.id.tv_order_amt);
                        ListView activitiesLv = (ListView) view.findViewById(R.id.order_listview);
                        view.findViewById(R.id.content_tv).setVisibility(View.GONE);
                        view.findViewById(R.id.dialog_linlay_tel).setVisibility(View.GONE);

                        /**
                         * 初始化并保存支付方式
                         */
                        initAndSavePayType(view, rgPaySelector);

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
                        Adapter adapter =  new Adapter<SettleDetail>(AccountBillActivity.this, R.layout.item_order_price) {
                            @Override
                            protected void convert(AdapterHelper helper, SettleDetail item) {
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
                                Intent intent = new Intent(AccountBillActivity.this, CommonWebViewActivity.class);
                                String baseUrl = Config.getBaseUrl();
                                intent.putExtra("REQUSTPOINT", baseUrl + "h5/limitWX.html");
                                intent.putExtra("H5TITLE", "微信支付限额说明");
                                startActivity(intent);
                            }
                        });

                        /**
                         * 去付款前先获取是否哪种支付方式
                         */
                        view.findViewById(R.id.order_pay_tv).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestPrepayInfo(mSettleId, FrxsApplication.getInstance().getPayType());
                                dialog.dismiss();
                            }
                        });

                        SaleSettle currentSettle = bd.getSaleSettle();
                        if (currentSettle != null) {
                            tvOrderAmt.setText(MathUtils.twolittercountString(currentSettle.getSettleAmt()));
                        }

                        List<SettleDetail> details = bd.getSaleSettleDetailList();
                        if (details != null && details.size() > 0) {
                            adapter.replaceAll(details);
                        }

                        activitiesLv.setAdapter(adapter);
                        dialog.hide();
                        dialog.setContentView(view);
                        View parent = (View) view.getParent();
                        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
                        behavior.setPeekHeight(DisplayUtil.getScreenHeight(AccountBillActivity.this) / 1);
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BillDetails>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 338){ // 支付后返回门店账单页面 刷新数据
            reqSaleSettleList();
        }
    }
}
