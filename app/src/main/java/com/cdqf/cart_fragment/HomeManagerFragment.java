package com.cdqf.cart_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_activity.AuditActivity;
import com.cdqf.cart_activity.LossManagerActivity;
import com.cdqf.cart_activity.NoticeManagerActivity;
import com.cdqf.cart_activity.ShopActivity;
import com.cdqf.cart_adapter.HomeManagerAdapter;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_view.MyGridView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 首页(店长)
 */
public class HomeManagerFragment extends Fragment {

    private String TAG = HomeFragment.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    private View view = null;

    //名称
    @BindView(R.id.tv_home_name)
    public TextView tvHomeName = null;

    //内容
    @BindView(R.id.tv_home_context)
    public TextView tvHomeContext = null;

    //集合
    @BindView(R.id.mgv_home_list)
    public MyGridView mgvHomeList = null;

    private HomeManagerAdapter homeManagerAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "---创建---");

        view = inflater.inflate(R.layout.fragment_home, container, false);

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
        mgvHomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //损耗品
                    case 0:
                        initIntent(LossManagerActivity.class);
                        break;
                    //服务
                    case 1:
                        initIntent(ShopActivity.class);
                        break;
                    //通知
                    case 2:
                        initIntent(NoticeManagerActivity.class);
                        break;
                    //用户
                    case 3:
                        initIntent(AuditActivity.class);
                        break;
                }
            }
        });
    }

    private void initAdapter() {
        homeManagerAdapter = new HomeManagerAdapter(getContext());
        mgvHomeList.setAdapter(homeManagerAdapter);
    }

    private void initBack() {
        tvHomeName.setText(cartState.getUser().getName() + ":");
        tvHomeContext.setText("今天是您加入脱狗车宝第" + cartState.getUser().getDay() + "天!");
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
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