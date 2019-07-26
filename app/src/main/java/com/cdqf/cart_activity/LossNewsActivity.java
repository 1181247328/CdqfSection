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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.LossNewsLeftAdapter;
import com.cdqf.cart_adapter.LossNewsRightAdapter;
import com.cdqf.cart_class.LossNews;
import com.cdqf.cart_dilog.LossNewsDilogFragment;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.LossManagerNumberFind;
import com.cdqf.cart_find.LossNewsAddFind;
import com.cdqf.cart_find.LossNewsFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.RoundProgressBar;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 新损耗品(店长)
 */
public class LossNewsActivity extends BaseActivity {
    private String TAG = LossNewsActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    @BindView(R.id.srl_lossnews_pull)
    public VerticalSwipeRefreshLayout srlLossnewsPull = null;

    @BindView(R.id.rpb_lossnews_bar)
    public RoundProgressBar rpbLossnewsBar = null;

    //返回
    @BindView(R.id.rl_lossnews_return)
    public RelativeLayout rlLossnewsReturn = null;

    //提交订单
    @BindView(R.id.tv_lossnews_order)
    public TextView tvLossnewsOrder = null;

    //服务名称集合
    @BindView(R.id.lv_lossnews_name)
    public ListViewForScrollView lvLossnewsName = null;

    private LossNewsLeftAdapter lossNewsLeftAdapter = null;

    //服务内容
    @BindView(R.id.lv_lossnews_list)
    public ListViewForScrollView lvLossnewsList = null;

    private LossNewsRightAdapter lossNewsRightAdapter = null;

