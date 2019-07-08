package com.frxs.order;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.InputUtils;
import com.ewu.core.utils.LogUtils;
import com.ewu.core.utils.TimeUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.ClearEditText;

import retrofit2.Call;

/**
 * Created by Chentie on 2017/6/14.
 */

public class FindPswActivity extends FrxsActivity {

    private ClearEditText cetRegisterMob;// 注册手机号
    private ClearEditText cetForgetCode;
    private TextView sendValidPsw;
    private ClearEditText cetForgetNew;
    private ClearEditText cetForgetNewSure;
    private Button btnUpdatePsw;// 确认提交

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_psw;
    }

    @Override
    protected void initViews() {
        TextView tvRight = (TextView) findViewById(R.id.tv_title_right);
        tvRight.setVisibility(View.INVISIBLE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("找回密码");
        cetRegisterMob = (ClearEditText) findViewById(R.id.cet_register_mob);
        findViewById(R.id.ll_password_forget).setVisibility(View.VISIBLE);
        cetForgetCode = (ClearEditText) findViewById(R.id.cet_forget_code);
        sendValidPsw = (TextView) findViewById(R.id.tv_send_valid_psw);
        cetForgetNew = (ClearEditText) findViewById(R.id.cet_forget_new);
        cetForgetNewSure = (ClearEditText) findViewById(R.id.cet_forget_new_sure);
        btnUpdatePsw = (Button) findViewById(R.id.btn_update_password);


        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")){
                    return "";
                } else {
                    return null;
                }
            }
        };
        cetForgetNew.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});
        cetForgetNewSure.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});
    }

    @Override
    protected void initEvent() {
        sendValidPsw.setOnClickListener(this);
        btnUpdatePsw.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_send_valid_psw:
                if (!isUsableMob(cetRegisterMob)) {
                    return;
                }
                new TimeUtils(this, sendValidPsw, getResources().getString(R.string.tips_send_code)).RunTimer();
                ToastUtils.show(this, String.format(getResources().getString(R.string.tips_has_valid_code), "5"));
                reqSendValid(1, cetRegisterMob.getText().toString().trim());
                break;

            case R.id.btn_update_password:
                findPsw();
                break;

            default:
                break;
        }
    }

        /**
         * 找回密码
         */
    private void findPsw() {
        // 检验手机号码
        if (isUsableMob(cetRegisterMob)) {
            String verifiCode = cetForgetCode.getText().toString().trim();
            if (TextUtils.isEmpty(verifiCode)){
                ToastUtils.show(this, R.string.hint_verify_code);
                shakeView(cetForgetCode);
                return;
            }
            if (TextUtils.isEmpty(cetForgetNew.getText().toString().trim())){
                ToastUtils.show(this, R.string.tips_not_null_pw_new);//新密码不能为空
                shakeView(cetForgetNew);
                return;
            }
            if (TextUtils.isEmpty(cetForgetNewSure.getText().toString().trim())){
                ToastUtils.show(this, R.string.tips_not_null_pw_new_sure);//确认新密码不能为空
                shakeView(cetForgetNewSure);
                return;
            }

            String psw = cetForgetNew.getText().toString().trim();
            String newPsw = cetForgetNewSure.getText().toString().trim();

            if (psw.length() < 6 || psw.length() > 20 || newPsw.length() < 6 || newPsw.length() > 20){
                ToastUtils.show(this, "密码长度为6-20个字符，请按规定设置密码!");
                return;
            }
            if (!psw.equals(newPsw)){
                ToastUtils.show(this, R.string.tips_psw_again);//确认新密码错误
                return;
            }

            String phoneNum = cetRegisterMob.getText().toString().trim();
            reqUpdatePSW(phoneNum, newPsw, verifiCode);
        }
    }

    /**
     * 验证注册手机号是否为空 是否可用
     *
     * @return
     */
    private boolean isUsableMob(ClearEditText edText) {
        String phoneNum = edText.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneNum)) {//手机号不为空
            if (!InputUtils.isMobileNO(phoneNum)) {
                ToastUtils.show(this, R.string.tips_not_mob_error);// 请输入正确的手机号码
                shakeView(edText);
                return false;
            }
        } else {
            ToastUtils.show(this, R.string.tips_not_null_mob);// 手机号不能为空
            shakeView(edText);
            return false;
        }
        return true;
    }

    /**
     * 窗口抖动
     */
    private void shakeView(EditText edit) {
        DisplayUtil.shakeView(this, edit);
        edit.requestFocus();
    }

    /**
     * 校验验证码
     *
     * @param codeType
     * @param verifiCode
     * @param phoneNum
     */
   /* private void reqIsValidCode(final int codeType, final String verifiCode, String codeNew, final String phoneNum, final String newPsw) {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("Type", codeType);// 1、找回密码；2、修改密码；3、登录验证码；4、注册验证码,5:修改绑定手机;6修改银行卡
        params.put("Phone", phoneNum);
        params.put("VerifiCode", verifiCode);
        if (codeType != 1) {
            params.put("UserId", getUserID());
            params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        }

        getService().IsValidCode(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    String data = result.getData();
                    if (data.equals("true")) {
                        LogUtils.e("SUCCESS : 验证码校验成功！");
                        reqUpdatePSW(phoneNum, newPsw, verifiCode);
                    } else {
                        ToastUtils.show(FindPswActivity.this, "验证码错误，请重新输入。");
                    }
                } else {
                    ToastUtils.show(FindPswActivity.this, "验证码错误，请重新输入。");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(FindPswActivity.this, t.getMessage());
            }
        });
    }*/

    /**
     * 提交更新的密码
     * @param phoneNum
     * @param newPsw
     * @param verifiCode
     */
    private void reqUpdatePSW(String phoneNum, String newPsw, String verifiCode) {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("UserMobile", phoneNum);
        params.put("Password", newPsw);
        params.put("VerifyCode", verifiCode);

        getService().FindPassWord(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(ApiResponse<Integer> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")){
                    ToastUtils.show(FindPswActivity.this, "修改成功!");
                    finish();
                } else {
                    ToastUtils.show(FindPswActivity.this, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(FindPswActivity.this, t.getMessage());
            }
        });
    }

    /**
     * 发送验证码
     *
     * @param codeType
     * @param phoneNum
     */
    private void reqSendValid(int codeType, String phoneNum) {
        AjaxParams params = new AjaxParams();
        params.put("Phone", phoneNum);
        params.put("Type", codeType);// 1、找回密码；2、修改密码；3、登录验证码；4、注册验证码,5:修改绑定手机;6修改银行卡
        if (codeType != 1) {
            params.put("UserId", getUserID());
            params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        }

        getService().SendValidCode(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(ApiResponse<Integer> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null && result.getData() > 0 && result.getInfo().equals("OK")) {
                        // 发送验证码成功
                        LogUtils.d("SUCCESS : 验证码已发送");
                    }
                } else {
                    ToastUtils.show(FindPswActivity.this, result.getInfo());
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtils.show(FindPswActivity.this, t.getMessage());
            }
        });
    }
}
