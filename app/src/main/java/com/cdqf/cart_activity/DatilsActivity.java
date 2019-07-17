package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.DatilsRemarksAdapter;
import com.cdqf.cart_class.Datils;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.DatilsPhoneFind;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
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
 * 订单详情
 */
public class DatilsActivity extends BaseActivity {

    private String TAG = DatilsActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_datils_pull)
    public SwipeRefreshLayout srlDatilsPull = null;

    @BindView(R.id.sv_datils_sc)
    public ScrollView svDatilsSc = null;

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    //平台
    @BindView(R.id.tv_datils_way)
    public TextView tvDatilsWay = null;

    //车牌号
    @BindView(R.id.tv_datils_number)
    public TextView tvDatilsNumber = null;

    //金额
    @BindView(R.id.tv_datils_mount)
    public TextView tvDatilsMount = null;

    //耗材成本
    @BindView(R.id.tv_datils_eliminatecost)
    public TextView tvDatilsEliminatecost = null;

    //服务项目
    @BindView(R.id.tv_datils_add)
    public TextView tvDatilsAdd = null;

    //服务人员
    @BindView(R.id.tv_datils_people)
    public TextView tvDatilsPeople = null;

    //手机号码
    @BindView(R.id.tv_datils_phone)
    public TextView tvDatilsPhone = null;

    //订单编号
    @BindView(R.id.tv_datils_serial)
    public TextView tvDatilsSerial = null;

    //下单时间
    @BindView(R.id.tv_datils_timer)
    public TextView tvDatilsTimer = null;

    //备注
    @BindView(R.id.tv_datils_note)
    public TextView tvDatilsNote = null;

    //电话拨打
    @BindView(R.id.rcrl_datils_call)
    public RCRelativeLayout rcrlDatilsCall = null;

    //追加服务
    @BindView(R.id.rcrl_datils_add)
    public RCRelativeLayout rcrlDatilsAdd = null;

    //给予优惠
    @BindView(R.id.rcrl_datils_preferential)
    public RCRelativeLayout rcrlDatilsPreferential = null;

    //添加备注
    @BindView(R.id.rcrl_datils_note)
    public RCRelativeLayout rcrlDatilsNote = null;

    @BindView(R.id.ll_datils_preferential)
    public LinearLayout llDatilsPreferential = null;

    @BindView(R.id.ll_datils_discount)
    public LinearLayout llDatilsDiscount = null;

    @BindView(R.id.ll_datils_money)
    public LinearLayout llDatilsMoney = null;

    //会员备注
    @BindView(R.id.ll_datils_list)
    public LinearLayout llDatilsList = null;
    @BindView(R.id.lvfsv_datils_list)
    public ListViewForScrollView lvfsvDatilsList = null;

    private int position = 0;

    private int type = 0;

    private Datils datils = null;

    private DatilsRemarksAdapter datilsRemarksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_datils);

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
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        type = intent.getIntExtra("type", 0);
        imageLoader = cartState.getImageLoader(context);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {
        datilsRemarksAdapter = new DatilsRemarksAdapter(context);
        lvfsvDatilsList.setAdapter(datilsRemarksAdapter);
    }

    private void initListener() {
        srlDatilsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });

        svDatilsSc.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                srlDatilsPull.setEnabled(svDatilsSc.getScrollY() == 0);
            }
        });
    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        int id = 0;
        switch (type) {
            case 1:
                //待服务
                id = cartState.getServiceLis().get(position).getId();
                break;
            case 2:
                //已完成
                id = cartState.getCompleteList().get(position).getId();
                break;
            case 3:
                //待付款
                id = cartState.getEntryList().get(position).getId();
                break;
        }
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String DATILS = CartAddaress.ADDRESS + "/staff/order/" + id + "";
        Log.e(TAG, "---订单详情---" + DATILS);
        okHttpRequestWrap.get(DATILS, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse详情---" + response);
                if (srlDatilsPull != null) {
                    srlDatilsPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 200:
                        cartState.initToast(context, msg, true, 0);
                        String data = resultJSON.getString("data");
                        datils = gson.fromJson(data, Datils.class);
                        cartState.setDatils(datils);
                        //支付
                        String pay = "";
                        switch (datils.getPay_type()) {
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
                        tvDatilsWay.setText(pay);
                        //车牌号
                        tvDatilsNumber.setText(datils.getCarnum());
                        //消费金额
                        tvDatilsMount.setText("￥" + datils.getZongprice());
                        //耗材成本
                        tvDatilsEliminatecost.setText("￥" + datils.getCost_price() + "");
                        //服务项目
                        tvDatilsAdd.setText(datils.getGoods_names());
                        //服务人员
                        for (int i = 0; i < datils.getStaff_sevice().size(); i++) {
                            tvDatilsPeople.setText(datils.getStaff_sevice().get(i) + ",");
                        }
                        //手机号码
                        tvDatilsPhone.setText(datils.getPhone());
                        //订单编号
                        tvDatilsSerial.setText(datils.getOrdernum());
                        //下单时间
                        tvDatilsTimer.setText(datils.getAddtime());
                        //备注
                        tvDatilsNote.setText(datils.getRemarks());
                        //是否显示会员备注
                        if (datils.getUserid() == 0) {
                            llDatilsList.setVisibility(View.GONE);
                        } else {
                            llDatilsList.setVisibility(View.VISIBLE);
                            if (datilsRemarksAdapter != null) {
                                datilsRemarksAdapter.notifyDataSetChanged();
                            }
                        }
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

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntentUser(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("userType", type);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @OnClick({R.id.rl_datils_return, R.id.rcrl_datils_call, R.id.rcrl_datils_add, R.id.rcrl_datils_preferential, R.id.rcrl_datils_note})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_datils_return:
                finish();
                break;
            //电话拨打
            case R.id.rcrl_datils_call:
                WhyDilogFragment whyTwoDilogFragment = new WhyDilogFragment();
                //datils.getPhone()
                whyTwoDilogFragment.setInit(7, "提示", "是否拨打" + "的电话", "否", "是");
                whyTwoDilogFragment.show(getSupportFragmentManager(), "联系我们");
                break;
            //追加服务
            case R.id.rcrl_datils_add:
                initIntent(UserActivity.class, position);
                break;
            //给予优惠
            case R.id.rcrl_datils_preferential:
                initIntent(PreferentialActivity.class);
                break;
            //添加备注
            case R.id.rcrl_datils_note:
                initIntentUser(NoteActivity.class);
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
    public void onEventMainThread(DatilsPullFind r) {
        initPull(true);
    }

    /**
     * 输入领取数量
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(DatilsPhoneFind r) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + datils.getPhone()));
        startActivity(intent);
    }
}
