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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_find.MembersDatilsFind;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
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
 * 会员管理
 */
public class MembersActivity extends BaseActivity {
    private String TAG = MembersActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_members_pull)
    public SwipeRefreshLayout srlMembersPull = null;

    @BindView(R.id.nsv_members_sc)
    public NestedScrollView nsvMembersSc = null;

    //返回
    @BindView(R.id.rl_members_return)
    public RelativeLayout rlMembersReturn = null;

    //店名
    @BindView(R.id.ll_members_shop)
    public LinearLayout llMembersShop = null;
    @BindView(R.id.tv_members_shop)
    public TextView tvMembersShop = null;

    //手机号
    @BindView(R.id.et_members_phone)
    public EditText etMembersPhone = null;

    @BindView(R.id.tv_members_cancel)
    public TextView tvMembersCancel = null;

    //会员总数
    @BindView(R.id.tv_members_number)
    public TextView tvMembersNumber = null;

    //下单会员
    @BindView(R.id.tv_members_numbers)
    public TextView tvMembersNumbers = null;

    //总下单金额
    @BindView(R.id.tv_members_price)
    public TextView tvMembersprice = null;

    //已用
    @BindView(R.id.tv_members_with)
    public TextView tvMembersWith = null;

    //剩余
    @BindView(R.id.tv_members_left)
    public TextView tvMembersLeft = null;

    //会员总数
    @BindView(R.id.ll_members_number)
    public LinearLayout llMembersNumber = null;

    //下单总数
    @BindView(R.id.ll_members_numbers)
    public LinearLayout llMembersNumbers = null;

    private String phone = "";

    private int shopId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_members);

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
        shopId = Integer.parseInt(cartState.getUser().getShopid());
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {
        srlMembersPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initShopId(false);
            }
        });
    }

    private void initBack() {
        tvMembersShop.setText(cartState.getUser().getShopName());
        initShopId(true);
    }

    private void initShopId(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        //门店id
        params.put("shop_id", shopId);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.MEMBERS, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---会员管理店面---" + response);
                if (srlMembersPull != null) {
                    srlMembersPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        JSONObject data = resultJSON.getJSONObject("data");
//                        cartState.initToast(context, msg, true, 0);
                        tvMembersNumber.setText(data.getString("all_menber"));
                        tvMembersNumbers.setText(data.getString("shop_menber"));
                        tvMembersprice.setText("￥" + data.getString("shop_amount"));
                        tvMembersWith.setText(data.getString("user_amount_money"));
                        tvMembersLeft.setText(data.getString("user_surplus_money"));
                        break;
                    default:
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlMembersPull != null) {
                    srlMembersPull.setRefreshing(false);
                }
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int type) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("type", type);
        intent.putExtra("shopId", shopId);
        startActivity(intent);
    }

    @OnClick({R.id.rl_members_return, R.id.ll_members_shop, R.id.ll_members_number, R.id.ll_members_numbers, R.id.tv_members_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_members_return:
                finish();
                break;
            //店名
            case R.id.ll_members_shop:
                if (cartState.getHomeList().size() <= 0) {
                    return;
                }
                String[] homeName = new String[cartState.getHomeList().size()];
                for (int i = 0; i < cartState.getHomeList().size(); i++) {
                    homeName[i] = cartState.getHomeList().get(i).getShop_new_name();
                }
                SinglePicker<String> pickerSource = new SinglePicker<String>(MembersActivity.this, homeName);
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
                        tvMembersShop.setText(item);
                        shopId = cartState.getHomeList().get(index).getId();
                        initShopId(true);
                    }
                });
                pickerSource.show();
                break;
            //会员总数
            case R.id.ll_members_number:
//                initIntent(MemebershipActivity.class, 1);
                break;
            //下单总数
            case R.id.ll_members_numbers:
                initIntent(MemebershipActivity.class, 2);
                break;
            //确定
            case R.id.tv_members_cancel:
                phone = etMembersPhone.getText().toString();
                if (phone.length() <= 0) {
                    cartState.initToast(context, "请输入电话号码", true, 0);
                    return;
                }
                if (!cartState.isMobile(phone)) {
                    cartState.initToast(context, "请输入正确的电话号码", true, 0);
                    return;
                }

                Map<String, Object> params = new HashMap<String, Object>();
                //门店id
                params.put("phone", phone);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.postString(CartAddaress.MEMBERS, true, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---会员管理店面---" + response);
                        if (srlMembersPull != null) {
                            srlMembersPull.setRefreshing(false);
                        }
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("code");
                        String msg = resultJSON.getString("message");
                        switch (error_code) {
                            //获取成功
                            case 204:
                            case 201:
                            case 200:
                                String data = resultJSON.getString("data");
                                eventBus.postSticky(new MembersDatilsFind(data));
//                        cartState.initToast(context, msg, true, 0);
                                break;
                            default:
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                        if (srlMembersPull != null) {
                            srlMembersPull.setRefreshing(false);
                        }
                        cartState.initToast(context, error, true, 0);
                    }
                });
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
