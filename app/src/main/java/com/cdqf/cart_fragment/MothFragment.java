package com.cdqf.cart_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.MothAdapter;
import com.cdqf.cart_find.MothPullFind;
import com.cdqf.cart_find.ReoptrPullFind;
import com.cdqf.cart_state.CartState;
import com.google.gson.Gson;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 月报
 */
public class MothFragment extends Fragment {

    private String TAG = MothFragment.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    private View view = null;

    @BindView(R.id.ptrl_daily_pull)
    public PullToRefreshLayout ptrlDailyPull = null;

    private ListView lvDailyList = null;

    private MothAdapter mothAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "---创建---");

        view = inflater.inflate(R.layout.fragment_daily, null);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();

        return view;
    }

    private void initAgo() {
        ButterKnife.bind(this, view);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {
        lvDailyList = (ListView) ptrlDailyPull.getPullableView();
    }

    private void initListener() {
        ptrlDailyPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
            }
        });

        lvDailyList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == view.getPaddingTop())) {
                    eventBus.post(new ReoptrPullFind(false, true));
                } else {
                    eventBus.post(new ReoptrPullFind(false, false));
                }
            }
        });
    }

    private void initAdapter() {
        mothAdapter = new MothAdapter(getContext());
        lvDailyList.setAdapter(mothAdapter);
    }

    private void initBack() {
        ptrlDailyPull.setPullDownEnable(false);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }

    @OnClick({})
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "---启动---");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        eventBus.unregister(this);
    }

    @Subscribe
    public void onEventMainThread(MothPullFind a) {

    }
}
