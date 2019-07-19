package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.cdqf.cart_state.StaturBar;
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
import de.greenrobot.event.Subscribe;

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

    @BindView(R.id.tv_noticemanager_no)
    public TextView tvNoticemanagerNo = null;

    private NoticeAdapter noticeAdapter = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    lvNoticeList.setVisibility(View.VISIBLE);
                    tvNoticemanagerNo.setVisibility(View.GONE);
                    break;
                case 0x002:
                    lvNoticeList.setVisibility(View.GONE);
                    tvNoticemanagerNo.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_noticemanager);

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
//        if(Integer.parseInt(cartState.getUser().getType() )== 1){
//            tvNoticemanagerRelease.setVisibility(View.GONE);
//        } else {
//            tvNoticemanagerRelease.setVisibility(View.VISIBLE);
//        }
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String notice = notice(cartState.getUser().getId()+"", cartState.getUser().getShopid()+"");
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
                        handler.sendEmptyMessage(0x001);
                        String data = resultJSON.getString("data");
                        Log.e(TAG, "---通知---"+data);
                        if(TextUtils.equals(data, "{}")){
                            handler.sendEmptyMessage(0x002);
                            return;
                        }
                        cartState.getNoticeList().clear();
                        List<Notice> noticeList = gson.fromJson(data, new TypeToken<List<Notice>>() {
                        }.getType());
                        cartState.setNoticeList(noticeList);
                        if (noticeAdapter != null) {
                            noticeAdapter.notifyDataSetChanged();
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
    @Subscribe
    public void onEventMainThread(ReleasePullFind r) {
        initPull(true);
    }
}

