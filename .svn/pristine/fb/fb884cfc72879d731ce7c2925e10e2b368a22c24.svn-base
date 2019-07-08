package com.frxs.order;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frxs.order.model.DataSynEvent;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by Shenpei on 2017/4/20.
 * 建行支付结果回调页面
 */

public class UPPayResuleActivity extends FrxsActivity{

    private HashMap<String, String> resuleInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void initViews() {
        CommonWebViewActivity.instance.finish();
        TextView tvTitle = (TextView) findViewById(R.id.title_tv);
        tvTitle.setText("支付成功");
        tvTitle.setTextColor(getResources().getColor(R.color.black_de));

        ImageView btnLeft = (ImageView) findViewById(R.id.left_btn);
        btnLeft.setVisibility(View.GONE);
    }

    @Override
    protected void initEvent() {}

    @Override
    protected void initData() {
        String ccbparam = getIntent().getStringExtra("CCBPARAM");
        if (!TextUtils.isEmpty(ccbparam)) {
            resuleInfo = new HashMap<String, String>();
            String[] split = ccbparam.split("&");
            if (split != null && split.length > 0) {
                for (String str : split) {
                    String[] str1 = str.split("=");
                    if (str1 != null && str1.length > 1) {
                        resuleInfo.put(!TextUtils.isEmpty(str1[0]) ? str1[0] : "",
                                !TextUtils.isEmpty(str1[1]) ? str1[1] : "");
                    }
                }
            }
        }

        DataSynEvent event = new DataSynEvent();
        BaseResp resp = new BaseResp() {
            @Override
            public int getType() {
                return 0;
            }

            @Override
            public boolean checkArgs() {
                return false;
            }
        };

        if (resuleInfo != null && resuleInfo.size() > 0) {
            String success = resuleInfo.get("SUCCESS");
            if (!TextUtils.isEmpty(success) && success.equals("Y")){
                // 跳转支付成功页面
                resp.errCode = 0;
                event.setResp(resp);
            } else {
                resp.errCode = 1;
                event.setResp(resp);
            }
        }else{
            resp.errCode = 1;
            event.setResp(resp);
        }
        EventBus.getDefault().postSticky(event);
        finish();
    }

}
