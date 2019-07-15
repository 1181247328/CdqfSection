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
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.CompletePullFind;
import com.cdqf.cart_find.EntryPullFind;
import com.cdqf.cart_find.ServicePullFind;
import com.cdqf.cart_find.ShopOneFind;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_find.ShopServiceTwoFind;
import com.cdqf.cart_find.ShopTwoFind;
import com.cdqf.cart_find.ShopViscousFind;
import com.cdqf.cart_find.SwipePullFind;
import com.cdqf.cart_fragment.CompleteFragment;
import com.cdqf.cart_fragment.EntryFragment;
import com.cdqf.cart_fragment.ServiceFragment;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
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
import de.greenrobot.event.ThreadMode;

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
        vpShopScreen.setOffscreenPageLimit(3);
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

        srlShopPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (position) {
                    case 0:
                        //待服务
                        eventBus.post(new ServicePullFind());
                        break;
                    case 1:
                        //已完成
                        eventBus.post(new CompletePullFind());
                        break;
                    case 2:
                        //录入
                        eventBus.post(new EntryPullFind());
                        break;
                }
                srlShopPull.setRefreshing(false);
            }
        });

//        lvShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                initIntent(DatilsActivity.class, position);
//            }
//        });
//        lvShopList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                View firstView = view.getChildAt(firstVisibleItem);
//
//                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
//                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
//                    srlShopPull.setEnabled(true);
//                } else {
//                    //不加这个if判断会引起其它问题，大家自行尝试
//                    if (srlShopPull.isRefreshing()) {
//                        srlShopPull.setRefreshing(false);
//                    }
//                    srlShopPull.setEnabled(false);
//                }
//            }
//        });
    }

    private void initBack() {

//        if (aCache.getAsString("shop") != null) {
////            handler.sendEmptyMessage(0x001);
//            String data = aCache.getAsString("shop");
//            cartState.getShopList().clear();
//            List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
//            }.getType());
//            cartState.setShopList(routeList);
//            if (shopAdapter != null) {
//                shopAdapter.notifyDataSetChanged();
//            }
//            scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//
//                @Override
//                public void run() {
//                    Log.e(TAG, "---次---");
//                    initPullData(false);
//                }
//            }, 2, 2, TimeUnit.SECONDS);
//        } else {
//            initPull(true);
//        }
    }
