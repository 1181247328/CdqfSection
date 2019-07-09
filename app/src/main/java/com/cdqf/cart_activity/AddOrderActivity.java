package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 加订单
 */
public class AddOrderActivity extends BaseActivity {

    private String TAG = AddOrderActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_order_return)
    public RelativeLayout rlOrderReturn = null;

    //车牌号码
    @BindView(R.id.tv_order_number)
    public TextView tvOrderNumber = null;

    //车辆车型
    @BindView(R.id.ll_order_type)
    public LinearLayout llOrderType = null;
    @BindView(R.id.tv_order_type)
    public TextView tvOrderType = null;

    //车辆颜色
    @BindView(R.id.ll_order_color)
    public LinearLayout llOrderColor = null;
    @BindView(R.id.tv_order_color)
    public TextView tvOrderColor = null;

    //服务项目
    @BindView(R.id.ll_order_service)
    public LinearLayout llOrderService = null;
    @BindView(R.id.tv_order_service)
    public TextView tvOrderService = null;

    //其它服务
    @BindView(R.id.ll_order_other)
    public LinearLayout llOrderOther = null;
    @BindView(R.id.tv_order_other)
    public TextView tvOrderOther = null;

    //备注
    @BindView(R.id.tv_datils_note)
    public TextView tvDatilsNote = null;

    //选择支付方式
    @BindView(R.id.rg_order_pay)
    public RadioGroup rgOrderPay = null;

    //余额
    @BindView(R.id.rb_order_payone)
    public RadioButton rbOrderPayone = null;

    //现金
    @BindView(R.id.rb_order_paytwo)
    public RadioButton rbOrderPaytwo = null;

    //农商
    @BindView(R.id.rb_order_paythree)
    public RadioButton rbOrderPaythree = null;

    //提交订单
    @BindView(R.id.tv_order_submit)
    public TextView tvOrderSubmit = null;

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
        setContentView(R.layout.actiivty_addorder);

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
        rgOrderPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_order_payone:
                        //余额
                        break;
                    case R.id.rb_order_paytwo:
                        //现金
                        break;
                    case R.id.rb_order_paythree:
                        //农商
                        break;
                }
            }
        });
    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_order_return, R.id.ll_order_type, R.id.ll_order_color, R.id.ll_order_service, R.id.tv_order_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_order_return:
                finish();
                break;
            //车辆车型
            case R.id.ll_order_type:
                break;
            //车辆颜色
            case R.id.ll_order_color:
                break;
            //服务项目
            case R.id.ll_order_service:
                break;
            //提交订单
            case R.id.tv_order_submit:
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
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughFind t) {

    }

}
