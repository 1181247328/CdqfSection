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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.LossNewsLeftAdapter;
import com.cdqf.cart_adapter.LossNewsRightAdapter;
import com.cdqf.cart_find.UserSumberFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 新损耗品(店长)
 */
public class LossNewsActivity extends BaseActivity {
    private String TAG = UserActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    @BindView(R.id.srl_lossnews_pull)
    public VerticalSwipeRefreshLayout srlLossnewsPull = null;

    //返回
    @BindView(R.id.rl_lossnews_return)
    public RelativeLayout rlLossnewsReturn = null;

    //提交订单
    @BindView(R.id.tv_lossnews_order)
    public TextView tvLossnewsOrder = null;

    //服务名称集合
    @BindView(R.id.lv_lossnews_name)
    public ListViewForScrollView lvLossnewsName = null;

    private LossNewsLeftAdapter lossNewsLeftAdapter = null;

    //服务内容
    @BindView(R.id.lv_lossnews_list)
    public ListViewForScrollView lvLossnewsList = null;

    private LossNewsRightAdapter lossNewsRightAdapter = null;

    private int type = 0;

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
        setContentView(R.layout.activity_lossnews);

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

    }

    private void initAdapter() {
        lossNewsLeftAdapter = new LossNewsLeftAdapter(context);
        lvLossnewsName.setAdapter(lossNewsLeftAdapter);

        lossNewsRightAdapter = new LossNewsRightAdapter(context);
        lvLossnewsList.setAdapter(lossNewsRightAdapter);
    }

    private void initListener() {
        lvLossnewsName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == lossNewsLeftAdapter.getType()) {
                    return;
                }
                lossNewsLeftAdapter.setType(position);
                type = position;
                lossNewsRightAdapter.setPosition(position);
            }
        });
    }

    private void initBack() {

    }


    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_lossnews_return, R.id.tv_lossnews_order})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_lossnews_return:
                finish();
                break;
            //提交订单
            case R.id.tv_lossnews_order:
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
     * 提交
     *
     * @param u
     */
    @Subscribe
    public void onEventMainThread(UserSumberFind u) {

    }
}
