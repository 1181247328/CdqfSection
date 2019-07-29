package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.SignSumitFind;
import com.cdqf.cart_find.SingFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.SignView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 签字
 */
public class SignActivity extends BaseActivity {
    private String TAG = SignActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    //返回
    @BindView(R.id.rl_sign_return)
    public RelativeLayout rlSignReturn = null;

    @BindView(R.id.ll_sign_layout)
    public LinearLayout llSignLayout = null;

    //确定
    @BindView(R.id.ll_sign_sumit)
    public LinearLayout llSignSumit = null;

    //消除
    @BindView(R.id.tv_sign_eliminate)
    public TextView tvSignEliminate = null;

    //画板内容
    @BindView(R.id.sv_sign_context)
    public SignView svSignContext = null;

    private Bitmap mSignBitmap;
    private String signPath;
    private long time;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_sign);

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

    }

    private void initBack() {
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_sign_return, R.id.ll_sign_sumit, R.id.tv_sign_eliminate})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_sign_return:
                finish();
                break;
            //确定
            case R.id.ll_sign_sumit:
                if (!svSignContext.isSign()) {
                    cartState.initToast(context, "您未签字", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(23, "提示", "是否签字", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "确定签字");

                break;
            //消除
            case R.id.tv_sign_eliminate:
                svSignContext.clear();
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
     * 签字完成
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(SignSumitFind s) {
        eventBus.post(new SingFind(cartState.viewSaveToImage(llSignLayout)));
        finish();
    }
}

