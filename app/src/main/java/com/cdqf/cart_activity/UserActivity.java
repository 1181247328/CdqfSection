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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.UserAdapter;
import com.cdqf.cart_find.LossReceiveFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 用户
 */
public class UserActivity extends BaseActivity {
    private String TAG = UserActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

//    @BindView(R.id.srl_user_pull)
//    public SwipeRefreshLayout srlUserPull = null;

    //返回
    @BindView(R.id.rl_user_return)
    public RelativeLayout rlUserReturn = null;

    //价格
    @BindView(R.id.tv_user_price)
    public TextView tvUserPrice = null;

    //提交订单
    @BindView(R.id.tv_user_order)
    public TextView tvUserOrder = null;

    @BindView(R.id.lv_user_list)
    public ListView lvUserList = null;

    private UserAdapter userAdapter = null;

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
        setContentView(R.layout.activity_user);

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
        userAdapter = new UserAdapter(context);
        lvUserList.setAdapter(userAdapter);
    }

    private void initAdapter() {

    }

    private void initListener() {

//        srlUserPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                srlUserPull.setRefreshing(false);
//            }
//        });
    }

    private void initBack() {
       // initPull();
    }

    private void initPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("", "");
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.LOSS, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse用户---" + response);
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

    @OnClick({R.id.rl_user_return, R.id.tv_user_order})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_user_return:
                finish();
                break;
            //提交订单
            case R.id.tv_user_order:
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

    }

}

