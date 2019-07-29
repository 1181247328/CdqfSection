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
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.ShopFragmentAdapter;
import com.cdqf.cart_find.CompletePullFind;
import com.cdqf.cart_find.EntryPullFind;
import com.cdqf.cart_find.ServicePullFind;
import com.cdqf.cart_find.ShopPositionFind;
import com.cdqf.cart_find.SwipePullFind;
import com.cdqf.cart_fragment.CompleteFragment;
import com.cdqf.cart_fragment.EntryFragment;
import com.cdqf.cart_fragment.ServiceFragment;
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
 * 店里的服务之类
 */
public class ShopActivity extends BaseActivity {
    private String TAG = ShopActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    private Fragment[] orderList = new Fragment[]{
            new ServiceFragment(),
            new CompleteFragment(),
            new EntryFragment(),
    };

    private List<String> orderName = Arrays.asList("待服务", "已完成", "待付款");

    //刷新器
    @BindView(R.id.srl_shop_pull)
    public ViewPageSwipeRefreshLayout srlShopPull = null;

    //返回
    @BindView(R.id.rl_shop_return)
    public RelativeLayout rlShopReturn = null;

    //录入
    @BindView(R.id.tv_shop_entry)
    public TextView tvShopEntry = null;

    @BindView(R.id.tv_shop_name)
    public TextView tvShopName = null;

    @BindView(R.id.ti_shop_dicatior)
    public TabIndicator tiShopDicatior = null;

    @BindView(R.id.vp_shop_screen)
    public ViewPager vpShopScreen = null;

    private ShopFragmentAdapter shopFragmentAdapter = null;

    //哪个碎片要刷新
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_shop);

        StaturBar.setStatusBar(this, R.color.tab_main_text_icon);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        ButterKnife.bind(this);
    }

    private void initView() {

    }

    private void initAdapter() {
        shopFragmentAdapter = new ShopFragmentAdapter(getSupportFragmentManager(), orderList);
        vpShopScreen.setAdapter(shopFragmentAdapter);
        vpShopScreen.setOffscreenPageLimit(1);
    }

    private void initListener() {
        tiShopDicatior.setViewPagerSwitchSpeed(vpShopScreen, 600);
        tiShopDicatior.setTabData(vpShopScreen, orderName, new TabIndicator.TabClickListener() {
            @Override
            public void onClick(int i) {
                position = i;
                vpShopScreen.setCurrentItem(i);
            }
        });

        vpShopScreen.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
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
                        //待服务
                        eventBus.post(new ServicePullFind(false));
                        break;
                    case 1:
                        //已完成
                        eventBus.post(new CompletePullFind(false));
                        break;
                    case 2:
                        //录入
                        eventBus.post(new EntryPullFind(false));
                        break;
                }
            }
        });
    }

    private void initBack() {
        tvShopName.setText(cartState.getUser().getShopName());
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    /**
     * 录入
     *
     * @param activity
     */
    private void initIntentOrder(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("type", 2);
        startActivity(intent);
    }


    @OnClick({R.id.rl_shop_return, R.id.tv_shop_entry})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shop_return:
                finish();
                break;
            //录入
            case R.id.tv_shop_entry:
                initIntentOrder(AddOrderActivity.class);
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
     * 用于通知修改页
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShopPositionFind s) {
        position = s.position;
        vpShopScreen.setCurrentItem(s.position);
    }

    /**
     * 是否刷新和禁用
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(SwipePullFind s) {
        srlShopPull.setRefreshing(s.isRefreshing);
        srlShopPull.setEnabled(s.isEnabled);
    }
}

