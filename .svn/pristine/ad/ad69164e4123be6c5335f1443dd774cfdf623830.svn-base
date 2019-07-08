package com.frxs.order;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ewu.core.widget.EmptyView;
import com.frxs.order.model.WarehouseMessage;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by ewu on 2016/5/4.
 */
public class MessageDetailActivity extends MaterialStyleActivity {

    private WebView webView;

    private EmptyView emptyView;

    private WarehouseMessage message;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        webView = (WebView) findViewById(R.id.web_view);
        ((TextView) findViewById(R.id.title_tv)).setText("消息详情");
    }

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.message_ptr_frame_ll;
    }

    @Override
    protected void initEvent() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                emptyView.setVisibility(View.GONE);
                refreshComplete();
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestGetMessageDetail();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent)
        {
            message = (WarehouseMessage)intent.getSerializableExtra("message");
            if (null != message)
            {
                emptyView.setVisibility(View.GONE);
                webView.loadDataWithBaseURL(null, message.getMessage(), "text/html", "utf-8", null);
            }
        }
    }

    private void requestGetMessageDetail()
    {
        AjaxParams params =  new AjaxParams();
        params.put("ShopID", getShopID());
        params.put("WID", getWID());
        params.put("ID", message.getID());

        getService().WarehouseMessageGetModel(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<WarehouseMessage>>() {
            @Override
            public void onResponse(ApiResponse<WarehouseMessage> result, int code, String msg) {
                mPtrFrameLayout.refreshComplete();
                WarehouseMessage respData = result.getData();
                if (null != respData)
                {
                    emptyView.setVisibility(View.GONE);
                    webView.loadDataWithBaseURL(null, respData.getMessage(), "text/html", "utf-8", null);
                }
                else
                {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setMode(EmptyView.MODE_NODATA);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<WarehouseMessage>> call, Throwable t) {
                super.onFailure(call, t);
                mPtrFrameLayout.refreshComplete();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
            }
        });
    }
}
