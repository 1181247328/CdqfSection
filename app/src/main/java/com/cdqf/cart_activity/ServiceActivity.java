package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.ServiceAdapter;
import com.cdqf.cart_class.Shop;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.AssistantShopFind;
import com.cdqf.cart_find.ServiceOneFind;
import com.cdqf.cart_find.ServiceTwoFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.ACache;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 服务(员工)
 */
public class ServiceActivity extends BaseActivity {
    private String TAG = ServiceActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_service_pull)
    public VerticalSwipeRefreshLayout srlServicePull = null;

    //帐户
    @BindView(R.id.rl_service_return)
    public RelativeLayout rlServiceReturn = null;

    @BindView(R.id.lv_service_list)
    public ListViewForScrollView lvServiceList = null;

    @BindView(R.id.tv_service_no)
    public TextView tvServiceNo = null;

    private ServiceAdapter serviceAdapter = null;

    private ACache aCache = null;

    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvServiceList.setVisibility(View.VISIBLE);
                    tvServiceNo.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvServiceList.setVisibility(View.GONE);
                    tvServiceNo.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_service);

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
        aCache = ACache.get(context);
        ButterKnife.bind(this);
    }

    private void initView() {
        serviceAdapter = new ServiceAdapter(context);
        lvServiceList.setAdapter(serviceAdapter);
    }

    private void initAdapter() {

    }

    private void initListener() {
        srlServicePull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        if (aCache.getAsString("shops") != null) {
            String data = aCache.getAsString("shops");
            cartState.getShopList().clear();
            List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
            }.getType());
            cartState.setShopList(routeList);
            if (serviceAdapter != null) {
                serviceAdapter.notifyDataSetChanged();
            }
            scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    initPullData(false);
                }
            }, 2, 2, TimeUnit.SECONDS);
        } else {
            initPull(true);
        }
    }

    private void initPullData(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        String shop = shop(cartState.getUser().getShopid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(shop, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse服务---" + response);
                if (srlServicePull != null) {
                    srlServicePull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        if (TextUtils.equals(data, "2222")) {
                            handler.sendEmptyMessage(0x002);
                            return;
                        }
                        handler.sendEmptyMessage(0x001);
                        aCache.put("shops", data);
                        cartState.getShopList().clear();
                        List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
                        }.getType());
                        cartState.setShopList(routeList);
                        if (serviceAdapter != null) {
                            serviceAdapter.notifyDataSetChanged();
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

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        String shop = shop(cartState.getUser().getShopid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(shop, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse服务---" + response);
                if (srlServicePull != null) {
                    srlServicePull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        if (TextUtils.equals(data, "2222")) {
                            handler.sendEmptyMessage(0x002);
                            return;
                        }
                        handler.sendEmptyMessage(0x001);
                        aCache.put("shops", data);
                        cartState.getShopList().clear();
                        List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
                        }.getType());
                        cartState.setShopList(routeList);
                        if (serviceAdapter != null) {
                            serviceAdapter.notifyDataSetChanged();
                        }
                        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

                            @Override
                            public void run() {

                                Log.e(TAG, "---次---");
                                initPullData(false);
                            }
                        }, 2, 2, TimeUnit.SECONDS);
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

    private String shop(String shopid) {
        String result = null;
        result = CartAddaress.ADDRESS + "/?s=order.staff&shopid=" + shopid;
        Log.e(TAG, "---店总---" + result);
        return result;
    }

    //服务订单
    private String shopService(String ordernumb, String staffid) {
        String result = null;
        result = CartAddaress.ADDRESS + "/?s=Service.setservice&ordernum=" + ordernumb + "&staffid=" + staffid;
        Log.e(TAG, "---服务---" + result);
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_service_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case R.id.rl_service_return:
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
     * 预加载得到的数据
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onEvent(AssistantShopFind s) {
        Log.e(TAG, "---预加载得到的数据---" + s.shop + "---是否加载了---" + s.isShop);
        if (s.isShop) {
            aCache.put("shops", s.shop);
            cartState.getShopList().clear();
            List<Shop> routeList = gson.fromJson(s.shop, new TypeToken<List<Shop>>() {
            }.getType());
            cartState.setShopList(routeList);
            if (serviceAdapter != null) {
                serviceAdapter.notifyDataSetChanged();
            }
            scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    Log.e(TAG, "---次---");
                    initPullData(false);
                }
            }, 2, 2, TimeUnit.SECONDS);
        } else {
            initPullData(true);
        }
    }

    /**
     * 服务第一次，用于提示
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ServiceOneFind s) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(4, "提示", "是否领取车牌号为" + cartState.getShopList().get(s.position).getCarnum() + "的订单.", "否", "是", s.position);
        whyDilogFragment.show(getSupportFragmentManager(), "领取订单");
    }

    /**
     * 服务第二次
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ServiceTwoFind s) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String shopService = shopService(cartState.getShopList().get(s.position).getOrdernum(), cartState.getUser().getId());
        okHttpRequestWrap.post(shopService, true, "领取中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse完成---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int ret = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (ret) {
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, "接单成功", true, 0);
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
}
