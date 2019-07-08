package com.frxs.order.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class MyNativeWebView extends WebView {

    private Context mContext;

    private OnWebViewEventListener mListener;

    public MyNativeWebView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public MyNativeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public void setWebViewEventListener(OnWebViewEventListener listener) {
        mListener = listener;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init() {
//		mWebView = this.getRefreshableView();
        final WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true);

        setWebChromeClient(new WebChromeClient() {
//            // The undocumented magic method override
//            // Eclipse will swear at you if you try to put @Override here
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                ((CommMyWebViewActivity)mContext).setmUploadMessage(uploadMsg);
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                ((CommMyWebViewActivity)mContext).startActivityForResult(Intent.createChooser(i, "File Chooser"), CommMyWebViewActivity.FILECHOOSER_RESULTCODE);
//            }
//
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                ((CommMyWebViewActivity)mContext).setmUploadMessage(uploadMsg);
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                ((CommMyWebViewActivity)mContext).startActivityForResult(Intent.createChooser(i, "File Browser"), CommMyWebViewActivity.FILECHOOSER_RESULTCODE);
//            }
//
//            // For Android 4.1
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                ((CommMyWebViewActivity)mContext).setmUploadMessage(uploadMsg);
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                ((CommMyWebViewActivity)mContext).startActivityForResult(Intent.createChooser(i, "File Chooser"), CommMyWebViewActivity.FILECHOOSER_RESULTCODE);
//            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e("WMTest", "onProgressChanged " + newProgress);
                if (null != mListener) {
                    mListener.onProgressChanged(newProgress);
                }
            }
        });

        initJsObject();

        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (null != mListener) {
                    mListener.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                if (null != mListener) {
                    mListener.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

        });
    }

    public void initJsObject() {
        addJavascriptInterface(this, "h5CommonWeb");

        JsObject jsObject = new JsObject(mContext);
        Js2JavaBridge.getInstance().addjsObject("jsObject", jsObject);
    }

    @JavascriptInterface
    public void js2java(String jsString) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject) jsonParser.parse(jsString);
        String object = jo.get("object").getAsString();
        String menthod = jo.get("menthod").getAsString();
        JsonElement param = jo.get("param");

        Js2JavaBridge.getInstance().run(object, menthod, param);

    }
}
