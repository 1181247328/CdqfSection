package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.ShopFragmentAdapter;
import com.cdqf.cart_find.AddEmployessPullFind;
import com.cdqf.cart_find.AllEmployeesPullFind;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_find.EmployeesPullFind;
import com.cdqf.cart_find.VacationPullFind;
import com.cdqf.cart_find.WorkPullFind;
import com.cdqf.cart_fragment.AddEmployeesFragment;
import com.cdqf.cart_fragment.AllEmployeesFragment;
import com.cdqf.cart_fragment.VacationFragment;
import com.cdqf.cart_fragment.WorkFragment;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ViewPageSwipeRefreshLayout;
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

    @BindView(R.id.srl_shop_pull)
    public ViewPageSwipeRefreshLayout srlShopPull = null;

    //返回
    @BindView(R.id.rl_employeess_return)
    public RelativeLayout rlEmployeessReturn = null;

    @BindView(R.id.ti_employees_dicatior)
    public TabIndicator tiEmployeesDicatior = null;

    @BindView(R.id.vp_employees_screen)
    public ViewPager vpEmployeesScreen = null;

    private ShopFragmentAdapter shopFragmentAdapter = null;

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_employees);

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
    }

    private void initView() {

    }

    private void initAdapter() {
        shopFragmentAdapter = new ShopFragmentAdapter(getSupportFragmentManager(), orderList);
        vpEmployeesScreen.setAdapter(shopFragmentAdapter);
        vpEmployeesScreen.setOffscreenPageLimit(1);
    }

    private void initListener() {
        tiEmployeesDicatior.setViewPagerSwitchSpeed(vpEmployeesScreen, 600);
        tiEmployeesDicatior.setTabData(vpEmployeesScreen, orderName, new TabIndicator.TabClickListener() {
            @Override
            public void onClick(int i) {
                Log.e(TAG, "---当前为1---" + i);
                position = i;
                srlShopPull.setEnabled(true);
                if (position == 3) {
                    Log.e(TAG, "---当前为3---" + position);
                    srlShopPull.setEnabled(false);
                }
                vpEmployeesScreen.setCurrentItem(i);
            }
        });

        vpEmployeesScreen.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.e(TAG, "---当前为2---" + i);
                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        srlShopPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (position) {
                    case 0:
                        //全部
                        eventBus.post(new AllEmployeesPullFind());
                        break;
                    case 1:
                        //上班中
                        eventBus.post(new WorkPullFind());
                        break;
                    case 2:
                        //休假
                        eventBus.post(new VacationPullFind());
                        break;
                    case 3:
                        //添加员工
                        eventBus.post(new AddEmployessPullFind());
                        break;
                }
            }
        });
    }

    private void initBack() {
        srlShopPull.setEnabled(true);
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
        vpEmployeesScreen.setCurrentItem(r.position);
    }

    /**
     * 是否刷新和禁用
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(EmployeesPullFind s) {
        Log.e(TAG, "---是否刷新和禁用---" + s.isRefreshing + "---" + s.isEnabled);
        srlShopPull.setRefreshing(s.isRefreshing);
        srlShopPull.setEnabled(s.isEnabled);
    }
}
