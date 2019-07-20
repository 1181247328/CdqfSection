package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.cdqf.cart_adapter.MembersDatilsAdapter;
import com.cdqf.cart_class.MembersDatils;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_find.ShopServiceTwoFind;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.util.ConvertUtils;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 会员详情
 */
public class MembersDatilsActivity extends BaseActivity {

    private String TAG = MembersDatilsActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    @BindView(R.id.srl_datils_pull)
    public SwipeRefreshLayout srlDatilspull = null;

    @BindView(R.id.sv_datils_pull)
    public NestedScrollView svDatilsPull = null;

    //标题
    @BindView(R.id.tv_datils_title)
    public TextView tvDatilsTitle = null;

    //手机号
    @BindView(R.id.tv_details_phone)
    public TextView tvDetailsPhone = null;

    //选择车牌号
    @BindView(R.id.rl_detils_next)
    public RelativeLayout rlDetilsNext = null;
    //车牌
    @BindView(R.id.tv_detils_next)
    public TextView tvDetilsNext = null;

    /**
     * 是否有订单
     */
    @BindView(R.id.tv_datils_order)
    public TextView tvDatilsOrder = null;

    @BindView(R.id.lvfsv_datails_list)
    public ListViewForScrollView lvfsvDatailsList = null;

    private MembersDatilsAdapter membersDatilsAdapter = null;

//    //平台
//    @BindView(R.id.tv_details_way)
//    public TextView tvDetailsWay = null;
//
//    //价格
//    @BindView(R.id.tv_details_price)
//    public TextView tvDetailsPrice = null;
//
//    //车牌
//    @BindView(R.id.tv_details_plate)
//    public TextView tvDetailsPlate = null;
//
//    //车型
//    @BindView(R.id.tv_details_type)
//    public TextView tvDetailsType = null;
//
//    //服务
//    @BindView(R.id.rcrl_details_claim)
//    public RCRelativeLayout rcrlDetailsClaim = null;
//
//    //加订单
//    @BindView(R.id.rcrl_details_addorder)
//    public RCRelativeLayout rcrlDetailsAddorder = null;
//
//    //下单时间
//    @BindView(R.id.tv_details_timer)
//    public TextView tvDetailsTimer = null;

    //备注
    @BindView(R.id.tv_datils_note)
    public TextView tvDatilsNote = null;

    //充值金额
    @BindView(R.id.tv_details_monery)
    public TextView tvDetailsMonery = null;

    //已用
    @BindView(R.id.tv_details_with)
    public TextView tvDetailsWith = null;

    //剩余
    @BindView(R.id.tv_details_remaining)
    public TextView tvDetailsRemaining = null;

