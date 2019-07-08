package com.frxs.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ewu.core.utils.DateUtil;
import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.ImeUtils;
import com.ewu.core.utils.MD5Util;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.NoScrollListView;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.Config;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.RequestListener;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.UrlDecorator;
import com.frxs.order.widget.ClearEditText;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import retrofit2.Call;

/**
 * 用户登录 by Tiepier
 */
public class LoginActivity extends FrxsActivity {
    public final static int SHOP_FREEZE = 0;// 表示门店冻结
    public final static int SHOP_NORMAL = 1;// 表示门店正常
    public final static int SHOP_ELIMINATE = 2;// 表示门店淘汰


    private Button loginBtn;

    private ClearEditText edtAccount;

    private ClearEditText edtPassword;

    private View envHiddenBtn;// 选择环境的暗门

    private String[] environments;

    private String strUserName;// 账号

    private String strPassWord;// 密码

    private TextView tvLeft;// 标题栏左侧按钮

    private TextView tvRight;// 标题栏右侧按钮

    private TextView tvTitle;//标题

    private long exitTime;// 退出时间

    private MaterialDialog selectShopDialog;// 选择门店的对话框

    private List<ShopInfo> infos = new ArrayList<>();

    private TextView registerTv;//注册帐号

    private TextView forgetPassword;//忘记密码

    private TextView remberLoginTv;//记住这次登记

    private boolean isRember = true;// 默认记住这次登记

    private TextView showPswTv;//是否可见密码

