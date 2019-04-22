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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.LossManagerAdapter;
import com.cdqf.cart_class.Notice;
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

    private LossManagerAdapter lossManagerAdapter = null;

    //领取的数量
    private int number = 0;

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
                srlLossPull.setRefreshing(false);
                initPull(false);
            }
        });
    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String lossShop = lossShop();
        okHttpRequestWrap.post(lossShop, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse通知---" + response);
                if (srlLossPull != null) {
                    srlLossPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.getNoticeList().clear();
                        List<Notice> noticeList = gson.fromJson(data, new TypeToken<List<Notice>>() {
                        }.getType());
                        cartState.setNoticeList(noticeList);
                        if (lossManagerAdapter != null) {
                            lossManagerAdapter.notifyDataSetChanged();
                        }
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

    private String lossShop() {
        String result = null;
//        CartAddaress.SHOP_LOSS = CartAddaress.SHOP_LOSS.replace("STAFFID", cartState.urlEnodeUTF8(staffid));
        result = CartAddaress.SHOP_NOTICE;
        Log.e(TAG, "---损耗品---" + result);
        return result;
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
    public void onEventMainThread(LossManagerOneFind r) {
        LossDilogFragment lossDilogFragment = new LossDilogFragment();
        lossDilogFragment.number(1, 5);
        lossDilogFragment.show(getSupportFragmentManager(), "领取数量");
    }

    /**
     * 数量
     *
     * @param l
     */
    public void onEventMainThread(LossManagerNumberFind l) {
        number = l.number;
    }

    /**
     * 确定认领取第一次
     *
     * @param l
     */
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
    public void onEventMainThread(LossReceiveSubmitFind l) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
//        String lossShop = lossShop();
//        okHttpRequestWrap.post(lossShop, true, "请稍候", params, new OnHttpRequest() {
//            @Override
//            public void onOkHttpResponse(String response, int id) {
//                Log.e(TAG, "---onOkHttpResponse通知---" + response);
//                if (srlLossPull != null) {
//                    srlLossPull.setRefreshing(false);
//                }
//                JSONObject resultJSON = JSON.parseObject(response);
//                int error_code = resultJSON.getInteger("ret");
//                String msg = resultJSON.getString("msg");
//                switch (error_code) {
//                    //获取成功
//                    case 200:
//                        String data = resultJSON.getString("data");
//                        break;
//                    default:
//                        cartState.initToast(context, msg, true, 0);
//                        break;
//                }
//            }
//
//            @Override
//            public void onOkHttpError(String error) {
//                Log.e(TAG, "---onOkHttpError---" + error);
//            }
//        });
    }
}
