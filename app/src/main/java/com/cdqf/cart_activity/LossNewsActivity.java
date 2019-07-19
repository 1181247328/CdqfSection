package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;
import okhttp3.Request;

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

    private int type = 0;

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
                lossNewsLeftAdapter.setType(position);
                type = position;
                if (cartState.getLossNewsList().get(position).getData().size() <= 0) {
                    initPullItem(cartState.getLossNewsList().get(position).getId());
                } else {
                    lossNewsRightAdapter.setPosition(type);
                }
            }
        });

        srlLossnewsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String LOSS_NEWS = CartAddaress.ADDRESS_THE + "/?s=TotalGoods.lists";
                final String lossId = cartState.getLossNewsList().get(type).getId();
                OkHttpUtils
                        .post()
                        .url(LOSS_NEWS)
                        .addParams("id", lossId)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onAfter(int id) {
                                super.onAfter(id);
                                Log.e(TAG, "---最后---");
                            }

                            @Override
                            public void onBefore(Request request, int id) {
                                super.onBefore(request, id);
                                Log.e(TAG, "---开始---");
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e(TAG, "---onError---" + e.getMessage());
                                rpbLossnewsBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e(TAG, "---onOkHttpResponse下拉刷新---" + response);
                                if (srlLossnewsPull != null) {
                                    srlLossnewsPull.setRefreshing(false);
                                }
                                JSONObject resultJSON = JSON.parseObject(response);
                                int error_code = resultJSON.getInteger("ret");
                                String msg = resultJSON.getString("msg");
                                switch (error_code) {
                                    //获取成功
                                    case 200:
                                        String data = resultJSON.getString("data");
                                        List<LossNews.Data> lossDataList = gson.fromJson(data, new TypeToken<List<LossNews.Data>>() {
                                        }.getType());
                                        int type = 0;
                                        for (int i = 0; i < cartState.getLossNewsList().size(); i++) {
                                            if (TextUtils.equals(cartState.getLossNewsList().get(i).getId(), lossId)) {
                                                type = i;
                                                cartState.getLossNewsList().get(i).setData(lossDataList);
                                            }
                                        }
                                        lossNewsRightAdapter = new LossNewsRightAdapter(context, type);
                                        lvLossnewsList.setAdapter(lossNewsRightAdapter);
                                        break;
                                    default:
                                        cartState.initToast(context, msg, true, 0);
                                        break;
                                }
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                Log.e(TAG, "---progress---" + progress + "---total---" + total + "---id---" + id);
                                rpbLossnewsBar.setProgress(progress * 100);
                                if (progress == 1) {
                                    rpbLossnewsBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }

    private void initBack() {
        initPull();
    }

    private void initPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        String LOSS_NEWS = CartAddaress.ADDRESS_THE + "/?s=TotalGoods.lists";
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(LOSS_NEWS, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse新损耗品---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.getLossNewsList().clear();
                        List<LossNews> lossNewsList = gson.fromJson(data, new TypeToken<List<LossNews>>() {
                        }.getType());
                        cartState.setLossNewsList(lossNewsList);
                        if (lossNewsLeftAdapter != null) {
                            lossNewsLeftAdapter.notifyDataSetChanged();
                        }
                        initPullItem(cartState.getLossNewsList().get(0).getId());
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

    //二次下载
    private void initPullItem(final String lossId) {
        String LOSS_NEWS = CartAddaress.ADDRESS_THE + "/?s=TotalGoods.lists";
        OkHttpUtils
                .post()
                .url(LOSS_NEWS)
                .addParams("id", lossId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Log.e(TAG, "---最后---");
                        rpbLossnewsBar.setVisibility(View.GONE);
                        lvLossnewsList.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        Log.e(TAG, "---开始---");
                        rpbLossnewsBar.setVisibility(View.VISIBLE);
                        lvLossnewsList.setVisibility(View.GONE);
                        rpbLossnewsBar.setProgress(0);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "---onError---" + e.getMessage());
                        rpbLossnewsBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse二次---" + response);
                        if (srlLossnewsPull != null) {
                            srlLossnewsPull.setRefreshing(false);
                        }
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("ret");
                        String msg = resultJSON.getString("msg");
                        switch (error_code) {
                            //获取成功
                            case 200:

                                String data = resultJSON.getString("data");
                                List<LossNews.Data> lossDataList = gson.fromJson(data, new TypeToken<List<LossNews.Data>>() {
                                }.getType());
                                int type = 0;
                                for (int i = 0; i < cartState.getLossNewsList().size(); i++) {
                                    if (TextUtils.equals(cartState.getLossNewsList().get(i).getId(), lossId)) {
                                        type = i;
                                        cartState.getLossNewsList().get(i).setData(lossDataList);
                                    }
                                }
                                lossNewsRightAdapter = new LossNewsRightAdapter(context, type);
                                lvLossnewsList.setAdapter(lossNewsRightAdapter);
                                break;
                            default:
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        Log.e(TAG, "---progress---" + progress + "---total---" + total + "---id---" + id);
                        rpbLossnewsBar.setProgress(progress * 100);
                        if (progress == 1) {
                            rpbLossnewsBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_lossnews_return, R.id.tv_lossnews_order})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_lossnews_return:
                finish();
                break;
            //提交订单
            case R.id.tv_lossnews_order:
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(12, "提示", "是否提交", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "提交申请数量");
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
        String name = cartState.getLossNewsList().get(type).getData().get(r.position).getName();
        String number = cartState.getLossNewsList().get(type).getData().get(r.position).getNumber();
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
        cartState.getLossNewsList().get(type).getData().get(u.position).setNumberSelete(u.number);
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
            for (int j = 0; j < cartState.getLossNewsList().get(i).getData().size(); j++) {
                boolean isSelete = cartState.getLossNewsList().get(i).getData().get(j).isSelete();
                if (isSelete) {
                    isShopSelete = true;
                    Loss loss = new Loss(
                            cartState.getLossNewsList().get(i).getId(),
                            cartState.getLossNewsList().get(i).getData().get(j).getId(),
                            cartState.getLossNewsList().get(i).getData().get(j).getNumberSelete() + "");
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
        String LOSS_NEWS = CartAddaress.ADDRESS_THE + "/?s=TotalGoods.receive";
        OkHttpUtils
                .post()
                .url(LOSS_NEWS)
                .addParams("id", cartState.getUser().getId()+"") //用户id
                .addParams("shopid", cartState.getUser().getShopid()+"") //店铺id
                .addParams("data", loss) //选择申请的商品
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Log.e(TAG, "---最后---");
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        Log.e(TAG, "---开始---");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "---onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse二次---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("ret");
                        String msg = resultJSON.getString("msg");
                        switch (error_code) {
                            //获取成功
                            case 200:
                                finish();
                                break;
                            default:
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        Log.e(TAG, "---progress---" + progress + "---total---" + total + "---id---" + id);
                    }
                });
    }

    class Loss {
        private String id;

        private String idItem;

        private String number;

        public Loss(String id, String idItem, String number) {
            this.id = id;
            this.idItem = idItem;
            this.number = number;
        }
    }
}
