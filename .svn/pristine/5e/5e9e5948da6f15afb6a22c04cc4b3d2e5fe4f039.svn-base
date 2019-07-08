package com.frxs.order.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.ewu.core.utils.DateUtil;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.HomeActivity;
import com.frxs.order.OrderRemarkActivity;
import com.frxs.order.R;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.OrderPreProducts;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostPreGood;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by Chentie on 2017/5/15.
 */

public class OrderPreGoodsFragment extends MaterialStyleFragment {

    public static final int REQ_CODE = 339;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private ListView lvOrderList;

    private Adapter<OrderPreProducts.ItemsBean> preGoodAdapter;

    private int mPageIndex = 1;

    private final int mPageSize = 20;

    private EmptyView emptyView;

    private List<OrderPreProducts.ItemsBean> preGoods;// 预订商品

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_pre;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        lvOrderList = (ListView) view.findViewById(R.id.lv_order_list);
        emptyView = (EmptyView) view.findViewById(R.id.emptyview);
    }

    @Override
    protected void initEvent() {
        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                refreshData();
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, lvOrderList, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                reqOrderPreGoods();
            }
        });

    }

    public void refreshData() {
        mActivity.showProgressDialog();
        reqOrderPreGoods();
    }

    @Override
    protected void initData() {
        reqOrderPreGoods();
        preGoodAdapter = new Adapter<OrderPreProducts.ItemsBean>(mActivity, R.layout.item_order_pre_good) {
            @Override
            protected void convert(AdapterHelper helper, final OrderPreProducts.ItemsBean item) {

                // 下单时间
                helper.setText(R.id.tv_order_time, item.getCreateTime().substring(0, item.getCreateTime().length() - 3));
                // 商品名称
                helper.setText(R.id.tv_good_name, Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                // 商品编号
                helper.setText(R.id.tv_good_sku, item.getSKU());
                // 商品单位
                helper.setText(R.id.tv_good_unit, item.getSaleUnit());
                // 商品价格
                helper.setText(R.id.tv_good_price, "￥" + MathUtils.twolittercountString(item.getSalePrice()));
                // 商品数量
                helper.setText(R.id.tv_good_count, "X" + MathUtils.doubleTrans(item.getSaleQty()));
                // 订单总金额
                helper.setText(R.id.tv_order_amt, "￥" + MathUtils.twolittercountString(item.getSaleAmt()));
                // 商品积分
                //helper.setText(R.id.tv_good_point, MathUtils.twolittercountString(MathUtils.mul(item.getShopPoint(), item.getSaleQty())));
                helper.setText(R.id.tv_good_point, MathUtils.twolittercountString(item.getTotalPoint()));
                // 商品备注
                if (!TextUtils.isEmpty(item.getRemark())) {
                    helper.setVisible(R.id.tv_good_remark, View.VISIBLE);
                    helper.setText(R.id.tv_good_remark, "备注：" + item.getRemark());
                } else {
                    helper.setVisible(R.id.tv_good_remark, View.GONE);
                }
                // 商品状态  0:录单状态;1.已确认;2:备货完成;3:已转订单;8:交易取消;9:交易关闭  （预订商品情况下，只会返回0、1、2 三种状态值）
                switch (item.getStatus()){
                    case 0:
                        helper.setText(R.id.tv_order_state, "等待确认");
                        break;

                    case 1:
                        helper.setText(R.id.tv_order_state, "正在备货");
                        break;

                    case 2:
                        helper.setText(R.id.tv_order_state, "备货完成");
                        break;

                    default:
                        break;
                }

                // 录单状态可以修改预订商品的信息
                if (item.getStatus() == 0) {
                    helper.setVisible(R.id.ll_good_modified, View.VISIBLE);
                    helper.setOnClickListener(R.id.btn_good_remark, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FrxsApplication.getInstance(), OrderRemarkActivity.class);
                            boolean isCart = true;
                            intent.putExtra("CART", isCart);
                            intent.putExtra("REMARK", item.getRemark());
                            intent.putExtra("PRE_GOOD", item);
                            mActivity.startActivityForResult(intent, REQ_CODE);
                        }
                    });

                    helper.setOnClickListener(R.id.btn_good_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mActivity.isShowingProgressDialog()) {
                                final MaterialDialog dialog = new MaterialDialog(mActivity);
                                dialog.setMessage("是否删除此商品？");
                                dialog.setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        item.setEditType(2);
                                        item.setNeedDelete(true);
                                        requestDelOrderPreGoods(PostEditCart.EDIT_TYPE_DELETE, 0, item);
                                    }
                                });
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
                } else {
                    helper.setVisible(R.id.ll_good_modified, View.GONE);
                }
            }
        };
        lvOrderList.setAdapter(preGoodAdapter);
    }

    private void reqOrderPreGoods() {
        AjaxParams params = new AjaxParams();
        params.put("WarehouseId", getWID());
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);

        getService().GetSaleOrderPreProducts(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<OrderPreProducts>>() {
            @Override
            public void onResponse(ApiResponse<OrderPreProducts> result, int code, String msg) {
                refreshComplete();
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null) {
                        OrderPreProducts data = result.getData();
                        if (data.getItems() != null && data.getItems().size() > 0) {
                            preGoods = data.getItems();
                            emptyView.setVisibility(View.GONE);
                            if (mPageIndex == 1) {
                                preGoodAdapter.replaceAll(preGoods);
                            } else {
                                preGoodAdapter.addAll(preGoods);
                            }
                        } else {
                            if (1 == mPageIndex) {
                                initNoData();
                            } else {
                                ToastUtils.show(mActivity, R.string.tips_pageending);
                            }
                        }

                        boolean hasMoreItems = (preGoodAdapter.getCount() < data.getRows());
                        mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                    }
                } else {
                    initNoData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderPreProducts>> call, Throwable t) {
                super.onFailure(call, t);
                String message = t.getMessage();
                ToastUtils.show(mActivity, message);
                refreshComplete();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshData();
                    }
                });
                mActivity.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void initNoData() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_NODATA);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showProgressDialog();
                reqOrderPreGoods();
            }
        });
    }

    /**
     * 删除预订商品的信息
     */
    private void requestDelOrderPreGoods(int EditType, int count, final OrderPreProducts.ItemsBean preGood) {
        mActivity.showProgressDialog();
        PostPreGood postPreGood = new PostPreGood();
        postPreGood.setBookOrderId(preGood.getBookOrderId());
        postPreGood.setID(preGood.getID());
        postPreGood.setQty(count);
        postPreGood.setRemark(preGood.getRemark());
        List<PostPreGood> list = new ArrayList<PostPreGood>();
        list.add(postPreGood);
        PostEditCart postEditCart = new PostEditCart();
        postEditCart.setEditType(EditType);
        postEditCart.setShopID(getShopID());
        postEditCart.setUserId(getUserID());
        postEditCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        postEditCart.setWID(getWID());
        postEditCart.setItems(list);

        getService().DelSaleOrderPreProducts(postEditCart).enqueue(new SimpleCallback<ApiResponse<Object>>() {
            @Override
            public void onResponse(ApiResponse<Object> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        if (preGoodAdapter != null && preGoodAdapter.getCount() < 0) {
                            preGoodAdapter.remove(preGood);
                        }
                        // 预订商品数量为0时 显示提示页面
                        if (preGoodAdapter.getCount() <= 0){
                            initNoData();
                        }
                    } else {
                        ToastUtils.show(mActivity, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
                ToastUtils.show(mActivity, R.string.network_request_failed + t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment currentFragment = ((HomeActivity) mActivity).getCurrentFragment();
        if (currentFragment instanceof OrdersFragment) {
            if (!emptyView.isShown()) {
                mActivity.showProgressDialog();
            }
            reqOrderPreGoods();
        }
    }
}
