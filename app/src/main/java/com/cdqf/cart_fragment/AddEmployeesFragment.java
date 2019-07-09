package com.cdqf.cart_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.AccountExitFind;
import com.cdqf.cart_state.CartState;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 添加员工
 */
public class AddEmployeesFragment extends Fragment {

    private String TAG = AddEmployeesFragment.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    private View view = null;

    //店铺
    @BindView(R.id.ll_allemployees_item_shopname)
    public LinearLayout llAllemployeesItemShopname = null;
    @BindView(R.id.tv_allemployees_item_shopname)
    public TextView tvAllemployeesItemShopname = null;

    //姓名
    @BindView(R.id.et_allemployees_name)
    public EditText etAllemployeesName = null;

    //职位
    @BindView(R.id.ll_addemployess_position)
    public LinearLayout llAddemployessPosition = null;
    @BindView(R.id.tv_addemployess_position)
    public TextView tvAddemployessPosition = null;

    //工号
    @BindView(R.id.tv_allemployees_id)
    public TextView tvAllemployeesId = null;

    //电话号码
    @BindView(R.id.et_allemployees_phone)
    public EditText etAllemployeesPhone = null;

    //提交
    @BindView(R.id.tv_allemployees_submit)
    public TextView tvAllemployeesSubmit = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "---创建---");

        view = inflater.inflate(R.layout.fragment_addemployees, null);

        initAgo();

        initView();

        initListener();

        initAdapter();

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

    }

    private void initListener() {

    }

    private void initAdapter() {

    }

    private void initBack() {

    }

    private void forIntent(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }

    @OnClick({R.id.ll_allemployees_item_shopname, R.id.ll_addemployess_position, R.id.tv_allemployees_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            //选择店铺
            case R.id.ll_allemployees_item_shopname:
                break;
            //选择职位
            case R.id.ll_addemployess_position:
                break;
            //提交
            case R.id.tv_allemployees_submit:
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
    public void onEventMainThread(AccountExitFind a) {

    }
}
