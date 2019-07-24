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
import com.cdqf.cart_adapter.BusinessAdapter;
import com.cdqf.cart_class.Business;
import com.cdqf.cart_find.AccountPullFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.LineGridView;
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
 * 营业状态
 */
public class BusinessActivity extends BaseActivity {
    private String TAG = BusinessActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //刷新
    @BindView(R.id.srl_business_pull)
    public SwipeRefreshLayout srlBusinessPull = null;

    //返回
    @BindView(R.id.rl_business_return)
    public RelativeLayout rlBusinessReturn = null;

    //列表
    @BindView(R.id.mgv_business_list)
    public LineGridView mgvBusinessList = null;

    private BusinessAdapter businessAdapter = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    private List<String> testList = null;

    private String[] tab = {
            "车牌", "车型", "项目", "金额", "付款", "状态"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.actiivty_business);

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
        businessAdapter = new BusinessAdapter(context);
        mgvBusinessList.setAdapter(businessAdapter);
    }

    private void initListener() {
        srlBusinessPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                testList.clear();
                initTab();
                initShopDatils(false);
            }
        });
        mgvBusinessList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    srlBusinessPull.setEnabled(true);
                } else {
                    srlBusinessPull.setEnabled(false);
                }
            }
        });
    }

    private void initBack() {
        initData();
    }

    private void initData() {
        srlBusinessPull.setEnabled(false);
        initTab();
        initShopDatils(false);
    }

    private void initTab() {
        testList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < tab.length; i++) {
            testList.add(tab[i]);
        }
    }

    /**
     * 店铺情况
     */
    private void initShopDatils(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.SHOP_DATILS + cartState.getUser().getShopid(), isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---营业状况---" + response);
                if (srlBusinessPull != null) {
                    srlBusinessPull.setEnabled(true);
                    srlBusinessPull.setRefreshing(false);
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
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        mgvBusinessList.setVisibility(View.VISIBLE);
                        cartState.initToast(context, msg, true, 0);
                        String data = resultJSON.getString("data");
                        List<Business> businessList = gson.fromJson(data, new TypeToken<List<Business>>() {
                        }.getType());
                        for (int i = 0; i < businessList.size(); i++) {
                            for (int j = 0; j < tab.length; j++) {
                                switch (j) {
                                    case 0:
                                        //车牌
                                        String carnum = businessList.get(i).getCarnum();
                                        if (TextUtils.equals(carnum, "") || carnum == null) {
                                            carnum = "暂无车牌";
                                        }
                                        testList.add(carnum);

                                        break;
                                    case 1:
                                        //车型
                                        String cart = "";
                                        switch (businessList.get(i).getCartype()) {
                                            case 1:
                                                cart = "小轿车";
                                                break;
                                            case 2:
                                                cart = "SUV";
                                                break;
                                            default:
                                                cart = "未知";
                                                break;
                                        }
                                        testList.add(cart);
                                        break;
                                    case 2:
                                        //项目
                                        testList.add(businessList.get(i).getGoods_name());
                                        break;
                                    case 3:
                                        //金额
                                        testList.add(businessList.get(i).getZongprice());
                                        break;
                                    case 4:
                                        //支付
                                        String pay = "";
                                        switch (businessList.get(i).getPay_type()) {
                                            case 1:
                                                pay = "余额";
                                                break;
                                            case 2:
                                                pay = "微信";
                                                break;
                                            case 3:
                                                pay = "现金";
                                                break;
                                            case 4:
                                                pay = "农商";
                                                break;
                                            default:
                                                pay = "现金";
                                                break;
                                        }
                                        testList.add(pay);
                                        break;
                                    case 5:
                                        //状态
                                        String type = "";
                                        switch (businessList.get(i).getType()) {
                                            case 1:
                                                type = "未支付";
                                                break;
                                            case 2:
                                                type = "待使用";
                                                break;
                                            case 3:
                                                type = "已完成";
                                                break;
                                            case 5:
                                                type = "已失效";
                                                break;
                                            default:
                                                break;
                                        }
                                        testList.add(type);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        businessAdapter.setTestList(testList);
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        mgvBusinessList.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlBusinessPull != null) {
                    srlBusinessPull.setEnabled(true);
                    srlBusinessPull.setRefreshing(false);
                }
                rlOrdersBar.setVisibility(View.GONE);
                mgvBusinessList.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_business_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_business_return:
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
     * @param s
     */
    @Subscribe
    public void onEventMainThread(AccountPullFind s) {

    }
}
