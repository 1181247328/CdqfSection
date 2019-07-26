package com.cdqf.cart_activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_class.User;
import com.cdqf.cart_find.ExitFind;
import com.cdqf.cart_find.LoginFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_service.DownloadUpdateDilogFragment;
import com.cdqf.cart_service.DownloadUpdateFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartPreferences;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.ScreenUtils;
import com.cdqf.cart_state.StaturBar;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.mylhyl.circledialog.CircleDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    private String TAG = LoginActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

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

    private CircleDialog.Builder builder = null;

    private boolean isUpdate = false;

    private String downloadurl = "";

    private String fileApkName = "";

    private File fileApk = null;

    private boolean isAPk = false;

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
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
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
        initPull(true);
    }

    private void initLogin() {
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

    /**
     * 用于检测是否有新版本
     *
     * @param isToast
     */
    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.DOWNLOAD, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---下载---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        JSONObject data = resultJSON.getJSONObject("data");
                        if (data == null) {
                            return;
                        }
                        //是否强制更新
                        int isForce = data.getInteger("is_force");
                        if (isForce == 1) {
                            isUpdate = true;
                        } else {
                            isUpdate = false;
                        }
                        //判断版本号是否需要提示更新
                        //当前更新
                        downloadurl = data.getString("download_url");
                        //网上版本
                        String versionNumber = data.getString("version_number");
                        //文件名
                        fileApkName = getResources().getString(R.string.app_name) + ".v." + versionNumber + ".apk";
                        //当前版本
                        String versionTwo = cartState.getVersion(context);
                        if (TextUtils.equals(versionNumber, versionTwo)) {
                            isUpdate = false;
                            initLogin();
                        } else {
                            DownloadUpdateDilogFragment downloadUpdateDilogFragment = new DownloadUpdateDilogFragment();
                            downloadUpdateDilogFragment.setInit(isUpdate, "提示", "检测到当前有新版本,是否更新", "否", "是");
                            downloadUpdateDilogFragment.show(getSupportFragmentManager(), "是否更新");
                        }
                        break;
                    default:
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
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
                        Manifest.permission.REQUEST_INSTALL_PACKAGES
                },
                1);
    }

    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(context, "com.cdqf.dire.fileprovider_cart", fileApk);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rcrl_login_sumbit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rcrl_login_sumbit:
                if (isUpdate) {
                    cartState.initToast(context, "请先更新", true, 0);
                    return;
                }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        eventBus.unregister(this);
    }

    /**
     * 不强制更新直接登录
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(LoginFind r) {
        initLogin();
    }

    /**
     * 退出登录
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(ExitFind r) {
        exit();
    }

    /**
     * 提示更新
     *
     * @param d
     */
    @Subscribe
    public void onEventMainThread(DownloadUpdateFind d) {
//        Intent intent = new Intent(LoginActivity.this, DownloadService.class);
////        intent.putExtra("apkUrl", downloadurl);
//        intent.putExtra("apkUrl", "https://download.dgstaticresources.net/fusion/android/app-c6-release.apk");
//        intent.putExtra("file", Environment.getExternalStorageDirectory() + "/" + "washcart");
//        startService(intent);
//        String downloadurl = "https://download.dgstaticresources.net/fusion/android/app-c6-release.apk";
//        String fileApkName = "app-c6-release.apk";
        builder = new CircleDialog.Builder();
        builder.setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("下载")
                .setProgressText("下载了")
                .show(getSupportFragmentManager());

        OkHttpUtils.get()
                .url(downloadurl)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), fileApkName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        builder.setProgress(100, (int) (progress * 100)).refresh();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "---onError---");

                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(File response, int id) {
                        Log.e(TAG, "---下载成功---");
                        fileApk = response;
                        isAPk = true;
                        //版本在7.0以上是不能直接通过uri访问的
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            installApk();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.fromFile(fileApk), "application/vnd.android.package-archive");
                            startActivity(intent);
                        }
                    }
                });
    }
}
