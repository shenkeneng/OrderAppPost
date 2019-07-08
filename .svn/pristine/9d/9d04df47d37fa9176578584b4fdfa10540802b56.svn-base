package com.frxs.order;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;

/**
 * Created by shenpei on 2017/6/10.
 * 退货橱窗
 */

public class SaleBackWindowActivity extends FrxsActivity {

    private ImageView scanIv;// 扫码获取退货商品

    private TextView searchTitleTv;// 点击搜索商品

    private EmptyView emptyView;// 暂无数据

    private ListView orderGoodsLv;

    private List<WProductExt> wproductslist = new ArrayList<WProductExt>();

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap;

    private Adapter<WProductExt> itemAdapter;

    private TextView titleTv;

    private TextView titleRightTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_return;
    }

    @Override
    protected void initViews() {
        titleRightTv = (TextView) findViewById(R.id.tv_title_right);
        titleTv = (TextView) findViewById(R.id.tv_title);
        scanIv = (ImageView) findViewById(R.id.im_scan);
        searchTitleTv = (TextView) findViewById(R.id.title_search);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        orderGoodsLv = (ListView) findViewById(R.id.lv_order_goods);
        findViewById(R.id.tv_order_submit).setVisibility(View.GONE);
        titleTv.setText(R.string.back_window_name);
        backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) getIntent().getSerializableExtra("GOODS");
    }

    @Override
    protected void initEvent() {
        scanIv.setOnClickListener(this);
        searchTitleTv.setOnClickListener(this);
        titleRightTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        reqGetSaleBackWindowList();
        /**
         * 添加商品
         */
        itemAdapter = new Adapter<WProductExt>(this, R.layout.item_new_apply_goods) {
            @Override
            protected void convert(final AdapterHelper helper, final WProductExt item) {
                TextView tv = helper.getView(R.id.tv_add_good);
                tv.setSelected(false);
                helper.setVisible(R.id.ll_back_window, View.VISIBLE);
                helper.setText(R.id.tv_good_name, item.getProductName());
                helper.setText(R.id.tv_good_sku, "编码：" + item.getSKU());
                helper.setText(R.id.tv_bar_code, "条码：" + item.getBarCode().split(",")[0]);
                double deliveryPrice = item.getSalePrice() * (1 + item.getShopAddPerc());
                helper.setText(R.id.tv_delivery_price, "配送价：￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + item.getUnit());
                //设置商品退货积分（单位积分 * 退货数量）
                if (item.getShopPoint() > 0) {
                    helper.setVisible(R.id.tv_good_point, View.VISIBLE);
                    helper.setText(R.id.tv_good_point, "退货积分：-" + MathUtils.twolittercountString(item.getShopPoint()) + "/" + item.getUnit());
                } else {
                    helper.setVisible(R.id.tv_good_point, View.INVISIBLE);
                }
                helper.setVisible(R.id.tv_delivery_qty, View.GONE);
                helper.setVisible(R.id.tv_back_reason, View.GONE);
                helper.setVisible(R.id.tv_describe, View.GONE);

                /**
                 * 添加商品
                 */
                helper.setOnClickListener(R.id.tv_add_good, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SaleBackWindowActivity.this, BackGoodsInfoActivity.class);
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
        orderGoodsLv.setAdapter(itemAdapter);
    }

    /**
     * 获取退货橱窗数据
     */
    private void reqGetSaleBackWindowList() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("ShopID", getShopID());
        params.put("WID", getWID());
        params.put("AdvertisementPosition", 4);//查询退货橱窗数据

        getService().WAdvertisementGetListModel(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<List<WAdvertisementGetListModelRespData>>>() {
            @Override
            public void onResponse(ApiResponse<List<WAdvertisementGetListModelRespData>> result, int code, String msg) {
                dismissProgressDialog();
                List<WAdvertisementGetListModelRespData> resultData = result.getData();
                if (null != resultData && resultData.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    wproductslist = resultData.get(0).getWproductslist();
                    /**
                     * 商品列表
                     */
                    if (wproductslist != null && wproductslist.size() > 0) {
                        itemAdapter.replaceAll(wproductslist);
                    } else {
                        initNoData();
                    }
                } else {
                    initNoData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<WAdvertisementGetListModelRespData>>> call, Throwable t) {
                super.onFailure(call, t);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqGetSaleBackWindowList();
                    }
                });
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_scan: {// 扫描条形码
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("FROM", "BACK");
                intent.putExtra("GOODS", backMap);
                hasCameraPermissions(intent, false);
                break;
            }

            case R.id.title_search: {// 按商品名称、条码、编码搜索商品
                Intent intent = new Intent(this, BackSearchActivity.class);
                intent.putExtra("GOODS", backMap);
                startActivity(intent);
                break;
            }

            case R.id.tv_title_right: {// 刷新数据
                reqGetSaleBackWindowList();
                break;
            }

            default:
                break;
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
                reqGetSaleBackWindowList();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case GlobelDefines.RESULT_BACK_SKU:
                if (data != null){
                    backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) data.getSerializableExtra("GOODS");
                    itemAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
