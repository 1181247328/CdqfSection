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
import com.cdqf.cart_activity.AccountDatilsActivity;
import com.cdqf.cart_adapter.WithdrawAccountAdapter;
import com.cdqf.cart_class.Withdraw;
import com.cdqf.cart_find.AccountPullFind;
import com.cdqf.cart_find.WithdrawPullFind;
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
 * 已撤回
 */
public class WithdrawFragment extends Fragment {

    private String TAG = WithdrawFragment.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    private View view = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.ptrl_service_pull)
    public PullToRefreshLayout ptrlServicePull = null;

    private ListView lvServiceList = null;

    private WithdrawAccountAdapter withdrawAccountAdapter = null;

    private int page = 1;

    private boolean isPull = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "---创建---");

        view = inflater.inflate(R.layout.fragment_audit, null);

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
                initIntent(AccountDatilsActivity.class, position);
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
                params.put("staff_id", cartState.getUser().getId());
                //3=已撤回
                params.put("status", 3);
                //页码
                params.put("page", page);
                okHttpRequestWrap.post(CartAddaress.RECORD, false, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---已撤回---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("code");
                        String msg = resultJSON.getString("message");
                        switch (error_code) {
                            //获取成功
                            case 204:
                            case 201:
                            case 200:
                                page++;
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                JSONObject data = resultJSON.getJSONObject("data");
                                String datas = data.getString("data");
                                cartState.initToast(getContext(), msg, true, 0);
                                eventBus.post(new AccountPullFind(false, true));
                                List<Withdraw> withdrawList = gson.fromJson(datas, new TypeToken<List<Withdraw>>() {
                                }.getType());
                                cartState.getWithdrawList().addAll(withdrawList);

                                if (withdrawAccountAdapter != null) {
                                    withdrawAccountAdapter.notifyDataSetChanged();
                                }
                                break;
                            default:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                eventBus.post(new AccountPullFind(false, true));
                                cartState.initToast(getContext(), msg, true, 0);
                                break;
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        Log.e(TAG, "---onOkHttpError---" + error);
                        eventBus.post(new AccountPullFind(false, false));
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
                    if(isPull) {
                        eventBus.post(new AccountPullFind(false, true));
                    } else {
                        eventBus.post(new AccountPullFind(false, false));
                    }
                } else {
                    eventBus.post(new AccountPullFind(false, false));
                }
            }
        });
    }

    private void initAdapter() {
        withdrawAccountAdapter = new WithdrawAccountAdapter(getContext());
        lvServiceList.setAdapter(withdrawAccountAdapter);
    }

    private void initBack() {
        ptrlServicePull.setPullDownEnable(false);
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        params.put("staff_id", cartState.getUser().getId());
        //3=已撤回
        params.put("status", 3);
        //页码
        params.put("page", page);
        okHttpRequestWrap.post(CartAddaress.RECORD, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---已撤回---" + response);
                isPull = true;
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        rlOrdersBar.setVisibility(View.GONE);
                        ptrlServicePull.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        page = 2;
                        JSONObject data = resultJSON.getJSONObject("data");
                        String datas = data.getString("data");
                        cartState.getWithdrawList().clear();
                        cartState.initToast(getContext(), msg, true, 0);
                        eventBus.post(new AccountPullFind(false, true));
                        List<Withdraw> withdrawList = gson.fromJson(datas, new TypeToken<List<Withdraw>>() {
                        }.getType());
                        cartState.setWithdrawList(withdrawList);
                        if (cartState.getWithdrawList().size() <= 0) {
                            rlOrdersBar.setVisibility(View.GONE);
                            ptrlServicePull.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.VISIBLE);;
                        }
                        if (withdrawAccountAdapter != null) {
                            withdrawAccountAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        ptrlServicePull.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        eventBus.post(new AccountPullFind(false, true));
                        cartState.initToast(getContext(), msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                rlOrdersBar.setVisibility(View.GONE);
                ptrlServicePull.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                Log.e(TAG, "---onOkHttpError---" + error);
                eventBus.post(new AccountPullFind(false, true));
            }
        });
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(getContext(), activity);
        intent.putExtra("position", position);
        intent.putExtra("type", 3);
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
    public void onEventMainThread(WithdrawPullFind s) {
        initPull(false);
//        Map<String, Object> params = new HashMap<String, Object>();
//        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
//        params.put("", "");
//        okHttpRequestWrap.post("", false, "请稍候", params, new OnHttpRequest() {
//            @Override
//            public void onOkHttpResponse(String response, int id) {
//                Log.e(TAG, "---onOkHttpResponse---待服务---" + response);
//                eventBus.post(new SwipePullFind(false, false));
//            }
//
//            @Override
//            public void onOkHttpError(String error) {
//                Log.e(TAG, "---onOkHttpError---" + error);
//                eventBus.post(new SwipePullFind(false, false));
//            }
//        });
    }
}