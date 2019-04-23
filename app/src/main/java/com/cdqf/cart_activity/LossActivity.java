package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.LossAdapter;
import com.cdqf.cart_class.LossStaff;
import com.cdqf.cart_dilog.LossDilogFragment;
import com.cdqf.cart_find.LossReceiveFind;
import com.cdqf.cart_find.LossReceiveSubmitFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
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

/**
 * 损耗品
 */
public class LossActivity extends BaseActivity {
    private String TAG = LossActivity.class.getSimpleName();

    private Context context = null;

    private Gson gson = new Gson();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    @BindView(R.id.srl_loss_pull)
    public VerticalSwipeRefreshLayout srlLossPull = null;

    //帐户
    @BindView(R.id.rl_loss_return)
    public RelativeLayout rlLossReturn = null;

    @BindView(R.id.lv_loss_list)
    public ListViewForScrollView lvLossList = null;

    @BindView(R.id.tv_loss_no)
    public TextView tvLossNo = null;

    @BindView(R.id.ll_loss_one)
    public LinearLayout llLossOne = null;

    private LossAdapter lossAdapter = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvLossList.setVisibility(View.VISIBLE);
                    llLossOne.setVisibility(View.VISIBLE);
                    tvLossNo.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvLossList.setVisibility(View.GONE);
                    llLossOne.setVisibility(View.GONE);
                    tvLossNo.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

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
        setContentView(R.layout.activity_loss);

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
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {
        lossAdapter = new LossAdapter(context);
        lvLossList.setAdapter(lossAdapter);
    }

    private void initAdapter() {

    }

    private void initListener() {

        srlLossPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                srlLossPull.setRefreshing(false);
                initPull(false);
            }
        });
    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shop_id", cartState.getUser().getShopid());
        params.put("s", "TotalGoods.staff_shop_list");
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.STAFF_TOTAL, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse损耗品---" + response);
                if (srlLossPull != null) {
                    srlLossPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        handler.sendEmptyMessage(0x001);
                        String data = resultJSON.getString("data");
                        cartState.getLossStaffList().clear();
                        List<LossStaff> lossStaffList = gson.fromJson(data, new TypeToken<List<LossStaff>>() {
                        }.getType());
                        cartState.setLossStaffList(lossStaffList);
                        if (lossAdapter != null) {
                            lossAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        handler.sendEmptyMessage(0x002);
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

    @OnClick({R.id.rl_loss_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case R.id.rl_loss_return:
                finish();
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
     * 领取
     *
     * @param l
     */
    public void onEventMainThread(LossReceiveFind l) {
        LossDilogFragment lossDilogFragment = new LossDilogFragment();
        lossDilogFragment.number(0, l.position);
        lossDilogFragment.show(getSupportFragmentManager(), "领取");
    }

    /**
     * 确定领取
     *
     * @param s
     */
    public void onEventMainThread(LossReceiveSubmitFind s) {
        Log.e(TAG, "---数量---" + s.number + "---位置---" + s.position);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shop_id", cartState.getUser().getShopid());
        params.put("user_id", cartState.getUser().getId());
        params.put("number", s.number);
        params.put("goods_id", cartState.getLossStaffList().get(s.position).getGoods_id());
        params.put("s", "TotalGoods.staff_receiving");
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.STAFF_RECEIVE, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse员工领取耗材---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, "申请成功", true, 0);
                        initPull(true);
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
