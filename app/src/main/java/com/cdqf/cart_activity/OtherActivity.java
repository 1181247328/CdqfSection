package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 任务
 */
public class OtherActivity extends BaseActivity {
    private String TAG = OtherActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_other_return)
    public RelativeLayout rlOtherReturn = null;

    //时间
    @BindView(R.id.tv_other_timer)
    public TextView tvOtherTimer = null;

    //本月任务
    @BindView(R.id.tv_other_total)
    public TextView tvOtherTotal = null;

    //本月任务
    @BindView(R.id.tv_other_has)
    public TextView tvOtherHas = null;

    //本月上班
    @BindView(R.id.tv_other_on)
    public TextView tvOtherOn = null;

    //本月上班
    @BindView(R.id.tv_other_hugh)
    public TextView tvOtherHugh = null;

    //明细
    @BindView(R.id.tv_other_detail)
    public TextView tvOtherDetail = null;

    //签字人员
    @BindView(R.id.tv_other_name)
    public TextView tvOtherName = null;

    //立即提现
    @BindView(R.id.tv_other_withdrawal)
    public TextView tvOtherWithdrawal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_other);

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

    }

    private void initBack() {

    }


    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_other_return, R.id.tv_other_withdrawal})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_other_return:
                finish();
                break;
            //立即提现
            case R.id.rcrl_again_position:
                break;
        }
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
