package com.frxs.order;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.CheckUtils;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.MaterialDialog;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.Details;
import com.frxs.order.model.OrderShop;
import com.frxs.order.model.PostOrderEditAll;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.frxs.order.utils.IpAdressUtils;
import com.frxs.order.widget.CountEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

/**
 * 修改订单界面
 * Created by Shenpei on 2016/11/29.
 */
public class ModifyOrderActivity extends FrxsActivity {


    private TextView tvTitle;

    private ListView goodsListView;

    private EmptyView emptyView;

    private TextView submitBtn;

    private TextView orderSumTv;

    private TextView orderPointsTv;

    private Adapter<CartGoodsDetail> quickAdapter;

    private List<CartGoodsDetail> cartGoodsList = new ArrayList<CartGoodsDetail>();

    private String strOrderID;

    private String strRemark;

    private String strDate;

    private double miniAmt = 0.00;//起订金额

    private ImageView rightBtn;

    private double orderAmount = 0.0;//系统价格

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orderstorecart;
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.title_tv);
        goodsListView = (ListView) findViewById(R.id.goods_list_view);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        submitBtn = (TextView) findViewById(R.id.submit_order_tv);
        orderSumTv = (TextView) findViewById(R.id.order_sum_tv);
        orderPointsTv = (TextView) findViewById(R.id.order_points_tv);
        rightBtn = (ImageView) findViewById(R.id.right_btn);
        rightBtn.setVisibility(View.VISIBLE);
        CartGoodsDetail detail = new CartGoodsDetail();

        tvTitle.setText("修改订单");
        cartGoodsList.add(0, detail);

    }

    @Override
    protected void initEvent() {
        // 修改整单备注
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrxsApplication.getInstance(), OrderRemarkActivity.class);
                intent.putExtra("REMARK", strRemark);
                startActivityForResult(intent, GlobelDefines.REQ_CODE_STORE_CART);
            }
        });

        quickAdapter = new Adapter<CartGoodsDetail>(this, R.layout.head_store_cart_item, R.layout.view_store_cart_item) {
            @Override
            protected void convert(final AdapterHelper helper, final CartGoodsDetail item) {
                int position = helper.getPosition();
                if (position == 0) {// 设置第一个条目内容
                    helper.setText(R.id.tv_order_number, (!TextUtils.isEmpty(strOrderID)) ? "编号：" + strOrderID : "编号：");
                    helper.setText(R.id.tv_order_date, (!TextUtils.isEmpty(strDate)) ? "时间：" + strDate : "时间：");
                    helper.setText(R.id.tv_order_remark, (!TextUtils.isEmpty(strRemark)) ? "备注：" + strRemark : "备注：");
                } else {
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

                    /**
                     * 修改商品数量
                     */
                    final CountEditText countEditText = helper.getView(R.id.count_edit_text);
                    countEditText.setCount((int) item.getPreQty());
                    countEditText.setEditTextClickale(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final MaterialDialog dialog = new MaterialDialog(ModifyOrderActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(ModifyOrderActivity.this);
                            final View view = inflater.inflate(R.layout.dialog_modify_num, null);
                            dialog.setContentView(view);
                            ((TextView) view.findViewById(R.id.my_title_tv)).setText("修改购买数量");
                            final EditText countEt = (EditText) view.findViewById(R.id.count_edit_et);
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

                                    if (count != item.getPreQty()) {
                                        item.setPreQty(count);
                                        quickAdapter.notifyDataSetChanged();
                                        renderViewWithData();
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
                    // 监听数量的变化
                    countEditText.setOnCountChangeListener(new CountEditText.onCountChangeListener() {
                        @Override
                        public void onCountAdd(int count) {
                            int cartCount = countEditText.getCount();
                            item.setPreQty(cartCount);
                            renderViewWithData();
                        }

                        @Override
                        public void onCountSub(int count) {
                            int cartCount = countEditText.getCount();
                            item.setPreQty(cartCount);
                            renderViewWithData();
                        }
                    });

                    /**
                     * 删除商品
                     */
                    helper.setOnClickListener(R.id.delete_tv, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isShowingProgressDialog()) {
                                final MaterialDialog dialog = new MaterialDialog(ModifyOrderActivity.this);
                                dialog.setMessage("是否删除此商品？");
                                dialog.setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                        item.setNeedDelete(true);
                                        deleteGoodsList();
                                        //如果商品全部删除，则清空购物车赠品
                                        if (!hasGoodsInCart()) {
                                            cartGoodsList.clear();
                                        }
                                        quickAdapter.replaceAll(cartGoodsList);
                                        renderViewWithData();
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

                    /**
                     * 订单备注
                     */
                    helper.setOnClickListener(R.id.remark_tv, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(FrxsApplication.getInstance(), OrderRemarkActivity.class);
                            boolean isCart = true;
                            item.setFromModifyOrder(true);
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
                        helper.setText(R.id.tv_goods_describe, item.getProductName());

                        //参加促销活动的商品显示“促”字
                        if (!TextUtils.isEmpty(item.getGiftPromotionID()) || item.getGoodsShopPoint() > 0){
                            helper.setVisible(R.id.tv_promotion_flag, View.VISIBLE);//促标识
                            helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_promotion));
                            helper.setText(R.id.tv_goods_describe, "      " + item.getProductName());
                        }
                    } else {
                        helper.setVisible(R.id.edit_layout, View.GONE);//商品数量编辑模块
                        helper.setVisible(R.id.tv_promotion_count, View.VISIBLE);//赠品数量
                        helper.setText(R.id.tv_promotion_count, "x" + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getPreQty())));
                        helper.setVisible(R.id.tv_promotion_flag, View.VISIBLE);//赠品标识
                        helper.setText(R.id.tv_goods_describe, "          " + item.getProductName());

                        if (1 == item.getIsGift()) {
                            helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_gift));
                        } else if (2 == item.getIsGift()) {
                            helper.setText(R.id.tv_promotion_flag, getString(R.string.activity_reduce));
                        }
                    }

                    /**
                     * 商品备注
                     */
                    if (TextUtils.isEmpty(item.getRemark())) {
                        helper.setVisible(R.id.remark_layout, View.GONE);
                    } else {
                        helper.setVisible(R.id.remark_layout, View.VISIBLE);
                        TextView remarkTv = helper.getView(R.id.tv_goods_remark);
                        remarkTv.setText(Html.fromHtml(item.getRemark()));
                    }
                    //移除起订量业务处理
                    TextView tvTipsQty = helper.getView(R.id.tv_tips_preqty);
                    tvTipsQty.setVisibility(View.GONE);
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }

            @Override
            public int getLayoutResId(int viewType) {
                if (viewType == 0) {
                    return R.layout.head_store_cart_item;
                } else {
                    return R.layout.view_store_cart_item;
                }
            }
        };

        /**
         * 提交修改订单
         */
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestGetShopInfo();
            }
        });

        goodsListView.setAdapter(quickAdapter);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            strOrderID = intent.getStringExtra("ORDERID");
            strRemark = intent.getStringExtra("REMARK");
            strDate = intent.getStringExtra("DATE");

            List<Details> detailsList = (List<Details>) intent.getSerializableExtra("ORDER");
            if (null != detailsList) {
                for (Details item : detailsList) {
                    CartGoodsDetail goodsDetail = new CartGoodsDetail();
                    goodsDetail.setProductId(String.valueOf(item.getProductId()));
                    goodsDetail.setProductName(item.getProductName());
                    goodsDetail.setSalePrice(item.getSalePrice());
                    goodsDetail.setShopPoint(item.getShopPoint());
                    goodsDetail.setPromotionShopPoint(item.getPromotionShopPoint());
                    goodsDetail.setSalePackingQty(item.getSalePackingQty());
                    goodsDetail.setShopAddPerc(item.getShopAddPerc());
                    goodsDetail.setOrderID(item.getOrderID());
                    goodsDetail.setSaleUnit(item.getSaleUnit());
                    goodsDetail.setID(item.getID());
                    goodsDetail.setWID(item.getWID());
                    goodsDetail.setPreQty(item.getSaleQty().intValue());
                    goodsDetail.setUserId(getUserID());
                    goodsDetail.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
                    goodsDetail.setWarehouseId(getWID());
                    goodsDetail.setRemark(item.getRemark());
                    goodsDetail.setIsGift(item.getIsGift());
                    goodsDetail.setGiftPromotionID(item.getGiftPromotionID());

                    cartGoodsList.add(goodsDetail);// 将购物车中所有的商品对象加入集合中
                }

                if (null != cartGoodsList && cartGoodsList.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    quickAdapter.addAll(cartGoodsList);
                    renderViewWithData();
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setMode(EmptyView.MODE_NODATA);
                }
            } else {
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_NODATA);
            }
        }
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
        }
    }

    private void renderViewWithData() {
        double orderPoints = 0;
        orderAmount = 0.0;//系统价格
        double orderFee = 0.0;
        int count = 0;

        if (null != cartGoodsList && cartGoodsList.size() > 0) {
            for (CartGoodsDetail item : cartGoodsList) {

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
    }

    /**
     * 判断是否达到起订金额
     */
    private void requestGetShopInfo() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", getShopID());
        getService().GetShopInfo(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopInfo>>() {
            @Override
            public void onResponse(ApiResponse<ShopInfo> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    ShopInfo shopInfo = result.getData();
                    if (null != shopInfo) {
                        miniAmt = shopInfo.getMininumAmt();
                        if (orderAmount < miniAmt) {
                            dismissProgressDialog();
                            final MaterialDialog mDialog = new MaterialDialog(ModifyOrderActivity.this);
                            mDialog.setMessage("订单起订金额为￥" + MathUtils.twolittercountString(miniAmt) + "，未达到起订金额的订单不能提交!");
                            mDialog.setNegativeButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });
                            mDialog.show();

                        } else {
                            submitModifiedOrder();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShopInfo>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

    /**
     * 提交修改的订单
     */
    private void submitModifiedOrder() {
        if (cartGoodsList != null && cartGoodsList.size() > 0) {
            OrderShop orderShop = new OrderShop();
            orderShop.setOrderId(strOrderID);
            orderShop.setOrderType(0);
            orderShop.setSubWID(getWID());
            orderShop.setWID(getWID());
            orderShop.setUserId(getUserID());
            orderShop.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
            orderShop.setWarehouseId(getWID());
            orderShop.setRemark("Remark");

            PostOrderEditAll orderEditAll = new PostOrderEditAll();
            orderEditAll.setShopId(FrxsApplication.getInstance().getmCurrentShopInfo().getShopID());
            orderEditAll.setSubWID(String.valueOf(FrxsApplication.getInstance().getmCurrentShopInfo().getWID()));
            orderEditAll.setWarehouseId(String.valueOf(FrxsApplication.getInstance().getmCurrentShopInfo().getWID()));
            orderEditAll.setbDeleteOld(true);
            orderEditAll.setUserID(getUserID());
            orderEditAll.setbFromCart(false);
            orderEditAll.setRemark(strRemark);
            orderEditAll.setOrderShop(orderShop);
            orderEditAll.setDetails(cartGoodsList);
            orderEditAll.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
            orderEditAll.setOrderId(strOrderID);
            orderEditAll.setClientIP(IpAdressUtils.getIpAdress2(this));
            orderEditAll.setClientModelType(getPhoneInfo());

            getService().OrderShopEditAll(orderEditAll).enqueue(new SimpleCallback<ApiResponse<String>>() {
                @Override
                public void onResponse(ApiResponse<String> result, int code, String msg) {
                    dismissProgressDialog();
                    if (result.getFlag().equals("0")) {
                        Intent intent = new Intent(ModifyOrderActivity.this, HomeActivity.class);
                        intent.putExtra("TAB", 3);
                        startActivity(intent);
                        ToastUtils.show(ModifyOrderActivity.this, "订单修改成功");
                        finish();
                    } else {
                        ToastUtils.show(ModifyOrderActivity.this, result.getInfo());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    super.onFailure(call, t);
                    dismissProgressDialog();
                    ToastUtils.show(ModifyOrderActivity.this, R.string.network_request_failed);//网络请求失败，请重试
                }
            });

        } else {
            ToastUtils.show(ModifyOrderActivity.this, "当前订单没有商品，不能提交");
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != cartGoodsList) {
            CartGoodsDetail resultItem = (CartGoodsDetail) data.getSerializableExtra("CART_GOODS");
            if (null != resultItem) {// 商品不为空修改商品备注
                for (CartGoodsDetail item : cartGoodsList) {
                    String itemId = item.getProductId();
                    String productId = resultItem.getProductId();
                    if (!TextUtils.isEmpty(itemId) && !TextUtils.isEmpty(productId)) {
                        if (item.getProductId().equals(resultItem.getProductId())) {
                            item.setRemark(resultItem.getRemark());
                        }
                    }
                }
            } else {// 商品为空修改订单整单备注
                String remarkOrder = data.getStringExtra("REMARK");
                strRemark = remarkOrder;
            }
            quickAdapter.notifyDataSetChanged();
        }
    }
}
