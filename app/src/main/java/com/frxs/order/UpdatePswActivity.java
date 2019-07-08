package com.frxs.order;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.InputUtils;
import com.ewu.core.utils.LogUtils;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.TimeUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.ShopApplyInfoStatus;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.ClearEditText;

import retrofit2.Call;

/**
 * 修改密码 by Tiepier
 */
public class UpdatePswActivity extends FrxsActivity {

    private Button btnUpdatePsw;// 确认提交

    private TextView tvTitle;//标题

    private TextView tvRight;

    private String strOldPsw;// 旧密码

    private String srtNewPsw;// 新密码

    private String srtNewPswSure;// 确认新密码

    private String from;

    private TextView RegisterMobTv;// 注册手机号

    // 修改密码
    private TextView pswTipsTv;
    private ClearEditText cetOldPsw;
    private ClearEditText cetNewPsw;
    private ClearEditText cetNewPswSure;

    // 修改手机号
    private ClearEditText cetVerifyCode;
    private ClearEditText cetMobNew;
    private ClearEditText cetVerifyCodeNew;
    private TextView sendValidTv;
    private TextView sendValidNewTv;

    // 修改银行卡
    private ClearEditText cetBankVerifyCode;
    private ClearEditText cetBankAccount;
    private ClearEditText cetBankType;
    private ClearEditText cetBankCard;
    private TextView sendBankValidTv;
    private ClearEditText cetLegalPerson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    protected void initViews() {
        /**
         * 实例化控件
         */
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_title_right);
        tvRight.setVisibility(View.INVISIBLE);
        RegisterMobTv = (TextView) findViewById(R.id.cet_register_mob);
        btnUpdatePsw = (Button) findViewById(R.id.btn_update_password);
        pswTipsTv = (TextView) findViewById(R.id.tv_psw_tips);
        pswTipsTv.setText(Html.fromHtml(getResources().getString(R.string.tips_modify_pas)));

