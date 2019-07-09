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
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * 会员管理
 */
public class MembersActivity extends BaseActivity {
    private String TAG = MembersActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

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

    //会员总数
    @BindView(R.id.tv_members_number)
    public TextView tvMembersNumber = null;

    //下单会员
    @BindView(R.id.tv_members_numbers)
    public EditText tvMembersNumbers = null;

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
        setContentView(R.layout.activity_members);

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

    }

    private void initBack() {

    }


    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity,int type) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @OnClick({R.id.rl_members_return, R.id.ll_members_shop, R.id.ll_members_number, R.id.ll_members_numbers})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_members_return:
                finish();
                break;
            //店名
            case R.id.ll_members_shop:
                break;
            //会员总数
            case R.id.ll_members_number:
                break;
            //下单总数
            case R.id.ll_members_numbers:
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