    @BindView(R.id.tv_lossnews_out)
    public TextView tvLossnewsOut = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.rl_loss_context)
    public RelativeLayout rlLossContext = null;

    private int type = 0;

    private int state = 0;

    private boolean isName = false;

    private boolean isList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_lossnews);

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

    }

    private void initAdapter() {
        lossNewsLeftAdapter = new LossNewsLeftAdapter(context);
        lvLossnewsName.setAdapter(lossNewsLeftAdapter);

    }

    private void initListener() {
        lvLossnewsName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == lossNewsLeftAdapter.getType()) {
                    return;
                }
                type = position;
                lossNewsLeftAdapter.setType(position);
                lossNewsRightAdapter.setPosition(position);
            }
        });

        srlLossnewsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });

        lvLossnewsName.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    isName = true;
                    if (isName && isList) {
                        srlLossnewsPull.setEnabled(true);
                    } else {
                        srlLossnewsPull.setEnabled(false);
                    }
                } else {
                    srlLossnewsPull.setEnabled(false);
                }
            }
        });

        lvLossnewsName.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    isList = true;
                    if (isName && isList) {
                        srlLossnewsPull.setEnabled(true);
                    } else {
                        srlLossnewsPull.setEnabled(false);
                    }
                } else {
                    srlLossnewsPull.setEnabled(false);
                }
            }
        });
    }

    private void initBack() {
        srlLossnewsPull.setEnabled(false);
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.LOSS_NEW + cartState.getUser().getShopid(), isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse新损耗品---" + response);
                if (srlLossnewsPull != null) {
                    srlLossnewsPull.setEnabled(true);
                    srlLossnewsPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 201:
                    case 204:
                    case 200:
                        rlOrdersBar.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        rlLossContext.setVisibility(View.VISIBLE);
                        String data = resultJSON.getString("data");
                        cartState.getLossNewsList().clear();
                        List<LossNews> lossNewsList = gson.fromJson(data, new TypeToken<List<LossNews>>() {
                        }.getType());
                        cartState.setLossNewsList(lossNewsList);
                        if (lossNewsLeftAdapter != null) {
                            lossNewsLeftAdapter.setType(type);
                            lossNewsLeftAdapter.notifyDataSetChanged();
                        }
                        lossNewsRightAdapter = new LossNewsRightAdapter(context, 0);
                        lvLossnewsList.setAdapter(lossNewsRightAdapter);
                        if (lossNewsRightAdapter != null) {
                            lossNewsRightAdapter.setPosition(type);
                            lossNewsRightAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        rlLossContext.setVisibility(View.GONE);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                rlOrdersBar.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                rlLossContext.setVisibility(View.GONE);
                if (srlLossnewsPull != null) {
                    srlLossnewsPull.setEnabled(true);
                    srlLossnewsPull.setRefreshing(false);
                }
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_lossnews_return, R.id.tv_lossnews_order, R.id.tv_lossnews_out})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_lossnews_return:
                finish();
                break;
            //入库
            case R.id.tv_lossnews_order:
                state = 1;
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(12, "提示", "是否入库", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "提交入库数量");
                break;
            //出库
            case R.id.tv_lossnews_out:
                state = 2;
                WhyDilogFragment whyDilogTwoFragment = new WhyDilogFragment();
                whyDilogTwoFragment.setInit(12, "提示", "是否出库", "否", "是");
                whyDilogTwoFragment.show(getSupportFragmentManager(), "提交出库数量");
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
     * 数量
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(LossNewsAddFind r) {
        LossNewsDilogFragment lossNewsDilogFragment = new LossNewsDilogFragment();
        String name = cartState.getLossNewsList().get(type).getChildren().get(r.position).getName();
        String number = cartState.getLossNewsList().get(type).getChildren().get(r.position).getStock() + "";
        lossNewsDilogFragment.number(r.position, name, number);
        lossNewsDilogFragment.show(getSupportFragmentManager(), "领取数量");
    }

    /**
     * 数量
     *
     * @param u
     */
    @Subscribe
    public void onEventMainThread(LossManagerNumberFind u) {
        cartState.getLossNewsList().get(type).getChildren().get(u.position).setNumberSelete(u.number);
        lossNewsRightAdapter.notifyDataSetChanged();
    }

    /**
     * 提交
     *
     * @param u
     */
    @Subscribe
    public void onEventMainThread(LossNewsFind u) {
        boolean isShopSelete = false;
        List<Loss> lossList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < cartState.getLossNewsList().size(); i++) {
            for (int j = 0; j < cartState.getLossNewsList().get(i).getChildren().size(); j++) {
                boolean isSelete = cartState.getLossNewsList().get(i).getChildren().get(j).isSelete();
                if (isSelete) {
                    isShopSelete = true;
                    Loss loss = new Loss(
                            cartState.getLossNewsList().get(i).getChildren().get(j).getId(),
                            cartState.getLossNewsList().get(i).getChildren().get(j).getConsumables_id(),
                            cartState.getLossNewsList().get(i).getChildren().get(j).getNumberSelete());
                    lossList.add(loss);
                }
            }
        }
        if (!isShopSelete) {
            cartState.initToast(context, "请选择申请的商品", true, 0);
            return;
        }
        String loss = gson.toJson(lossList);
        Log.e(TAG, "---最后所得数据---" + loss);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staff_id", cartState.getUser().getId());
        params.put("status", state);
        params.put("shop_id", cartState.getUser().getShopid());
        params.put("data", lossList);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.CONSUMABLES, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse提交出入库---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 201:
                    case 204:
                    case 200:
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
//        String LOSS_NEWS = CartAddaress.ADDRESS_THE + "/?s=TotalGoods.receive";
//        OkHttpUtils
//                .post()
//                .url(LOSS_NEWS)
//                .addParams("id", cartState.getUser().getId() + "") //用户id
//                .addParams("shopid", cartState.getUser().getShopid() + "") //店铺id
//                .addParams("data", loss) //选择申请的商品
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onAfter(int id) {
//                        super.onAfter(id);
//                        Log.e(TAG, "---最后---");
//                    }
//
//                    @Override
//                    public void onBefore(Request request, int id) {
//                        super.onBefore(request, id);
//                        Log.e(TAG, "---开始---");
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Log.e(TAG, "---onError---" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.e(TAG, "---onOkHttpResponse二次---" + response);
//                        JSONObject resultJSON = JSON.parseObject(response);
//                        int error_code = resultJSON.getInteger("ret");
//                        String msg = resultJSON.getString("msg");
//                        switch (error_code) {
//                            //获取成功
//                            case 200:
//                                finish();
//                                break;
//                            default:
//                                cartState.initToast(context, msg, true, 0);
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void inProgress(float progress, long total, int id) {
//                        Log.e(TAG, "---progress---" + progress + "---total---" + total + "---id---" + id);
//                    }
//                });
    }

    class Loss {

        private int id;

        private int consumables_id;

        private int number;

        public Loss(int id, int consumables_id, int number) {
            this.id = id;
            this.consumables_id = consumables_id;
            this.number = number;
        }
    }
}
