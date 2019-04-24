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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.RecordAdapter;
import com.cdqf.cart_class.Audit;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class RecordActivity extends BaseActivity {
    private String TAG = AuditActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.vsrl_record_pull)
    public VerticalSwipeRefreshLayout vsrlRecordPull = null;

    @BindView(R.id.ptrl_record_pull)
    public PullToRefreshLayout ptrlRecordPull = null;

    //返回
    @BindView(R.id.rl_record_return)
    public RelativeLayout rlRecordReturn = null;

    private ListView lvRecrodList = null;

    private RecordAdapter recordAdapter = null;

    //领取的数量
    private int number = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvRecrodList.setVisibility(View.VISIBLE);
                    vsrlRecordPull.setEnabled(false);
                    break;
                case 0x002:
                    lvRecrodList.setVisibility(View.GONE);
                    vsrlRecordPull.setEnabled(true);
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
        setContentView(R.layout.activity_record);

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
    }

    private void initView() {
        lvRecrodList = (ListView) ptrlRecordPull.getPullableView();

    }

    private void initAdapter() {
        recordAdapter = new RecordAdapter(context);
        lvRecrodList.setAdapter(recordAdapter);
    }

    private void initListener() {

        vsrlRecordPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        ptrlRecordPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
            }
        });
    }

    private void initBack() {
        //initPull(true);
        vsrlRecordPull.setEnabled(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("s", "TotalGoods.shop_approval_list");
        params.put("shop_id", cartState.getUser().getShopid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.post(CartAddaress.SHOP_AUDIT, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse审核审核记录---" + response);

                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:

                        String data = resultJSON.getString("data");
                        Log.e(TAG, "---审核记录---" + data);
                        if (TextUtils.equals(data, "20")) {
                            handler.sendEmptyMessage(0x002);
                            return;
                        }
                        handler.sendEmptyMessage(0x001);
                        cartState.getAuditList().clear();
                        List<Audit> auditList = gson.fromJson(data, new TypeToken<List<Audit>>() {
                        }.getType());
                        cartState.setAuditList(auditList);
                        if (recordAdapter != null) {
                            recordAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.rl_record_return})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_record_return:
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
    }

}
