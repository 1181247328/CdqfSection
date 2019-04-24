package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.AuditAdapter;
import com.cdqf.cart_class.Audit;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.RefusedFind;
import com.cdqf.cart_find.RefusedTwoFind;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_find.ThroughTwoFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
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
 * 审核
 */
public class AuditActivity extends BaseActivity {
    private String TAG = AuditActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_audit_pull)
    public SwipeRefreshLayout srlAuditPull = null;

    //帐户
    @BindView(R.id.rl_audit_return)
    public RelativeLayout rlAuditReturn = null;

    //记录
    @BindView(R.id.tv_audit_record)
    public TextView tvAuditRecord = null;

    @BindView(R.id.lv_audit_list)
    public ListView lvAuditList = null;

    @BindView(R.id.tv_audit_no)
    public TextView tvAuditNo = null;

    private AuditAdapter auditAdapter = null;

    //领取的数量
    private int number = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvAuditList.setVisibility(View.VISIBLE);
                    tvAuditNo.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvAuditList.setVisibility(View.GONE);
                    tvAuditNo.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_audit);

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
        auditAdapter = new AuditAdapter(context);
        lvAuditList.setAdapter(auditAdapter);
    }

    private void initAdapter() {

    }

    private void initListener() {

        srlAuditPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("s", "TotalGoods.shop_approval_list");
        params.put("shop_id", cartState.getUser().getShopid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.SHOP_AUDIT, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse审核---" + response);
                if (srlAuditPull != null) {
                    srlAuditPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:

                        String data = resultJSON.getString("data");
                        Log.e(TAG, "---审核---" + data);
                        if (TextUtils.equals(data, "20")) {
                            handler.sendEmptyMessage(0x002);
                            return;
                        }
                        handler.sendEmptyMessage(0x001);
                        cartState.getAuditList().clear();
                        List<Audit> auditList = gson.fromJson(data, new TypeToken<List<Audit>>() {
                        }.getType());
                        cartState.setAuditList(auditList);
                        if (auditAdapter != null) {
                            auditAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.rl_audit_return, R.id.tv_audit_record})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_audit_return:
                finish();
                break;
            case R.id.tv_audit_record:
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
     * 通过第一次
     *
     * @param t
     */
    public void onEventMainThread(ThroughFind t) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(8, "提示", "是否通过" + cartState.getAuditList().get(t.position).getName() + "申请" + cartState.getAuditList().get(t.position).getGoods_name() + "的请求", "否", "是", t.position);
        whyDilogFragment.show(getSupportFragmentManager(), "通过");
    }

    /**
     * 通过第二次
     *
     * @param t
     */
    public void onEventMainThread(ThroughTwoFind t) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("s", "TotalGoods.shop_approval");
        params.put("goods_id", cartState.getAuditList().get(t.position).getGoods_id());
        params.put("shop_id", cartState.getUser().getShopid());
        params.put("id", cartState.getAuditList().get(t.position).getId());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.SHOP_THROUGH, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse通过---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        cartState.initToast(context, "已同意申请", true, 0);
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

    /**
     * 拒绝第一次
     *
     * @param t
     */
    public void onEventMainThread(RefusedFind t) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(9, "提示", "是否拒绝" + cartState.getAuditList().get(t.position).getName() + "申请" + cartState.getAuditList().get(t.position).getGoods_name() + "的请求", "否", "是", t.position);
        whyDilogFragment.show(getSupportFragmentManager(), "拒绝");
    }

    /**
     * 拒绝第二次
     *
     * @param r
     */
    public void onEventMainThread(RefusedTwoFind r) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("s", "TotalGoods.shop_exit");
        params.put("goods_id", cartState.getAuditList().get(r.position).getGoods_id());
        params.put("shop_id", cartState.getUser().getShopid());
        params.put("id", cartState.getAuditList().get(r.position).getId());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.SHOP_THROUGH_NO, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse通过---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        cartState.initToast(context, "已拒绝申请", true, 0);
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

