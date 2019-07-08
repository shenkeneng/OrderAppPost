package com.frxs.order;

import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.frxs.order.model.AttachedShopInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * <pre>
 *     author : ewu
 *     e-mail : xxx@xx
 *     time   : 2017/05/08
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */
public class MyBizCircleShopsActivity extends FrxsActivity {

    private ListView attachShopLv;
    private TextView totalShopsTv;
    private Adapter attachedShopAdapter;
    private List<AttachedShopInfo> attachedShopList = new ArrayList<AttachedShopInfo>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_biz_circle_shops;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.tv_title_right).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.attached_shops));

        totalShopsTv = (TextView) findViewById(R.id.total_shops_tv);
        attachShopLv = (ListView) findViewById(R.id.attached_shop_lv);
        totalShopsTv.setText(Html.fromHtml(String.format(getString(R.string.total_biz_circle_shops), attachedShopList.size())));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        attachedShopAdapter = new Adapter<AttachedShopInfo>(this, R.layout.item_single_line) {
            @Override
            protected void convert(AdapterHelper helper, AttachedShopInfo item) {
                int position = helper.getPosition();
                String content = String.valueOf(position + 1) + ". <font color=\"#404040\">" + item.getShopName() + "</font>";
                helper.setText(R.id.single_line_tv, Html.fromHtml(content));
            }
        };
        attachShopLv.setAdapter(attachedShopAdapter);
        requestAttachedShops();
    }

    private void requestAttachedShops() {
        showProgressDialog();

        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());

        getService().GetAttachedShopList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<List<AttachedShopInfo>>>() {
            @Override
            public void onResponse(ApiResponse<List<AttachedShopInfo>> result, int code, String msg) {
                dismissProgressDialog();
                attachedShopList.clear();
                if (result.isSuccessful()) {
                    List<AttachedShopInfo> resultList = result.getData();
                    if (null != resultList) {
                        attachedShopList.addAll(resultList);
                    }
                }

                attachedShopAdapter.replaceAll(attachedShopList);
                totalShopsTv.setText(Html.fromHtml(String.format(getString(R.string.total_biz_circle_shops), attachedShopList.size())));
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AttachedShopInfo>>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }
}
