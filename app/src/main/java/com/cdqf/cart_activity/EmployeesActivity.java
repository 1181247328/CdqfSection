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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.EmployessAdapter;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.google.gson.Gson;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
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

    //返回
    @BindView(R.id.rl_employees_return)
    public RelativeLayout rlEmployeesReturn = null;

    //添加店员
    @BindView(R.id.tv_employess_add)
    public TextView tvEmployessAdd = null;

    @BindView(R.id.sp_employess_data)
    public ScrollablePanel spEmployessData = null;

    private EmployessAdapter employessAdapter = null;

    private List<String> title = Arrays.asList("工号", "姓名", "手机号", "职位", "状态");

    private List<String> shopOneList = Arrays.asList("W0001", "杨显澎", "13551021222", "店长", "上班");

    private List<String> shopTwoList = Arrays.asList("W0001", "杨显澎", "13551021222", "店长", "请假");

    private List<String> shopThreeList = Arrays.asList("W0001", "杨显澎", "13551021222", "店长", "离职");

    private List<String> shopFoueList = Arrays.asList("W0001", "杨显澎", "13551021222", "店长", "审核中");


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
        List<List<String>> data = new ArrayList<>();
        data.add(title);
        data.add(shopOneList);
        data.add(shopTwoList);
        data.add(shopThreeList);
        data.add(shopFoueList);
        for (int i = 0; i < 50; i++) {
            List<String> shopFiveList = Arrays.asList("W0001", "杨显澎", "13551021222", "店长", "休假");
            data.add(shopFiveList);
        }
        employessAdapter = new EmployessAdapter(data, context);
        spEmployessData.setPanelAdapter(employessAdapter);
    }

    private void initListener() {
    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_employees_return, R.id.tv_employess_add})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_employees_return:
                finish();
                break;
            //添加店员
            case R.id.tv_employess_add:
                initIntent(EmployeesAddActivity.class);
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
