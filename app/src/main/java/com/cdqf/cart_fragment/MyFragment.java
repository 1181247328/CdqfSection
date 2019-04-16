package com.cdqf.cart_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.MyAdapter;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_view.ListViewForScrollView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    //头像
    @BindView(R.id.iv_my_hear)
    public ImageView ivMyHear = null;

    //职位
    @BindView(R.id.tv_my_position)
    public TextView tvMyPosition = null;

    @BindView(R.id.lvfsv_my_list)
    public ListViewForScrollView lvfsvMyList = null;

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

    }

    private void initAdapter() {
        myAdapter = new MyAdapter(getContext());
        lvfsvMyList.setAdapter(myAdapter);
    }

    private void initBack() {

    }

    private void forIntent(Class<?> activity) {
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
