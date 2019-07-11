package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_dilog.OtherDilogFragment;
import com.cdqf.cart_dilog.ServiceDilogFragment;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

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

        //加载布局
        setContentView(R.layout.actiivty_addorder);

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

    @OnClick({R.id.rl_order_return, R.id.ll_order_type, R.id.ll_order_color, R.id.ll_order_service, R.id.ll_order_other, R.id.tv_order_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_order_return:
                finish();
                break;
            //车辆车型
            case R.id.ll_order_type:
                SinglePicker<String> pickerSource = new SinglePicker<String>(AddOrderActivity.this, new String[]{
                        "USA"
                });
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
                    }
                });
                pickerSource.show();
                break;
            //车辆颜色
            case R.id.ll_order_color:
                SinglePicker<String> pickerSourcecolor = new SinglePicker<String>(AddOrderActivity.this, new String[]{
                        "黄色"
                });
                LineConfig configSourcecolor = new LineConfig();
                configSourcecolor.setColor(ContextCompat.getColor(context, R.color.addstore_one));//线颜色
                configSourcecolor.setThick(ConvertUtils.toPx(context, 1));//线粗
                configSourcecolor.setItemHeight(20);
                pickerSourcecolor.setLineConfig(configSourcecolor);
                pickerSourcecolor.setCanLoop(false);//不禁用循环
                pickerSourcecolor.setLineVisible(true);
                pickerSourcecolor.setTopLineColor(Color.TRANSPARENT);
                pickerSourcecolor.setTextSize(14);
                pickerSourcecolor.setTitleText("店名");
                pickerSourcecolor.setSelectedIndex(0);
                pickerSourcecolor.setWheelModeEnable(true);
                pickerSourcecolor.setWeightEnable(true);
                pickerSourcecolor.setWeightWidth(1);
                pickerSourcecolor.setCancelTextColor(ContextCompat.getColor(context, R.color.house_eight));//顶部取消按钮文字颜色
                pickerSourcecolor.setCancelTextSize(14);
                pickerSourcecolor.setSubmitTextColor(ContextCompat.getColor(context, R.color.house_eight));//顶部确定按钮文字颜色
                pickerSourcecolor.setSubmitTextSize(14);
                pickerSourcecolor.setBackgroundColor(ContextCompat.getColor(context, R.color.white));//背景色
                pickerSourcecolor.setSelectedTextColor(ContextCompat.getColor(context, R.color.house_eight));//前四位值是透明度
                pickerSourcecolor.setUnSelectedTextColor(ContextCompat.getColor(context, R.color.addstore_one));
                pickerSourcecolor.setOnSingleWheelListener(new OnSingleWheelListener() {
                    @Override
                    public void onWheeled(int index, String item) {

                    }
                });
                pickerSourcecolor.setOnItemPickListener(new OnItemPickListener<String>() {
                    @Override
                    public void onItemPicked(int index, String item) {

                    }
                });
                pickerSourcecolor.show();
                break;
            //服务项目
            case R.id.ll_order_service:
                ServiceDilogFragment serviceDilogFragment = new ServiceDilogFragment();
                serviceDilogFragment.show(getSupportFragmentManager(), "服务项目");
                break;
            //其它服务
            case R.id.ll_order_other:
                OtherDilogFragment otherDilogFragment = new OtherDilogFragment();
                otherDilogFragment.show(getSupportFragmentManager(), "其它服务");
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
