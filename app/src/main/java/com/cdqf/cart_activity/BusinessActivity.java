package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.BusinessAdapter;
import com.cdqf.cart_find.AccountPullFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.MyGridView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 营业状态
 */
public class BusinessActivity extends BaseActivity {
    private String TAG = BusinessActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //刷新
    @BindView(R.id.srl_business_pull)
    public SwipeRefreshLayout srlBusinessPull = null;

    //返回
    @BindView(R.id.rl_business_return)
    public RelativeLayout rlBusinessReturn = null;

    //列表
    @BindView(R.id.mgv_business_list)
    public MyGridView mgvBusinessList = null;

    private BusinessAdapter businessAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.actiivty_business);

        StaturBar.setStatusBar(this, R.color.tab_main_text_icon);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        ButterKnife.bind(this);
    }

    private void initView() {

    }

    private void initAdapter() {
        businessAdapter = new BusinessAdapter(context);
        mgvBusinessList.setAdapter(businessAdapter);
    }

    private void initListener() {
        srlBusinessPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlBusinessPull.setRefreshing(false);
            }
        });
        mgvBusinessList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    srlBusinessPull.setEnabled(true);
                } else {
                    srlBusinessPull.setEnabled(false);
                }
            }
        });
    }

    private void initBack() {
        initData();
    }

    private void initData() {
        List<String> testList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (i >= 0 && i <= 5) {
                switch (i) {
                    case 0:
                        testList.add("车牌");
                        break;
                    case 1:
                        testList.add("车型");
                        break;
                    case 2:
                        testList.add("项目");
                        break;
                    case 3:
                        testList.add("金额");
                        break;
                    case 4:
                        testList.add("付款");
                        break;
                    case 5:
                        testList.add("状态");
                        break;
                }
            } else {
                testList.add("数据");
            }
        }
        businessAdapter.setTestList(testList);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_business_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_business_return:
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
        eventBus.unregister(this);

    }

    /**
     * @param s
     */
    @Subscribe
    public void onEventMainThread(AccountPullFind s) {

    }
}
