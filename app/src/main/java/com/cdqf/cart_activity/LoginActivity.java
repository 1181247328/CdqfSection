package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_class.User;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartPreferences;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    private String TAG = LoginActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //帐户
    @BindView(R.id.et_login_account)
    public EditText etLoginAccount = null;

    //密码
    @BindView(R.id.xet_login_passwrod)
    public XEditText xetLoginPasswrod = null;

    //登录
    @BindView(R.id.rcrl_login_sumbit)
    public RCRelativeLayout rcrlLoginSumbit = null;

//    //帐户
//    private String account = null;
//
//    //密码
//    private String passwrod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //加载布局
        setContentView(R.layout.activity_login);

        //API>=20以上用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉侵
            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.black));
        }

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        ButterKnife.bind(this);
        cartState.permission(this);
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {
        //判断是有登录的
        if (CartPreferences.getUser(context) != null) {
            cartState.setUser(CartPreferences.getUser(context));
            etLoginAccount.setText(cartState.getUser().getLogin_account());
            if (TextUtils.equals(cartState.getUser().getType(), "1")) {
                //员工
                initIntent(MainActivity.class);
            } else if (TextUtils.equals(cartState.getUser().getType(), "2")) {
                //店长
                initIntent(MainManagerActivity.class);
            } else {
                //TODO
            }
        }
    }

    private String loginAccPass(String account, String password) {
        String result = "";
        result = CartAddaress.ADDRESS+"/?s=Staff.login&account="+account+"&password="+password;
        Log.e(TAG, "---登录---" + result);
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.rcrl_login_sumbit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rcrl_login_sumbit:
                String account = etLoginAccount.getText().toString();
                if (account.length() <= 0) {
                    cartState.initToast(context, "请输入帐户", true, 0);
                    return;
                }
                String passwrod = xetLoginPasswrod.getText().toString();
                if (passwrod.length() <= 0) {
                    cartState.initToast(context, "请输入密码", true, 0);
                    return;
                }
                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("", account);
//                params.put("", passwrod);
                String accountPasswrod = loginAccPass(account, passwrod);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.post(accountPasswrod, true, "登录中", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse登录---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("ret");
                        String msg = resultJSON.getString("msg");
                        switch (error_code) {
                            //获取成功
                            case 200:
                                String data = resultJSON.getString("data");
                                User user = gson.fromJson(data, User.class);
                                cartState.setUser(user);
                                CartPreferences.setUser(context, user);
                                if (TextUtils.equals(user.getState(), "1")) {
                                    //审核通过
                                    if (TextUtils.equals(user.getType(), "1")) {
                                        //员工
                                        initIntent(MainActivity.class);
                                    } else if (TextUtils.equals(user.getType(), "2")) {
                                        //店长
                                        initIntent(MainManagerActivity.class);
                                    } else {
                                        //TODO
                                    }
                                } else if (TextUtils.equals(user.getState(), "0")) {
                                    //审核未通过
                                    cartState.initToast(context, "审核未通过", true, 0);
                                } else {
                                    //TODO
                                }
                                finish();
                                break;
                            default:
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "---启动---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "---恢复---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
    }
}
