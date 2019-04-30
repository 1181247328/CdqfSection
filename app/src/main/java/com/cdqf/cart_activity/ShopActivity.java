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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.ShopAdapter;
import com.cdqf.cart_class.Shop;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.ShopOneFind;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_find.ShopServiceTwoFind;
import com.cdqf.cart_find.ShopTwoFind;
import com.cdqf.cart_find.ShopViscousFind;
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
 * 店总
 */
public class ShopActivity extends BaseActivity {
    private String TAG = ShopActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_shop_pull)
    public VerticalSwipeRefreshLayout srlShopPull = null;

    //帐户
    @BindView(R.id.rl_shop_return)
    public RelativeLayout rlShopReturn = null;

    @BindView(R.id.lv_shop_list)
    public ListViewForScrollView lvShopList = null;

    @BindView(R.id.tv_shop_no)
    public TextView tvShopNo = null;

    private ShopAdapter shopAdapter = null;

    private ACache aCache = null;

    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvShopList.setVisibility(View.VISIBLE);
                    tvShopNo.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvShopList.setVisibility(View.GONE);
                    tvShopNo.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_shop);

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

    }

    private void initAdapter() {
        shopAdapter = new ShopAdapter(context);
        lvShopList.setAdapter(shopAdapter);
    }

    private void initListener() {
        srlShopPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
        lvShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initIntent(DatilsActivity.class, position);
            }
        });
    }

    private void initBack() {
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        if (aCache.getAsString("shop") != null) {
//            handler.sendEmptyMessage(0x001);
            String data = aCache.getAsString("shop");
            cartState.getShopList().clear();
            List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
            }.getType());
            cartState.setShopList(routeList);
            if (shopAdapter != null) {
                shopAdapter.notifyDataSetChanged();
            }
            scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    Log.e(TAG, "---次---");
                    initPullData(false);
                }
            }, 2, 2, TimeUnit.SECONDS);
        } else {
            initPull(true);
        }
    }

    private void initPullData(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String shop = shop(cartState.getUser().getShopid());
        okHttpRequestWrap.post(shop, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse店总---" + response);
                if (srlShopPull != null) {
                    srlShopPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        handler.sendEmptyMessage(0x001);
                        String data = resultJSON.getString("data");
                        aCache.put("shop", data);
                        cartState.getShopList().clear();
                        List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
                        }.getType());
                        cartState.setShopList(routeList);
                        if (shopAdapter != null) {
                            shopAdapter.notifyDataSetChanged();
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
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String shop = shop(cartState.getUser().getShopid());
        okHttpRequestWrap.post(shop, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse店总---" + response);
                if (srlShopPull != null) {
                    srlShopPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        handler.sendEmptyMessage(0x001);
                        String data = resultJSON.getString("data");
                        cartState.getShopList().clear();
                        aCache.put("shop", data);
                        List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
                        }.getType());
                        cartState.setShopList(routeList);
                        if (shopAdapter != null) {
                            shopAdapter.notifyDataSetChanged();
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
        result = CartAddaress.ADDRESS + "/?s=order.shopowenr&shopid=" + shopid;
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

    //完成订单
    private String shopYes(String id, int number) {
        String result = null;
        result = CartAddaress.ADDRESS + "/?s=order.getordertype&id=" + id + "&number=" + number;
        Log.e(TAG, "---订单---" + result);
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        startActivity(intent);
    }


    @OnClick({R.id.rl_shop_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case R.id.rl_shop_return:
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
        eventBus.removeAllStickyEvents();
        eventBus.unregister(this);
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown();
        }
    }

    /**
     * 预加载得到的数据
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onEvent(ShopViscousFind s) {
        Log.e(TAG, "---预加载得到的数据---" + s.shop + "---是否加载了---" + s.isShop);
        if (s.isShop) {
//            handler.sendEmptyMessage(0x001);
            cartState.getShopList().clear();
            aCache.put("shop", s.shop);
            List<Shop> routeList = gson.fromJson(s.shop, new TypeToken<List<Shop>>() {
            }.getType());
            cartState.setShopList(routeList);
            if (shopAdapter != null) {
                shopAdapter.notifyDataSetChanged();
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
    public void onEventMainThread(ShopServiceOneFind s) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(1, "提示", "是否领取车牌号为" + cartState.getShopList().get(s.position).getCarnum() + "的订单.", "否", "是", s.position);
        whyDilogFragment.show(getSupportFragmentManager(), "领取订单");
    }

    /**
     * 服务第二次
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShopServiceTwoFind s) {
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
                        initPull(true);
                        cartState.initToast(context, "接单成功", true, 0);
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
     * 完成第一次，用于提示
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(ShopOneFind r) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(0, "提示", "是否完成车牌号为" + cartState.getShopList().get(r.position).getCarnum() + "的订单.", "否", "是", r.position);
        whyDilogFragment.show(getSupportFragmentManager(), "完成订单");
    }

    /**
     * 完成第二次
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(ShopTwoFind r) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String shopYes = shopYes(cartState.getShopList().get(r.position).getId(), 3);
        okHttpRequestWrap.post(shopYes, true, "完成中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse完成---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int ret = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (ret) {
                    case 200:
                        String data = resultJSON.getString("data");
                        if (TextUtils.equals(data, "1")) {
                            initPull(true);
                        } else {
                            //TODO
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
}

