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
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.AuditPullFind;
import com.cdqf.cart_find.RefusedFind;
import com.cdqf.cart_find.RefusedTwoFind;
import com.cdqf.cart_find.SwipePullFind;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_find.ThroughPullFind;
import com.cdqf.cart_find.ThroughTwoFind;
import com.cdqf.cart_find.WithdrawPullFind;
import com.cdqf.cart_fragment.AuditsFragment;
import com.cdqf.cart_fragment.ThroughsFragment;
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
 * 审核
 */
public class AuditActivity extends BaseActivity {
    private String TAG = AuditActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    private Fragment[] orderList = new Fragment[]{
            new AuditsFragment(),
            new ThroughsFragment(),
//            new WithdrawsFragment(),
    };

    private List<String> orderName = Arrays.asList("待审批", "已通过");

    @BindView(R.id.srl_audit_pull)
    public ViewPageSwipeRefreshLayout srlAuditPull = null;

    //帐户
    @BindView(R.id.rl_audit_return)
    public RelativeLayout rlAuditReturn = null;

    @BindView(R.id.ti_audit_dicatior)
    public TabIndicator tiAuditDicatior = null;

    @BindView(R.id.vp_audit_screen)
    public ViewPager vpAuditScreen = null;

    private ShopFragmentAdapter shopFragmentAdapter = null;

    //哪个碎片要刷新
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_audit);

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
        imageLoader = cartState.getImageLoader(context);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {
        shopFragmentAdapter = new ShopFragmentAdapter(getSupportFragmentManager(), orderList);
        vpAuditScreen.setAdapter(shopFragmentAdapter);
        vpAuditScreen.setOffscreenPageLimit(3);
    }

    private void initListener() {

        tiAuditDicatior.setViewPagerSwitchSpeed(vpAuditScreen, 600);
        tiAuditDicatior.setTabData(vpAuditScreen, orderName, new TabIndicator.TabClickListener() {
            @Override
            public void onClick(int i) {
                position = i;
                vpAuditScreen.setCurrentItem(i);
            }
        });
        srlAuditPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (position) {
                    case 0:
                        //待审批
                        eventBus.post(new AuditPullFind());
                        break;
                    case 1:
                        //已通过
                        eventBus.post(new ThroughPullFind());
                        break;
                    case 2:
                        //已撤回
                        eventBus.post(new WithdrawPullFind());
                        break;
                }
                srlAuditPull.setRefreshing(false);
            }
        });
        vpAuditScreen.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

    private void initBack() {

    }


    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_audit_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_audit_return:
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

    /**
     * 是否刷新和禁用
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(SwipePullFind s) {
        srlAuditPull.setRefreshing(s.isRefreshing);
        srlAuditPull.setEnabled(s.isEnabled);
    }

    /**
     * 通过第一次
     *
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughFind t) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(8, "提示", "是否通过" + cartState.getAuditList().get(t.position).getName() + "申请" + cartState.getAuditList().get(t.position).getGoods_name() + "的请求", "否", "是", t.position);
        whyDilogFragment.show(getSupportFragmentManager(), "通过");
    }

    /**
     * 通过第二次
     *
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughTwoFind t) {

    }

    /**
     * 拒绝第一次
     *
     * @param t
     */
    @Subscribe
    public void onEventMainThread(RefusedFind t) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(9, "提示", "是否拒绝" + cartState.getAuditList().get(t.position).getName() + "申请" + cartState.getAuditList().get(t.position).getGoods_name() + "的请求", "否", "是", t.position);
        whyDilogFragment.show(getSupportFragmentManager(), "拒绝");
    }

    /**
     * 拒绝第二次
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(RefusedTwoFind r) {

    }
}

