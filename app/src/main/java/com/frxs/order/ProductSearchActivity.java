package com.frxs.order;

import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.SharedPreferencesHelper;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.BadgeView;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.comms.GlobelDefines;
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
 * Created by ewu on 2016/5/4.
 */
public class ProductSearchActivity extends MaterialStyleActivity {

    private ListView goodsResultsLv;

    private Adapter<WProductExt> quickAdapter;

    private View llSearchHistory;

    private ListView searchHistLv;

    private ArrayAdapter<String> mSearchHistoryAdapter;

    private Button clearHistBtn;

    private EditText searchContentEt;

    private TextView rightBtn;

    private int mPageIndex = 1;

    private final int mPageSize = 30;

    private String keyword;

    private int shopCartCount = 0;

    private BadgeView badgeView;

    private ImageView cartIv;

    private FrameLayout flBottomCart;

    private View shopCartBtn;

    private EmptyView emptyView;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private ImageView imgClear;

    private String from;

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    protected void initViews() {
        super.initViews();
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        llSearchHistory = findViewById(R.id.search_history_layout);
        searchHistLv = (ListView) findViewById(R.id.search_hist_lv);
        clearHistBtn = (Button) findViewById(R.id.clear_history_btn);
        goodsResultsLv = (ListView) findViewById(R.id.goods_list_view);
        flBottomCart = (FrameLayout) findViewById(R.id.bottom);
        imgClear = (ImageView) findViewById(R.id.search_delete);
        rightBtn = (TextView) findViewById(R.id.right_btn);
        rightBtn.setText(R.string.search_action);
        rightBtn.setBackgroundResource(R.drawable.shape_button_write);
        searchContentEt = (EditText) findViewById(R.id.title_search);
        shopCartBtn = findViewById(R.id.good_cartrl);
        cartIv = (ImageView) findViewById(R.id.good_cart_iv);
        badgeView = new BadgeView(this, cartIv);
        badgeView.setTextSize(8);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        from = getIntent().getStringExtra("FROM");
        backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) getIntent().getSerializableExtra("GOODS");
        flBottomCart.setVisibility(isSalesReturn() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initEvent() {
        clearHistBtn.setOnClickListener(this);
        findViewById(R.id.left_layout).setOnClickListener(this);
        findViewById(R.id.right_layout).setOnClickListener(this);
        shopCartBtn.setOnClickListener(this);

        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                showProgressDialog();
                requestSearchGoods(keyword);
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, goodsResultsLv, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                requestSearchGoods(keyword);
            }
        });

        searchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        searchContentEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchContentEt.getText().toString().trim().length() > 0)// 当输入内容不为空
                {
                    imgClear.setVisibility(View.VISIBLE);
                    // mSearchTv.setText("");
                } else { // 当输入内容为空
                    imgClear.setVisibility(View.GONE);
                }
            }
        });

        imgClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchContentEt.setText("");
            }
        });
    }

    @Override
    protected void initData() {
        initSearchHistory();
        if (!isSalesReturn()) {// 搜索所有商品
            shopCartCount = FrxsApplication.getInstance().getShopCartCount();
            showBadgeView(shopCartCount);
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
                    double deliveryPrice = item.getBigSalePrice() * (1 + item.getShopAddPerc());
                    helper.setText(R.id.tv_goods_price, "￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getBigUnit());
                    //商品名称（0：有库存；1：无库存）
                    if (item.getIsNoStock() == 0) {
                        helper.setText(R.id.tv_goods_describe, item.getProductName());
                    } else if (item.getIsNoStock() == 1) {
                        helper.setText(R.id.tv_goods_describe, Html.fromHtml(getResources().getString(R.string.pre_good_tag) + item.getProductName()));
                    }
                    if (!TextUtils.isEmpty(item.getImageUrl200x200())) {
                        helper.setImageUrl(R.id.img_goods, item.getImageUrl200x200());
                    } else {
                        helper.setImageResource(R.id.img_goods, R.mipmap.showcase_product_default);
                    }
//                helper.setText(R.id.tv_platform_rate, String.valueOf(MathUtils.mul(item.getShopAddPerc(), 100)) + "%");
                    helper.setText(R.id.tv_unit_qty, "1x" + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(item.getBigPackingQty())) + item.getUnit());

                    if (item.getBigShopPoint() > 0) {
                        helper.setVisible(R.id.tv_goods_integral, View.VISIBLE);
                        helper.setText(R.id.tv_goods_integral, String.format(getResources().getString(R.string.points), item.getBigShopPoint()));
                    } else {
                        helper.setVisible(R.id.tv_goods_integral, View.GONE);
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

                    final CountEditText cetCount = helper.getView(R.id.count_edit_text);
                    cetCount.setCount(1);//设置初始化数量

                    tvGoodsBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reqBuyGoods(item, cetCount.getCount(), tvGoodsBuy);
                        }
                    });
                    /**
                     * 进入商品详情
                     */
                    helper.setOnClickListener(R.id.ll_product, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ProductSearchActivity.this, ProductDetailActivity.class);
                            intent.putExtra("product", item);
                            ProductSearchActivity.this.startActivity(intent);
                        }
                    });
                }
            };
        } else { // 搜索可退货商品
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
                    helper.setText(R.id.tv_bar_code, "条码：" +item.getBarCode().split(",")[0]);
                    // 价格
                    double deliveryPrice = item.getSalePrice() * (1 + item.getShopAddPerc());
                    helper.setText(R.id.tv_goods_price,  "配送价：￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getUnit());
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
                            Intent intent = new Intent(ProductSearchActivity.this, BackGoodsInfoActivity.class);
                            intent.putExtra("product", item);
                            intent.putExtra("GOODS", backMap);
                            startActivityForResult(intent, GlobelDefines.RESULT_BACK_SKU);
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
        goodsResultsLv.setAdapter(quickAdapter);

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
                        ToastUtils.show(ProductSearchActivity.this, "加入购物车成功");
                        FrxsApplication.getInstance().addShopCartCount(addCount);
                        FrxsApplication.getInstance().updateSaleCartProduct(ext.getProductId(), addCount);
                        double count = FrxsApplication.getInstance().getSaleCartProductCount(ext.getProductId());
                        //容错处理
                        if (count <= 0) {
                            count = addCount;
                        }
                        tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                        initBadgeView();
                    } else {
                        ToastUtils.show(ProductSearchActivity.this, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(ProductSearchActivity.this, R.string.network_request_failed);//网络请求失败，请重试
            }
        });
    }

    private void doSearch() {
        if (TextUtils.isEmpty(searchContentEt.getText().toString().trim())) {
            ToastUtils.show(this, "请输入搜索关键字");
            return;
        }
        keyword = searchContentEt.getText().toString().trim();
        requestSearchGoods(keyword);
        saveSearchHistory(keyword);// 保存历史搜索
    }

    /**
     * 搜索商品
     * @param keyword
     */
    private void requestSearchGoods(String keyword) {
        showProgressDialog();

        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("Search", keyword);
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);
        if (isSalesReturn()) { // 搜索可退货商品
            params.put("isSaleBack", 1);
        }

        getService().ProductWProductsGetToB2B(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ProductWProductsGetToB2BRespData>>() {
            @Override
            public void onResponse(ApiResponse<ProductWProductsGetToB2BRespData> result, int code, String msg) {
                dismissProgressDialog();
                refreshComplete();

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
                            ToastUtils.show(ProductSearchActivity.this, R.string.tips_pageending);
                        }
                    }
                } else {
                    if (mPageIndex == 1) {
                        initEmptyView(EmptyView.MODE_NODATA);
                    } else {
                        ToastUtils.show(ProductSearchActivity.this, R.string.tips_pageending);
                    }
                }

                goodsResultsLv.setVisibility(View.VISIBLE);
                llSearchHistory.setVisibility(View.GONE);
                // 搜索退货商品隐藏购买栏
                flBottomCart.setVisibility(isSalesReturn() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductWProductsGetToB2BRespData>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                refreshComplete();
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
                ProductSearchActivity.this.showProgressDialog();
                requestSearchGoods(keyword);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        quickAdapter.notifyDataSetChanged();
        // 不是搜索退货商品 初始化购物车数量
        if (!isSalesReturn()) {
            initBadgeView();
        }
    }

    private void initBadgeView() {
        shopCartCount = FrxsApplication.getInstance().getShopCartCount();
        showBadgeView(shopCartCount);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.left_layout: {
                finish();
                break;
            }
            case R.id.right_layout: {
                doSearch();
                break;
            }
            case R.id.clear_history_btn: {
                clearSearchHistory();
                break;
            }
            case R.id.good_cartrl: {
                Intent intent = new Intent(this, StoreCartActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 清空历史搜索
     */
    private void clearSearchHistory() {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.SEARCH_PREFS_NAME);
        helper.putValue(Config.KEY_HIST, "");
        searchHistLv.setAdapter(null);
        llSearchHistory.setVisibility(View.GONE);
    }

    /**
     * 初始化历史搜索
     */
    private void initSearchHistory() {
        if (llSearchHistory.getVisibility() == View.VISIBLE) {
            goodsResultsLv.setVisibility(View.GONE);
            flBottomCart.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            goodsResultsLv.setVisibility(View.VISIBLE);
            // 搜索退货商品隐藏购买栏
            flBottomCart.setVisibility(isSalesReturn() ? View.GONE : View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }

        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.SEARCH_PREFS_NAME);
        String longHistorys = helper.getString(Config.KEY_HIST, "");
        String[] histories = longHistorys.split(",");
        mSearchHistoryAdapter = new ArrayAdapter<String>(this, R.layout.item_search_history, R.id.tv_search_history,
                histories);
        // 只保留最近的10条的记录
        if (histories.length > 10) {
            String[] newHistories = new String[10];
            System.arraycopy(histories, 0, newHistories, 0, 10);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < newHistories.length; i++) {
                sb.append(newHistories[i] + ",");
            }
            helper.putValue(Config.KEY_HIST, sb.toString());

            mSearchHistoryAdapter = new ArrayAdapter<String>(this, R.layout.item_search_history,
                    R.id.tv_search_history, newHistories);
        } else if (histories.length == 1 && TextUtils.isEmpty(histories[0])) {
            llSearchHistory.setVisibility(View.GONE);
        }

        searchHistLv.setAdapter(mSearchHistoryAdapter);
        searchHistLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                keyword = (String) mSearchHistoryAdapter.getItem(position);
                searchContentEt.setText(keyword);
                requestSearchGoods(keyword);
                saveSearchHistory(keyword);// 保存历史搜索
            }
        });
    }

    /**
     * 保存历史搜索
     */
    private void saveSearchHistory(String text) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.SEARCH_PREFS_NAME);
        String longHistorys = helper.getString(Config.KEY_HIST, "");

        if (!TextUtils.isEmpty(longHistorys) && longHistorys.contains(text + ",")) {
            longHistorys = longHistorys.replaceAll(text + ",", "");
        }

        StringBuilder sb = new StringBuilder(longHistorys);
        sb.insert(0, text + ",");
        helper.putValue(Config.KEY_HIST, sb.toString());
    }

    private boolean isSalesReturn() {
        boolean isSalesReturn = !TextUtils.isEmpty(from) && from.equals("sales_return");
        return isSalesReturn;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case GlobelDefines.RESULT_BACK_SKU:
                if (data != null){
                    backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) data.getSerializableExtra("GOODS");
                    quickAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
