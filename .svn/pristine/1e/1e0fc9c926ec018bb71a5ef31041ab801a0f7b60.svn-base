package com.frxs.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.BadgeView;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.ProductWProductsGetToB2BRespData;
import com.frxs.order.model.SaleBackCart;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.WProductExt;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.frxs.order.widget.CountEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import java.util.HashMap;
import java.util.List;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * 商品列表 by ewu on 2016/5/5.
 */
public class ProductListActivity extends MaterialStyleActivity {

    private EmptyView emptyView;

    private ListView productListLv;

    private Adapter<WProductExt> quickAdapter;

    private TextView rightBtn;

    private ImageView leftBtn;

    private View shopCartBtn;

    private int mPageIndex = 1;

    private final int mPageSize = 30;

    private String strSearch = "";

    private int categoryId1 = -1;

    private int categoryId2 = -1;

    private int categoryId3 = -1;

    private BadgeView badgeView;

    private ImageView cartIv;

    private int shopCartCount = 0;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private String from;

    private SaleBackCart saleBackOrder;

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_list;
    }

    @Override
    protected void initViews() {
        super.initViews();
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        productListLv = (ListView) findViewById(R.id.goods_list_view);
        leftBtn = (ImageView) findViewById(R.id.left_btn);
        leftBtn.setImageResource(R.drawable.selector_back);
        rightBtn = (TextView) findViewById(R.id.right_btn);
        rightBtn.setBackgroundResource(R.mipmap.icon_scan);
        shopCartBtn = findViewById(R.id.good_cartrl);
        cartIv = (ImageView) findViewById(R.id.good_cart_iv);
        badgeView = new BadgeView(this, cartIv);
        badgeView.setTextSize(8);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            strSearch = getIntent().getStringExtra("SEARCH");//查询关键字
            from = getIntent().getStringExtra("FROM");
            backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) getIntent().getSerializableExtra("GOODS");
            categoryId1 = getIntent().getIntExtra("CategoryId1", -1);
            categoryId2 = getIntent().getIntExtra("CategoryId2", -1);
            categoryId3 = getIntent().getIntExtra("CategoryId3", -1);
        }
        if (isSalesReturn()){
            findViewById(R.id.bottom).setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.title_search).setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        shopCartBtn.setOnClickListener(this);

        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                showProgressDialog();
                requestProductList(strSearch);
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, productListLv, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                requestProductList(strSearch);
            }
        });

        if (!isSalesReturn()) {
            quickAdapter = new Adapter<WProductExt>(this, R.layout.view_product_item) {
                @Override
                protected void convert(AdapterHelper helper, final WProductExt item) {
                    //商品冻结业务处理
                    final TextView tvGoodsBuy = helper.getView(R.id.order_btn);
                    if (item.getWStatus() == 3) {
                        tvGoodsBuy.setEnabled(false);
                        tvGoodsBuy.setBackgroundResource(R.mipmap.add_disable_btn);
                        helper.setVisible(R.id.sold_out_img, View.VISIBLE);
                    } else {
                        tvGoodsBuy.setEnabled(true);
                        tvGoodsBuy.setBackgroundResource(R.mipmap.add_cart_circle_btn);
                        helper.setVisible(R.id.sold_out_img, View.GONE);
                    }

                    double count = FrxsApplication.getInstance().getSaleCartProductCount(item.getProductId());
                    if (count > 0) {
                        tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                    } else {
                        tvGoodsBuy.setText("");
                    }

                    //促销商品业务处理（0：非促销商品；1：促销商品）
                    if (item.getIsGift() == 1 || item.getProPoints() > 0) {
                        helper.setVisible(R.id.tv_good_discount, View.VISIBLE);
                    } else {
                        helper.setVisible(R.id.tv_good_discount, View.GONE);
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
                    helper.setText(R.id.tv_goods_describe, item.getProductName());
                    helper.setImageUrl(R.id.img_goods, item.getImageUrl200x200());
                    //商品价格
                    double deliveryPrice = item.getBigSalePrice() * (1 + item.getShopAddPerc());
                    helper.setText(R.id.tv_goods_price, "￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getBigUnit());
                    //包装数
                    helper.setText(R.id.tv_unit_qty, "1x" + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getBigPackingQty())) + item.getUnit());

                    if (item.getBigShopPoint() > 0) {
                        helper.setVisible(R.id.tv_goods_integral, View.VISIBLE);
                        helper.setText(R.id.tv_goods_integral, String.format(getResources().getString(R.string.points), item.getBigShopPoint()));
                    } else {
                        helper.setVisible(R.id.tv_goods_integral, View.GONE);
                    }

                    final CountEditText countEditText = helper.getView(R.id.count_edit_text);
                    countEditText.setCount(1);

                    tvGoodsBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reqBuyGoods(item, countEditText.getCount(), tvGoodsBuy);
                        }
                    });
                    /**
                     * 进入商品详情
                     */
                    helper.setOnClickListener(R.id.ll_product, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                            intent.putExtra("product", item);
                            ProductListActivity.this.startActivity(intent);
                        }
                    });
                }
            };
        } else {
            quickAdapter = new Adapter<WProductExt>(this, R.layout.item_search_goods) {
                @Override
                protected void convert(AdapterHelper helper, final WProductExt item) {
                    TextView tv = helper.getView(R.id.tv_goods_add);
                    tv.setSelected(false);
                    // 商品名称
                    helper.setText(R.id.tv_goods_describe, item.getProductName());
                    // 编码
                    helper.setText(R.id.tv_goods_sku, "编码：" + item.getSKU());
                    // 条码
                    helper.setText(R.id.tv_bar_code, "条码：" + item.getBarCode().split(",")[0]);
                    // 价格
                    double deliveryPrice = item.getBigSalePrice() * (1 + item.getShopAddPerc());
                    helper.setText(R.id.tv_goods_price, "配送价：￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getUnit());
                    //设置退货积分 没有积分时不显示积分字段
                    if (item.getShopPoint() > 0) {
                        helper.setVisible(R.id.tv_back_point, View.VISIBLE);
                        helper.setText(R.id.tv_back_point, "退货积分：-" + MathUtils.twolittercountString(item.getShopPoint()) + "/" + item.getUnit());
                    } else {
                        helper.setVisible(R.id.tv_back_point, View.INVISIBLE);
                    }
                    /**
                     * 退货商品列表Item点击事件
                     */
                    helper.setOnClickListener(R.id.ll_product, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ProductListActivity.this, BackGoodsInfoActivity.class);
                            intent.putExtra("product", item);
                            intent.putExtra("GOODS", saleBackOrder);
                            startActivity(intent);
                            finish();
                        }
                    });

                    if (backMap != null) {
                        if (backMap.get(item.getProductId()) != null) {
                            tv.setSelected(true);
                        }
                    }
                }
            };
        }
        productListLv.setAdapter(quickAdapter);

    }

    @Override
    protected void initData() {
        from = getIntent().getStringExtra("FROM");
        if (!isSalesReturn()) {
            initBadgeView();
        }
        requestProductList(strSearch);
    }

    private void initBadgeView() {
        shopCartCount = FrxsApplication.getInstance().getShopCartCount();
        showBadgeView(shopCartCount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        quickAdapter.notifyDataSetChanged();
        initBadgeView();
    }

    /**
     * 请求商品列表
     * @param keyword
     */
    private void requestProductList(String keyword) {
        AjaxParams params = new AjaxParams();

        params.put("UserID", getUserID());
        params.put("WID", getWID());
        if (-1 != categoryId1) {
            params.put("CategoryId1", categoryId1);
            if (-1 != categoryId2) {
                params.put("CategoryId2", categoryId2);
                if (-1 != categoryId3) {
                    params.put("CategoryId3", categoryId3);
                }
            }
        }
        params.put("Search", keyword);
        params.put("ShopID", getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);
        if (isSalesReturn()) {
            params.put("isSaleBack", 1);
        }

        getService().ProductWProductsGetToB2B(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ProductWProductsGetToB2BRespData>>() {
            @Override
            public void onResponse(ApiResponse<ProductWProductsGetToB2BRespData> result, int code, String msg) {
                refreshComplete();
                dismissProgressDialog();
                ProductWProductsGetToB2BRespData respData = result.getData();
                if (null != respData) {
                    List<WProductExt> goodsList = respData.getItemList();
                    if (null != goodsList && goodsList.size() > 0) {
                        if (mPageIndex == 1) {
                            quickAdapter.replaceAll(goodsList);
                        } else {
                            quickAdapter.addAll(goodsList);
                        }

                        emptyView.setVisibility(View.GONE);

                        boolean hasMoreItems = (quickAdapter.getCount() < respData.getTotalRecords());
                        mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                    } else {
                        if (mPageIndex == 1) {
                            initEmptyView(EmptyView.MODE_NODATA);
                        } else {
                            ToastUtils.show(ProductListActivity.this, R.string.tips_pageending);
                        }
                    }
                } else {
                    if (mPageIndex == 1) {
                        initEmptyView(EmptyView.MODE_NODATA);
                    } else {
                        ToastUtils.show(ProductListActivity.this, R.string.tips_pageending);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductWProductsGetToB2BRespData>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();

                dismissProgressDialog();

                initEmptyView(EmptyView.MODE_ERROR);
            }
        });
    }

    private void initEmptyView(int mode) {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(mode);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductListActivity.this.showProgressDialog();
                requestProductList(strSearch);
            }
        });
    }

    private void reqBuyGoods(final WProductExt ext, final int addCount, final TextView tvGoodsBuy) {
        showProgressDialog();
        // 处理单次购买商品数据
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();

        PostInfo postInfo = new PostInfo();
        postInfo.setProductID(ext.getProductId());
        postInfo.setPreQty(addCount);
        postInfo.setWID(info.getWID());
        postInfo.setWarehouseId(info.getWID());

        PostEditCart editCart = new PostEditCart();
        editCart.setEditType(0);
        editCart.setShopID(info.getShopID());
        editCart.setUserId(FrxsApplication.getInstance().getUserInfo().getUserId());
        editCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        editCart.setWarehouseId(info.getWID());
        editCart.setCart(postInfo);

        getService().SaleCartEditSingle(editCart).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        ToastUtils.show(ProductListActivity.this, "加入购物车成功");

                        FrxsApplication.getInstance().addShopCartCount(addCount);
                        FrxsApplication.getInstance().updateSaleCartProduct(ext.getProductId(), addCount);
                        double count = FrxsApplication.getInstance().getSaleCartProductCount(ext.getProductId());
                        //容错处理
                        if (count <= 0) {
                            count = addCount;
                        }
                        tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                        shopCartCount += addCount;
                        showBadgeView(shopCartCount);
//                        quickAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(ProductListActivity.this, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(ProductListActivity.this, R.string.network_request_failed);//网络请求失败，请重试
            }
        });
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.left_btn: {
                finish();
                break;
            }
            case R.id.right_btn: {
                Intent intent = new Intent(this, CaptureActivity.class);
                if (isSalesReturn()) { //退货商品 传递退货标识
                    intent.putExtra("FROM", "sales_return");
                    intent.putExtra("GOODS", saleBackOrder);
                }
                hasCameraPermissions(intent, true);
                //startActivity(intent);
                //finish();
                break;
            }
            case R.id.good_cartrl: {
                Intent intent = new Intent(this, StoreCartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.title_search: {
                Intent intent = new Intent(this, ProductSearchActivity.class);
                if (isSalesReturn()) {
                    intent.putExtra("FROM", "sales_return");
                    intent.putExtra("GOODS", saleBackOrder);
                }
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    private boolean isSalesReturn() {
        boolean isSalesReturn = !TextUtils.isEmpty(from) && from.equals("sales_return");
        return isSalesReturn;
    }
}