    //次数
    @BindView(R.id.tv_details_number)
    public TextView tvDetailsNumber = null;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_memebersdatils);

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
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
    }

    private void initView() {

    }

    private void initAdapter() {
        membersDatilsAdapter = new MembersDatilsAdapter(context);
        lvfsvDatailsList.setAdapter(membersDatilsAdapter);
    }

    private void initListener() {
        srlDatilspull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", cartState.getMemebersshipList().get(position).getUserid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.MEMBERS_DATILS, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---会员详情---" + response);
                if (srlDatilspull != null) {
                    srlDatilspull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        MembersDatils membersDatils = gson.fromJson(data, MembersDatils.class);
                        cartState.setMembersDatils(membersDatils);
                        if (membersDatils.getService().size() <= 0) {
                            tvDatilsOrder.setVisibility(View.VISIBLE);
                            lvfsvDatailsList.setVisibility(View.GONE);
                        } else {
                            tvDatilsOrder.setVisibility(View.GONE);
                            lvfsvDatailsList.setVisibility(View.VISIBLE);
                        }
                        if (membersDatilsAdapter != null) {
                            membersDatilsAdapter.notifyDataSetChanged();
                        }
                        tvDetailsPhone.setText(membersDatils.getPhone());
                        if (membersDatils.getGet_cars().size() > 0) {
                            tvDetilsNext.setText(membersDatils.getGet_cars().get(0).getCarnum());
                        } else {
                            tvDetilsNext.setText("请下拉刷新获取");
                        }
                        tvDetailsMonery.setText("￥" + membersDatils.getBalance_price());
                        tvDetailsWith.setText("￥" + membersDatils.getPayment_price());
                        tvDetailsRemaining.setText("￥" + membersDatils.getUse_price());
                        //下单次数
//                        tvDetailsNumber.setText(membersDatils+"次");
                        break;
                    default:
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlDatilspull != null) {
                    srlDatilspull.setRefreshing(false);
                }
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_datils_return, R.id.rl_detils_next})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_datils_return:
                finish();
                break;
            //车牌号
            case R.id.rl_detils_next:
                if (cartState.getMembersDatils().getGet_cars().size() <= 0) {
                    return;
                }
                String[] datils = new String[cartState.getMembersDatils().getGet_cars().size()];
                for (int i = 0; i < cartState.getMembersDatils().getGet_cars().size(); i++) {
                    datils[i] = cartState.getMembersDatils().getGet_cars().get(i).getCarnum();
                }
                SinglePicker<String> pickerSource = new SinglePicker<String>(MembersDatilsActivity.this, datils);
                LineConfig configSource = new LineConfig();
                configSource.setColor(ContextCompat.getColor(context, R.color.addstore_one));//线颜色
                configSource.setThick(ConvertUtils.toPx(context, 1));//线粗
                configSource.setItemHeight(20);
                pickerSource.setLineConfig(configSource);
                pickerSource.setCanLoop(false);//不禁用循环
                pickerSource.setLineVisible(true);
                pickerSource.setTopLineColor(Color.TRANSPARENT);
                pickerSource.setTextSize(14);
                pickerSource.setTitleText("店名");
                pickerSource.setSelectedIndex(0);
                pickerSource.setWheelModeEnable(true);
                pickerSource.setWeightEnable(true);
                pickerSource.setWeightWidth(1);
                pickerSource.setCancelTextColor(ContextCompat.getColor(context, R.color.house_eight));//顶部取消按钮文字颜色
                pickerSource.setCancelTextSize(14);
                pickerSource.setSubmitTextColor(ContextCompat.getColor(context, R.color.house_eight));//顶部确定按钮文字颜色
                pickerSource.setSubmitTextSize(14);
                pickerSource.setBackgroundColor(ContextCompat.getColor(context, R.color.white));//背景色
                pickerSource.setSelectedTextColor(ContextCompat.getColor(context, R.color.house_eight));//前四位值是透明度
                pickerSource.setUnSelectedTextColor(ContextCompat.getColor(context, R.color.addstore_one));
                pickerSource.setOnSingleWheelListener(new OnSingleWheelListener() {
                    @Override
                    public void onWheeled(int index, String item) {

                    }
                });
                pickerSource.setOnItemPickListener(new OnItemPickListener<String>() {
                    @Override
                    public void onItemPicked(int index, String item) {
                        tvDetilsNext.setText(item);
                    }
                });
                pickerSource.show();
                break;
//            //服务
//            case R.id.rcrl_details_claim:
//                break;
//            //加订单
//            case R.id.rcrl_details_addorder:
//                initIntent(AddOrderActivity.class);
//                break;

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
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughFind t) {

    }

    /**
     * 服务第一次，用于提示
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShopServiceOneFind s) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(1, "提示", "是否领取", "否", "是", s.position);
        whyDilogFragment.show(getSupportFragmentManager(), "领取订单");
    }

    /**
     * 服务第二次
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShopServiceTwoFind s) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        params.put("order_num", cartState.getMembersDatils().getService().get(position).getOrdernum());
        params.put("staff_id", cartState.getUser().getId());
        okHttpRequestWrap.postString(CartAddaress.USER_SERVICE, true, "领取中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---服务---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int ret = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (ret) {
                    case 201:
                    case 204:
                    case 200:
                        String data = resultJSON.getString("data");
                        initPull(true);
                        cartState.initToast(context, "接单成功", true, 0);
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

}
