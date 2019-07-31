package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.DatilsAdapter;
import com.cdqf.cart_class.Number;
import com.cdqf.cart_find.ShopPositionFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.DoubleOperationUtil;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.PieChartView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xclcharts.chart.PieData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 报表详情
 */
public class ReportDatilsActivity extends BaseActivity {
    private String TAG = ReportDatilsActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //刷新器
    @BindView(R.id.srl_datils_pull)
    public SwipeRefreshLayout srlDatilsPull = null;

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    //刷新器
    @BindView(R.id.ptrl_datils_pull)
    public PullToRefreshLayout ptrlDatilsPull = null;

    public NestedScrollView nsvDatilsPull = null;

    //时间
    @BindView(R.id.tv_datils_timer)
    public TextView tvDatilsTimer = null;

    //下单次数
    @BindView(R.id.tv_datils_place)
    public TextView tvDatilsPlace = null;

    //金额
    @BindView(R.id.tv_datils_price)
    public TextView tvDatilsPrice = null;

    //服务次数
    @BindView(R.id.tv_datils_service)
    public TextView tvDatilsService = null;

    //提成
    @BindView(R.id.tv_datils_commission)
    public TextView tvDatilsCommission = null;

    //比列
    @BindView(R.id.tv_datils_rate)
    public TextView tvDatilsRate = null;

    //实收成本
    @BindView(R.id.tv_datils_proportion)
    public TextView tvDatilsProportion = null;

    //饼图
    @BindView(R.id.pcv_datils_bread)
    public PieChartView pcvDatilsBread = null;

    //余额
    @BindView(R.id.tv_datils_platform)
    public TextView tvDatilsPlatform = null;

    //微信
    @BindView(R.id.tv_datils_pay)
    public TextView tvDatilsPay = null;

    //现金
    @BindView(R.id.tv_datils_cash)
    public TextView tvDatilsCash = null;

    //农商
    @BindView(R.id.tv_datils_agri)
    public TextView tvDatilsAgri = null;

    @BindView(R.id.tv_orders_list)
    public TextView tvOrdersList = null;

    //明细
    @BindView(R.id.lvfsv_datils_list)
    public ListViewForScrollView lvfsvDatilsList = null;

    private DatilsAdapter datilsAdapter = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    private int position = 0;

    private int type = 0;

    private int id = 0;

    private String timer = "";

    private String place = "";

    private String price = "";

    private String service = "";

    private String commission = "";

    private String rate = "";

    private String proportion = "";

    private int pull = 0;

    private int srcpull = 0;

    private ArrayList<PieData> chartData = new ArrayList<PieData>();

    private int page = 1;

