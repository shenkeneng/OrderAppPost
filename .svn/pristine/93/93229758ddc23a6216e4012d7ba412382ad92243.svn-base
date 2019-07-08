package com.frxs.order;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.WindowManager;
import com.ewu.core.utils.DateUtil;
import com.ewu.core.utils.MD5Util;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.RequestListener;
import com.frxs.order.utils.UrlDecorator;
import java.util.Date;

/**
 * 启动页 by Tiepier
 */
public class SplashActivity extends FrxsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initData() {
        new CountDownTimer(3000, 1500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
                if (null != userInfo && !TextUtils.isEmpty(userInfo.getUserId()) && null != userInfo.getCurrenShopInfo()) {
                    reqNeedPerfectShopInfo();
                } else {
                    go2LoginActivity();
                }
            }
        }.start();
    }

    private void go2LoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.just_fade_in, R.anim.just_fade_out);
    }

    private void go2HomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private void reqNeedPerfectShopInfo() {
        reqIsPrefectShopInfo(new RequestListener() {

            @Override
            public void handleRequestResponse(ApiResponse result) {
                if(result.isSuccessful()) {
                    go2HomeActivity();
                } else {
                    Intent intent = new Intent(SplashActivity.this, CommMyWebViewActivity.class);
                    UrlDecorator urlDecorator = new UrlDecorator(Config.getBaseUrl() + "AppData/Fillin");
                    urlDecorator.add("shopId", getShopID());
                    String content = getShopID() +  DateUtil.format(new Date(), "yyyyMMdd") + "frxs";
                    urlDecorator.add("secret", MD5Util.MD5Encode(content, ""));
                    intent.putExtra("H5_URL", urlDecorator.toString());
                    intent.putExtra("Title", getString(R.string.perfect_cert_info));
                    startActivityForResult(intent, GlobelDefines.REQ_CODE_SPLASH);
                }

                dismissProgressDialog();
            }

            @Override
            public void handleExceptionResponse(String errMsg) {
                dismissProgressDialog();
                FrxsApplication.getInstance().logout();
                go2LoginActivity();
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobelDefines.REQ_CODE_SPLASH) {
            go2HomeActivity();
        }
    }
}
