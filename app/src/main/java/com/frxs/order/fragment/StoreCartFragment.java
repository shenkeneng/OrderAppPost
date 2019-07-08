package com.frxs.order.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewu.core.utils.CheckUtils;
import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.NoScrollGridView;
import com.frxs.order.HomeActivity;
import com.frxs.order.OrderRemarkActivity;
import com.frxs.order.OrderSubmitActivity;
import com.frxs.order.PromotionDetailActivity;
import com.frxs.order.PromotionListActivity;
import com.frxs.order.R;
import com.frxs.order.StoreCartActivity;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.BaseCartGoodsInfo;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.Promotion;
import com.frxs.order.model.SaleCartGetRespData;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.frxs.order.utils.ListToGroup;
import com.frxs.order.widget.CountEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by ewu on 2016/5/3.
 */
public class StoreCartFragment extends FrxsFragment {

    protected EmptyView emptyView;

    private EditText goodSearchEt;

    protected List<CartGoodsDetail> cartGoodsList = new ArrayList<CartGoodsDetail>();

    private List<Promotion> promotionList = new ArrayList<Promotion>();

    private List<CartGoodsDetail> goodsSearchResultList = new ArrayList<CartGoodsDetail>();

    private TextView submitBtn;

    private TextView orderSumTv;

    private TextView orderPointsTv;

    private EditText searchContentEt;

    private View rightLayout;

    private TextView leftBtn;
/*
    private boolean isModifyOrder = false; //表示是否是修改订单*/

    private LocalBroadcastManager broadcastManager;

    private BroadcastReceiver mItemViewListClickReceiver;

    private TextView tvGoodsSort;//购物车排序

    private String[] mBillTime;

    private NoScrollGridView mPopGrid;

    private LinearLayout mPopContent;

    private PopupWindow mWindow;

    private View mPopView;

    private int mCurrentType = 0;

    private MaterialDialog materialDialog;

    private String strOrderID;

    private String strRemark;

    private View promotionEnterView;

    private double miniAmt = 0.00;//起订金额

    private FrameLayout allTabFl;//全部商品

    private FrameLayout preTabFl;//预订商品

    private int selectCategory = 0;// 0:全部、 1：预订；

    private CartCategoryFragment cartCategoryFragment;// 全部商品

    private PreGoodsFragment preGoodsFragment;//　预订商品

    private Adapter<CartGoodsDetail> searchAdapter; // 搜索商品

    private ListView searchLv;// 搜索商品展示栏

    private LinearLayout cartLl;// 购物车商品栏

    private RelativeLayout amountRl;// 购物车合计栏

    private List<CartGoodsDetail> noStockGoods = new ArrayList<CartGoodsDetail>();// 预定商品分组 1:无库存商品

    private List<List<CartGoodsDetail>> cartgoryGroupList;//全部商品组

    private LinearLayout tabLl; // 商品分类TAB

