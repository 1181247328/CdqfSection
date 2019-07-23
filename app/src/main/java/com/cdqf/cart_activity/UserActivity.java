package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.NameAdapter;
import com.cdqf.cart_adapter.UserAdapter;
import com.cdqf.cart_class.UserGoods;
import com.cdqf.cart_dilog.MoneyDilogFragment;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_find.UserAddFind;
import com.cdqf.cart_find.UserNumberFind;
import com.cdqf.cart_find.UserSumberFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.DoubleOperationUtil;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 用户
 */
public class UserActivity extends BaseActivity {
    private String TAG = UserActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    @BindView(R.id.srl_user_pull)
    public VerticalSwipeRefreshLayout srlUserPull = null;

    //返回
    @BindView(R.id.rl_user_return)
    public RelativeLayout rlUserReturn = null;

    @BindView(R.id.tv_user_name)
    public TextView tvUserName = null;

    //价格
    @BindView(R.id.tv_user_price)
    public TextView tvUserPrice = null;

    //提交订单
    @BindView(R.id.tv_user_order)
    public TextView tvUserOrder = null;

    //服务名称集合
    @BindView(R.id.lv_user_name)
    public ListViewForScrollView lvUserName = null;

    private NameAdapter nameAdapter = null;

    //服务内容
    @BindView(R.id.lv_user_list)
    public ListViewForScrollView lvUserList = null;

    private UserAdapter userAdapter = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.rl_datils_pull)
    public RelativeLayout rlDatilsPull = null;

    private int position = 0;

    private int type = 0;

    private int numbers = 1;

    private double sum = 0;

    private int userType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_user);

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
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        userType = intent.getIntExtra("userType", 0);
    }

    private void initView() {
        nameAdapter = new NameAdapter(context);
        lvUserName.setAdapter(nameAdapter);
    }

    private void initAdapter() {

    }

    private void initListener() {
        lvUserName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == nameAdapter.getType()) {
                    return;
                }
                nameAdapter.setType(position);
                type = position;
                userAdapter.setPosition(position);
            }
        });
        lvUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "---type---" + type);
                if (type == cartState.getUserGoodsList().size() - 1) {
                    Log.e(TAG, "---其它---");
                    MoneyDilogFragment moneyDilogFragment = new MoneyDilogFragment();
                    moneyDilogFragment.show(getSupportFragmentManager(), "添加数量");
                }
            }
        });
        srlUserPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        tvUserName.setText(cartState.getDatils().getCarnum());
        srlUserPull.setEnabled(false);
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.SHOP_GOODS, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse追加的商品---" + response);
                if (srlUserPull != null) {
                    srlUserPull.setEnabled(true);
                    srlUserPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        rlOrdersBar.setVisibility(View.GONE);
                        rlDatilsPull.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        String data = resultJSON.getString("data");
                        tvUserPrice.setText("总计:￥0.0");
                        cartState.getUserGoodsList().clear();
                        List<UserGoods> userGoodsList = gson.fromJson(data, new TypeToken<List<UserGoods>>() {
                        }.getType());
                        cartState.setUserGoodsList(userGoodsList);
                        Log.e(TAG, "---集合数量---" + gson.toJson(userGoodsList));
                        if (nameAdapter != null) {
                            nameAdapter.notifyDataSetChanged();
                        }
                        userAdapter = new UserAdapter(context);
                        lvUserList.setAdapter(userAdapter);
                        userAdapter.setPosition(0);
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
                rlOrdersBar.setVisibility(View.GONE);
                rlDatilsPull.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                if (srlUserPull != null) {
                    srlUserPull.setEnabled(true);
                    srlUserPull.setRefreshing(false);
                }
            }
        });
    }

    private String order(int cartype, String goodsid, int userid, String shopid, String carnum, String phone, String number) {
        String result = null;
        result = CartAddaress.ADDRESS_THE + "/?s=order.appendorder&cartype=" + cartype + "&goodsid=" + goodsid + "&userid=" + userid + "&shopid=" + shopid + "&carnum=" + carnum + "&phone=" + phone + "&number=" + number;
        Log.e(TAG, "---提交订单---" + result);
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_user_return, R.id.tv_user_order})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_user_return:
                finish();
                break;
            //提交订单
            case R.id.tv_user_order:
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(10, "提示", "是否提交车牌" + cartState.getDatils().getCarnum() + "的追加服务", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "提交订单");
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
     * 统计价格
     *
     * @param l
     */
    @Subscribe
    public void onEventMainThread(UserAddFind l) {
        sum = 0;
        for (int i = 0; i < cartState.getUserGoodsList().size(); i++) {
            for (int j = 0; j < cartState.getUserGoodsList().get(i).getData().size(); j++) {
                if (cartState.getUserGoodsList().get(i).getData().get(j).isSelect()) {
                    double money = Double.parseDouble(cartState.getUserGoodsList().get(i).getData().get(j).getPrice());
                    sum = DoubleOperationUtil.add(sum, money);
                }
            }
        }
        tvUserPrice.setText("总计:￥" + sum);
    }

    /**
     * 补差价
     *
     * @param u
     */
    @Subscribe
    public void onEventMainThread(UserNumberFind u) {
        numbers = u.number;
        sum = numbers;
        for (int i = 0; i < cartState.getUserGoodsList().size(); i++) {
            for (int j = 0; j < cartState.getUserGoodsList().get(i).getData().size(); j++) {
                if (cartState.getUserGoodsList().get(i).getData().get(j).isSelect()) {
                    double money = Double.parseDouble(cartState.getUserGoodsList().get(i).getData().get(j).getPrice());
                    sum = DoubleOperationUtil.add(sum, money);
                }
            }
        }
        tvUserPrice.setText("总计:￥" + sum);
    }

    /**
     * 提交订单
     *
     * @param u
     */
    @Subscribe
    public void onEventMainThread(UserSumberFind u) {
        boolean isSelect = false;
        //商品id
        String goodsid = "";
        for (int i = 0; i < cartState.getUserGoodsList().size(); i++) {
            for (int j = 0; j < cartState.getUserGoodsList().get(i).getData().size(); j++) {
                if (cartState.getUserGoodsList().get(i).getData().get(j).isSelect()) {
                    isSelect = true;
                    goodsid = goodsid + cartState.getUserGoodsList().get(i).getData().get(j).getId() + ",";
                }
            }
        }
        Log.e(TAG, "---商品id---" + goodsid);
        if (numbers >= 1) {
            isSelect = true;
            if (cartState.getUserGoodsList().get(cartState.getUserGoodsList().size() - 1).getData().size() <= 0) {
                //TODO
            } else {
                goodsid = goodsid + cartState.getUserGoodsList().get(cartState.getUserGoodsList().size() - 1).getData().get(0).getId();
            }
        }
        Log.e(TAG, "---获得的数量---" + goodsid);
        if (!isSelect) {
            cartState.initToast(context, "请选择追加的商品", true, 0);
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        //车辆车型
        int cartype = cartState.getDatils().getCartype();
        //用户id
        int userid = cartState.getDatils().getUserid();
        //店铺id
        String shopid = cartState.getUser().getShopid() + "";
        //车牌号
        String carnum = cartState.getDatils().getCarnum();
        //用户电话
        String phone = cartState.getDatils().getPhone();
        //数量
        String number = numbers + "";

        String order = order(cartype, goodsid, userid, shopid, carnum, phone, number);
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(order, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse提交订单---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, data, true, 0);
                        cartState.getUserGoodsList().clear();
                        eventBus.post(new DatilsPullFind());
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
            }
        });
    }
}

