package com.frxs.order;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.takephoto.model.TImage;
import com.ewu.core.widget.takephoto.model.TResult;
import com.frxs.order.model.ShopImgPath;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.RequestListener;
import com.frxs.order.webview.MyNativeWebView;
import com.frxs.order.webview.OnWebViewEventListener;

import java.util.ArrayList;

/**
 * <pre>
 *     author : ewu
 *     e-mail : xxx@xx
 *     time   : 2017/06/20
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */
public class CommMyWebViewActivity extends UploadPictureActivity implements OnWebViewEventListener {

    protected MyNativeWebView myNativeWebView;
    protected EmptyView emptyView;
    private TextView tvRight;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commom_web_view;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tvRight = (TextView) findViewById(R.id.tv_title_right);
        myNativeWebView = (MyNativeWebView)findViewById(R.id.native_web_view);
        emptyView = (EmptyView)findViewById(R.id.emptyview);
        findViewById(R.id.tv_title_right).setVisibility(View.GONE);
    }

    @Override
    protected void initEvent() {
        tvRight.setOnClickListener(this);
        myNativeWebView.setWebViewEventListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            String title = intent.getStringExtra("Title");
            ((TextView) findViewById(R.id.tv_title)).setText(title);
            url = intent.getStringExtra("H5_URL");

            if (!TextUtils.isEmpty(url)) {
                loadUrl(url);
            }
        }
    }

    private void loadUrl(String url) {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_LOADING);
        myNativeWebView.loadUrl(url);
    }

    @Override
    public void onProgressChanged(int newProgress) {
        if (newProgress >= 100) {
            myNativeWebView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        myNativeWebView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_ERROR);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result != null) {
            final ArrayList<TImage> images = result.getImages();
            if (images != null) {
                showProgressDialog();
                for (int i = 0; i < images.size(); i++) {
                    subBackImg(images.get(i), new RequestListener() {
                        @Override
                        public void handleRequestResponse(ApiResponse result) {
                            if (result.getFlag().equals("SUCCESS")){
                                if (result.getData() != null) {
                                    final String imgPath = ((ShopImgPath)result.getData()).getImgPath();
                                   if (null != imgPath) {
                                       myNativeWebView.post(new Runnable() {
                                           @Override
                                           public void run() {
                                               myNativeWebView.loadUrl("javascript: setImg1('" + imgPath + "','"+ imgType +"')");
                                           }
                                       });
                                   }
                                }
                            }
                            dismissProgressDialog();
                        }

                        @Override
                        public void handleExceptionResponse(String errMsg) {

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_title_right:
                if (!TextUtils.isEmpty(url)) {
                    loadUrl(url);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myNativeWebView.canGoBack()) {
            myNativeWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