    private double orderAmount = 0.0;// 系统价格
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_cart;
    }

    @Override
    protected void initViews(View view) {
        allTabFl = (FrameLayout) view.findViewById(R.id.fl_all_tab);
        preTabFl = (FrameLayout) view.findViewById(R.id.fl_pre_tab);
        tabLl = (LinearLayout) view.findViewById(R.id.ll_tab);
        cartLl = (LinearLayout) view.findViewById(R.id.ll_goods_list);
        amountRl = (RelativeLayout) view.findViewById(R.id.rl_order_amount);
        searchLv = (ListView) view.findViewById(R.id.lv_search_list);
        goodSearchEt = (EditText) view.findViewById(R.id.title_search);
        emptyView = (EmptyView) view.findViewById(R.id.emptyview);
        submitBtn = (TextView) view.findViewById(R.id.submit_order_tv);
        orderSumTv = (TextView) view.findViewById(R.id.order_sum_tv);
        orderPointsTv = (TextView) view.findViewById(R.id.order_points_tv);
        tvGoodsSort = (TextView) view.findViewById(R.id.right_btn);
        promotionEnterView = view.findViewById(R.id.promotion_enter_view);
        /**
         * 购物车右上角弹出对话框处理
         */
        mBillTime = getResources().getStringArray(R.array.select_item);
        tvGoodsSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_delete, 0);
        mPopView = LayoutInflater.from(mActivity).inflate(R.layout.pop_ordertype, null);
        mPopView.setPadding(0, 50, 0, 0);
        mPopContent = (LinearLayout) mPopView.findViewById(R.id.content);
        mPopGrid = (NoScrollGridView) mPopView.findViewById(R.id.gridView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.item_btn, R.id.btn, mBillTime);
        mPopGrid.setAdapter(adapter);
        mWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mWindow.setAnimationStyle(R.style.ZoomAnimation);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mWindow.setBackgroundDrawable(dw);
        mWindow.setOutsideTouchable(true);
        rightLayout = view.findViewById(R.id.right_layout);
        searchContentEt = (EditText) view.findViewById(R.id.title_search);
        searchContentEt.setHint(R.string.hint_search_cart);
        leftBtn = (TextView) view.findViewById(R.id.left_btn);

        //接收广播，刷新正在配送列表
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ORDER_BROADCAST");
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mItemViewListClickReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                mActivity.showProgressDialog();
                requestGetStoreCartGoodsList();//刷新购物车列表
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        broadcastManager.unregisterReceiver(mItemViewListClickReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity instanceof HomeActivity) {
            Fragment currentFragment = ((HomeActivity) mActivity).getCurrentFragment();
            // 从下单成功页面返回，刷新购物车
            if (currentFragment instanceof StoreCartFragment) {
                if (!emptyView.isShown()) {
                    mActivity.showProgressDialog();
                }
                requestGetStoreCartGoodsList();
            }
        } else if (mActivity instanceof StoreCartActivity) {
            requestGetStoreCartGoodsList();
        }
    }

    @Override
    protected void initEvent() {

        promotionEnterView.setOnClickListener(this);
        /**
         * 标题点击监听事件
         */
        tvGoodsSort.setOnClickListener(this);

        /**
         * 弹窗监听事件
         */
        mPopView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mWindow.dismiss();
            }
        });

        /**
         * 弹窗内容选择监听事件
         */
        mPopGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取某个指定position的view，并对该view进行刷新。
                mPopGrid.getChildAt(mCurrentType).findViewById(R.id.btn).setSelected(false);
                view.findViewById(R.id.btn).setSelected(true);
                /**
                 *
                 */
                switch (position) {
                    // 默认排序
                    case 0:
                        setPosition(position);
                        break;
                    // 按类目排序
                    case 1:
                        setPosition(position);
                        break;
                    //清空购物车
                    case 2:
                        setPosition(position);
                        materialDialog = new MaterialDialog(mActivity);
                        materialDialog.setMessage("是否确定清空购物车？");
                        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reqCartDelete();
                                materialDialog.dismiss();
                            }
                        });
                        materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                materialDialog.dismiss();
                            }
                        });
                        materialDialog.show();
                        break;
                    default:
                        break;
                }
                mWindow.dismiss();
            }

        });

        mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                tvGoodsSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_white_down, 0);
            }
        });

        submitBtn.setOnClickListener(this);
        rightLayout.setOnClickListener(this);
        allTabFl.setOnClickListener(this);
        preTabFl.setOnClickListener(this);

        goodSearchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!emptyView.isShown()) {
                    String keyword = s.toString();
                    if (!TextUtils.isEmpty(s.toString())) {
                        searchLv.setVisibility(View.VISIBLE);
                        cartLl.setVisibility(View.GONE);
                        amountRl.setVisibility(View.GONE);
                        goodsSearchResultList.clear();
                        for (CartGoodsDetail item : cartGoodsList) {
                            String productName = item.getProductName();
                            if (productName.contains(keyword)) {
                                goodsSearchResultList.add(item);
                            }
                        }
                        searchAdapter.replaceAll(goodsSearchResultList);
                    } else {
                        searchAdapter.replaceAll(cartGoodsList);
                        searchLv.setVisibility(View.GONE);
                        cartLl.setVisibility(View.VISIBLE);
                        amountRl.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchAdapter = new Adapter<CartGoodsDetail>(mActivity, R.layout.view_store_cart_item) {

            @Override
            protected void convert(final AdapterHelper helper, final CartGoodsDetail item) {

                double deliveryPrice = item.getSalePrice() * (1 + item.getShopAddPerc());
                helper.setText(R.id.tv_goods_price, "￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getSaleUnit());
                /**
                 * 积分为"0"处理
                 */
                if (item.getGoodsShopPoint() == 0) {
                    helper.setVisible(R.id.tv_goods_integral, View.GONE);
                } else {
                    helper.setVisible(R.id.tv_goods_integral, View.VISIBLE);
                    /**
                     * 积分计算公式 ((PromotionShopPoint!=null && PromotionShopPoint>0)?PromotionShopPoint:ShopPoint)*SalePackingQty
                     */
                    double point = (String.valueOf(item.getPromotionShopPoint()) != null && item.getPromotionShopPoint() > 0) ? item.getPromotionShopPoint() : item.getShopPoint();
                    point = point * item.getSalePackingQty();
                    helper.setText(R.id.tv_goods_integral, String.format(getResources().getString(R.string.points), point));
                }
                final CountEditText countEditText = helper.getView(R.id.count_edit_text);
                countEditText.setCount(item.getNewPreQty() > 0 ? (int) item.getNewPreQty() : (int) item.getPreQty());
                /**
                 * 修改数量 隐藏显示确定按钮
                 */
                if (countEditText.getCount() == item.getPreQty()) {
                    helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                } else {
                    helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                }
                countEditText.setEditTextClickale(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog dialog = new MaterialDialog(mActivity);
                        LayoutInflater inflater = LayoutInflater.from(mActivity);
                        final View view = inflater.inflate(R.layout.dialog_modify_num, null);
                        dialog.setContentView(view);
                        ((TextView) view.findViewById(R.id.my_title_tv)).setText("修改购买数量");
                        final EditText countEt = (EditText) view.findViewById(R.id.count_edit_et);
                        countEt.setText(String.valueOf(countEditText.getCount()));
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String strCount = countEt.getText().toString().trim();
                                int count = 1;
                                if (CheckUtils.strIsNumber(strCount)) {
                                    count = Integer.valueOf(strCount);
                                    if (count < 1) {
                                        count = 1;
                                    }
                                }
                                countEditText.setCount(count);

                                if (count == item.getPreQty()) {
                                    item.setNewPreQty(count);
                                    helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                                } else {
                                    helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                                    item.setNewPreQty(count);
                                }

                                dialog.dismiss();
                            }
                        });
                        dialog.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                countEditText.setOnCountChangeListener(new CountEditText.onCountChangeListener() {
                    @Override
                    public void onCountAdd(int count) {
                        if (!mActivity.isShowingProgressDialog()) {
                            helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                            int cartCount = countEditText.getCount();
                            if (cartCount == item.getPreQty()) {
                                item.setNewPreQty(cartCount);
                                helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                            } else {
                                item.setNewPreQty(cartCount);
                            }
                        }
                    }

                    @Override
                    public void onCountSub(int count) {
                        if (!mActivity.isShowingProgressDialog()) {
                            helper.setVisible(R.id.tv_cart_confirm, View.VISIBLE);
                            int cartCount = countEditText.getCount();
                            if (cartCount == item.getPreQty()) {
                                item.setNewPreQty(cartCount);
                                helper.setVisible(R.id.tv_cart_confirm, View.GONE);
                            } else {
                                item.setNewPreQty(cartCount);
                            }
                        }
                    }
                });
                helper.setOnClickListener(R.id.tv_cart_confirm, new View.OnClickListener() {// 确定修改商品数量 刷新数据
                    @Override
                    public void onClick(View view) {
                        int cartCount = countEditText.getCount();
                        item.setNewPreQty(cartCount);
                        item.setEditType(1);
                        requestEditSingleGoods(PostEditCart.EDIT_TYPE_EDIT, cartCount, item);
                    }
                });

                helper.setOnClickListener(R.id.delete_tv, new View.OnClickListener() {
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
                                            requestEditSingleGoods(PostEditCart.EDIT_TYPE_DELETE, 0, item);
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
                /**
                 * 购物车备注
                 */
                helper.setOnClickListener(R.id.remark_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, OrderRemarkActivity.class);
                        boolean isCart = true;
                        intent.putExtra("CART", isCart);
                        intent.putExtra("REMARK", item.getRemark());
                        intent.putExtra("CART_GOODS", item);
                        startActivityForResult(intent, GlobelDefines.REQ_CODE_STORE_CART);
                    }
                });

                /**
                 * 促销商品业务处理(0：非促销商品；1：促销商品; 2：搭售商品)
                 */
                if (0 == item.getIsGift()) {
                    helper.setVisible(R.id.edit_layout, View.VISIBLE);
                    helper.setVisible(R.id.tv_promotion_count, View.GONE);
                    helper.setVisible(R.id.tv_promotion_flag, View.GONE);
                    //商品名称（0：有库存；1：无库存）
                    if (item.getIsNoStock() == 0) {
                        helper.setText(R.id.tv_goods_describe, item.getProductName());
                    } else if (item.getIsNoStock() == 1) {
                        helper.setText(R.id.tv_goods_describe, Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                    }

                    //参加促销活动的商品显示“促”字
                    if (!TextUtils.isEmpty(item.getGiftPromotionID()) || item.getGoodsShopPoint() > 0) {
                        helper.setVisible(R.id.tv_promotion_flag, View.VISIBLE);//促标识
                        helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_promotion));
                        //商品名称（0：有库存；1：无库存）
                        if (item.getIsNoStock() == 0) {
                            //helper.setVisible(R.id.tv_goods_pre, View.GONE);
                            helper.setText(R.id.tv_goods_describe, "      " + item.getProductName());
                        } else if (item.getIsNoStock() == 1) {
                            helper.setText(R.id.tv_goods_describe, Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;" + getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                        }
                    }
                } else {
                    helper.setVisible(R.id.edit_layout, View.GONE);//商品数量编辑模块
                    helper.setVisible(R.id.tv_promotion_count, View.VISIBLE);//赠品数量
                    helper.setText(R.id.tv_promotion_count, "x" + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getPreQty())));
                    helper.setVisible(R.id.tv_promotion_flag, View.VISIBLE);//赠品标识
                    //商品名称（0：有库存；1：无库存）
                    if (item.getIsNoStock() == 0) {
                        //helper.setVisible(R.id.tv_goods_pre, View.GONE);
                        helper.setText(R.id.tv_goods_describe, "          " + item.getProductName());
                    } else if (item.getIsNoStock() == 1) {
                        helper.setText(R.id.tv_goods_describe, Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                    }

                    if (1 == item.getIsGift()) {
                        helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_gift));
                    } else if (2 == item.getIsGift()) {
                        helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_reduce));
                    }
                }

                if (TextUtils.isEmpty(item.getRemark())) {
                    helper.setVisible(R.id.remark_layout, View.GONE);
                } else {
                    helper.setVisible(R.id.remark_layout, View.VISIBLE);
                    TextView remarkTv = helper.getView(R.id.tv_goods_remark);
                    remarkTv.setText(Html.fromHtml(item.getRemark()));
                }
                //起订量业务处理
                TextView tvTipsQty = helper.getView(R.id.tv_tips_preqty);
                if (item.getMinPreQty() != null && item.getMaxaPreQty() != null) {
                    tvTipsQty.setVisibility(View.VISIBLE);
                    if (item.getMinPreQty() == 0) {
                        String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMaxaPreQty()));//最大起订量
                        tvTipsQty.setText("最大订购量：" + strMaxQty);
                    } else if (item.getMaxaPreQty() == 0) {
                        String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMinPreQty()));//最小起订量
                        tvTipsQty.setText("最小起订量：" + strMinQty);
                    } else if (item.getMinPreQty() == 0 && item.getMaxaPreQty() == 0) {
                        tvTipsQty.setVisibility(View.VISIBLE);
                    } else {
                        String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMinPreQty()));//最小起订量
                        String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMaxaPreQty()));//最大起订量
                        tvTipsQty.setText(strMinQty + "≤订购量≤" + strMaxQty);
                    }
                } else if (item.getMinPreQty() != null && item.getMaxaPreQty() == null) {
                    String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMinPreQty()));//最小起订量
                    tvTipsQty.setText("最小起订量：" + strMinQty);
                } else if (item.getMaxaPreQty() != null && item.getMinPreQty() == null) {
                    String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getMaxaPreQty()));//最大起订量
                    tvTipsQty.setText("最大订购量：" + strMaxQty);
                } else {
                    tvTipsQty.setVisibility(View.GONE);
                }
            }
        };
        searchLv.setAdapter(searchAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == mActivity.RESULT_OK && null != cartGoodsList) {
            CartGoodsDetail resultItem = (CartGoodsDetail) data.getSerializableExtra("CART_GOODS");
            if (null != resultItem) {
                for (CartGoodsDetail item : cartGoodsList) {
                    if (item.getProductId().equals(resultItem.getProductId())) {
                        item.setRemark(resultItem.getRemark());
                        break;
                    }
                }
                // 将全部商品分组
                getAllGoodsArray();
                // 更新购物车商品
                updateStoreCartGoods();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mActivity.showProgressDialog();
            requestGetStoreCartGoodsList();
        }
    }

    @Override
    protected void initData() {
        Intent intent = mActivity.getIntent();
        if (null != intent) {
            strOrderID = intent.getStringExtra("ORDERID");
            strRemark = intent.getStringExtra("REMARK");
        }

        if (mActivity instanceof HomeActivity) {
            leftBtn.setBackgroundResource(0);
            leftBtn.setText(R.string.title_cart);
        } else {
            leftBtn.setOnClickListener(this);
        }

        FragmentManager manager = getChildFragmentManager();
        Fragment allFragment = manager.findFragmentByTag("all_product");
        Fragment preFragment = manager.findFragmentByTag("pre_product");
        if (null != preFragment && preFragment instanceof PreGoodsFragment) {
            preGoodsFragment = (PreGoodsFragment) preFragment;
        }
        if (null != allFragment && allFragment instanceof CartCategoryFragment) {
            cartCategoryFragment = (CartCategoryFragment) allFragment;
        }
    }

    /**
     * 单行修改购物车商品
     */
    protected void requestEditSingleGoods(final int EditType, int count, final CartGoodsDetail goodsDetail) {
        mActivity.showProgressDialog();

        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();
        PostInfo postInfo = new PostInfo();
        postInfo.setID(goodsDetail.getID());
        postInfo.setProductID(goodsDetail.getProductId());
        postInfo.setPreQty(count);
        postInfo.setWID(info.getWID());
        postInfo.setWarehouseId(info.getWID());
        postInfo.setRemark(goodsDetail.getRemark());
        PostEditCart editCart = new PostEditCart();
        editCart.setEditType(EditType);
        editCart.setShopID(info.getShopID());
        editCart.setUserId(FrxsApplication.getInstance().getUserInfo().getUserId());
        editCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        editCart.setWarehouseId(info.getWID());
        editCart.setCart(postInfo);

        getService().SaleCartEditSingle(editCart).enqueue(new SimpleCallback<ApiResponse<List<CartGoodsDetail>>>() {
            @Override
            public void onResponse(ApiResponse<List<CartGoodsDetail>> result, int code, String msg) {
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        mActivity.dismissProgressDialog();
                        List<CartGoodsDetail> giftGoodsList = result.getData();
                        updateGiftGooodsList(giftGoodsList);
                        if (EditType == PostEditCart.EDIT_TYPE_DELETE) {
                            deleteGoodsList();
                            if (cartGoodsList.size() > 0) {
                                noStockGoods.clear();// 预定商品分组 1:无库存商品
                                for (CartGoodsDetail cartGood : cartGoodsList) {
                                    if (cartGood.getIsNoStock() == 1) {
                                        noStockGoods.add(cartGood);
                                    }
                                }
                                updateStoreCartGoods();// 更新购物车商品
                                renderViewWithData();
                            } else {
                                initEmptyCart();
                            }
                        } else {
                            goodsDetail.setPreQty(goodsDetail.getNewPreQty());
                            goodsDetail.setNewPreQty(0);
                            // 刷新列表
                            if (cartCategoryFragment != null && cartCategoryFragment.cartPagerAdapter != null) {
                                cartCategoryFragment.cartPagerAdapter.getCurrentFragment().notifyDataSetChanged();
                            }
                            if (preGoodsFragment != null) {
                                preGoodsFragment.preProductAdapter.notifyDataSetChanged();
                            }
                            searchAdapter.notifyDataSetChanged();
                            renderViewWithData();
                        }
                        showCurrentFragment();
                    } else {
                        requestGetStoreCartGoodsList();
                        ToastUtils.show(mActivity, result.getInfo());
                    }
                } else {
                    mActivity.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CartGoodsDetail>>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    /**
     * 删除购物车列表
     */
    private void reqCartDelete() {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", getShopID());
        params.put("WarehouseId", getWID());
        params.put("UserID", getUserID());
        params.put("UserName", userInfo.getUserName());
        getService().SaleCartDelete(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    mActivity.dismissProgressDialog();
                    cartGoodsList.clear();
                    initEmptyCart();
                } else {
                    mActivity.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    protected void requestGetStoreCartGoodsList() {
        goodSearchEt.setText("");

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
                mActivity.dismissProgressDialog();
                cartGoodsList.clear();
                if (null != result) {
                    SaleCartGetRespData resultData = result.getData();
                    if (null != resultData) {
                        List<CartGoodsDetail> goodsList = resultData.getDetails();
                        if (null != goodsList && goodsList.size() > 0) {
                            cartGoodsList.addAll(goodsList);
                            // 获取全部商品分组
                            getAllGoodsArray();
                            // 预定商品分组 1:无库存商品
                            noStockGoods.clear();
                            for (CartGoodsDetail cartGood : cartGoodsList) {
                                if (cartGood.getIsNoStock() == 1) {
                                    noStockGoods.add(cartGood);
                                }
                            }
                            // 购物车中有商品隐藏空白页提示
                            emptyView.setVisibility(View.GONE);
                            //根据购物车商品，收集所有购物车商品参加的活动
                            collectPromotionActivitys(cartGoodsList);
                            // 合计栏
                            renderViewWithData();
                            // 更新购物车商品
                            updateStoreCartGoods();
                            showCurrentFragment();
                        } else {
                            initEmptyCart();
                        }
                    } else {
                        initEmptyCart();
                    }
                } else {
                    initEmptyCart();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SaleCartGetRespData>> call, Throwable t) {
                super.onFailure(call, t);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setBtnVisibility(false);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.showProgressDialog();
                        requestGetStoreCartGoodsList();
                    }
                });
                mActivity.dismissProgressDialog();
            }
        });
    }

    /**
     * 获取全部商品分组
     */
    private void getAllGoodsArray() {
        cartgoryGroupList = ListToGroup.subArray(cartGoodsList);
        // 当全部商品种类大于1时，显示全部分类；小于一时不显示标签栏；
        if (cartgoryGroupList != null && cartgoryGroupList.size() > 1) {
            cartgoryGroupList.add(0, cartGoodsList);
        }
    }

    /**
     * 更新购物车商品
     */
    public void updateStoreCartGoods() {
        if (cartCategoryFragment != null && cartCategoryFragment.cartPagerAdapter != null) {
            cartCategoryFragment.updateAdvertisements(cartgoryGroupList);
            for (int i = 0; i < cartgoryGroupList.size(); i++) {
                PreGoodsFragment pref = (PreGoodsFragment) cartCategoryFragment.cartPagerAdapter.getFragment(i);
                if (pref != null && pref.preProductAdapter != null) {
                    pref.preProductAdapter.replaceAll(cartgoryGroupList.get(i));
                }
            }
        }

        if (preGoodsFragment != null) {
            preGoodsFragment.preProductAdapter.replaceAll(noStockGoods);
        }

        searchAdapter.replaceAll(cartGoodsList);
    }

    /**
     * 是否含有非赠品的正式商品
     */
    private boolean hasGoodsInCart() {
        boolean hasGoods = false;
        if (null != cartGoodsList || cartGoodsList.size() > 0) {
            for (CartGoodsDetail item : cartGoodsList) {
                if (0 == item.getIsGift()) {
                    hasGoods = true;
                    break;
                }
            }
        }

        return hasGoods;
    }

    /**
     * 是否需要删除的商品
     */
    private void deleteGoodsList() {
        if (null != cartGoodsList) {
            List<CartGoodsDetail> tempGoodsList = new ArrayList<CartGoodsDetail>();
            tempGoodsList.addAll(cartGoodsList);
            for (CartGoodsDetail item : tempGoodsList) {
                if (item.isNeedDelete()) {
                    cartGoodsList.remove(item);
                }
            }
            getAllGoodsArray();
            collectPromotionActivitys(cartGoodsList);
        }
    }

    /**
     * 更新赠品的列表
     */
    private void updateGiftGooodsList(List<CartGoodsDetail> giftGoodsList) {
        if (null != cartGoodsList) {
            List<CartGoodsDetail> tempGoodsList = new ArrayList<CartGoodsDetail>();
            tempGoodsList.addAll(cartGoodsList);
            for (CartGoodsDetail item : tempGoodsList) {
                if (1 == item.getIsGift() || 2 == item.getIsGift()) {
                    cartGoodsList.remove(item);
                }
            }

            if (null != giftGoodsList && giftGoodsList.size() > 0) {
                cartGoodsList.addAll(giftGoodsList);
            }

            getAllGoodsArray();
        }
    }

    /**
     * 促销活动ID是否已经被包含到了活动列表
     */
    private boolean promotionHasContained(String promotionId) {
        for (int i = 0; i < promotionList.size(); i++) {
            Promotion item = promotionList.get(i);
            if (item.getPromotionID().equals(promotionId)) {
                return true;
            }
        }

        return false;
    }

    private void collectPromotionActivitys(List<CartGoodsDetail> cartGoodsList) {
        promotionList.clear();

        for (CartGoodsDetail item : cartGoodsList) {
            String promotionId = item.getGiftPromotionID();
            if (0 == item.getIsGift() && !TextUtils.isEmpty(promotionId) && promotionList.size() < 6 && !promotionHasContained(promotionId)) {
                Promotion promotionItem = new Promotion();
                promotionItem.setPromotionID(item.getGiftPromotionID());
                promotionItem.setPromotionName(item.getGiftPromotionName());
                promotionList.add(promotionItem);
            }
        }

        //如果购物车商品没有参加促销活动则隐藏促销活动入口
        if (promotionList.size() <= 0) {
            promotionEnterView.setVisibility(View.GONE);
        } else {
            promotionEnterView.setVisibility(View.VISIBLE);
        }
    }

    private void renderViewWithData() {
        double orderPoints = 0;
        orderAmount = 0.0;// 系统价格
        double orderFee = 0.0;
        int count = 0;

        //保存购物车商品的数量
        List<BaseCartGoodsInfo> cartGoodsInfoList = new ArrayList<BaseCartGoodsInfo>();
        if (null != cartGoodsList && cartGoodsList.size() > 0) {
            for (CartGoodsDetail item : cartGoodsList) {
                cartGoodsInfoList.add(item);

                double point = item.getPromotionShopPoint() > 0 ? item.getPromotionShopPoint() : item.getShopPoint();
                point = point * item.getSalePackingQty();
                orderPoints += (item.getPreQty() * point);
                orderAmount += (item.getSalePrice() * item.getPreQty());
                count += item.getPreQty();
                orderFee += ((item.getSalePrice() * item.getPreQty()) * item.getShopAddPerc());// 平台费用=商品金额x商品数量x平台费率
            }
        }

        orderSumTv.setText("￥" + MathUtils.twolittercountString(MathUtils.round(orderAmount, 2) + MathUtils.round(orderFee, 2)));   // 总数
        orderPointsTv.setText(String.format("%1$.2f", orderPoints));    // 积分
        FrxsApplication.getInstance().setShopCartCount(count); // 设置购物车商品数量
        FrxsApplication.getInstance().saveSaleCartProducts(cartGoodsInfoList);
    }

    private void initEmptyCart() {
        if (cartGoodsList.size() == 0) {
            //if (quickAdapter != null) {
            //quickAdapter.clear();
            //}

            FrxsApplication.getInstance().setShopCartCount(0);
            FrxsApplication.getInstance().saveSaleCartProducts(null);

            if (isAdded()) { //防止Fragment not attached to Activity
                emptyView.setCurrentMode(-1);
                emptyView.setImageResource(R.mipmap.img_shopping);
                emptyView.setText(getString(R.string.empty_cart_msg));
                /**
                 * 购物车无商品列表(点击进入首页)
                 */
                emptyView.setBtnTextAndListener(getString(R.string.go_shopping), getResources().getDrawable(R.drawable.circle_frame_border), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //TODO jump to homeFragment
                        if (mActivity instanceof HomeActivity) {
                            ((HomeActivity) mActivity).setCurrentTab(0);
                        } else {
                            Intent intent = new Intent(mActivity, HomeActivity.class);
                            intent.putExtra("TAB", 0);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    /**
     * 获取店铺信息
     */
    private void requestGetShopInfo() {
        mActivity.showProgressDialog();

        AjaxParams params = new AjaxParams();
        params.put("ShopId", getShopID());
        getService().GetShopInfo(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopInfo>>() {
            @Override
            public void onResponse(ApiResponse<ShopInfo> result, int code, String msg) {
                mActivity.dismissProgressDialog();

                if (result.getFlag().equals("0")) {
                    ShopInfo shopInfo = result.getData();
                    if (null != shopInfo) {
                        miniAmt = shopInfo.getMininumAmt();
                        if (orderAmount < miniAmt) {
                            final MaterialDialog mDialog = new MaterialDialog(mActivity);
                            mDialog.setMessage("订单起订金额为￥" + MathUtils.twolittercountString(miniAmt) + "，未达到起订金额的订单不能提交!");
                            mDialog.setPositiveButton("继续购物", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mActivity instanceof HomeActivity) {
                                        ((HomeActivity) mActivity).setCurrentTab(1);
                                    } else {
                                        Intent intent = new Intent(mActivity, HomeActivity.class);
                                        intent.putExtra("TAB", 1);
                                        startActivity(intent);
                                    }
                                    mDialog.dismiss();
                                }
                            });
                            mDialog.setNegativeButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });
                            mDialog.show();

                        } else {
                            if (cartgoryGroupList != null && cartGoodsList.size() > 0) {
                                FrxsApplication.getInstance().setStoreCart(cartGoodsList);
                                Intent intent = new Intent(mActivity, OrderSubmitActivity.class);
                                //intent.putExtra("GOODSLIST", (Serializable) cartGoodsList);
                                startActivity(intent);
                            } else {
                                ToastUtils.show(mActivity, "购物车暂无数据，无法提交!");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShopInfo>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_layout: {
                break;
            }
            case R.id.left_btn: {
                mActivity.finish();
                break;
            }
            case R.id.submit_order_tv: {
                requestGetShopInfo();
                break;
            }
            case R.id.right_btn:
                materialDialog = new MaterialDialog(mActivity);
                materialDialog.setMessage("是否确定清空购物车？");
                materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reqCartDelete();
                        materialDialog.dismiss();
                    }
                });
                materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                    }
                });
                materialDialog.show();
                //窗口弹出选择排序功能暂时隐藏
//                mWindow.showAsDropDown(v, 0, DensityUtils.dip2px(mActivity, 1));
//                mPopContent.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_zoomin2));
//                tvGoodsSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_white_up, 0);
                break;
            case R.id.promotion_enter_view:
                //底部促销活动对话框
                final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
                View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bottom_sheet, null);
                view.findViewById(R.id.more_click_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(mActivity, PromotionListActivity.class);
                        mActivity.startActivity(intent);
                    }
                });
                ListView activitiesLv = (ListView) view.findViewById(R.id.promotion_activity_lv);
                //底部对话框点击消失交互
                final TextView tvPromotion = (TextView) view.findViewById(R.id.tv_promotion_title);
                tvPromotion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Drawable drawable = getResources().getDrawable(R.mipmap.detail_unfold);
                        tvPromotion.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                        dialog.dismiss();
                    }
                });
                Adapter adapter = new Adapter<Promotion>(mActivity, R.layout.item_sheet) {
                    @Override
                    protected void convert(AdapterHelper helper, Promotion item) {
                        helper.setText(R.id.promotion_name_tv, item.getPromotionName());
                    }
                };
                activitiesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        Promotion clickedItem = (Promotion) parent.getAdapter().getItem(position);
                        if (null != clickedItem) {
                            Intent intent = new Intent(mActivity, PromotionDetailActivity.class);
                            intent.putExtra("PromotionID", clickedItem.getPromotionID());
                            mActivity.startActivity(intent);
                        }
                    }
                });
                adapter.addAll(promotionList);
                activitiesLv.setAdapter(adapter);
                dialog.hide();
                dialog.setContentView(view);
                //修改BottomSheetDialog 默认STATE_COLLAPSED状态的高度（初始弹出的高度，设置成全屏高度使全部显示出来)
                View parent = (View) view.getParent();
                BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
                behavior.setPeekHeight(DisplayUtil.getScreenHeight(mActivity) / 1);
                dialog.show();
                break;

            case R.id.fl_all_tab:
                selectCategory = 0;
                showCurrentFragment();
                break;

            case R.id.fl_pre_tab:
                selectCategory = 1;
                showCurrentFragment();
                break;

            default:
                break;
        }
    }

    public void setPosition(int position) {
        mCurrentType = position;
    }

    /**
     * 初始化有库存商品与无库存商品的TAB与数量
     */
    private void initSelectorCategory() {
//        int goodsCount = 0;// 全部商品数量
//        int onStockCount = 0;// 预订商品数量
        if (noStockGoods.size() == 0) {// 没有预订商品不显示分类
            selectCategory = 0;
            tabLl.setVisibility(View.GONE);
        } else {
            tabLl.setVisibility(View.VISIBLE);
//            for (CartGoodsDetail good : noStockGoods) {
//                onStockCount += good.getPreQty();
//            }
//            for (CartGoodsDetail good : cartGoodsList) {
//                goodsCount += good.getPreQty();
            TextView allCountTv = (TextView) allTabFl.findViewById(R.id.tv_all_count);
            TextView preCountTv = (TextView) preTabFl.findViewById(R.id.tv_pre_count);
            allCountTv.setText(String.valueOf(cartGoodsList.size()));
            preCountTv.setText(String.valueOf(noStockGoods.size()));
            TextView allTextTv = (TextView) allTabFl.findViewById(R.id.tv_all_text);
            TextView preTextTv = (TextView) preTabFl.findViewById(R.id.tv_pre_text);
            if (selectCategory == 0) {
                allTextTv.setTextColor(getResources().getColor(R.color.red));
                preTextTv.setTextColor(getResources().getColor(R.color.frxs_black_dark));
                allTabFl.findViewById(R.id.tv_indicate_all).setVisibility(View.VISIBLE);
                preTabFl.findViewById(R.id.tv_indicate_pre).setVisibility(View.GONE);
            } else {
                allTextTv.setTextColor(getResources().getColor(R.color.frxs_black_dark));
                preTextTv.setTextColor(getResources().getColor(R.color.red));
                allTabFl.findViewById(R.id.tv_indicate_all).setVisibility(View.GONE);
                preTabFl.findViewById(R.id.tv_indicate_pre).setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * 显示当前选中的fragment
     */
    private void showCurrentFragment() {
        initSelectorCategory();
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (selectCategory == 0) {
            Bundle cartb = new Bundle();
            cartb.putSerializable("product_list", (Serializable) cartgoryGroupList);
            if (cartCategoryFragment == null) {
                cartCategoryFragment = new CartCategoryFragment();
                cartCategoryFragment.setArguments(cartb);
                transaction.add(R.id.tab_goods, cartCategoryFragment, "all_product");
                if (preGoodsFragment != null) {
                    transaction.hide(preGoodsFragment);
                }
            } else {
                transaction.show(cartCategoryFragment);
                if (preGoodsFragment != null) {
                    transaction.hide(preGoodsFragment);
                }
            }
        } else {
            Bundle preb = new Bundle();
            preb.putSerializable("product_list", (Serializable) noStockGoods);
            if (preGoodsFragment == null) {
                preGoodsFragment = new PreGoodsFragment();
                preGoodsFragment.setArguments(preb);
                transaction.add(R.id.tab_goods, preGoodsFragment, "pre_product");

                if (cartCategoryFragment != null) {
                    transaction.hide(cartCategoryFragment);
                }
            } else {
                transaction.show(preGoodsFragment);
                if (cartCategoryFragment != null) {
                    transaction.hide(cartCategoryFragment);
                }
            }
        }
        transaction.commitAllowingStateLoss();
    }

    public List<CartGoodsDetail> getCartGoodsList() {
        return cartGoodsList;
    }

    public void setCartgoryGroupList(List<List<CartGoodsDetail>> cartgoryGroupList) {
        this.cartgoryGroupList = cartgoryGroupList;
    }
}
