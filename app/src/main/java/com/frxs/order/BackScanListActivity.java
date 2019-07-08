package com.frxs.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.widget.EmptyView;
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
 * 退货橱窗扫码搜索页面
 */

public class BackScanListActivity extends FrxsActivity {
    private EmptyView emptyView;

    private ListView productListLv;

    private Adapter<WProductExt> quickAdapter;

    private TextView rightBtn;

    private ImageView leftBtn;

    private String strSearch = "";

    private String from;

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_back_scan;
    }

    @Override
    protected void initViews() {
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        productListLv = (ListView) findViewById(R.id.goods_list_view);
        leftBtn = (ImageView) findViewById(R.id.left_btn);
        leftBtn.setImageResource(R.drawable.selector_back);
        rightBtn = (TextView) findViewById(R.id.right_btn);
        rightBtn.setBackgroundResource(R.mipmap.icon_scan);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            strSearch = getIntent().getStringExtra("SEARCH");//查询关键字
            from = getIntent().getStringExtra("FROM");
            backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) getIntent().getSerializableExtra("GOODS");
        }
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.title_search).setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

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
                helper.setText(R.id.tv_bar_code, "条码:" + item.getBarCode().split(",")[0]);
                // 价格
                double deliveryPrice = item.getSalePrice() * (1 + item.getShopAddPerc());
                helper.setText(R.id.tv_goods_price, "配送价：￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getUnit());
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
                        Intent intent = new Intent(BackScanListActivity.this, BackGoodsInfoActivity.class);
                        intent.putExtra("product", item);
                        intent.putExtra("GOODS", backMap);
                        startActivityForResult(intent, GlobelDefines.RESULT_BACK_SKU);
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
        productListLv.setAdapter(quickAdapter);
    }

    @Override
    protected void initData() {
        requestProductList(strSearch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        quickAdapter.notifyDataSetChanged();
    }

    /**
     * 请求商品列表
     * @param keyword
     */
    private void requestProductList(String keyword) {
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
                    List<WProductExt> wproductslist = resultData.get(0).getWproductslist();
                    if (wproductslist != null && wproductslist.size() > 0) {
                        quickAdapter.replaceAll(wproductslist);
                        emptyView.setVisibility(View.GONE);
                    } else {
                        initEmptyView(EmptyView.MODE_NODATA);
                    }
                } else {
                    initEmptyView(EmptyView.MODE_NODATA);
                }
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
                BackScanListActivity.this.showProgressDialog();
                requestProductList(strSearch);
            }
        });
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
                intent.putExtra("FROM", "sales_return");
                intent.putExtra("GOODS", backMap);
                hasCameraPermissions(intent, true);
                break;
            }
            case R.id.title_search: {
                Intent intent = new Intent(this, BackSearchActivity.class);
                intent.putExtra("GOODS", backMap);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
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
