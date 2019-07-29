package com.cdqf.cart_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_activity.LoginActivity;
import com.cdqf.cart_adapter.MyAdapter;
import com.cdqf.cart_class.MyUser;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.AccountExitFind;
import com.cdqf.cart_find.MyShopNameFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.ACache;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartPreferences;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.VerticalSwipeRefreshLayout;
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
 * 我的
 */
public class MyFragment extends Fragment {

    private String TAG = MyFragment.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    private View view = null;

    @BindView(R.id.vsrl_my_pull)
    public VerticalSwipeRefreshLayout vsrlMyPull = null;

    //头像
    @BindView(R.id.iv_my_hear)
    public ImageView ivMyHear = null;

    //职位
    @BindView(R.id.tv_my_position)
    public TextView tvMyPosition = null;

    @BindView(R.id.lvfsv_my_list)
    public ListViewForScrollView lvfsvMyList = null;

    @BindView(R.id.tv_my_exit)
    public TextView tvMyExit = null;

    private MyAdapter myAdapter = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    @BindView(R.id.nsv_orders_pull)
    public NestedScrollView nsvOrdersPull = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "---创建---");

        view = inflater.inflate(R.layout.fragment_my, null);

        initAgo();

        initListener();

        initAdapter();

        initBack();

        return view;
    }

    private void initAgo() {
        ButterKnife.bind(this, view);
        imageLoader = cartState.getImageLoader(getContext());
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initListener() {
        vsrlMyPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initAdapter() {

    }

    private void initBack() {
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        String lossShop = userInformation(cartState.getUser().getId() + "", cartState.getUser().getShopid() + "");
        okHttpRequestWrap.get(lossShop, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---我的---" + response);
                if (vsrlMyPull != null) {
                    vsrlMyPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        rlOrdersBar.setVisibility(View.GONE);
                        nsvOrdersPull.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        String data = resultJSON.getString("data");
                        MyUser myUser = gson.fromJson(data, MyUser.class);
                        imageLoader.displayImage(myUser.getAvatar(), ivMyHear, cartState.getImageLoaderOptions(R.mipmap.test6, R.mipmap.test6, R.mipmap.test6));
                        cartState.setMyUser(myUser);
                        myAdapter = new MyAdapter(getContext());
                        lvfsvMyList.setAdapter(myAdapter);
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        nsvOrdersPull.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        cartState.initToast(getContext(), msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                rlOrdersBar.setVisibility(View.GONE);
                nsvOrdersPull.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
            }
        });
    }

    private String userInformation(String staffid, String shopid) {
        String result = null;
        result = CartAddaress.ADDRESS + "/staff/find/" + staffid;
        Log.e(TAG, "---我的---" + result);
        return result;
    }

    private void forIntent(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }

    @OnClick({R.id.tv_my_exit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_my_exit:
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(11, "提示", "是否退出当前账户", "否", "是");
                whyDilogFragment.show(getFragmentManager(), "退出当前账户");
                break;
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
    public void onEventMainThread(MyShopNameFind a) {
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 退出当前账户
     *
     * @param a
     */
    @Subscribe
    public void onEventMainThread(AccountExitFind a) {
        CartPreferences.clearUser(getContext());
        cartState.setUser(null);
        forIntent(LoginActivity.class);
        ACache.get(getContext()).clear();
        getActivity().finish();
    }
}
