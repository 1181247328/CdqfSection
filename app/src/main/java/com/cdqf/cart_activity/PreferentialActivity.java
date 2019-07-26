package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_find.PreferentialFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.CashierInputFilter;
import com.cdqf.cart_state.DoubleOperationUtil;
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
 * 给予优惠
 */
public class PreferentialActivity extends BaseActivity {

    private String TAG = PreferentialActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_preferential_pull)
    public SwipeRefreshLayout srlPreferentialPull = null;

    //返回
    @BindView(R.id.rl_preferential_return)
    public RelativeLayout rlPreferentialReturn = null;

    //车牌号
    @BindView(R.id.tv_preferential_number)
    public TextView tvPreferentialNumber = null;

    //金额
    @BindView(R.id.tv_preferential_mount)
    public TextView tvPreferentialMount = null;

    //优惠折扣
    @BindView(R.id.et_preferential_discount)
    public EditText etPreferentialDiscount = null;

    //折扣价
    @BindView(R.id.tv_preferential_discount)
    public TextView tvPreferentialDiscount = null;

    //返余额
    @BindView(R.id.et_preferential_money)
    public EditText etPreferentialMoney = null;

    //服务项目
    @BindView(R.id.tv_preferential_add)
    public TextView tvPreferentialAdd = null;

    //提交
    @BindView(R.id.ll_preferential_submit)
    public LinearLayout llPreferentialSubmit = null;

    private double price = 0;

    private double discounts = 0;

    private double discountTurn = 0;

    private double money = 0;

    private boolean discountFocus = false;

    private boolean moneyFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_preferential);

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
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {
        srlPreferentialPull.setEnabled(false);
        srlPreferentialPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });

        etPreferentialDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                discountFocus = hasFocus;
                if (hasFocus) {
                    String discount = etPreferentialDiscount.getText().toString();
                    discount(discount);
                } else {

                }
            }
        });

        etPreferentialDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!discountFocus) {
                    return;
                }
                String discount = etPreferentialDiscount.getText().toString();
                discount(discount);
            }
        });
        etPreferentialMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                moneyFocus = hasFocus;
                if (hasFocus) {
//                    etPreferentialMoney.setText("");
//                    etPreferentialDiscount.setText("");
                    String moneys = etPreferentialMoney.getText().toString();
                    moneys(moneys);
                } else {

                }
            }
        });
        etPreferentialMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!moneyFocus) {
                    return;
                }
//                //获得字符串的返余额
                String moneys = etPreferentialMoney.getText().toString();
                moneys(moneys);
            }
        });
    }

    private void initBack() {
        //车牌号
        tvPreferentialNumber.setText(cartState.getDatils().getCarnum());
        //金额
        tvPreferentialMount.setText(cartState.getDatils().getZongprice());
        //折扣价
//        tvPreferentialDiscount.setText("");
        //返余额
//        tvPreferentialMoney.setText("");
        //服务项目
        tvPreferentialAdd.setText(cartState.getDatils().getGoods_names());
        InputFilter[] filters = {new CashierInputFilter(10)};
        etPreferentialDiscount.setFilters(filters);
        etPreferentialDiscount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        //订单号
        String order_num = cartState.getDatils().getOrdernum();
        params.put("order_num", order_num);
        //用户id
        int user_id = cartState.getDatils().getUserid();
        params.put("user_id", user_id);
        //金额
        params.put("money", price + "");
        //折扣扣数
        String discountTurn = etPreferentialDiscount.getText().toString();
        params.put("discount_num", discountTurn + "");
        //折扣价
        String discounts = tvPreferentialDiscount.getText().toString();
        params.put("discount_money", discounts + "");
        //返余额
        String money = etPreferentialMoney.getText().toString();
        params.put("balance", money + "");
        Log.e(TAG, "---返余额---" + money);
        //服务项目
        String goodsNmae = "";
//        goodsNmae = cartState.getDatils().getGoods_names();
//        for (String name : cartState.getDatils().getGoodsname()) {
//            goodsNmae += name + " ";
//        }
        params.put("info", goodsNmae);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        Log.e(TAG, "---" + CartAddaress.SHOP_PREFERENT);
        okHttpRequestWrap.post(CartAddaress.SHOP_PREFERENT, isToast, "提交中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse给予优惠---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        cartState.initToast(context, "提交成功", true, 0);
                        eventBus.post(new DatilsPullFind());
                        finish();
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
        result = CartAddaress.ADDRESS_THE + "/?s=Order.getorderinfo&ordernum=" + ordernum;
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

    private void discount(String discount) {
        int number = discount.length();
        String point = "";
        if (number == 2) {
            point = discount.substring(1, 2);
        }
        if (TextUtils.equals(point, ".")) {
            Log.e(TAG, "---点不做处理---");
            return;
        }
        if (number >= 1) {
            Log.e(TAG, "---折扣字符---" + discount);
            //折扣扣数
            discountTurn = Double.parseDouble(discount) / 10;
            Log.e(TAG, "---折扣双精度---" + discountTurn);
            //商品原价
            price = Double.parseDouble(cartState.getDatils().getZongprice());
            //折扣价
            discounts = DoubleOperationUtil.mul(price, discountTurn);
            tvPreferentialDiscount.setText(discounts + "");
            //返余额
            money = DoubleOperationUtil.sub(price, discounts);
            etPreferentialMoney.setText(money + "");
        } else {
            tvPreferentialDiscount.setText("");
            etPreferentialMoney.setText("");
        }
    }

    private void moneys(String moneys) {
        if (moneys.length() <= 0) {
            return;
        }
        if (TextUtils.equals(moneys.substring(moneys.length() - 1), ".")) {
            return;
        }
        //返的余额
        double money = Double.parseDouble(moneys);
        //商品原价
        price = Double.parseDouble(cartState.getDatils().getZongprice());
        if (money > price) {
            return;
        }
        //折扣扣数
//        discountTurn = DoubleOperationUtil.sub(1.0, DoubleOperationUtil.div(money, price)) * 10;
//        etPreferentialDiscount.setText(DoubleOperationUtil.round(discountTurn, 2) + "");
        etPreferentialDiscount.setText("");
        //折扣价
        discounts = DoubleOperationUtil.sub(price, money);
        tvPreferentialDiscount.setText(discounts + "");
    }

    @OnClick({R.id.rl_preferential_return, R.id.ll_preferential_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_preferential_return:
                finish();
                break;
            //提交
            case R.id.ll_preferential_submit:
                String dis = etPreferentialDiscount.getText().toString();
                String disMoney = etPreferentialMoney.getText().toString();
                if (dis.length() <= 0 && disMoney.length() <= 0) {
                    cartState.initToast(context, "请输入返余额或折扣", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(14, "提交", "您正在进行提交操作", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "提交操作");
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
    public void onEventMainThread(PreferentialFind r) {
        initPull(true);
    }
}
