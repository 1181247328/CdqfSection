package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.AccountDatilsContextAdapter;
import com.cdqf.cart_adapter.AccountDatilsImageAdapter;
import com.cdqf.cart_class.AccountDatils;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.AuditPositionFind;
import com.cdqf.cart_find.AuditPullFind;
import com.cdqf.cart_find.AuditsAgreedFind;
import com.cdqf.cart_find.AuditsDatilsCencalFind;
import com.cdqf.cart_find.ThroughPullFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.MyGridView;
import com.gcssloop.widget.RCRelativeLayout;
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
 * 审批详情
 */
public class AuditsDatilsActivity extends BaseActivity {

    private String TAG = AccountDatilsActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_datils_pull)
    public SwipeRefreshLayout srlDatilsPull = null;

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    //工号
    @BindView(R.id.tv_datils_title)
    public TextView tvDatilsTitle = null;

    @BindView(R.id.sv_datils_view)
    public NestedScrollView svDatilsView = null;

    //时间
    @BindView(R.id.tv_datils_data)
    public TextView tvDatilsData = null;

    @BindView(R.id.lvfsv_datils_list)
    public ListViewForScrollView lvfsvDatilsList = null;

    private AccountDatilsContextAdapter accountDatilsContextAdapter = null;

    //总额
    @BindView(R.id.tv_datils_total)
    public TextView tvDatilsTotal = null;

    @BindView(R.id.mgv_datals_list)
    public MyGridView mgvDatalsList = null;

    private AccountDatilsImageAdapter accountDatilsImageAdapter = null;

    //状态
    @BindView(R.id.ll_datils_state)
    public LinearLayout llDatilsState = null;

    @BindView(R.id.tv_datils_state)
    public TextView tvDatilsState = null;

    /****待审批状态*****/
    @BindView(R.id.ll_datis_operation)
    public LinearLayout llDatisOperation = null;

    //取消
    @BindView(R.id.rcrl_datils_cencal)
    public RCRelativeLayout rcrlDatilsCencal = null;

    //同意
    @BindView(R.id.rcrl_datils_agreed)
    public RCRelativeLayout rcrlDatilsAgreed = null;

    @BindView(R.id.tv_datils_credentials)
    public TextView tvDatilsCredentials = null;

    private AccountDatils accountDatils = null;

    private int position;

    private int type = 0;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.rl_datils_pull)
    public RelativeLayout rlDatilsPull = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_audistsdatils);

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
        imageLoader = cartState.getImageLoader(context);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        type = intent.getIntExtra("type", 0);
    }

    private void initView() {

    }

    private void initAdapter() {
        accountDatilsContextAdapter = new AccountDatilsContextAdapter(context);
        lvfsvDatilsList.setAdapter(accountDatilsContextAdapter);
        accountDatilsImageAdapter = new AccountDatilsImageAdapter(context);
        mgvDatalsList.setAdapter(accountDatilsImageAdapter);
    }

    private void initListener() {
        mgvDatalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        srlDatilsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        srlDatilsPull.setEnabled(false);
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        int id = 0;
        String timer = "";
        switch (type) {
            case 1:
                //待审批
                id = cartState.getAuditsJudgeList().get(position).getExamines().getId();
                timer = cartState.getAuditsJudgeList().get(position).getCreated_at();
                llDatisOperation.setVisibility(View.VISIBLE);
                break;
            case 2:
                //已通过
                id = cartState.getThroughsJudgeList().get(position).getExamines().getId();
                timer = cartState.getThroughsJudgeList().get(position).getCreated_at();
                llDatisOperation.setVisibility(View.GONE);
                break;
        }
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String datils = CartAddaress.ADDRESS + "/staff/examine/" + id + "";
        Log.e(TAG, "----审核详情---" + datils);
        final String finalTimer = timer;
        okHttpRequestWrap.get(datils, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---审核详情---" + response);
                if (srlDatilsPull != null) {
                    srlDatilsPull.setEnabled(true);
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
                        rlOrdersBar.setVisibility(View.GONE);
                        rlDatilsPull.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, msg, true, 0);
                        accountDatils = gson.fromJson(data, AccountDatils.class);
                        tvDatilsTitle.setText(accountDatils.getLogin_account());
                        tvDatilsData.setText(finalTimer);
                        accountDatilsContextAdapter.setContext(accountDatils.getShop_new_name(),
                                "￥" + accountDatils.getExamine_price(),
                                accountDatils.getType(),
                                accountDatils.getDescribe());
                        int state = Integer.parseInt(accountDatils.getStatus());
                        String states = "";
                        switch (state) {
                            case 0:
                                states = "待审批";
                                break;
                            case 1:
                                states = "已通过";
                                break;
                        }
                        tvDatilsState.setText(states);
                        if (accountDatils.getImg().size() <= 0) {
                            tvDatilsCredentials.setVisibility(View.VISIBLE);
                            mgvDatalsList.setVisibility(View.GONE);
                        } else {
                            tvDatilsCredentials.setVisibility(View.GONE);
                            mgvDatalsList.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        rlDatilsPull.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlDatilsPull != null) {
                    srlDatilsPull.setEnabled(true);
                    srlDatilsPull.setRefreshing(false);
                }
                rlOrdersBar.setVisibility(View.GONE);
                rlDatilsPull.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_datils_return, R.id.rcrl_datils_cencal, R.id.rcrl_datils_agreed})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_datils_return:
                finish();
                break;
            //取消
            case R.id.rcrl_datils_cencal:
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(19, "提示", "是否拒绝当前报销", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "取消报销");
                break;
            //同意
            case R.id.rcrl_datils_agreed:
                WhyDilogFragment whyAgreedDilogFragment = new WhyDilogFragment();
                whyAgreedDilogFragment.setInit(20, "提示", "是否同意当前报销", "否", "是");
                whyAgreedDilogFragment.show(getSupportFragmentManager(), "同意报销");
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
     * 取消报销
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(AuditsDatilsCencalFind s) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", accountDatils.getId());
        params.put("staff_id", cartState.getUser().getId());
        params.put("type", 2);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.AGRED_AUDITS, true, "拒绝中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---拒绝报销---" + response);

                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, msg, true, 0);
                        eventBus.post(new AuditPullFind());
                        finish();
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

    /**
     * 同意报销
     *
     * @param a
     */
    @Subscribe
    public void onEventMainThread(AuditsAgreedFind a) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", accountDatils.getId());
        params.put("staff_id", cartState.getUser().getId());
        params.put("type", 1);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.AGRED_AUDITS, true, "同意中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---同意报销---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, msg, true, 0);
                        eventBus.post(new AuditPullFind());
                        eventBus.post(new ThroughPullFind());
                        eventBus.post(new AuditPositionFind(1));
                        finish();
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

}
