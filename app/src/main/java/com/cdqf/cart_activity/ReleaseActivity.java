package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.ReleaseFind;
import com.cdqf.cart_find.ReleasePullFind;
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
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 发布(店长)
 */
public class ReleaseActivity extends BaseActivity {

    private String TAG = ReleaseActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_release_return)
    public RelativeLayout rlReleaseReturn = null;

    @BindView(R.id.tv_release_release)
    public TextView tvReleaseRelease = null;

    //内容
    @BindView(R.id.et_release_input)
    public EditText etReleaseInput = null;

    //通知
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activty_release);

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

    //服务订单
    private String shopService(String content, String staffid, String type) {
        String result = null;
        result = CartAddaress.ADDRESS + "/?s=Notice.stenotice&content=" + content + "&staffid=" + staffid + "&type=" + type;
        Log.e(TAG, "---店长发布通知---" + result);
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_release_return, R.id.tv_release_release})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_release_return:
                content = etReleaseInput.getText().toString().trim();
                if (content.length() <= 0) {
                    finish();
                    return;
                }
                WhyDilogFragment whyDilogOneFragment = new WhyDilogFragment();
                whyDilogOneFragment.setInit(3, "提示", "您正在发布通知中,是否放弃现有的编辑.", "否", "是");
                whyDilogOneFragment.show(getSupportFragmentManager(), "放弃通知");
                break;
            //发布
            case R.id.tv_release_release:
                content = etReleaseInput.getText().toString().trim();
                if (content.length() <= 0) {
                    cartState.initToast(context, "通知内容不能为空", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(2, "提示", "您正在发布通知,是否发布.", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "发布通知");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (content.length() <= 0) {
            finish();
            return;
        }
        WhyDilogFragment whyDilogOneFragment = new WhyDilogFragment();
        whyDilogOneFragment.setInit(3, "提示", "您正在发布通知中,是否放弃现有的编辑.", "否", "是");
        whyDilogOneFragment.show(getSupportFragmentManager(), "放弃通知");
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
     * 通知
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(ReleaseFind r) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String shopService = shopService(content, cartState.getUser().getId()+"", "1");
        okHttpRequestWrap.post(shopService, true, "发布中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse发布通知---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, data, true, 0);
                        eventBus.post(new ReleasePullFind());
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
