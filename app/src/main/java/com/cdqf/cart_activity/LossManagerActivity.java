package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.LossManagerAdapter;
import com.cdqf.cart_class.LossMan;
import com.cdqf.cart_dilog.LossDilogFragment;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.LossManagerNumberFind;
import com.cdqf.cart_find.LossManagerOneFind;
import com.cdqf.cart_find.LossManagerReceiveFind;
import com.cdqf.cart_find.LossReceiveSubmitFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 损耗品(店长)
 */
public class LossManagerActivity extends BaseActivity {
    private String TAG = LossManagerActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_loss_pull)
    public SwipeRefreshLayout srlLossPull = null;

    //帐户
    @BindView(R.id.rl_loss_return)
    public RelativeLayout rlLossReturn = null;

    @BindView(R.id.lv_loss_list)
    public ListView lvLossList = null;

    @BindView(R.id.tv_lossmanager_no)
    public TextView tvLossmanagerNo = null;

    private LossManagerAdapter lossManagerAdapter = null;

    //领取的数量
    private int number = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvLossList.setVisibility(View.VISIBLE);
                    tvLossmanagerNo.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvLossList.setVisibility(View.GONE);
                    tvLossmanagerNo.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

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
        setContentView(R.layout.activity_lossmanager);

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
        lossManagerAdapter = new LossManagerAdapter(context);
        lvLossList.setAdapter(lossManagerAdapter);
    }

    private void initAdapter() {

    }

    private void initListener() {

        srlLossPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("s", "TotalGoods.lists");
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.SHOP_TOTAL, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse耗材列表---" + response);
                if (srlLossPull != null) {
                    srlLossPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        handler.sendEmptyMessage(0x001);
                        String data = resultJSON.getString("data");
                        cartState.getLossManList().clear();
                        List<LossMan> lossManList = gson.fromJson(data, new TypeToken<List<LossMan>>() {
                        }.getType());
                        cartState.setLossManList(lossManList);
                        if (lossManagerAdapter != null) {
                            lossManagerAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        handler.sendEmptyMessage(0x002);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_loss_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case R.id.rl_loss_return:
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
     * 输入领取数量
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(LossManagerOneFind r) {
        LossDilogFragment lossDilogFragment = new LossDilogFragment();
        lossDilogFragment.number(1, r.position);
        lossDilogFragment.show(getSupportFragmentManager(), "领取数量");
    }

    /**
     * 数量
     *
     * @param l
     */
    @Subscribe
    public void onEventMainThread(LossManagerNumberFind l) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("s", "TotalGoods.receive");
        params.put("goods_id", cartState.getLossManList().get(l.position).getId());
        params.put("user_id", cartState.getUser().getId());
        params.put("number", l.number);
        params.put("shop_id", cartState.getUser().getShopid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.SHOP_RECEIVE, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse领取物品---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, "领取物品成功", true, 0);
                        initPull(true);
                        break;
                    default:
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
            }
        });
    }

    /**
     * 确定认领取第一次
     *
     * @param l
     */
    @Subscribe
    public void onEventMainThread(LossManagerReceiveFind l) {
        if (number <= 0) {
            cartState.initToast(context, "请选择领取数量", true, 0);
            return;
        }
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(5, "提示", "您正在领取毛巾" + number + "条", "否", "是");
        whyDilogFragment.show(getSupportFragmentManager(), "确定领取");
    }

    /**
     * 确定领取
     *
     * @param l
     */
    @Subscribe
    public void onEventMainThread(LossReceiveSubmitFind l) {
    }
}
