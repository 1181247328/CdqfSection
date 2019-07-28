package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_class.ServiceOrder;
import com.cdqf.cart_dilog.OtherDilogFragment;
import com.cdqf.cart_dilog.ServiceDilogFragment;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.AddOrderFind;
import com.cdqf.cart_find.OtherFind;
import com.cdqf.cart_find.ServiceOrderFind;
import com.cdqf.cart_find.ServicePullFind;
import com.cdqf.cart_find.ShopPositionFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
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

    //手机号码
    @BindView(R.id.tv_order_phone)
    public EditText tvOrderPhone = null;

    //车牌号码
    @BindView(R.id.tv_order_number)
    public EditText tvOrderNumber = null;

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

    //手机号
    private String phone = "";

    //车牌号
    private String number = "";

    //车辆车型
    private String type = "";

    //颜色
    private String color = "";
    private int colorId = 0;

    //服务项目
    private String services = "";

    private int serviceId = 0;

    private List<Integer> serviceList = new CopyOnWriteArrayList<>();

    private boolean isService = false;

    //其它服务
    private String others = "";

    //备注
    private String note = "";

    //服务项目
    private String name;

    //服务金额
    private String project;

    //耗材成本
    private String cost;

    //支付方式
    private int pay = 0;

    //小轿车1,SUV = 2;
    private int model = 0;

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
                        pay = 1;
                        break;
                    case R.id.rb_order_paytwo:
                        //现金
                        pay = 3;
                        break;
                    case R.id.rb_order_paythree:
                        //农商
                        pay = 4;
                        break;
                }
            }
        });
    }

    private void initBack() {
        initColorPull();
        initServicePull();
    }

    /**
     * 获取车辆颜色
     */
    private void initColorPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.get(CartAddaress.COLOR, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---获取颜色---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 200:
                        cartState.initToast(context, msg, true, 0);
                        String data = resultJSON.getString("data");
                        cartState.getColorList().clear();
                        List<com.cdqf.cart_class.Color> colorList = gson.fromJson(data, new TypeToken<List<com.cdqf.cart_class.Color>>() {
                        }.getType());
                        cartState.setColorList(colorList);
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
     * 获取服务项目
     */
    private void initServicePull() {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        params.put("shop_id", cartState.getUser().getShopid());
        okHttpRequestWrap.postString(CartAddaress.SERVICE_ITEM, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---服务项目---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, msg, true, 0);
                        cartState.getServiceOrderList().clear();
                        List<ServiceOrder> colorList = gson.fromJson(data, new TypeToken<List<ServiceOrder>>() {
                        }.getType());
                        cartState.setServiceOrderList(colorList);
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
                        "小轿车", "SUV"
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
                        tvOrderService.setText("");
                        tvOrderOther.setText("");
                        if (index == 0) {
                            //小轿车
                            model = 1;
                        } else if (index == 1) {
                            //SUV
                            model = 2;
                        }
                        cartState.setModel(model);
                        tvOrderType.setText(item);
                    }
                });
                pickerSource.show();
                break;
            //车辆颜色
            case R.id.ll_order_color:
                if (cartState.getColorList().size() <= 0) {
                    initColorPull();
                    return;
                }
                String[] colorName = new String[cartState.getColorList().size()];
                for (int i = 0; i < cartState.getColorList().size(); i++) {
                    colorName[i] = cartState.getColorList().get(i).getColor_name();
                }
                SinglePicker<String> pickerSourcecolor = new SinglePicker<String>(AddOrderActivity.this, colorName);
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
                        colorId = cartState.getColorList().get(index).getId();
                        tvOrderColor.setText(item);
                    }
                });
                pickerSourcecolor.show();
                break;
            //服务项目
            case R.id.ll_order_service:
                if (model == 0) {
                    cartState.initToast(context, "请先选择车型", true, 0);
                    return;
                }
                //判断其它服务是不是已经填写
