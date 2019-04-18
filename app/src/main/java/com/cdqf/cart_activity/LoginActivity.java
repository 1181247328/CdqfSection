package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.cdqf.cart.R;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.gcssloop.widget.RCRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

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

    //帐户
    @BindView(R.id.et_login_account)
    public EditText etLoginAccount = null;

    //密码
    @BindView(R.id.xet_login_passwrod)
    public XEditText xetLoginPasswrod = null;

    //登录
    @BindView(R.id.rcrl_login_sumbit)
    public RCRelativeLayout rcrlLoginSumbit = null;

    //帐户
    private String account = null;

    //密码
    private String passwrod = null;

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
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {

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
//                account = etLoginAccount.getText().toString();
//                if (account.length() <= 0) {
//                    cartState.initToast(context, "请输入帐户", true, 0);
//                    return;
//                }
//                passwrod = xetLoginPasswrod.getText().toString();
//                if (passwrod.length() <= 0) {
//                    cartState.initToast(context, "请输入密码", true, 0);
//                    return;
//                }
//                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("", account);
//                params.put("", passwrod);
//                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
//                okHttpRequestWrap.post(CartAddaress.LOGIN, true, "登录中", params, new OnHttpRequest() {
//                    @Override
//                    public void onOkHttpResponse(String response, int id) {
//                        Log.e(TAG, "---onOkHttpResponse登录---" + response);
//                    }
//
//                    @Override
//                    public void onOkHttpError(String error) {
//                        Log.e(TAG, "---onOkHttpError---" + error);
//                    }
//                });
                initIntent(MainManagerActivity.class);
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
