package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.ShopFragmentAdapter;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_fragment.AddEmployeesFragment;
import com.cdqf.cart_fragment.AllEmployeesFragment;
import com.cdqf.cart_fragment.VacationFragment;
import com.cdqf.cart_fragment.WorkFragment;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhengsr.viewpagerlib.indicator.TabIndicator;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 店员管理
 */
public class EmployeesActivity extends BaseActivity {

    private String TAG = EmployeesActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    private Fragment[] orderList = new Fragment[]{
            new AllEmployeesFragment(),
            new WorkFragment(),
            new VacationFragment(),
            new AddEmployeesFragment(),
    };

    private List<String> orderName = Arrays.asList("全部", "上班中", "休假", "添加");

    //返回
    @BindView(R.id.rl_employeess_return)
    public RelativeLayout rlEmployeessReturn = null;

    @BindView(R.id.ti_employees_dicatior)
    public TabIndicator tiEmployeesDicatior = null;

    @BindView(R.id.vp_employees_screen)
    public ViewPager vpEmployeesScreen = null;

    private ShopFragmentAdapter shopFragmentAdapter = null;

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
        setContentView(R.layout.activity_employees);

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
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {
        shopFragmentAdapter = new ShopFragmentAdapter(getSupportFragmentManager(), orderList);
        vpEmployeesScreen.setAdapter(shopFragmentAdapter);
        vpEmployeesScreen.setOffscreenPageLimit(4);
    }

    private void initListener() {
        tiEmployeesDicatior.setViewPagerSwitchSpeed(vpEmployeesScreen, 600);
        tiEmployeesDicatior.setTabData(vpEmployeesScreen, orderName, new TabIndicator.TabClickListener() {
            @Override
            public void onClick(int i) {
                vpEmployeesScreen.setCurrentItem(i);
            }
        });
    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_employeess_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_employeess_return:
                finish();
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

    @Subscribe
    public void onEventMainThread(DatilsPullFind r) {

    }

}
