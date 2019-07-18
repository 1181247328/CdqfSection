package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.MembershipAdapter;
import com.cdqf.cart_class.Memebersship;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
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
 * 会员总数和下单会员
 */
public class MemebershipActivity extends BaseActivity {

    private String TAG = MemebershipActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_membership_pull)
    public SwipeRefreshLayout srlMembershipPull = null;

    //返回
    @BindView(R.id.rl_membersship_return)
    public RelativeLayout rlMembersshipReturn = null;

    //标题
    @BindView(R.id.tv_membership_title)
    public TextView tvMembershipTitle = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.ptrl_membership_pull)
    public PullToRefreshLayout ptrlMembershipPull = null;

    private ListView lvMembershipList = null;

    private MembershipAdapter membershipAdapter = null;

    private int type = 0;

    private int shopId = 0;

    private int page = 1;

    private boolean isPull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_membership);

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
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        shopId = intent.getIntExtra("shopId", 0);
    }

    private void initView() {
        lvMembershipList = (ListView) ptrlMembershipPull.getPullableView();
    }

    private void initAdapter() {
        membershipAdapter = new MembershipAdapter(context);
        lvMembershipList.setAdapter(membershipAdapter);
    }

    private void initListener() {
        lvMembershipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initIntents(MembersDatilsActivity.class, position);
            }
        });
        lvMembershipList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    if (isPull) {
                        srlMembershipPull.setEnabled(true);
                    } else {
                        srlMembershipPull.setEnabled(false);
                    }
                } else {
                    srlMembershipPull.setEnabled(false);
                }
            }
        });
        srlMembershipPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initPull(false);
            }
        });

        ptrlMembershipPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                //上拉加载
                final Map<String, Object> params = new HashMap<String, Object>();
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                //页码
                params.put("page", page);
                okHttpRequestWrap.get(CartAddaress.MEMBERS_LIST + shopId, false, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---会员下单总数之上拉加载---" + response);
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
                                cartState.initToast(context, msg, true, 0);
                                List<Memebersship> memebersshipList = gson.fromJson(datas, new TypeToken<List<Memebersship>>() {
                                }.getType());
                                cartState.getMemebersshipList().addAll(memebersshipList);
                                if (membershipAdapter != null) {
                                    membershipAdapter.notifyDataSetInvalidated();
                                }
                                break;
                            default:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }

                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        cartState.initToast(context, error, true, 0);
                    }
                });
            }
        });
    }

    private void initBack() {
        ptrlMembershipPull.setPullDownEnable(false);
        srlMembershipPull.setEnabled(false);
        initPull(false);
    }

    private void initPull(boolean isToast) {
        final Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        params.put("page", page);
        okHttpRequestWrap.get(CartAddaress.MEMBERS_LIST + shopId, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---会员列表---" + response);
                if (srlMembershipPull != null) {
                    isPull = true;
                    srlMembershipPull.setEnabled(true);
                    srlMembershipPull.setRefreshing(false);
                }
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
                        ptrlMembershipPull.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, msg, true, 0);
                        cartState.getMemebersshipList().clear();
                        List<Memebersship> memebersshipList = gson.fromJson(data, new TypeToken<List<Memebersship>>() {
                        }.getType());
                        cartState.setMemebersshipList(memebersshipList);
                        if (cartState.getMemebersshipList().size() <= 0) {
                            rlOrdersBar.setVisibility(View.GONE);
                            ptrlMembershipPull.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        }
                        if (membershipAdapter != null) {
                            membershipAdapter.notifyDataSetInvalidated();
                        }
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        ptrlMembershipPull.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlMembershipPull != null) {
                    isPull = true;
                    srlMembershipPull.setEnabled(false);
                    srlMembershipPull.setRefreshing(false);
                }
                rlOrdersBar.setVisibility(View.GONE);
                ptrlMembershipPull.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initIntents(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int type) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @OnClick({R.id.rl_membersship_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_membersship_return:
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
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughFind t) {

    }

}