    private boolean isShowPsw = false;//　默认密码不可见


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {

        /**
         * 实例化控件
         */
        loginBtn = (Button) findViewById(R.id.login_commit_btn);//登录
        edtAccount = (ClearEditText) findViewById(R.id.login_account_edit);// 用户名编辑框
        edtPassword = (ClearEditText) findViewById(R.id.login_password_edit);// 密码编辑框
        envHiddenBtn = findViewById(R.id.select_environment);// 环境选择按钮
        tvLeft = (TextView) findViewById(R.id.tv_title_left);
        tvRight = (TextView) findViewById(R.id.tv_title_right);
        tvTitle = (TextView) findViewById(R.id.tv_title);//标题
        registerTv = (TextView) findViewById(R.id.tv_register);
        forgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        remberLoginTv = (TextView) findViewById(R.id.tv_rember_login);
        showPswTv = (TextView) findViewById(R.id.tv_show_psw);
        remberLoginTv.setSelected(isRember ? true : false);
        showPswTv.setSelected(isShowPsw ? true : false);
        tvLeft.setVisibility(View.INVISIBLE);
        tvRight.setVisibility(View.INVISIBLE);
        tvTitle.setText(R.string.title_login);

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
        edtPassword.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});
    }

    @Override
    protected void initData() {
        String account = FrxsApplication.getInstance().getUserAccount();
        if (!TextUtils.isEmpty(account)) {
            edtAccount.setText(account);
            edtAccount.setSelection(edtAccount.length());
        }

        initEnvironment();

//        if (FrxsApplication.getInstance().isNeedCheckUpgrade()) {
//            FrxsApplication.getInstance().prepare4Update(this, false);
//        }
    }

    @Override
    protected void initEvent() {
        loginBtn.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        remberLoginTv.setOnClickListener(this);
        showPswTv.setOnClickListener(this);

        envHiddenBtn.setOnClickListener(new View.OnClickListener() {
            int keyDownNum = 0;

            @Override
            public void onClick(View view) {
                keyDownNum++;
                if (keyDownNum == 10) {
                    ToastUtils.show(LoginActivity.this, "已进入环境选择模式");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);

                    final int spEnv = FrxsApplication.getInstance().getEnvironment();
                    String env = spEnv < environments.length ? environments[spEnv] : "";
                    dialog.setTitle(getString(R.string.str_dialog_title_head) + env + getString(R.string.str_dialog_title_foot));
                    dialog.setCancelable(false);
                    dialog.setItems(environments, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            if (spEnv == which) {
                                return;
                            }
                            if (which != 0) {
                                final AlertDialog verifyMasterDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                View contentView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_evironments, null);
                                final EditText pswEt = (EditText) contentView.findViewById(R.id.password_et);
                                contentView.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (TextUtils.isEmpty(pswEt.getText().toString().trim())) {
                                            ToastUtils.show(LoginActivity.this, "密码不能为空！");
                                            return;
                                        }

                                        if (!pswEt.getText().toString().trim().equals(getString(R.string.str_psw))) {
                                            ToastUtils.show(LoginActivity.this, "密码错误！");
                                            return;
                                        }
                                        FrxsApplication.getInstance().setEnvironment(which);
                                        ImeUtils.hideSoftKeyboard(pswEt);
                                        verifyMasterDialog.dismiss();
                                    }
                                });

                                contentView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImeUtils.hideSoftKeyboard(pswEt);
                                        verifyMasterDialog.dismiss();
                                    }
                                });
                                verifyMasterDialog.setView(contentView);
                                verifyMasterDialog.show();
                            } else {
                                FrxsApplication.getInstance().setEnvironment(which);
                            }
                        }
                    });
                    dialog.setNegativeButton(getString(R.string.str_dialog_cancle),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                    keyDownNum = 0;
                }
            }
        });
    }

    private void initEnvironment() {
        environments = getResources().getStringArray(R.array.run_environments);
        for (int i = 0; i < environments.length; i++) {
            environments[i] = String.format(environments[i], Config.getBaseUrl(i));
        }
    }

    /**
     * 显示店铺选择的对话框
     *
     * @param shopInfos
     */
    private void showSelectShopDialog(List<ShopInfo> shopInfos) {
        selectShopDialog = new MaterialDialog(LoginActivity.this);
        View selectShopView = LayoutInflater.from(this).inflate(R.layout.dialog_select_shop, null);
        NoScrollListView lvShopList = (NoScrollListView) selectShopView.findViewById(R.id.lv_shop_list);
//        selectShopDialog.setCanceledOnTouchOutside(false);// 默认点击阴影不消失对话框，强制选择店铺
        final Adapter<ShopInfo> shopAdapter = new Adapter<ShopInfo>(this, shopInfos, R.layout.item_select_shop) {
            @Override
            protected void convert(AdapterHelper helper, final ShopInfo item) {
                // 0、冻结;1、正常；2、淘汰(淘汰门店不能等于系统)
                if (item.getStatus() == SHOP_NORMAL || item.getStatus() == SHOP_FREEZE || item.getStatus() == SHOP_ELIMINATE) {
                    helper.setText(R.id.tv_shop_name, item.getShopName() + "");
                }
            }
        };
        lvShopList.setAdapter(shopAdapter);
        selectShopDialog.setContentView(selectShopView);
        selectShopDialog.show();

        lvShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (shopAdapter.get(position).getStatus() == SHOP_ELIMINATE) {
                    ToastUtils.show(LoginActivity.this, "该门店已淘汰");
                    return;
                } else {
                    FrxsApplication.getInstance().setmCurrentShopInfo(shopAdapter.get(position));
                    selectShopDialog.dismiss();
                    reqNeedPerfectShopInfo();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.login_commit_btn: {// 登录
                if (TextUtils.isEmpty(edtAccount.getText().toString().trim())) {
                    ToastUtils.show(this, R.string.tips_null_account);// 账号不能为空
                    shakeView(edtAccount);
                } else if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
                    ToastUtils.show(LoginActivity.this, R.string.tips_null_password);// 密码不能为空
                    shakeView(edtPassword);
                } else {
                    strUserName = edtAccount.getText().toString().trim();
                    strPassWord = edtPassword.getText().toString().trim();
                    if (SystemUtils.isNetworkAvailable(LoginActivity.this)) {
                        requestLogin();
                    } else {
                        ToastUtils.show(LoginActivity.this, "网络异常，请检查网络是否连接");
                    }
                }
                break;
            }

            case R.id.tv_register: {// 注册
                Intent intent = new Intent(LoginActivity.this, CommMyWebViewActivity.class);
                UrlDecorator urlDecorator = new UrlDecorator(Config.getBaseUrl() + "AppLogin/Register");
                intent.putExtra("H5_URL", urlDecorator.toString() + "?a=" + new Random().nextInt());
                intent.putExtra("Title", getString(R.string.register));
                startActivity(intent);
                break;
            }

            case R.id.tv_forget_password: {// 忘记密码
                Intent intent = new Intent(this, FindPswActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tv_rember_login: {//记住这次登录
                if (isRember) {
                    isRember = false;
                    remberLoginTv.setSelected(false);
                } else {
                    isRember = true;
                    remberLoginTv.setSelected(true);
                }
                break;
            }

            case R.id.tv_show_psw: {// 显示密码
                if (isShowPsw) {
                    isShowPsw = false;
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPswTv.setSelected(false);
                } else {
                    isShowPsw = true;
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPswTv.setSelected(true);
                }
                // 设置光标在内容后面
                Editable etableSetting = edtPassword.getText();
                Selection.setSelection(etableSetting, etableSetting.length());
                break;
            }
            default:
                break;
        }
    }

    /**
     * 登录网络请求
     */
    private void requestLogin() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("UserAccount", strUserName);
        params.put("Password", strPassWord);
        params.put("StoreType", 2);
        getService().UserLogin(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<UserInfo>>() {
            @Override
            public void onResponse(ApiResponse<UserInfo> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    UserInfo userInfo = result.getData();
                    if (null != userInfo) {
                        infos = userInfo.getShopList();
                        if (infos != null && infos.size() > 0) {
                            FrxsApplication application = FrxsApplication.getInstance();
                            application.setUserInfo(userInfo);
                            if (isRember) {
                                application.setUserAccount(strUserName);
                            } else {
                                application.setUserAccount("");
                            }

                            if (infos.size() > 1) {// 表示有两个及以上的店铺，需要用户选择店铺
                                showSelectShopDialog(infos);
                            } else {// <=1 表示只有一个店铺，则不需要选择店铺直接进入
                                int shopStatus = infos.get(0).getStatus();
                                Log.i("CODE", String.valueOf(shopStatus));
                                application.setmCurrentShopInfo(infos.get(0));
                                if (shopStatus == SHOP_ELIMINATE) {
                                    ToastUtils.show(LoginActivity.this, "该门店已淘汰");
                                    dismissProgressDialog();
                                    return;
                                } else {
                                    if (shopStatus == SHOP_FREEZE) { //门店已冻结
                                        ToastUtils.show(LoginActivity.this, String.format(getResources().getString(R.string.str_freeze), infos.get(0).getShopName()));
                                    }
                                    reqNeedPerfectShopInfo();
                                    return;
                                }
                            }
                        } else {
                            ToastUtils.show(LoginActivity.this, "对不起，您名下还没有关联任何门店");
                        }
                    }
                } else {
                    ToastUtils.show(LoginActivity.this, result.getInfo());
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {
                dismissProgressDialog();
                ToastUtils.show(LoginActivity.this, R.string.network_request_failed);//网络请求失败，请重试
            }
        });
    }

    private void reqNeedPerfectShopInfo() {
        reqIsPrefectShopInfo(new RequestListener() {

            @Override
            public void handleRequestResponse(ApiResponse result) {
                if (result.isSuccessful()) {
                    go2HomeActivity();
                } else {
                    Intent intent = new Intent(LoginActivity.this, CommMyWebViewActivity.class);
                    UrlDecorator urlDecorator = new UrlDecorator(Config.getBaseUrl() + "AppData/Fillin");
                    urlDecorator.add("shopId", getShopID());
                    String content = getShopID() + DateUtil.format(new Date(), "yyyyMMdd") + "frxs";
                    urlDecorator.add("secret", MD5Util.MD5Encode(content, ""));
                    intent.putExtra("H5_URL", urlDecorator.toString() + "&a=" + new Random().nextInt());
                    intent.putExtra("Title", getString(R.string.perfect_cert_info));
                    startActivityForResult(intent, GlobelDefines.REQ_CODE_LOGIN);
                }

                dismissProgressDialog();
            }

            @Override
            public void handleExceptionResponse(String errMsg) {
                dismissProgressDialog();
            }
        });
    }

    private void go2HomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("sync_sale_cart", true);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobelDefines.REQ_CODE_LOGIN) {
            go2HomeActivity();
        }
    }

    /**
     * 窗口抖动
     */
    private void shakeView(EditText edit) {
        DisplayUtil.shakeView(this, edit);
        edit.requestFocus();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
