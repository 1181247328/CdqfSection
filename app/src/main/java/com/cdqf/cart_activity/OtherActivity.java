package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.WithdrawalFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartCalender;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 任务
 */
public class OtherActivity extends BaseActivity {
    private String TAG = OtherActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_other_pull)
    public SwipeRefreshLayout srlOtherPull = null;

    //返回
    @BindView(R.id.rl_other_return)
    public RelativeLayout rlOtherReturn = null;

    //时间
    @BindView(R.id.tv_other_timer)
    public TextView tvOtherTimer = null;

    //本月任务
    @BindView(R.id.tv_other_total)
    public TextView tvOtherTotal = null;

    //本月完成
    @BindView(R.id.tv_other_has)
    public TextView tvOtherHas = null;

    //本月上班
    @BindView(R.id.tv_other_on)
    public TextView tvOtherOn = null;

    //本月休假
    @BindView(R.id.tv_other_hugh)
    public TextView tvOtherHugh = null;

    //明细
    @BindView(R.id.tv_other_detail)
    public TextView tvOtherDetail = null;

    //签字人员
    @BindView(R.id.tv_other_name)
    public TextView tvOtherName = null;

    //立即提现
    @BindView(R.id.tv_other_withdrawal)
    public TextView tvOtherWithdrawal = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.ll_other_pull)
    public LinearLayout llOtherPull = null;

    private boolean isConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_other);

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
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {
        srlOtherPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        srlOtherPull.setEnabled(false);
        initPull(false);
//        initPosition(false);
    }

    /**
     * 任务
     *
     * @param isToast
     */
    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.OTHER + cartState.getUser().getId(), isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---任务---" + response);
                if (srlOtherPull != null) {
                    srlOtherPull.setEnabled(true);
                    srlOtherPull.setRefreshing(false);
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
                        llOtherPull.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        JSONObject data = resultJSON.getJSONObject("data");
                        cartState.initToast(context, msg, true, 0);
                        //日期
                        tvOtherTimer.setText(CartCalender.getYear() + "-" + CartCalender.getMonth());
                        //本月任务
                        tvOtherTotal.setText(data.getInteger("work_day") + "");
                        //本月完成
                        tvOtherHas.setText(data.getInteger("vacation_day") + "");
                        //本月上班
                        tvOtherOn.setText(data.getInteger("task_num") + "");
                        //本月休假
                        tvOtherHugh.setText(data.getInteger("finish_task") + "");
                        //工资
                        JSONObject payroll = data.getJSONObject("payroll");
                        tvOtherDetail.setText(payroll.getString("real_wage"));
                        //签字人员
                        tvOtherName.setText(cartState.getUser().getName());
                        int confirm = payroll.getInteger("is_confirm");
                        switch (confirm) {
                            case 0:
                                //代表未签字
                                isConfirm = false;
                                tvOtherWithdrawal.setText("马上签字");
                                tvOtherWithdrawal.setBackgroundColor(ContextCompat.getColor(context, R.color.tab_main_text_icon));
                                break;
                            case 1:
                                //代表签字了
                                isConfirm = true;
                                tvOtherWithdrawal.setText("已签字");
                                tvOtherWithdrawal.setBackgroundColor(ContextCompat.getColor(context, R.color.service_bak));
                                break;
                            default:
                                //TODO 签字状态
                                break;
                        }
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        llOtherPull.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlOtherPull != null) {
                    srlOtherPull.setEnabled(true);
                    srlOtherPull.setRefreshing(false);
                }
                rlOrdersBar.setVisibility(View.GONE);
                llOtherPull.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    /**
     * 确定工资
     */
    private void initPosition(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staff_id", cartState.getUser().getId());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.POSTIION, isToast, "签字中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---工资---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        cartState.initToast(context, "签字成功", true, 0);
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
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_other_return, R.id.tv_other_withdrawal})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_other_return:
                finish();
                break;
            //马上签字
            case R.id.tv_other_withdrawal:
                if (isConfirm) {
                    cartState.initToast(context, "已签字,请勿重复操作", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(22, "提示", "是否签字", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "是否签字");
                break;
        }
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
     * 立即提现
     *
     * @param w
     */
    @Subscribe
    public void onEventMainThread(WithdrawalFind w) {
        initPosition(true);
    }

}
