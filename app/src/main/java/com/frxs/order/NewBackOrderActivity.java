package com.frxs.order;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.PostEditBackCart;
import com.frxs.order.model.SaleBackCart;
import com.frxs.order.model.SaleBackCartInfo;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.CountEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by Chentie on 2017/6/10.
 */

public class NewBackOrderActivity  extends MaterialStyleActivity {

    private ImageView scanIv;// 扫码获取退货商品

    private TextView searchTitleTv;// 点击搜索商品

    private EmptyView emptyView;// 暂无数据

    private ListView orderGoodsLv;

    private TextView orderSubmitTv;// 提交退货订单

    private List<String> list = new ArrayList<String>();

    private Adapter<SaleBackCart.SaleBackCartBean> itemAdapter;

    private TextView rightTv;

    private Dialog modifyDialog;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    public static NewBackOrderActivity instance = null;

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();

    private SaleBackCart saleBackOrder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_return;
    }

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }
    @Override
    protected void initViews() {
        super.initViews();
        instance = this;
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        rightTv = (TextView) findViewById(R.id.tv_title_right);
        rightTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rightTv.setText(R.string.back_window_name);
        rightTv.setTextSize(14);
        TextView titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText("新增退货申请单");
        scanIv = (ImageView) findViewById(R.id.im_scan);
        searchTitleTv = (TextView) findViewById(R.id.title_search);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        orderGoodsLv = (ListView) findViewById(R.id.lv_order_goods);
        orderSubmitTv = (TextView) findViewById(R.id.tv_order_submit);
    }

    @Override
    protected void initEvent() {
        scanIv.setOnClickListener(this);
        searchTitleTv.setOnClickListener(this);
        orderSubmitTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);

        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                reqSaleBackCartList();
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, orderGoodsLv, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                reqSaleBackCartList();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reqSaleBackCartList();
    }

    @Override
    protected void initData() {
        reqSaleBackCartList();
        itemAdapter = new Adapter<SaleBackCart.SaleBackCartBean>(this, R.layout.item_new_apply_goods) {
            @Override
            protected void convert(final AdapterHelper helper, final SaleBackCart.SaleBackCartBean item) {
                helper.setVisible(R.id.ll_apply_back, View.VISIBLE);
                helper.setText(R.id.tv_good_name, item.getProductName());
                helper.setText(R.id.tv_good_sku, "编码:" + item.getSKU());
                helper.setText(R.id.tv_bar_code, "条码:" + item.getBarCode().split(",")[0]);
                helper.setText(R.id.tv_delivery_price, "配送价:￥" + MathUtils.twolittercountString(item.getPrice()) + "/" + item.getUnit());
                //设置商品退货积分（单位积分 * 退货数量）
                if (item.getShopPoint() == 0) {
                    helper.setText(R.id.tv_good_point, "小计退货积分:" + MathUtils.twolittercountString(MathUtils.mul(item.getShopPoint(), item.getQty()) ));
                } else {
                    helper.setText(R.id.tv_good_point, "小计退货积分:-" + MathUtils.twolittercountString(MathUtils.mul(item.getShopPoint(), item.getQty())));
                }
                helper.setText(R.id.tv_delivery_qty, "数量:" + MathUtils.doubleTrans(MathUtils.round(item.getQty(), 2)) + item.getUnit());
                helper.setText(R.id.tv_back_reason, "退货原因:" + item.getBackReasonName());
                if (TextUtils.isEmpty(item.getBackReasonDes())) {
                    helper.setVisible(R.id.tv_describe, View.GONE);
                } else {
                    helper.setVisible(R.id.tv_describe, View.VISIBLE);
                    helper.setText(R.id.tv_describe, "问题描述:" + item.getBackReasonDes());
                }
                /**
                 * 确认修改
                 */
                helper.setOnClickListener(R.id.tv_modify_good, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (modifyDialog == null) {
                            modifyDialog = new Dialog(NewBackOrderActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                        }
                        modifyDialog.setContentView(R.layout.dialog_modify_back);// 自定义对话框
                        modifyDialog.setCanceledOnTouchOutside(true);// 对话框外点击有效
                        final CountEditText editText = (CountEditText) modifyDialog.findViewById(R.id.count_edit_text);
                        int maxCount = item.getQty() > GlobelDefines.MAX_BACK_COUNT ? (int)item.getQty() : GlobelDefines.MAX_BACK_COUNT;
                        editText.setMaxCount(maxCount);
                        editText.setCount((int) item.getQty());
                        /**
                         * 监听退货原因
                         */
                        Spinner backReasonSp = (Spinner) modifyDialog.findViewById(R.id.sp_back_reason);
                        String[] reasonArr = getResources().getStringArray(R.array.selector_back_reason);
                        for (int i = 0; i < reasonArr.length; i++) {
                            if (reasonArr[i].equals(item.getBackReasonName())) {
                                backReasonSp.setSelection(i, true);
                                break;
                            }
                        }
                        backReasonSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String[] reasonArr = getResources().getStringArray(R.array.selector_back_reason);
                                switch (position) {  //商品临期 01 商品破损 02 到货差异 03 质量问题 04 其他原因 99
                                    case 0://商品临期 01;
                                        //saleBackCartList.get(posi).setNewBackReasonName();
                                        item.setBackReasonCode(01);
                                        item.setBackReasonName(reasonArr[0]);
                                        break;

                                    case 1://商品破损 02;
                                        item.setBackReasonCode(02);
                                        item.setBackReasonName(reasonArr[1]);
                                        break;

                                    case 2://到货差异 03;
                                        item.setBackReasonCode(03);
                                        item.setBackReasonName(reasonArr[2]);
                                        break;

                                    case 3://质量问题 04；
                                        item.setBackReasonCode(04);
                                        item.setBackReasonName(reasonArr[3]);
                                        break;

                                    case 4://其他原因 99
                                        item.setBackReasonCode(99);
                                        item.setBackReasonName(reasonArr[4]);
                                        break;

                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        TextView btnCommit = (TextView) modifyDialog.findViewById(R.id.btn_commit);
                        TextView btnCancel = (TextView) modifyDialog.findViewById(R.id.btn_cancel);
                        btnCommit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item.setQty(editText.getCount());
                                updateSaleBackCart(item, 1);
                                modifyDialog.dismiss();
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                modifyDialog.dismiss();
                            }
                        });

                        modifyDialog.show();
                    }
                });

                /**
                 * 删除商品
                 */
                helper.setOnClickListener(R.id.tv_del_good, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isShowingProgressDialog()) {
                            final MaterialDialog dialog = new MaterialDialog(NewBackOrderActivity.this);
                            dialog.setMessage("是否确认删除该商品？");
                            dialog.setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            //删除该商品
                                            updateSaleBackCart(item, 2);
                                        }
                                    }
                            );
                            dialog.setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }
                });
            }
        };
        orderGoodsLv.setAdapter(itemAdapter);
    }

    /**
     * 请求新增退货申请单中商品列表
     */
    private void reqSaleBackCartList() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("ShopID", getShopID());
        params.put("WarehouseId", getWID());
        params.put("UserId", getUserID());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        params.put("WID", getWID());
        getService().GetSaleBackCartList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<SaleBackCart>>() {
            @Override
            public void onResponse(ApiResponse<SaleBackCart> result, int code, String msg) {
                refreshComplete();
                if (result.getFlag().equals("0")) {
                    saleBackOrder = result.getData();
                    if (saleBackOrder != null) {
                        if (saleBackOrder.getSaleBackCart() != null && saleBackOrder.getSaleBackCart().size() > 0) {
                            List<SaleBackCart.SaleBackCartBean> saleBackCart = saleBackOrder.getSaleBackCart();
                            backMap.clear();
                            for (SaleBackCart.SaleBackCartBean goodBack : saleBackCart) {
                                backMap.put(goodBack.getProductID(), goodBack);
                            }
                            emptyView.setVisibility(View.GONE);
                            itemAdapter.replaceAll(saleBackCart);
                        } else {
                            initNoData();
                        }
                    }
                } else {
                    saleBackOrder = null;
                    backMap.clear();
                    initNoData();
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ApiResponse<SaleBackCart>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                dismissProgressDialog();
                ToastUtils.show(NewBackOrderActivity.this, t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_scan: {// 扫描条形码
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("FROM", "sales_return");
                intent.putExtra("GOODS", backMap);
                hasCameraPermissions(intent, false);
                break;
            }

            case R.id.title_search: {// 按商品名称、条码、编码搜索商品
                Intent intent = new Intent(this, ProductSearchActivity.class);
                intent.putExtra("FROM", "sales_return");
                intent.putExtra("GOODS", backMap);
                startActivity(intent);
                break;
            }

            case R.id.tv_order_submit: {// 提交退货申请
                // 有商品的时候才能提交
                if (saleBackOrder != null && saleBackOrder.getSaleBackCart().size() > 0) {
                    Intent intent = new Intent(this, SubmitBackOrderActivity.class);
                    intent.putExtra("ORDER", saleBackOrder);
                    startActivity(intent);
                } else {
                    ToastUtils.show(this, "当前无退货商品可提交");
                }
                break;
            }

            case R.id.tv_title_right: {// 退货橱窗 如：奖瓶奖盖奖券
                Intent intent = new Intent(this, SaleBackWindowActivity.class);
                intent.putExtra("GOODS", backMap);
                startActivity(intent);
                break;
            }
        }
    }

    /**
     * 无数据
     */
    private void initNoData() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_NODATA);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求数据
                reqSaleBackCartList();
            }
        });
    }

    /**
     * 修改、删除数据
     * 0新增； 1修改； 2删除；
     */
    private void updateSaleBackCart(SaleBackCart.SaleBackCartBean product, final int editType) {
        showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        SaleBackCartInfo saleBackCartInfo = new SaleBackCartInfo();
        saleBackCartInfo.setID(product.getID());
        saleBackCartInfo.setWID(getWID());
        saleBackCartInfo.setShopID(getShopID());
        saleBackCartInfo.setProductID(product.getProductID());
        saleBackCartInfo.setQty(product.getQty());
        saleBackCartInfo.setBackReasonCode(product.getBackReasonCode());// 退货原因对应码
        saleBackCartInfo.setBackReasonName(product.getBackReasonName());// 退货名字

        PostEditBackCart postEditBackCart = new PostEditBackCart();
        postEditBackCart.setEditType(editType);
        postEditBackCart.setShopId(getShopID());
        postEditBackCart.setUserId(getUserID());
        postEditBackCart.setUserName(userInfo.getUserName());
        postEditBackCart.setWID(getWID());
        postEditBackCart.setSaleBackCart(saleBackCartInfo);

        getService().SaleBackCartEditSingle(postEditBackCart).enqueue(new SimpleCallback<ApiResponse<Objects>>() {

            @Override
            public void onResponse(ApiResponse<Objects> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    String resuleInfo = "";
                    switch (editType) {
                        case 0:// 0新增；
                            resuleInfo = "添加成功!";
                            break;

                        case 1:// 1修改；
                            resuleInfo = "修改成功!";
                            break;

                        case 2:// 2删除；
                            resuleInfo = "删除成功!";
                            break;

                        default:
                            break;

                    }
                    if (!TextUtils.isEmpty(resuleInfo)) {
                        ToastUtils.show(NewBackOrderActivity.this, resuleInfo);
                    }
                    reqSaleBackCartList();
                } else {
                    ToastUtils.show(NewBackOrderActivity.this, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(NewBackOrderActivity.this, t.getMessage());
            }
        });
    }
}
