package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.DatilsAdapter;
import com.cdqf.cart_class.Number;
import com.cdqf.cart_find.ServiceTwoFind;
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

    private ArrayList<PieData> chartData = new ArrayList<PieData>();

    private Gson gson = new Gson();

    @BindView(R.id.srl_datils_pull)
    public SwipeRefreshLayout srlDatilsPull = null;

    @BindView(R.id.ptrl_datils_pull)
    public PullToRefreshLayout ptrlDatilsPull = null;

    private NestedScrollView svDatilsList = null;

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    //时间
    @BindView(R.id.tv_datils_timer)
    public TextView tvDatilsTimer = null;

    //下单次数
    @BindView(R.id.tv_datils_place)
    public TextView tvDatilsPlace = null;

    //服务次数
    @BindView(R.id.tv_datils_service)
    public TextView tvDatilsService = null;

    //比例
    @BindView(R.id.tv_datils_proportion)
    public TextView tvDatilsProportion = null;

    //比例率
    @BindView(R.id.tv_datils_rate)
    public TextView tvDatilsRate = null;

    //服务金额
    @BindView(R.id.tv_datils_price)
    public TextView tvDatilsPrice = null;

    //提成
    @BindView(R.id.tv_datils_commission)
    public TextView tvDatilsCommission = null;

    //饼图
    @BindView(R.id.pcv_datils_bread)
    public PieChartView pcvDatilsBread = null;

    //平台
    @BindView(R.id.tv_datils_platform)
    public TextView tvDatilsPlatform = null;

    //现金
    @BindView(R.id.tv_datils_cash)
    public TextView tvDatilsCash = null;

    //农商
    @BindView(R.id.tv_datils_agri)
    public TextView tvDatilsAgri = null;

    //下单明细
    @BindView(R.id.lvfsv_datils_list)
    public ListViewForScrollView lvfsvDatilsList = null;

    private DatilsAdapter datilsAdapter = null;

    private int position = 0;

    private int type = 0;

    private int page = 1;

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
        position = intent.getIntExtra("position", position);
        type = intent.getIntExtra("type", 0);
    }

    private void initView() {
        svDatilsList = (NestedScrollView) ptrlDatilsPull.getPullableView();
    }

    private void initAdapter() {
        lvfsvDatilsList.setFocusable(false);
        datilsAdapter = new DatilsAdapter(context);
        lvfsvDatilsList.setAdapter(datilsAdapter);
    }

    private void initListener() {

        srlDatilsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initPull(false);
                initPullList(false);
            }
        });

        ptrlDatilsPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {

                Map<String, Object> params = new HashMap<String, Object>();
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                params.put("page", page);
                okHttpRequestWrap.get(CartAddaress.REPORT_DATILS_LIST + "568", false, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---明细详情---" + response);
                        if (srlDatilsPull != null) {
                            srlDatilsPull.setRefreshing(false);
                        }
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
                                List<Number> numberList = gson.fromJson(datas, new TypeToken<List<Number>>() {
                                }.getType());
                                cartState.getNumberList().addAll(numberList);
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
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        Log.e(TAG, "---onOkHttpError---" + error);
                        cartState.initToast(context, error, true, 0);
                    }
                });
            }
        });
    }

    private void initBack() {
        ptrlDatilsPull.setPullDownEnable(false);
        initReportDatils();
        initPull(true);
        initPullList(true);
    }

    //报表详情
    private void initReportDatils() {
        String timer = "";
        String serviceNumber = "";
        String staffServiceNumber = "";
        String proportion = "";
        String rate = "";
        String price = "";
        String commission = "";
        switch (type) {
            case 1:
                //日报
                timer = cartState.getDailyList().get(position).getStart_time();
                serviceNumber = cartState.getDailyList().get(position).getService_number() + "";
                staffServiceNumber = cartState.getDailyList().get(position).getStaff_service_number() + "";
                proportion = cartState.getDailyList().get(position).getTurnover() + "/" + cartState.getDailyList().get(position).getCost_price();
                rate = cartState.getDailyList().get(position).getTrend() == 1 ?
                        "+" + cartState.getDailyList().get(position).getCompare_val() + "%" :
                        "-" + cartState.getDailyList().get(position).getCompare_val() + "%";
                price = cartState.getDailyList().get(position).getTurnover();
                commission = cartState.getDailyList().get(position).getGet_money();
                break;
            case 2:
                //周报
                timer = cartState.getWeekList().get(position).getStart_time() + "-" + cartState.getWeekList().get(position).getEnd_time();
                serviceNumber = cartState.getWeekList().get(position).getService_number() + "";
                staffServiceNumber = cartState.getWeekList().get(position).getStaff_service_number() + "";
                proportion = cartState.getWeekList().get(position).getTurnover() + "/" + cartState.getWeekList().get(position).getCost_price();
                rate = cartState.getWeekList().get(position).getTrend() == 1 ?
                        "+" + cartState.getWeekList().get(position).getCompare_val() + "%" :
                        "-" + cartState.getWeekList().get(position).getCompare_val() + "%";
                price = cartState.getWeekList().get(position).getTurnover();
                commission = cartState.getWeekList().get(position).getGet_money();
                break;
            case 3:
                //月报
                timer = cartState.getMothList().get(position).getStart_time();
                serviceNumber = cartState.getMothList().get(position).getService_number() + "";
                staffServiceNumber = cartState.getMothList().get(position).getStaff_service_number() + "";
                proportion = cartState.getMothList().get(position).getTurnover() + "/" + cartState.getMothList().get(position).getCost_price();
                rate = cartState.getMothList().get(position).getTrend() == 1 ?
                        "+" + cartState.getMothList().get(position).getCompare_val() + "%" :
                        "-" + cartState.getMothList().get(position).getCompare_val() + "%";
                price = cartState.getMothList().get(position).getTurnover();
                commission = cartState.getMothList().get(position).getGet_money();
                break;
            default:
                break;
        }
        tvDatilsTimer.setText(timer);
        tvDatilsPlace.setText(serviceNumber);
        tvDatilsService.setText(staffServiceNumber);
        tvDatilsProportion.setText(proportion);
        tvDatilsRate.setText(rate);
        tvDatilsPrice.setText(price);
        tvDatilsCommission.setText(commission);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.REPORT_DATILS + "568", isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---报表详情---" + response);
                if (srlDatilsPull != null) {
                    srlDatilsPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        cartState.initToast(context, msg, true, 0);
                        JSONObject data = resultJSON.getJSONObject("data");
                        //平台
                        int balance = data.getInteger("balance");
                        //现金
                        int cash = data.getInteger("cash");
                        //农商
                        int backRcb = data.getInteger("back_rcb");
                        tvDatilsPlatform.setText(balance + "");
                        tvDatilsCash.setText(cash + "");
                        tvDatilsAgri.setText(backRcb + "");
                        int sum = balance + cash + backRcb;


                        double balanceOne = DoubleOperationUtil.div(balance, sum, 2) * 100;
                        PieData pieDataOne = new PieData(balanceOne + "%", balanceOne, Color.rgb(155, 187, 90));
                        chartData.add(pieDataOne);

                        double cashTwo = DoubleOperationUtil.div(cash, sum, 2) * 100;
                        PieData pieDataTwo = new PieData(cashTwo + "%", cashTwo, Color.rgb(191, 79, 75));
                        chartData.add(pieDataTwo);

                        double backRcbTwo = DoubleOperationUtil.div(backRcb, sum, 2) * 100;
                        PieData pieDataThreee = new PieData(backRcbTwo + "%", backRcbTwo, Color.rgb(242, 167, 69));
                        chartData.add(pieDataThreee);

                        pcvDatilsBread.setChartData(chartData);

                        break;
                    default:
                        cartState.initToast(context, msg, true, 0);
                        break;
                }

            }

            @Override
            public void onOkHttpError(String error) {
                if (srlDatilsPull != null) {
                    srlDatilsPull.setRefreshing(false);
                }
                Log.e(TAG, "---onOkHttpError---" + error);
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initPullList(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        params.put("page", page);
        okHttpRequestWrap.get(CartAddaress.REPORT_DATILS_LIST + "568", isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---明细详情---" + response);
                if (srlDatilsPull != null) {
                    srlDatilsPull.setRefreshing(false);
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
                        JSONObject data = resultJSON.getJSONObject("data");
                        String datas = data.getString("data");
                        cartState.initToast(context, msg, true, 0);
                        cartState.getNumberList().clear();
                        cartState.initToast(context, msg, true, 0);

                        List<Number> numberList = gson.fromJson(datas, new TypeToken<List<Number>>() {
                        }.getType());
                        cartState.setNumberList(numberList);
                        if (datilsAdapter != null) {
                            datilsAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        cartState.initToast(context, msg, true, 0);
                        break;
                }

            }

            @Override
            public void onOkHttpError(String error) {
                if (srlDatilsPull != null) {
                    srlDatilsPull.setRefreshing(false);
                }
                Log.e(TAG, "---onOkHttpError---" + error);
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
            //返回
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
    public void onEventMainThread(ServiceTwoFind s) {

    }
}