package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.NoticeAdapter;
import com.cdqf.cart_class.Notice;
import com.cdqf.cart_find.ReleasePullFind;
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
 * 通知(店长)
 */
public class NoticeManagerActivity extends BaseActivity {

    private String TAG = NoticeManagerActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_notice_pull)
    public VerticalSwipeRefreshLayout srlNoticePull = null;

    //返回
    @BindView(R.id.rl_notice_return)
    public RelativeLayout rlNoticeReturn = null;

    @BindView(R.id.tv_noticemanager_release)
    public TextView tvNoticemanagerRelease = null;

    @BindView(R.id.lv_notice_list)
    public ListViewForScrollView lvNoticeList = null;

    private NoticeAdapter noticeAdapter = null;

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
        setContentView(R.layout.activity_noticemanager);

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
        noticeAdapter = new NoticeAdapter(context);
        lvNoticeList.setAdapter(noticeAdapter);

    }

    private void initAdapter() {

    }

    private void initListener() {
        srlNoticePull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String notice = notice(cartState.getUser().getId(), cartState.getUser().getShopid());
        okHttpRequestWrap.post(notice, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse通知---" + response);
                if (srlNoticePull != null) {
                    srlNoticePull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.getNoticeList().clear();
                        List<Notice> noticeList = gson.fromJson(data, new TypeToken<List<Notice>>() {
                        }.getType());
                        cartState.setNoticeList(noticeList);
                        if (noticeAdapter != null) {
                            noticeAdapter.notifyDataSetChanged();
                        }
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

    private String notice(String staffid, String shopid) {
        String result = null;
        CartAddaress.SHOP_NOTICE = CartAddaress.SHOP_NOTICE.replace("STAFFID", cartState.urlEnodeUTF8(staffid));
        CartAddaress.SHOP_NOTICE = CartAddaress.SHOP_NOTICE.replace("SHOPID", cartState.urlEnodeUTF8(shopid));
        result = CartAddaress.SHOP_NOTICE;
        Log.e(TAG, "---通知---" + result);
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
//        finish();
    }

    @OnClick({R.id.rl_notice_return, R.id.tv_noticemanager_release})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_notice_return:
                finish();
                break;
            //发布
            case R.id.tv_noticemanager_release:
                initIntent(ReleaseActivity.class);
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
     * 发布通知后刷新
     *
     * @param r
     */
    public void onEventMainThread(ReleasePullFind r) {
        initPull(true);
    }
}

