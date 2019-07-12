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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.ExitFind;
import com.cdqf.cart_find.ScanFind;
import com.cdqf.cart_fragment.HomeFragment;
import com.cdqf.cart_fragment.MyFragment;
import com.cdqf.cart_fragment.ReoprtFragment;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    private String TAG = MainActivity.class.getSimpleName();

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

    //报表
    @BindView(R.id.ll_main_report)
    public LinearLayout llMainReport = null;

    @BindView(R.id.iv_main_report)
    public ImageView ivMainReport = null;

    @BindView(R.id.tv_main_report)
    public TextView tvMainReport = null;

//    //扫一扫
//    @BindView(R.id.ll_main_scan)
//    public LinearLayout llMainScan = null;
//
//    @BindView(R.id.iv_main_scan)
//    public ImageView ivMainScan = null;
//
//    @BindView(R.id.tv_main_scan)
//    public TextView tvMainScane = null;

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
    private HomeFragment homeFragment = null;
    private ReoprtFragment reoprtFragment = null;
    private MyFragment myFragment = null;

    //相机
    private static final int REQUEST_CODE_TAKE_PICTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_main);

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
                //报表
                ivMainReport.setImageResource(R.mipmap.main_tab_report_default);
                tvMainReport.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                //我的
                ivMainMy.setImageResource(R.mipmap.main_tab_my_default);
                tvMainMy.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_main_fragment, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            //报表
            case 1:
                //首页
                ivMainHome.setImageResource(R.mipmap.main_tab_home_default);
                tvMainHome.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                //报表
                ivMainReport.setImageResource(R.mipmap.main_tab_report_icon);
                tvMainReport.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_icon));
                //我的
                ivMainMy.setImageResource(R.mipmap.main_tab_my_default);
                tvMainMy.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                if (reoprtFragment == null) {
                    reoprtFragment = new ReoprtFragment();
                    transaction.add(R.id.fl_main_fragment, reoprtFragment);
                } else {
                    transaction.show(reoprtFragment);
                }
                break;
            //我的
            case 2:
                //首页
                ivMainHome.setImageResource(R.mipmap.main_tab_home_default);
                tvMainHome.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                //报表
                ivMainReport.setImageResource(R.mipmap.main_tab_report_default);
                tvMainReport.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
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
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (reoprtFragment != null) {
            transaction.hide(reoprtFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
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

    private Uri photoUri = null;

    @OnClick({R.id.ll_main_home, R.id.ll_main_report, R.id.ll_main_my})
    public void onClick(View v) {
        switch (v.getId()) {
            //首页
            case R.id.ll_main_home:
                tabImage(0);
                break;
            //报表
            case R.id.ll_main_report:
                tabImage(1);
                break;
            //我的
            case R.id.ll_main_my:
                tabImage(2);
                break;
//            //扫一扫
//            case R.id.ll_main_scan:
//                if (Build.VERSION.SDK_INT >= 23) {
//                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 8);
//                    } else {
//                        camera();
//                    }
//                } else {
//                    camera();
//                }
//                break;
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
    @Subscribe
    public void onEventMainThread(ExitFind r) {
        exit();
    }

    /**
     * 扫一扫
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ScanFind s) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 8);
            } else {
                camera();
            }
        } else {
            camera();
        }
    }
}