//
//    private void initPullData(boolean isToast) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
//        String shop = shop(cartState.getUser().getShopid());
//        okHttpRequestWrap.post(shop, isToast, "请稍候", params, new OnHttpRequest() {
//            @Override
//            public void onOkHttpResponse(String response, int id) {
//                Log.e(TAG, "---onOkHttpResponse店总---" + response);
//                if (srlShopPull != null) {
//                    srlShopPull.setRefreshing(false);
//                }
//                JSONObject resultJSON = JSON.parseObject(response);
//                int error_code = resultJSON.getInteger("ret");
//                String msg = resultJSON.getString("msg");
//                switch (error_code) {
//                    //获取成功
//                    case 200:
//                        handler.sendEmptyMessage(0x001);
//                        String data = resultJSON.getString("data");
//                        aCache.put("shop", data);
//                        cartState.getShopList().clear();
//                        List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
//                        }.getType());
//                        cartState.setShopList(routeList);
//                        if (shopAdapter != null) {
//                            shopAdapter.notifyDataSetChanged();
//                        }
//                        break;
//                    default:
//                        handler.sendEmptyMessage(0x002);
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
//    }
//
//    private void initPull(boolean isToast) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
//        String shop = shop(cartState.getUser().getShopid());
//        okHttpRequestWrap.post(shop, isToast, "请稍候", params, new OnHttpRequest() {
//            @Override
//            public void onOkHttpResponse(String response, int id) {
//                Log.e(TAG, "---onOkHttpResponse店总---" + response);
//                if (srlShopPull != null) {
//                    srlShopPull.setRefreshing(false);
//                }
//                JSONObject resultJSON = JSON.parseObject(response);
//                int error_code = resultJSON.getInteger("ret");
//                String msg = resultJSON.getString("msg");
//                switch (error_code) {
//                    //获取成功
//                    case 200:
//                        handler.sendEmptyMessage(0x001);
//                        String data = resultJSON.getString("data");
//                        cartState.getShopList().clear();
//                        aCache.put("shop", data);
//                        List<Shop> routeList = gson.fromJson(data, new TypeToken<List<Shop>>() {
//                        }.getType());
//                        cartState.setShopList(routeList);
//                        if (shopAdapter != null) {
//                            shopAdapter.notifyDataSetChanged();
//                        }
//                        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                Log.e(TAG, "---次---");
//                                initPullData(false);
//                            }
//                        }, 2, 2, TimeUnit.SECONDS);
//                        break;
//                    default:
//                        handler.sendEmptyMessage(0x002);
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
//    }

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


    @OnClick({R.id.rl_shop_return, R.id.tv_shop_entry})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shop_return:
                finish();
                break;
            //录入
            case R.id.tv_shop_entry:
                initIntent(AddOrderActivity.class);
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
    public void onEvent(ShopViscousFind s) {
//        Log.e(TAG, "---预加载得到的数据---" + s.shop + "---是否加载了---" + s.isShop);
//        if (s.isShop) {
////            handler.sendEmptyMessage(0x001);
//            cartState.getShopList().clear();
//            aCache.put("shop", s.shop);
//            List<Shop> routeList = gson.fromJson(s.shop, new TypeToken<List<Shop>>() {
//            }.getType());
//            cartState.setShopList(routeList);
//            if (shopAdapter != null) {
//                shopAdapter.notifyDataSetChanged();
//            }
//            scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//
//                @Override
//                public void run() {
//                    Log.e(TAG, "---次---");
//                    initPullData(false);
//                }
//            }, 2, 2, TimeUnit.SECONDS);
//        } else {
//            initPullData(true);
//        }
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
//        Map<String, Object> params = new HashMap<String, Object>();
//        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
//        String shopService = shopService(cartState.getShopList().get(s.position).getOrdernum(), cartState.getUser().getId());
//        okHttpRequestWrap.post(shopService, true, "领取中", params, new OnHttpRequest() {
//            @Override
//            public void onOkHttpResponse(String response, int id) {
//                Log.e(TAG, "---onOkHttpResponse完成---" + response);
//                JSONObject resultJSON = JSON.parseObject(response);
//                int ret = resultJSON.getInteger("ret");
//                String msg = resultJSON.getString("msg");
//                switch (ret) {
//                    case 200:
//                        String data = resultJSON.getString("data");
//                        initPull(true);
//                        cartState.initToast(context, "接单成功", true, 0);
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

    /**
     * 完成第一次，用于提示
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(ShopOneFind r) {
//        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
//        whyDilogFragment.setInit(0, "提示", "是否完成车牌号为" + cartState.getShopList().get(r.position).getCarnum() + "的订单.", "否", "是", r.position);
//        whyDilogFragment.show(getSupportFragmentManager(), "完成订单");
    }

    /**
     * 完成第二次
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(ShopTwoFind r) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
//        String shopYes = shopYes(cartState.getShopList().get(r.position).getId(), 3);
//        okHttpRequestWrap.post(shopYes, true, "完成中", params, new OnHttpRequest() {
//            @Override
//            public void onOkHttpResponse(String response, int id) {
//                Log.e(TAG, "---onOkHttpResponse完成---" + response);
//                JSONObject resultJSON = JSON.parseObject(response);
//                int ret = resultJSON.getInteger("ret");
//                String msg = resultJSON.getString("msg");
//                switch (ret) {
//                    case 200:
//                        String data = resultJSON.getString("data");
//                        if (TextUtils.equals(data, "1")) {
//                            initPull(true);
//                        } else {
//                            //TODO
//                        }
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

