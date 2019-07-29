package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.RecordAdapter;
import com.cdqf.cart_class.Record;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
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

/**
 * 记录
 */
public class RecordActivity extends BaseActivity {
    private String TAG = RecordActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_lossnews_pull)
    public VerticalSwipeRefreshLayout vsrlRecordPull = null;

    @BindView(R.id.ptrl_record_pull)
    public PullToRefreshLayout ptrlRecordPull = null;

    //返回
    @BindView(R.id.rl_record_return)
    public RelativeLayout rlRecordReturn = null;

    private ListView lvRecrodList = null;

    private RecordAdapter recordAdapter = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    //领取的数量
    private int number = 0;

    private boolean isName = false;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_record);

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
    }

    private void initView() {
        lvRecrodList = (ListView) ptrlRecordPull.getPullableView();
    }

    private void initAdapter() {
        recordAdapter = new RecordAdapter(context);
        lvRecrodList.setAdapter(recordAdapter);
    }

    private void initListener() {
        vsrlRecordPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initPull(false);
            }
        });

        ptrlRecordPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("shop_id", cartState.getUser().getShopid());
                params.put("page", page);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.get(CartAddaress.SHOP_RECORD, false, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse出入库记录之上拉加载---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("code");
                        String msg = resultJSON.getString("message");
                        switch (error_code) {
                            //获取成功
                            case 204:
                            case 200:
                            case 201:
                                page++;
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                JSONObject data = resultJSON.getJSONObject("data");
                                String datas = data.getString("data");
                                List<Record> recordList = gson.fromJson(datas, new TypeToken<List<Record>>() {
                                }.getType());
                                if (recordList.size() <= 0) {
                                    cartState.initToast(context, "没有更多了", true, 0);
                                }
                                cartState.getRecordList().addAll(recordList);
                                if (recordAdapter != null) {
                                    recordAdapter.notifyDataSetChanged();
                                }
                                break;
                            default:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                });
            }
        });

        lvRecrodList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    if (isName) {
                        vsrlRecordPull.setEnabled(true);
                    } else {
                        vsrlRecordPull.setEnabled(false);
                    }
                } else {
                    vsrlRecordPull.setEnabled(false);
                }
            }
        });
    }

    private void initBack() {
        initPull(false);
        ptrlRecordPull.setPullDownEnable(false);
        vsrlRecordPull.setEnabled(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shop_id", cartState.getUser().getShopid());
        params.put("page", page);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.SHOP_RECORD, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse出入库记录---" + response);
                if (vsrlRecordPull != null) {
                    isName = true;
                    vsrlRecordPull.setEnabled(true);
                    vsrlRecordPull.setRefreshing(false);
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
                        ptrlRecordPull.setVisibility(View.VISIBLE);
                        rlOrdersBar.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        JSONObject data = resultJSON.getJSONObject("data");
                        String datas = data.getString("data");
                        cartState.getRecordList().clear();
                        List<Record> recordList = gson.fromJson(datas, new TypeToken<List<Record>>() {
                        }.getType());
                        cartState.setRecordList(recordList);
                        if (cartState.getRecordList().size() <= 0) {
                            ptrlRecordPull.setVisibility(View.GONE);
                            rlOrdersBar.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        }
                        if (recordAdapter != null) {
                            recordAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        ptrlRecordPull.setVisibility(View.GONE);
                        rlOrdersBar.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                ptrlRecordPull.setVisibility(View.GONE);
                rlOrdersBar.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                if (vsrlRecordPull != null) {
                    isName = false;
                    vsrlRecordPull.setEnabled(true);
                    vsrlRecordPull.setRefreshing(false);
                }
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_record_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_record_return:
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
    }

}