    private boolean isPulls = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_reportdatils);

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

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        type = intent.getIntExtra("type", 0);
    }

    private void initView() {
        nsvDatilsPull = (NestedScrollView) ptrlDatilsPull.getPullableView();
    }

    private void initAdapter() {
        datilsAdapter = new DatilsAdapter(context);
        lvfsvDatilsList.setAdapter(datilsAdapter);
    }

    private void initListener() {
        srlDatilsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pull = 0;
                srcpull = 0;
                page = 1;
                isPulls = true;
                //饼图
                initPie(false);
                //明细
                initList(false);
            }
        });
        ptrlDatilsPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("page", page);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.get(CartAddaress.REPORT_DATILS_LIST + id, false, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---明细---" + response);
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
                                List<Number> numberList = gson.fromJson(datas, new TypeToken<List<Number>>() {
                                }.getType());
                                cartState.getNumberList().addAll(numberList);
                                if (numberList.size() <= 0) {
                                    cartState.initToast(context, "没有更多了", true, 0);
                                }
                                if (datilsAdapter != null) {
                                    datilsAdapter.notifyDataSetChanged();
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
        ptrlDatilsPull.setPullDownEnable(false);
        srlDatilsPull.setEnabled(false);
        //报表情况
        initReport();
        //饼图
        initPie(false);
        //明细
        initList(false);
    }

    private void initReport() {
        switch (type) {
            case 1:
                id = cartState.getDailyList().get(position).getId();
                timer = cartState.getDailyList().get(position).getStart_time();
                place = cartState.getDailyList().get(position).getService_number() + "";
                price = cartState.getDailyList().get(position).getTurnover();
                service = cartState.getDailyList().get(position).getStaff_service_number() + "";
                commission = cartState.getDailyList().get(position).getGet_money();
                rate = cartState.getDailyList().get(position).getTrend() == 1 ?
                        "+" + cartState.getDailyList().get(position).getCompare_val() + "%" :
                        "-" + cartState.getDailyList().get(position).getCompare_val() + "%";
                proportion = cartState.getDailyList().get(position).getTurnover() + "/" + cartState.getDailyList().get(position).getCost_price();
                break;
            case 2:
                id = cartState.getWeekList().get(position).getId();
                timer = cartState.getWeekList().get(position).getStart_time();
                place = cartState.getWeekList().get(position).getService_number() + "";
                price = cartState.getWeekList().get(position).getTurnover();
                service = cartState.getWeekList().get(position).getStaff_service_number() + "";
                commission = cartState.getWeekList().get(position).getGet_money();
                rate = cartState.getWeekList().get(position).getTrend() == 1 ?
                        "+" + cartState.getWeekList().get(position).getCompare_val() + "%" :
                        "-" + cartState.getWeekList().get(position).getCompare_val() + "%";
                proportion = cartState.getWeekList().get(position).getTurnover() + "/" + cartState.getWeekList().get(position).getCost_price();
                break;
            case 3:
                id = cartState.getMothList().get(position).getId();
                timer = cartState.getMothList().get(position).getStart_time();
                place = cartState.getMothList().get(position).getService_number() + "";
                price = cartState.getMothList().get(position).getTurnover();
                service = cartState.getMothList().get(position).getStaff_service_number() + "";
                commission = cartState.getMothList().get(position).getGet_money();
                rate = cartState.getMothList().get(position).getTrend() == 1 ?
                        "+" + cartState.getMothList().get(position).getCompare_val() + "%" :
                        "-" + cartState.getMothList().get(position).getCompare_val() + "%";
                proportion = cartState.getMothList().get(position).getTurnover() + "/" + cartState.getMothList().get(position).getCost_price();
                break;
        }
        //时间
        tvDatilsTimer.setText(timer);
        //下单次数
        tvDatilsPlace.setText(place);
        //金额
        tvDatilsPrice.setText(price);
        //服务次数
        tvDatilsService.setText(service);
        //提成
        tvDatilsCommission.setText(commission);
        //比列
        tvDatilsRate.setText(rate);
        //实收成本
        tvDatilsProportion.setText(proportion);
    }

    /**
     * 饼图
     *
     * @param isToast
     */
    private void initPie(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.REPORT_DATILS + id, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---饼图---" + response);
                srcpull++;
                if (srcpull == 2) {
                    if (srlDatilsPull != null) {
                        srlDatilsPull.setEnabled(true);
                        srlDatilsPull.setRefreshing(false);
                    }
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        if (!isPulls) {
                            pull++;
                            if (pull == 2) {
                                rlOrdersBar.setVisibility(View.GONE);
                                ptrlDatilsPull.setVisibility(View.VISIBLE);
                                tvOrdersAbnormal.setVisibility(View.GONE);
                            } else {
                                rlOrdersBar.setVisibility(View.VISIBLE);
                                ptrlDatilsPull.setVisibility(View.GONE);
                                tvOrdersAbnormal.setVisibility(View.GONE);
                            }
                        } else {
                            rlOrdersBar.setVisibility(View.GONE);
                            ptrlDatilsPull.setVisibility(View.VISIBLE);
                            tvOrdersAbnormal.setVisibility(View.GONE);
                        }
                        chartData.clear();
                        JSONObject data = resultJSON.getJSONObject("data");
                        //余额
                        int balance = data.getInteger("balance");
                        tvDatilsPlatform.setText(balance + "");
                        //微信
                        int pay = data.getInteger("wechat");
                        tvDatilsPay.setText(pay + "");
                        //现金
                        int cash = data.getInteger("cash");
                        tvDatilsCash.setText(cash + "");
                        //农商
                        int backRcb = data.getInteger("back_rcb");
                        tvDatilsAgri.setText(backRcb + "");

                        int sum = balance + pay + cash + backRcb;

                        PieData pieDataOne = new PieData("余额",
                                DoubleOperationUtil.mul(DoubleOperationUtil.div(balance, sum, 2), 100) + "%",
                                DoubleOperationUtil.div(balance, sum, 2) * 100,
                                ContextCompat.getColor(context, R.color.route_determine));

                        PieData pieDataFive = new PieData("微信",
                                DoubleOperationUtil.mul(DoubleOperationUtil.div(pay, sum, 2), 100) + "%",
                                DoubleOperationUtil.mul(DoubleOperationUtil.div(pay, sum, 2), 100),
                                ContextCompat.getColor(context, R.color.colorPrimary));

                        PieData pieDataTwo = new PieData("现金",
                                DoubleOperationUtil.mul(DoubleOperationUtil.div(cash, sum, 2), 100) + "%",
                                DoubleOperationUtil.mul(DoubleOperationUtil.div(cash, sum, 2), 100),
                                ContextCompat.getColor(context, R.color.lossmanager_add));

                        PieData pieDataThree = new PieData("农商",
                                DoubleOperationUtil.mul(DoubleOperationUtil.div(backRcb, sum, 2), 100) + "%",
                                DoubleOperationUtil.mul(DoubleOperationUtil.div(backRcb, sum, 2), 100),
                                ContextCompat.getColor(context, R.color.tab_main_text_icon));
                        chartData.add(pieDataOne);
                        chartData.add(pieDataTwo);
                        chartData.add(pieDataThree);
                        chartData.add(pieDataFive);
                        pcvDatilsBread.setChartData(chartData);
                        Log.e(TAG, "---" + gson.toJson(chartData));
                        break;
                    default:
                        pull++;
                        if (pull == 2) {
                            rlOrdersBar.setVisibility(View.GONE);
                            ptrlDatilsPull.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        } else {
                            rlOrdersBar.setVisibility(View.VISIBLE);
                            ptrlDatilsPull.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.GONE);
                        }
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                srcpull++;
                if (srcpull == 2) {
                    if (srlDatilsPull != null) {
                        srlDatilsPull.setEnabled(true);
                        srlDatilsPull.setRefreshing(false);
                    }
                }
                pull++;
                if (pull == 2) {
                    rlOrdersBar.setVisibility(View.GONE);
                    ptrlDatilsPull.setVisibility(View.GONE);
                    tvOrdersAbnormal.setVisibility(View.VISIBLE);
                } else {
                    rlOrdersBar.setVisibility(View.VISIBLE);
                    ptrlDatilsPull.setVisibility(View.GONE);
                    tvOrdersAbnormal.setVisibility(View.GONE);
                }
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    /**
     * 明细
     *
     * @param isToast
     */
    private void initList(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.REPORT_DATILS_LIST + id, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---明细---" + response);
                srcpull++;
                if (srcpull == 2) {
                    if (srlDatilsPull != null) {
                        srlDatilsPull.setEnabled(true);
                        srlDatilsPull.setRefreshing(false);
                    }
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        if (!isPulls) {
                            pull++;
                            if (pull == 2) {
                                rlOrdersBar.setVisibility(View.GONE);
                                ptrlDatilsPull.setVisibility(View.VISIBLE);
                                tvOrdersAbnormal.setVisibility(View.GONE);
                            } else {
                                rlOrdersBar.setVisibility(View.VISIBLE);
                                ptrlDatilsPull.setVisibility(View.GONE);
                                tvOrdersAbnormal.setVisibility(View.GONE);
                            }
                        } else {
                            rlOrdersBar.setVisibility(View.GONE);
                            ptrlDatilsPull.setVisibility(View.VISIBLE);
                            tvOrdersAbnormal.setVisibility(View.GONE);
                        }
                        page = 2;
                        JSONObject data = resultJSON.getJSONObject("data");
                        String datas = data.getString("data");
                        cartState.getNumberList().clear();
                        List<Number> numberList = gson.fromJson(datas, new TypeToken<List<Number>>() {
                        }.getType());
                        cartState.setNumberList(numberList);
                        if (cartState.getNumberList().size() <= 0) {
                            tvOrdersList.setVisibility(View.VISIBLE);
                            lvfsvDatilsList.setVisibility(View.GONE);
                        } else {
                            tvOrdersList.setVisibility(View.GONE);
                            lvfsvDatilsList.setVisibility(View.VISIBLE);
                        }
                        if (datilsAdapter != null) {
                            datilsAdapter.notifyDataSetChanged();
                        }
                        nsvDatilsPull.smoothScrollTo(0, 0);
                        break;
                    default:
                        pull++;
                        if (pull == 2) {
                            rlOrdersBar.setVisibility(View.GONE);
                            ptrlDatilsPull.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        } else {
                            rlOrdersBar.setVisibility(View.VISIBLE);
                            ptrlDatilsPull.setVisibility(View.GONE);
                            tvOrdersAbnormal.setVisibility(View.GONE);
                        }
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                srcpull++;
                if (srcpull == 2) {
                    if (srlDatilsPull != null) {
                        srlDatilsPull.setEnabled(true);
                        srlDatilsPull.setRefreshing(false);
                    }
                }
                pull++;
                if (pull == 2) {
                    rlOrdersBar.setVisibility(View.GONE);
                    ptrlDatilsPull.setVisibility(View.GONE);
                    tvOrdersAbnormal.setVisibility(View.VISIBLE);
                } else {
                    rlOrdersBar.setVisibility(View.VISIBLE);
                    ptrlDatilsPull.setVisibility(View.GONE);
                    tvOrdersAbnormal.setVisibility(View.GONE);
                }
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }


    @OnClick({R.id.rl_datils_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_datils_return:
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

    @Subscribe
    public void onEventMainThread(ShopPositionFind s) {

    }
}