        from = getIntent().getStringExtra("FROM");
        if (!TextUtils.isEmpty(from) && from.equals("PSW")) {
            tvTitle.setText(R.string.title_update_password);
            findViewById(R.id.ll_password_modify).setVisibility(View.VISIBLE);
            cetOldPsw = (ClearEditText) findViewById(R.id.cet_pwd_old);
            cetNewPsw = (ClearEditText) findViewById(R.id.cet_pwd_new);
            cetNewPswSure = (ClearEditText) findViewById(R.id.cet_pwd_new_sure);
            InputFilter inputFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (source.equals(" ")) {
                        return "";
                    } else {
                        return null;
                    }
                }
            };
            cetNewPsw.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});
            cetNewPswSure.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});
        } else if (!TextUtils.isEmpty(from) && from.equals("MOB")) {
            tvTitle.setText(R.string.title_update_mob);
            findViewById(R.id.ll_mob_bundle).setVisibility(View.VISIBLE);
            cetVerifyCode = (ClearEditText) findViewById(R.id.cet_verify_code);
            cetMobNew = (ClearEditText) findViewById(R.id.cet_mob_new);
            cetVerifyCodeNew = (ClearEditText) findViewById(R.id.cet_verify_code_new);
            sendValidTv = (TextView) findViewById(R.id.tv_send_valid);
            sendValidNewTv = (TextView) findViewById(R.id.tv_send_valid_new);
            sendValidTv.setSelected(false);
            sendValidNewTv.setSelected(false);
        } else if (!TextUtils.isEmpty(from) && from.equals("BANK")) {
            ShopApplyInfoStatus bankInfo = (ShopApplyInfoStatus) getIntent().getSerializableExtra("BANK_INFO");
            tvTitle.setText(R.string.title_update_bank);
            findViewById(R.id.ll_bank_modify).setVisibility(View.VISIBLE);
            cetBankVerifyCode = (ClearEditText) findViewById(R.id.cet_bank_verify_code);
            cetLegalPerson = (ClearEditText) findViewById(R.id.cet_legal_person);
            cetBankAccount = (ClearEditText) findViewById(R.id.cet_bank_account);
            cetBankType = (ClearEditText) findViewById(R.id.cet_bank_type);
            cetBankCard = (ClearEditText) findViewById(R.id.cet_bank_card);
            sendBankValidTv = (TextView) findViewById(R.id.tv_bank_send_valid);
            cetLegalPerson.setText(bankInfo.getLegalPerson());
            cetBankAccount.setText(bankInfo.getBankAccountName());
            cetBankType.setText(bankInfo.getBankType());
            cetBankCard.setText(bankInfo.getBankAccount());
        }

    }

    @Override
    protected void initData() {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        RegisterMobTv.setText(userInfo.getUserMobile());
    }

    @Override
    protected void initEvent() {
        btnUpdatePsw.setOnClickListener(this);
        if (!TextUtils.isEmpty(from) && from.equals("MOB")) {
            sendValidTv.setOnClickListener(this);
            sendValidNewTv.setOnClickListener(this);
        } else if (!TextUtils.isEmpty(from) && from.equals("BANK")) {
            sendBankValidTv.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_update_password: { // 提交修改内容
                if (!TextUtils.isEmpty(from) && from.equals("PSW")) {// 修改密码
                    modifyPSW();
                } else if (!TextUtils.isEmpty(from) && from.equals("MOB")) {// 修改手机号
                    modifyMob();
                } else if (!TextUtils.isEmpty(from) && from.equals("BANK")) {// 修改银行卡
                    modifyCard();
                }
                break;
            }

            case R.id.tv_send_valid: // 发送修改手机验证码
                if (TextUtils.isEmpty(RegisterMobTv.getText().toString().trim())) {
                    ToastUtils.show(UpdatePswActivity.this, getString(R.string.tips_mob_null));
                    return;
                }
                if (cetMobNew.getText().toString().trim().equals(RegisterMobTv.getText().toString().trim())) {
                    ToastUtils.show(UpdatePswActivity.this, getString(R.string.tips_mob_rpeat));// 新旧手机号相同时不能获取验证码
                } else {
                    reqSendValid(5, RegisterMobTv.getText().toString().trim(), sendValidTv);
                }
                break;
            case R.id.tv_send_valid_new: //发送新手机验证码
                if (cetMobNew.getText().toString().trim().equals(RegisterMobTv.getText().toString().trim())) {
                    ToastUtils.show(UpdatePswActivity.this, getString(R.string.tips_mob_rpeat));// 新旧手机号相同时不能获取验证码
                } else {
                    reqSendValid(5, cetMobNew.getText().toString().trim(), sendValidNewTv);
                }
                break;

            case R.id.tv_bank_send_valid: // 发送修改银行卡验证码
                if (TextUtils.isEmpty(RegisterMobTv.getText().toString().trim())) {
                    ToastUtils.show(UpdatePswActivity.this, getString(R.string.tips_mob_null));
                    return;
                }
                reqSendValid(6, RegisterMobTv.getText().toString().trim(), sendBankValidTv);
                break;

            default:
                break;
        }
    }


    /**
     * 修改银行卡
     */
    private void modifyCard() {
        // 检验手机号码
        String code = cetBankVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(this, R.string.hint_verify_code);
            shakeView(cetBankVerifyCode);
            return;
        }
        if (TextUtils.isEmpty(cetLegalPerson.getText().toString().trim())){
            ToastUtils.show(this, R.string.hint_legal_person);
            shakeView(cetLegalPerson);
            return;
        }
        if (TextUtils.isEmpty(cetBankAccount.getText().toString().trim())) {
            ToastUtils.show(this, R.string.hint_bank_account);
            shakeView(cetBankAccount);
            return;
        }
        if (TextUtils.isEmpty(cetBankType.getText().toString().trim())) {
            ToastUtils.show(this, R.string.hint_bank_type);
            shakeView(cetBankType);
            return;
        }
        String bankCard = cetBankCard.getText().toString().trim();
        if (TextUtils.isEmpty(bankCard) && bankCard.length() <= 30) {
            ToastUtils.show(this, R.string.hint_bank_card);
            shakeView(cetBankCard);
            return;
        }

        String legalPerson = cetLegalPerson.getText().toString().trim();
        String bankAccountName = cetBankAccount.getText().toString().trim();
        String bankType = cetBankType.getText().toString().trim();
        reqUpdateUserCard(code, legalPerson, bankAccountName, bankType, bankCard);
    }

    /**
     * 修改手机号码
     */
    private void modifyMob() {
        // 检验手机号码
        if (isUsableMob(cetMobNew)) {
            String code = cetVerifyCode.getText().toString().trim();
            String codeNew = cetVerifyCodeNew.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                ToastUtils.show(this, R.string.hint_verify_code);
                shakeView(cetVerifyCode);
                return;
            }
            if (TextUtils.isEmpty(codeNew)) {
                ToastUtils.show(this, R.string.hint_verify_code_new);
                shakeView(cetVerifyCodeNew);
                return;
            }
            reqUpdateUserModify(code, codeNew, cetMobNew.getText().toString().trim());
        }
    }

    /**
     * 发送验证码
     *
     * @param codeType
     * @param phoneNum
     */
    private void reqSendValid(int codeType, String phoneNum, TextView sendValid) {
        new TimeUtils(this, sendValid, getResources().getString(R.string.tips_send_code)).RunTimer();
        ToastUtils.show(this, String.format(getResources().getString(R.string.tips_has_valid_code), "5"));
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
                    LogUtils.d("Failure : " + result.getInfo());
                    ToastUtils.show(UpdatePswActivity.this, result.getInfo());
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtils.show(UpdatePswActivity.this, t.getMessage());
            }
        });
    }

    /**
     * 更新用户绑定银行卡
     */
    private void reqUpdateUserCard(String verifiCode, String legalPerson, String bankAccountName, String bankType, String bankCard) {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        params.put("UserAccount", userInfo.getUserAccount());
        params.put("Telephone", userInfo.getUserMobile());
        params.put("VerifyCode", verifiCode);
        params.put("LegalPerson", legalPerson);
        params.put("BankType", bankType);
        params.put("BankAccountName", legalPerson);
        params.put("BankAccount", bankCard);
        getService().EditBindingBankCard(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(ApiResponse<Integer> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    showDialog(getResources().getString(R.string.dialog_success_card), 0, "");
                    finish();
                } else {
                    showDialog(result.getInfo(), 1, "");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                showDialog(t.getMessage(), 1, "");
            }
        });
    }

    /**
     * 更新用户绑定手机
     */
    private void reqUpdateUserModify(String verifiCode, String verifiCodeNew, String mobNew) {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        params.put("UserAccount", userInfo.getUserAccount());
        params.put("OldTelephone", userInfo.getUserMobile());
        params.put("OldVerifyCode", verifiCode);
        params.put("NewTelephone", mobNew);
        params.put("NewVerifyCode", verifiCodeNew);

        getService().EditBindingPhone(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(ApiResponse<Integer> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    showDialog(getResources().getString(R.string.dialog_success_mob), 0, "login_again");
                } else {
                    // 接口返回错误信息 清空现有验证码
                    showDialog(getString(R.string.tips_verify_error), 1, "");
                    cetVerifyCode.setText("");
                    cetVerifyCodeNew.setText("");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                showDialog(t.getMessage(), 1, "");
            }
        });
    }


    /**
     * 修改用户密码
     */
    private void modifyPSW() {
        if (TextUtils.isEmpty(cetOldPsw.getText().toString().trim())) {
            ToastUtils.show(this, R.string.tips_not_null_pw_old);//旧密码不能为空
            shakeView(cetOldPsw);
        } else if (TextUtils.isEmpty(cetNewPsw.getText().toString().trim())) {
            ToastUtils.show(this, R.string.tips_not_null_pw_new);//新密码不能为空
            shakeView(cetNewPsw);
        } else if (TextUtils.isEmpty(cetNewPswSure.getText().toString().trim())) {
            ToastUtils.show(this, R.string.tips_not_null_pw_new_sure);//确认新密码不能为空
            shakeView(cetNewPswSure);
        } else {
            strOldPsw = cetOldPsw.getText().toString().trim();
            srtNewPsw = cetNewPsw.getText().toString().trim();
            srtNewPswSure = cetNewPswSure.getText().toString().trim();
            if (srtNewPsw.length() < 6 || srtNewPsw.length() > 20 ||
                    srtNewPswSure.length() < 6 || srtNewPswSure.length() > 20) {
                ToastUtils.show(UpdatePswActivity.this, "密码长度为6-20个字符，请按规定设置密码!");
                return;
            }
            if (srtNewPswSure.equals(srtNewPsw)) {
                if (SystemUtils.isNetworkAvailable(UpdatePswActivity.this)) {
                    requestUpdatePassword();
                } else {
                    ToastUtils.show(UpdatePswActivity.this, "网络异常，请检查网络是否连接");
                }
            } else {
                ToastUtils.show(this, R.string.tips_new_password_error);// 新密码确认错误
                shakeView(cetNewPswSure);
            }
        }
    }

    /**
     * 请求修改密码
     */
    private void requestUpdatePassword() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("Sign", "");
        params.put("UserId", FrxsApplication.getInstance().getUserInfo().getUserId());
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        params.put("OldPassword", strOldPsw);
        params.put("NewPassword", srtNewPsw);
        getService().UserEditPassword(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        showDialog(getResources().getString(R.string.dialog_success_psw), 0, "login_again");
                    } else {
                        showDialog(result.getInfo(), 1, "");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                showDialog(t.getMessage(), 1, "");
            }
        });
    }

    /**
     * 窗口抖动
     */
    private void shakeView(EditText edit) {
        DisplayUtil.shakeView(this, edit);
        edit.requestFocus();
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

    private void showDialog(String message, int status, final String logiAgain) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_hint);
        dialog.setCanceledOnTouchOutside(true);// 对话框外点击有效

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);// 关闭对话框
        TextView messageHintTv = (TextView) dialog.findViewById(R.id.tv_message_hint);// 提示信息
        TextView confirmTv = (TextView) dialog.findViewById(R.id.tv_confirm);// 关闭对话框

        messageHintTv.setText(message);
        Drawable drawable = null;
        if (status > 0) {// 失败
            drawable = getResources().getDrawable(R.mipmap.icon_caveat);
        } else {// 成功
            drawable = getResources().getDrawable(R.mipmap.icon_success);
        }
        messageHintTv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        /**
         * 关闭对话框
         */
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(logiAgain) && logiAgain.equals("login_again")) {
                        FrxsApplication.getInstance().logout();
                        Intent outLogin = new Intent(UpdatePswActivity.this, LoginActivity.class);
                        startActivity(outLogin);
                        finish();
                    }
                }
            }
        });
        /**
         * 关闭对话框
         */
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(logiAgain) && logiAgain.equals("login_again")) {
                        FrxsApplication.getInstance().logout();
                        Intent outLogin = new Intent(UpdatePswActivity.this, LoginActivity.class);
                        startActivity(outLogin);
                        finish();
                    }
                }
            }
        });

        dialog.show();
    }

    /**
     * 获取银行信息
     */
    private void reqGetBankInfo() {
        showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", getShopID());
        params.put("ShopAccount", userInfo.getUserAccount());
        params.put("UserId", getUserID());
        params.put("UserName", userInfo.getUserName());

        getService().GetShopApplyInfoStatus(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopApplyInfoStatus>>() {
            @Override
            public void onResponse(ApiResponse<ShopApplyInfoStatus> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null) {
                        ShopApplyInfoStatus info = result.getData();

                        cetBankAccount.setText(info.getBankAccountName());
                        cetBankType.setText(info.getBankType());
                        cetBankCard.setText(info.getBankAccount());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShopApplyInfoStatus>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

}
