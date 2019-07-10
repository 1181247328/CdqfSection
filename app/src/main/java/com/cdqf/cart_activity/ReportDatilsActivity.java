package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.DatilsAdapter;
import com.cdqf.cart_find.ServiceTwoFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.PieChartView;
import com.google.gson.Gson;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 *
 */
public class ReportDatilsActivity extends BaseActivity {
    private String TAG = ReportDatilsActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_datils_pull)
    public SwipeRefreshLayout srlDatilsPull = null;

    @BindView(R.id.ptrl_datils_pull)
    public PullToRefreshLayout ptrlDatilsPull = null;

    private ScrollView svDatilsList = null;

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    //时间
    @BindView(R.id.tv_datils_timer)
    public TextView tvDatilsTimer = null;

    //下单次数
    @BindView(R.id.tv_datils_place)
    public TextView tvDatilsPlace = null;

    //服务次数
    @BindView(R.id.tv_datils_service)
    public TextView tvDatilsService = null;

    //比例
    @BindView(R.id.tv_datils_proportion)
    public TextView tvDatilsProportion = null;

    //比例率
    @BindView(R.id.tv_datils_rate)
    public TextView tvDatilsRate = null;

    //服务金额
    @BindView(R.id.tv_datils_price)
    public TextView tvDatilsPrice = null;

    //提成
    @BindView(R.id.tv_datils_commission)
    public TextView tvDatilsCommission = null;

    //饼图
    @BindView(R.id.pcv_datils_bread)
    public PieChartView pcvDatilsBread = null;

    //平台
    @BindView(R.id.tv_datils_platform)
    public TextView tvDatilsPlatform = null;

    //现金
    @BindView(R.id.tv_datils_cash)
    public TextView tvDatilsCash = null;

    //农商
    @BindView(R.id.tv_datils_agri)
    public TextView tvDatilsAgri = null;

    //下单明细
    @BindView(R.id.lvfsv_datils_list)
    public ListViewForScrollView lvfsvDatilsList = null;

    private DatilsAdapter datilsAdapter = null;

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
        setContentView(R.layout.activity_reportdatils);

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
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        ButterKnife.bind(this);
    }

    private void initView() {
        svDatilsList = (ScrollView) ptrlDatilsPull.getPullableView();
    }

    private void initAdapter() {
        datilsAdapter = new DatilsAdapter(context);
        lvfsvDatilsList.setAdapter(datilsAdapter);
    }

    private void initListener() {

    }

    private void initBack() {

    }


    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_datils_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_datils_return:
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
    public void onEventMainThread(ServiceTwoFind s) {

    }
}