package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 考勤打卡
 */
public class ClockActivity extends BaseActivity {
    private String TAG = ClockActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_clock_pull)
    public SwipeRefreshLayout srlClockPull = null;

    @BindView(R.id.sv_clock_sc)
    public ScrollView svClockSc = null;

    //返回
    @BindView(R.id.rl_clock_return)
    public RelativeLayout rlClockReturn = null;

    //头像
    @BindView(R.id.iv_clock_hear)
    public ImageView ivClockHear = null;

    //姓名
    @BindView(R.id.tv_clock_name)
    public TextView tvClockName = null;

    //所属店
    @BindView(R.id.tv_clock_address)
    public TextView tvClockAddress = null;

    //时间
    @BindView(R.id.tv_clock_timer)
    public TextView tvClockTimer = null;

    @BindView(R.id.rcrl_clock_state)
    public RCRelativeLayout rcrlClockState = null;

    //状态
    @BindView(R.id.tv_clock_state)
    public TextView tvClockState = null;

    //状态时间
    @BindView(R.id.tv_clock_statetimer)
    public TextView tvClockStateTimer = null;

    //范围
    @BindView(R.id.tv_clock_scope)
    public TextView tvClockScope = null;

    //重新定位
    @BindView(R.id.tv_clock_positioning)
    public TextView tvClockPositioning = null;

    //固定上班时间
    @BindView(R.id.tv_clock_fixed)
    public TextView tvClockFixed = null;

    //上班打卡时间
    @BindView(R.id.tv_clock_workon)
    public TextView tvClockWorkon = null;

    //上班打卡地点
    @BindView(R.id.tv_clock_addresson)
    public TextView tvClockAddresson = null;

    //上班打卡图片
    @BindView(R.id.rcrl_clock_imageon)
    public RCRelativeLayout rcrlClockImageon = null;
    @BindView(R.id.iv_clock_imageon)
    public ImageView ivClockImageon = null;

    //固定下班时间
    @BindView(R.id.tv_clock_after)
    public TextView tvClockAfter = null;

    //下班打卡时间
    @BindView(R.id.tv_clock_fixedafterafter)
    public TextView tvClockFixedafterafter = null;

    //下班打卡地点
    @BindView(R.id.tv_clock_addressafter)
    public TextView tvClockAddressafter = null;

    //下班打卡图片
    @BindView(R.id.rcrl_clock_imageafter)
    public RCRelativeLayout rcrlClockImageafter = null;
    @BindView(R.id.iv_clock_imageafter)
    public ImageView ivClockImageafter = null;

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
        setContentView(R.layout.activity_clock);

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
        imageLoader = cartState.getImageLoader(context);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {
        srlClockPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlClockPull.setRefreshing(false);
            }
        });
    }

    private void initBack() {
        svClockSc.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                srlClockPull.setEnabled(svClockSc.getScrollY() == 0);
            }
        });
        initPull(true);
    }

    private void initPull(boolean isToast) {
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_clock_return, R.id.rcrl_clock_state, R.id.tv_clock_positioning, R.id.rcrl_clock_imageon})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_clock_return:
                finish();
                break;
            case R.id.rcrl_clock_state:
                break;
            //重新定位
            case R.id.tv_clock_positioning:
                initIntent(AgainActivity.class);
                break;
            //上班打卡图片
            case R.id.rcrl_clock_imageon:
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
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughFind t) {

    }

}