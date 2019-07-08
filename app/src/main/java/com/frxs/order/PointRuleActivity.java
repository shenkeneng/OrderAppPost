package com.frxs.order;

import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ewu.core.widget.EmptyView;

/**
 * Created by Chentie on 2017/3/9.
 */

public class PointRuleActivity extends FrxsActivity {

    private WebView wvPointRule;

    private EmptyView emptyView;

    private String requstPoint;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_point_rule;
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.title_tv)).setText("积分规则");
        wvPointRule = (WebView) findViewById(R.id.wv_point_rule);
        wvPointRule.getSettings().setJavaScriptEnabled(false);
        wvPointRule.getSettings().setSupportZoom(false);
        wvPointRule.getSettings().setBuiltInZoomControls(false);
        wvPointRule.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvPointRule.getSettings().setDefaultFontSize(26);
        wvPointRule.setVerticalScrollBarEnabled(false);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        requstPoint = getIntent().getStringExtra("REQUSTPOINT");
        wvPointRule.loadUrl(requstPoint);
        //设置WebViewClient
        wvPointRule.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgressDialog();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissProgressDialog();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
    }
}
