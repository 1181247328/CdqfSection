package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.cdqf.cart_fragment.HomeFragment;
import com.cdqf.cart_fragment.MyFragment;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

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
    private HomeFragment homeFragment = null;
    private MyFragment myFragment = null;

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

                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_main_fragment, homeFragment);
                } else {
                    transaction.show(homeFragment);
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
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
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
