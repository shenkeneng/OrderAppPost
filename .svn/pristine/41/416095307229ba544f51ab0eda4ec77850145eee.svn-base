package com.frxs.order;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ewu.core.widget.EmptyView;

/**
 * Created by Chentie on 2017/3/9.
 */

public class CommonWebViewActivity extends FrxsActivity {

    private WebView mWebView;

    private String requstPoint;

    private TextView titleTv;

    private EmptyView emptyview;

    public static CommonWebViewActivity instance = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_point_rule;
    }

    @Override
    protected void initViews() {
        titleTv = ((TextView) findViewById(R.id.title_tv));
        mWebView = (WebView) findViewById(R.id.wv_point_rule);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        emptyview = (EmptyView) findViewById(R.id.emptyview);
        emptyview.setVisibility(View.GONE);
        instance = this;
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        requstPoint = getIntent().getStringExtra("REQUSTPOINT");
        String h5Title = getIntent().getStringExtra("H5TITLE");
        // 设置标题
        titleTv.setText(h5Title);
        //设置WebViewClient
        mWebView.setWebViewClient(new WebViewClient(){

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
                if(url.startsWith("mbspay:")){
                    showProgressDialog();
                    Log.i("ccbpay", url);
                    PackageManager pm = getPackageManager();
                    Intent checkIntent = pm.getLaunchIntentForPackage("com.chinamworld.main");
                    if(checkIntent != null){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        mWebView.setVisibility(View.INVISIBLE);
                        emptyview.setMode(EmptyView.MODE_LOADING);
                        emptyview.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else {
                    Log.i("ccbpay", url);
                    return false;
                }
            }
        });

        mWebView.loadUrl(requstPoint);
    }

    /**
     * webview退出时候，释放内存占用，防止内存泄漏
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