//                String other = tvOrderOther.getText().toString();
//                if (other.length() <= 0) {
                ServiceDilogFragment serviceDilogFragment = new ServiceDilogFragment();
                serviceDilogFragment.show(getSupportFragmentManager(), "服务项目");
//                } else {
//                    cartState.initToast(context, "其它服目中已经选择了服务项目", true, 0);
//                }
                break;
            //其它服务
            case R.id.ll_order_other:
                if (model == 0) {
                    cartState.initToast(context, "请先选择车型", true, 0);
                    return;
                }
//                String service = tvOrderService.getText().toString();
//                if (service.length() <= 0) {
                OtherDilogFragment otherDilogFragment = new OtherDilogFragment();
                otherDilogFragment.show(getSupportFragmentManager(), "其它服务");
//                } else {
//                    cartState.initToast(context, "服务项目已存在", true, 0);
//                }
                break;
            //提交订单
            case R.id.tv_order_submit:
                //手机号
                phone = tvOrderPhone.getText().toString();
                if (phone.length() <= 0) {
                    cartState.initToast(context, "手机号不能为空", true, 0);
                    return;
                }
                if (!cartState.isMobile(phone)) {
                    cartState.initToast(context, "请输入正确的手机号", true, 0);
                    return;
                }
                //车牌号
                number = tvOrderNumber.getText().toString();
                if (number.length() <= 0) {
                    cartState.initToast(context, "车牌号不能为空", true, 0);
                    return;
                }
                if (!cartState.licensePlate(number)) {
                    cartState.initToast(context, "请输入正确的车牌号", true, 0);
                    return;
                }
                //车辆车型
                type = tvOrderType.getText().toString();
                if (type.length() <= 0) {
                    cartState.initToast(context, "请选择车辆车型", true, 0);
                    return;
                }
                //车辆颜色
                color = tvOrderColor.getText().toString();
                if (color.length() <= 0) {
                    cartState.initToast(context, "请选择车辆颜色", true, 0);
                    return;
                }
                //服务
                services = tvOrderService.getText().toString();
                others = tvOrderOther.getText().toString();
                if (services.length() <= 0 && others.length() <= 0) {
                    cartState.initToast(context, "请选择服务项目", true, 0);
                    return;
                }
                //支付方式
                if (pay == 0) {
                    cartState.initToast(context, "请选择支付方式", true, 0);
                    return;
                }

                //备注
                note = tvDatilsNote.getText().toString();
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(16, "提示", "是否添加订单", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "添加订单");
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
        cartState.setModel(0);
    }

    /**
     * 添加订单
     *
     * @param a
     */
    @Subscribe
    public void onEventMainThread(AddOrderFind a) {
        Map<String, Object> params = new HashMap<String, Object>();
        //店铺id
        params.put("shop_id", cartState.getUser().getShopid());
        //手机号
        params.put("phone", phone);
        //车辆车型
        params.put("car_type", model);
        //车牌号
        params.put("car_num", number);
        if (isService) {
            //服务项目
            params.put("goods_id", serviceList);
        } else {
            //其它服务
            params.put("goods_id", 0);
            params.put("name", name);
            params.put("cost_price", cost);
            params.put("amount", project);
        }
        //车辆颜色
        params.put("color", color);
        //备注
        params.put("remarks", note);
        //支付方式
        params.put("pay_type", pay);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.ADD_ORDER, true, "添加中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---添加订单---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 200:
                        cartState.initToast(context, msg, true, 0);
                        eventBus.post(new ShopPositionFind(0));
                        eventBus.post(new ServicePullFind(true));
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
     * 获取服务项
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ServiceOrderFind s) {
        isService = true;
        serviceList.add(cartState.getServiceOrderList().get(s.position).getId());
        tvOrderService.setText(cartState.getServiceOrderList().get(s.position).getGoods_name());
        tvOrderOther.setText("");
    }

    /**
     * 设置其它项目
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(OtherFind s) {
        isService = false;
        name = s.name;
        project = s.project;
        cost = s.cost;
        tvOrderOther.setText(name + "," + project + "," + cost);
        tvOrderService.setText("");
    }
}
