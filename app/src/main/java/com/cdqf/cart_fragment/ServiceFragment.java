package com.cdqf.cart_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_activity.DatilsActivity;
import com.cdqf.cart_adapter.ShopAdapter;
import com.cdqf.cart_class.Service;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.CompletePullFind;
import com.cdqf.cart_find.ServcieKeyFind;
import com.cdqf.cart_find.ServicePullFind;
import com.cdqf.cart_find.ServiceYesOneFind;
import com.cdqf.cart_find.ServiceYesTwoFind;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_find.ShopServiceTwoFind;
import com.cdqf.cart_find.SwipePullFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
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
 * 待服务
 */
public class ServiceFragment extends Fragment {

    private String TAG = ServiceFragment.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    private View view = null;

    private int page = 1;

    private String key = "";

    @BindView(R.id.ptrl_service_pull)
    public PullToRefreshLayout ptrlServicePull = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    private ListView lvServiceList = null;

    private ShopAdapter shopAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "---创建---");

        view = inflater.inflate(R.layout.fragment_service, null);

        initAgo();

        initView();

        initListener();

        initAdapter();

        initBack();

        return view;
    }

    private void initAgo() {
        ButterKnife.bind(this, view);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {
        lvServiceList = (ListView) ptrlServicePull.getPullableView();
    }

    private void initListener() {
        lvServiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "---" + position);
                initIntent(DatilsActivity.class, position - 1);
            }
        });

        ptrlServicePull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                Map<String, Object> params = new HashMap<String, Object>();
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
                //店铺id
                params.put("shop_id", cartState.getUser().getShopid());
                //关键字2 = 待服务
                params.put("type", "2");
                //页码
                params.put("page", page);
                okHttpRequestWrap.postString(CartAddaress.service, false, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---待服务之上拉加载---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("code");
                        String msg = resultJSON.getString("message");
                        switch (error_code) {
                            //获取成功
                            case 204:
                            case 201:
                            case 200:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                JSONObject data = resultJSON.getJSONObject("data");
                                String datas = data.getString("data");
                                cartState.initToast(getContext(), msg, true, 0);
                                page++;
                                List<Service> serviceList = gson.fromJson(datas, new TypeToken<List<Service>>() {
                                }.getType());
                                cartState.getServiceLis().addAll(serviceList);
                                if (serviceList.size() <= 0) {
                                    cartState.initToast(getContext(), "没有更多了", true, 0);
                                }
                                if (shopAdapter != null) {
                                    shopAdapter.notifyDataSetChanged();
                                }
                                break;
                            default:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                cartState.initToast(getContext(), msg, true, 0);
                                break;
                        }

                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        cartState.initToast(getContext(), error, true, 0);
                    }
                });
            }
        });
        lvServiceList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    eventBus.post(new SwipePullFind(false, true));
                } else {
                    eventBus.post(new SwipePullFind(false, false));
                }
            }
        });
    }

    private void initAdapter() {
        shopAdapter = new ShopAdapter(getContext());
        lvServiceList.setAdapter(shopAdapter);
    }

    private void initBack() {
        ptrlServicePull.setPullDownEnable(false);
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        //店铺id
        params.put("shop_id", cartState.getUser().getShopid());
        //2 = 待服务
        params.put("type", "2");
        //关键字
        params.put("keywords", key);
        //页码
        params.put("page", page);
        okHttpRequestWrap.postString(CartAddaress.service, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---待服务---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        page = 2;
                        rlOrdersBar.setVisibility(View.GONE);
                        ptrlServicePull.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        JSONObject data = resultJSON.getJSONObject("data");
                        String datas = data.getString("data");
                        cartState.getServiceLis().clear();
//                        cartState.initToast(getContext(), msg, true, 0);
                        eventBus.post(new SwipePullFind(false, true));
                        List<Service> serviceList = gson.fromJson(datas, new TypeToken<List<Service>>() {
                        }.getType());
                        cartState.setServiceLis(serviceList);
                        if (cartState.getServiceLis().size() <= 0) {
                            rlOrdersBar.setVisibility(View.GONE);
                            ptrlServicePull.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        }
                        if (shopAdapter != null) {
                            shopAdapter.notifyDataSetChanged();
                        }
                        cartState.closeKeyboard(getActivity());
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        ptrlServicePull.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        eventBus.post(new SwipePullFind(false, true));
                        cartState.initToast(getContext(), msg, true, 0);
                        break;
                }

            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                rlOrdersBar.setVisibility(View.GONE);
                ptrlServicePull.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                eventBus.post(new SwipePullFind(false, true));
            }
        });
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(getContext(), activity);
        intent.putExtra("type", 1);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @OnClick({})
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "---启动---");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        eventBus.unregister(this);
    }

    /**
     * 刷新
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ServicePullFind s) {
        page = 1;
        key = "";
        ptrlServicePull.setPullUpEnable(true);
        initPull(s.isToast);
    }

    /**
     * 是否使用关键字
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ServcieKeyFind s) {
        page = 1;
        key = s.key;
        initPull(true);
        ptrlServicePull.setPullUpEnable(false);
    }

    /**
     * 服务第一次，用于提示
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShopServiceOneFind s) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(1, "提示", "是否领取车牌号为" + cartState.getServiceLis().get(s.position - 1).getCarnum() + "的订单.", "否", "是", s.position - 1);
        whyDilogFragment.show(getFragmentManager(), "领取订单");
    }

    /**
     * 服务第二次
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShopServiceTwoFind s) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        params.put("order_num", cartState.getServiceLis().get(s.position).getOrdernum());
        params.put("staff_id", cartState.getUser().getId());
        okHttpRequestWrap.postString(CartAddaress.USER_SERVICE, true, "领取中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---服务---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int ret = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (ret) {
                    case 201:
                    case 204:
                    case 200:
                        String data = resultJSON.getString("data");
                        page = 1;
                        initPull(true);
                        cartState.initToast(getContext(), "接单成功", true, 0);
                        break;
                    default:
                        cartState.initToast(getContext(), msg, true, 0);
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
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ServiceYesOneFind s) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(24, "提示", "是否完成车牌号为" + cartState.getServiceLis().get(s.position - 1).getCarnum() + "的订单.", "否", "是", s.position - 1);
        whyDilogFragment.show(getFragmentManager(), "完成订单");
    }

    /**
     * 完成第二次
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ServiceYesTwoFind s) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        params.put("order_id", cartState.getServiceLis().get(s.position).getId());
        okHttpRequestWrap.postString(CartAddaress.USER_YES, true, "完成中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---完成中---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int ret = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (ret) {
                    case 201:
                    case 204:
                    case 200:
                        String data = resultJSON.getString("data");
                        eventBus.post(new CompletePullFind(false));
                        page = 1;
                        initPull(true);
                        cartState.initToast(getContext(), "完成订单", true, 0);
                        break;
                    default:
                        cartState.initToast(getContext(), msg, true, 0);
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