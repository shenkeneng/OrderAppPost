package com.frxs.order;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.SharedPreferencesHelper;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.frxs.order.comms.Config;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.SaleBackCart;
import com.frxs.order.model.WAdvertisementGetListModelRespData;
import com.frxs.order.model.WProductExt;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/15.
 * 退货橱窗搜索页面
 */

public class BackSearchActivity extends FrxsActivity {

    private ListView goodsResultsLv;//商品列表

    private Adapter<WProductExt> quickAdapter;

    private View llSearchHistory;

    private ListView searchHistLv;// 搜索列表

    private ArrayAdapter<String> mSearchHistoryAdapter;

    private Button clearHistBtn;

    private EditText searchContentEt;

    private TextView rightBtn;

    private String keyword;

    private EmptyView emptyView;

    private ImageView imgClear;

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_back_search;
    }

    @Override
    protected void initViews() {
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        llSearchHistory = findViewById(R.id.search_history_layout);
        searchHistLv = (ListView) findViewById(R.id.search_hist_lv);
        clearHistBtn = (Button) findViewById(R.id.clear_history_btn);
        goodsResultsLv = (ListView) findViewById(R.id.goods_list_view);
        imgClear = (ImageView) findViewById(R.id.search_delete);
        rightBtn = (TextView) findViewById(R.id.right_btn);
        rightBtn.setText(R.string.search_action);
        rightBtn.setBackgroundResource(R.drawable.shape_button_write);
        searchContentEt = (EditText) findViewById(R.id.title_search);
        backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) getIntent().getSerializableExtra("GOODS");
    }

    @Override
    protected void initEvent() {
        clearHistBtn.setOnClickListener(this);
        findViewById(R.id.left_layout).setOnClickListener(this);
        findViewById(R.id.right_layout).setOnClickListener(this);

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
                //设置退货单位积分 没有积分时不显示积分字段
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
                        Intent intent = new Intent(BackSearchActivity.this, BackGoodsInfoActivity.class);
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
        goodsResultsLv.setAdapter(quickAdapter);

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
     *
     * @param keyword
     */
    private void requestSearchGoods(String keyword) {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("ShopID", getShopID());
        params.put("WID", getWID());
        params.put("AdvertisementPosition", 4);//查询退货橱窗数据
        params.put("SearchKey", keyword);

        getService().WAdvertisementGetListModel(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<List<WAdvertisementGetListModelRespData>>>() {
            @Override
            public void onResponse(ApiResponse<List<WAdvertisementGetListModelRespData>> result, int code, String msg) {
                dismissProgressDialog();
                List<WAdvertisementGetListModelRespData> resultData = result.getData();
                if (null != resultData && resultData.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    List<WProductExt>  wproductslist = resultData.get(0).getWproductslist();
                    if (wproductslist != null && wproductslist.size() > 0) {
                        quickAdapter.replaceAll(wproductslist);
                        emptyView.setVisibility(View.GONE);
                    } else {
                        initEmptyView(EmptyView.MODE_NODATA);
                    }
                } else {
                    initEmptyView(EmptyView.MODE_NODATA);
                }
                goodsResultsLv.setVisibility(View.VISIBLE);
                llSearchHistory.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<WAdvertisementGetListModelRespData>>> call, Throwable t) {
                super.onFailure(call, t);
                initEmptyView(EmptyView.MODE_ERROR);
                dismissProgressDialog();
            }
        });
    }

    private void initEmptyView(int mode) {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(mode);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackSearchActivity.this.showProgressDialog();
                requestSearchGoods(keyword);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        quickAdapter.notifyDataSetChanged();
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
            emptyView.setVisibility(View.GONE);
        } else {
            goodsResultsLv.setVisibility(View.VISIBLE);
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
