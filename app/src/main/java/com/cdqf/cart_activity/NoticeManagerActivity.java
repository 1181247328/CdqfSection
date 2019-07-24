package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.NoticeAdapter;
import com.cdqf.cart_class.Notice;
import com.cdqf.cart_find.ReleasePullFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
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
 * 通知(店长)
 */
public class NoticeManagerActivity extends BaseActivity {

    private String TAG = NoticeManagerActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_notice_pull)
    public VerticalSwipeRefreshLayout srlNoticePull = null;

    //返回
    @BindView(R.id.rl_notice_return)
    public RelativeLayout rlNoticeReturn = null;

    @BindView(R.id.tv_noticemanager_release)
    public TextView tvNoticemanagerRelease = null;

    @BindView(R.id.lv_notice_list)
    public ListViewForScrollView lvNoticeList = null;

    private NoticeAdapter noticeAdapter = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_noticemanager_no)
    public TextView tvNoticemanagerNo = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    private boolean isPull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_noticemanager);

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
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {
        noticeAdapter = new NoticeAdapter(context);
        lvNoticeList.setAdapter(noticeAdapter);

    }

    private void initAdapter() {

    }

    private void initListener() {

        lvNoticeList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    if (isPull) {
                        srlNoticePull.setEnabled(true);
                    } else {
                        srlNoticePull.setEnabled(false);
                    }
                } else {
                    srlNoticePull.setEnabled(false);
                }
            }
        });

        srlNoticePull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        srlNoticePull.setRefreshing(false);
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.STAFF_NOTICE + cartState.getUser().getId() + "/notice/" + cartState.getUser().getShopid(), isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse通知---" + response);
                isPull = true;
                if (srlNoticePull != null) {
                    srlNoticePull.setEnabled(true);
                    srlNoticePull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        rlOrdersBar.setVisibility(View.GONE);
                        lvNoticeList.setVisibility(View.VISIBLE);
                        tvNoticemanagerNo.setVisibility(View.GONE);
                        String data = resultJSON.getString("data");
                        Log.e(TAG, "---通知---" + data);
                        if (TextUtils.equals(data, "{}")) {
                            rlOrdersBar.setVisibility(View.GONE);
                            lvNoticeList.setVisibility(View.GONE);
                            tvNoticemanagerNo.setVisibility(View.VISIBLE);
                            return;
                        }
                        cartState.getNoticeList().clear();
                        List<Notice> noticeList = gson.fromJson(data, new TypeToken<List<Notice>>() {
                        }.getType());
                        cartState.setNoticeList(noticeList);
                        if (cartState.getNoticeList().size() <= 0) {
                            rlOrdersBar.setVisibility(View.GONE);
                            lvNoticeList.setVisibility(View.GONE);
                            tvNoticemanagerNo.setVisibility(View.VISIBLE);
                        }
                        if (noticeAdapter != null) {
                            noticeAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        lvNoticeList.setVisibility(View.GONE);
                        tvNoticemanagerNo.setVisibility(View.VISIBLE);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                isPull = true;
                rlOrdersBar.setVisibility(View.GONE);
                lvNoticeList.setVisibility(View.GONE);
                tvNoticemanagerNo.setVisibility(View.VISIBLE);
                if (srlNoticePull != null) {
                    srlNoticePull.setEnabled(true);
                    srlNoticePull.setRefreshing(false);
                }
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_notice_return, R.id.tv_noticemanager_release})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_notice_return:
                finish();
                break;
            //发布
            case R.id.tv_noticemanager_release:
                initIntent(ReleaseActivity.class);
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
     * 发布通知后刷新
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(ReleasePullFind r) {
        initPull(true);
    }
}

