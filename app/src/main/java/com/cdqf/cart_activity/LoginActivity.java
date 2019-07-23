package com.cdqf.cart_activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.cdqf.cart_state.ScreenUtils;
import com.cdqf.cart_state.StaturBar;
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

        //加载布局
        setContentView(R.layout.activity_login);

        StaturBar.setStatusBar(this, R.color.tab_main_text_icon);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        ButterKnife.bind(this);
        Log.e(TAG, "---宽度---" + ScreenUtils.getScreenWidth(context) + "---高度---" + ScreenUtils.getScreenHeight(context));
        permissions();
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
            etLoginAccount.setText(cartState.getUser().getUsername());
            xetLoginPasswrod.setText(cartState.getUser().getPassword());
            String account = etLoginAccount.getText().toString();
            if (account.length() <= 0) {
                return;
            }
            final String passwrod = xetLoginPasswrod.getText().toString();
            if (passwrod.length() <= 0) {
                return;
            }
            loing(account, passwrod);
        }
    }

    private void loing(String account, final String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", account);
        params.put("password", password);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.LOGIN_NEW, true, "登录中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse登录---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        cartState.initToast(context, "登录成功", true, 0);
                        String data = resultJSON.getString("data");
                        User user = gson.fromJson(data, User.class);
                        if (user.getMenu().size() <= 0) {
                            cartState.initToast(context, "权限操作为空,请重新登录获取", true, 0);
                            return;
                        }
                        user.setPassword(password);
                        cartState.setUser(user);
                        CartPreferences.setUser(context, user);
                        initIntent(MainManagerActivity.class);
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
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    //请求权限
    private void permissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                },
                1);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
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
                final String passwrod = xetLoginPasswrod.getText().toString();
                if (passwrod.length() <= 0) {
                    cartState.initToast(context, "请输入密码", true, 0);
                    return;
                }
                loing(account, passwrod);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    //授权能过
                } else {
                    //授权拒绝，如果拒绝将提示关闭程序功能
                    exit();
                }
                break;
            default:
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
