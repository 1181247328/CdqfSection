package com.cdqf.cart_activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.ExitFind;
import com.cdqf.cart_fragment.HomeManagerFragment;
import com.cdqf.cart_fragment.MyFragment;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 首页(店长)
 */
public class MainManagerActivity extends BaseActivity {
    private String TAG = MainManagerActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

//    //载体
//    @BindView(R.id.fl_main_fragment)
//    public FrameLayout flMainFragment = null;

    //首页
    @BindView(R.id.ll_main_home)
    public LinearLayout llMainHome = null;

    @BindView(R.id.iv_main_home)
    public ImageView ivMainHome = null;

    @BindView(R.id.tv_main_home)
    public TextView tvMainHome = null;

    //扫一扫
    @BindView(R.id.ll_main_scan)
    public LinearLayout llMainScan = null;

    @BindView(R.id.iv_main_scan)
    public ImageView ivMainScan = null;

    @BindView(R.id.tv_main_scan)
    public TextView tvMainScane = null;

    //我的
    @BindView(R.id.ll_main_my)
    public LinearLayout llMainMy = null;

    @BindView(R.id.iv_main_my)
    public ImageView ivMainMy = null;

    @BindView(R.id.tv_main_my)
    public TextView tvMainMy = null;

    //用于管理所有的fragment
    private FragmentManager fragmentManager;

    //首页的fragment
    private HomeManagerFragment homeManagerFragment = null;
    private MyFragment myFragment = null;

    private Uri photoUri = null;

    //相机
    private static final int REQUEST_CODE_TAKE_PICTURE = 2;

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
        setContentView(R.layout.activity_main);

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
        fragmentManager = getSupportFragmentManager();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {
        tabImage(0);
    }

    private void tabImage(int position) {
        //开启fragement事物所
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 藏掉所有的Fragment，以防止有多个Fragment显示在界面上
        hideFragments(transaction);
        switch (position) {
            //首页
            case 0:
                //首页
                ivMainHome.setImageResource(R.mipmap.main_tab_home_icn);
                tvMainHome.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_icon));
                //我的
                ivMainMy.setImageResource(R.mipmap.main_tab_my_default);
                tvMainMy.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));

                if (homeManagerFragment == null) {
                    homeManagerFragment = new HomeManagerFragment();
                    transaction.add(R.id.fl_main_fragment, homeManagerFragment);
                } else {
                    transaction.show(homeManagerFragment);
                }
                break;
            //我的
            case 1:
                //首页
                ivMainHome.setImageResource(R.mipmap.main_tab_home_default);
                tvMainHome.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                //我的
                ivMainMy.setImageResource(R.mipmap.main_tab_my_icn);
                tvMainMy.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_icon));

                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.fl_main_fragment, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeManagerFragment != null) {
            transaction.hide(homeManagerFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    /**
     * 相机
     */
    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_scan, R.id.ll_main_my})
    public void onClick(View v) {
        switch (v.getId()) {
            //首页
            case R.id.ll_main_home:
                tabImage(0);
                break;
            //我的
            case R.id.ll_main_my:
                tabImage(1);
                break;
            //扫一扫
            case R.id.ll_main_scan:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(MainManagerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainManagerActivity.this, new String[]{Manifest.permission.CAMERA}, 8);
                    } else {
                        camera();
                    }
                } else {
                    camera();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //相机
            case REQUEST_CODE_TAKE_PICTURE:
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    Log.e(TAG, "---Uri不为空---");
                    uri = data.getData();
                } else {
                    Log.e(TAG, "---Uri为空---");
                    uri = photoUri;
                }
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(uri).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        Log.e(TAG, "---员工使用扫一扫---" + outfile);
                    }
                });
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 8:
                cartState.initToast(context, "请打开相机权限,否则无法使用扫一扫功能", true, 0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(6, "提示", "是否退出当前程序", "否", "是");
        whyDilogFragment.show(getSupportFragmentManager(), "退出登录");
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
     * 退出登录
     *
     * @param r
     */
    public void onEventMainThread(ExitFind r) {
        exit();
    }
}
