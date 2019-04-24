package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_class.Datils;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.DatilsPhoneFind;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

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

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    //金额
    @BindView(R.id.tv_datils_mount)
    public TextView tvDatilsMount = null;

    //账户
    @BindView(R.id.tv_datils_user)
    public TextView tvDatilsUser = null;

    //电话
    @BindView(R.id.tv_datils_phone)
    public TextView tvDatilsPhone = null;

    //电话拨打
    @BindView(R.id.rcrl_datils_call)
    public RCRelativeLayout rcrlDatilsCall = null;

    //服务项目
    @BindView(R.id.tv_datils_add)
    public TextView tvDatilsAdd = null;

    //追加
    @BindView(R.id.tv_datils_adds)
    public TextView tvDatilsAdds = null;

    //订单编号
    @BindView(R.id.tv_datils_serial)
    public TextView tvDatilsSerial = null;

    //下单时间
    @BindView(R.id.tv_datils_timer)
    public TextView tvDatilsTimer = null;

    //备注
    @BindView(R.id.tv_datils_note)
    public TextView tvDatilsNote = null;

    //追加服务
    @BindView(R.id.rcrl_datils_add)
    public RCRelativeLayout rcrlDatilsAdd = null;

    private int position = 0;

    private Datils datils = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //加载布局
        setContentView(R.layout.activity_datils);

        //API>=20以上用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉侵
            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.black));
        }

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

        srlDatilsPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String ordernum = ordernum(cartState.getShopList().get(position).getOrdernum());
        okHttpRequestWrap.post(ordernum, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse详情---" + response);
                if (srlDatilsPull != null) {
                    srlDatilsPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        datils = gson.fromJson(data, Datils.class);
                        //金额
                        tvDatilsMount.setText(datils.getZongprice());
                        //电话
                        tvDatilsPhone.setText(datils.getPhone());
                        //服务项目
                        String goodsNmae = "";
                        for (String name : datils.getGoodsname()) {
                            goodsNmae += name + " ";
                        }
                        tvDatilsAdd.setText(goodsNmae);
                        //订单编号
                        tvDatilsSerial.setText(datils.getOrdernum());
                        //下单时间
                        tvDatilsTimer.setText(datils.getAddtime());
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

    private String ordernum(String ordernum) {
        String result = null;
        result = CartAddaress.ADDRESS + "/?s=Order.getorderinfo&ordernum=" + ordernum;
        Log.e(TAG, "---详情---" + result);
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @OnClick({R.id.rl_datils_return, R.id.rcrl_datils_call, R.id.rcrl_datils_add})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_datils_return:
                finish();
                break;
            //电话拨打
            case R.id.rcrl_datils_call:
                WhyDilogFragment whyTwoDilogFragment = new WhyDilogFragment();
                whyTwoDilogFragment.setInit(7, "提示", "是否拨打" + datils.getPhone() + "的电话", "否", "是");
                whyTwoDilogFragment.show(getSupportFragmentManager(), "联系我们");
                break;
            //追加服务
            case R.id.rcrl_datils_add:
                initIntent(UserActivity.class, position);
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

    public void onEventMainThread(DatilsPullFind r) {
        initPull(true);
    }

    /**
     * 输入领取数量
     *
     * @param r
     */
    public void onEventMainThread(DatilsPhoneFind r) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + datils.getPhone()));
        startActivity(intent);
    }
}
