package com.cdqf.cart_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_activity.LoginActivity;
import com.cdqf.cart_adapter.MyAdapter;
import com.cdqf.cart_class.MyUser;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
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
    }

    private void initListener() {
        vsrlMyPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vsrlMyPull.setRefreshing(false);
                initPull(false);
            }
        });
    }

    private void initAdapter() {

    }

    private void initBack() {
        initPull(true);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        Log.e(TAG, "---id---" + cartState.getUser().getId());
        String lossShop = userInformation(cartState.getUser().getId(), cartState.getUser().getShopid());
        okHttpRequestWrap.post(lossShop, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse我的---" + response);
                if (vsrlMyPull != null) {
                    vsrlMyPull.setRefreshing(false);
                }
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        MyUser myUser = gson.fromJson(data, MyUser.class);
                        cartState.setMyUser(myUser);
                        //正式名称
                        if (TextUtils.equals(myUser.getType(), "1")) {
                            //员工
                            tvMyPosition.setText("店员");
                        } else if (TextUtils.equals(myUser.getType(), "2")) {
                            //店长
                            tvMyPosition.setText("店长");
                        } else {
                            //TODO
                        }
                        myAdapter = new MyAdapter(getContext());
                        lvfsvMyList.setAdapter(myAdapter);
                        break;
                    default:
                        cartState.initToast(getContext(), msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
            }
        });
    }

    private String userInformation(String staffid, String shopid) {
        String result = null;
        result = CartAddaress.ADDRESS + "/?s=staff.getstaff&staffid=" + staffid + "&shopid=" + shopid;
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
                CartPreferences.clearUser(getContext());
                cartState.setUser(null);
                forIntent(LoginActivity.class);
                getActivity().finish();
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
    }
}
