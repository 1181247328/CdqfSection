package com.cdqf.cart_fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.util.ConvertUtils;
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
                SinglePicker<String> pickerSource = new SinglePicker<>(getActivity(), new String[]{
                        "店长", "员工", "洗车工"
                });
                LineConfig configSource = new LineConfig();
                configSource.setColor(ContextCompat.getColor(getContext(), R.color.addstore_one));//线颜色
                configSource.setThick(ConvertUtils.toPx(getActivity(), 1));//线粗
                configSource.setItemHeight(20);
                pickerSource.setLineConfig(configSource);
                pickerSource.setCanLoop(false);//不禁用循环
                pickerSource.setLineVisible(true);
                pickerSource.setTopLineColor(Color.TRANSPARENT);
                pickerSource.setTextSize(14);
                pickerSource.setTitleText("职位");
                pickerSource.setSelectedIndex(0);
                pickerSource.setWheelModeEnable(true);
                pickerSource.setWeightEnable(true);
                pickerSource.setWeightWidth(1);
                pickerSource.setCancelTextColor(ContextCompat.getColor(getContext(), R.color.house_eight));//顶部取消按钮文字颜色
                pickerSource.setCancelTextSize(14);
                pickerSource.setSubmitTextColor(ContextCompat.getColor(getContext(), R.color.house_eight));//顶部确定按钮文字颜色
                pickerSource.setSubmitTextSize(14);
                pickerSource.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));//背景色
                pickerSource.setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.house_eight));//前四位值是透明度
                pickerSource.setUnSelectedTextColor(ContextCompat.getColor(getContext(), R.color.addstore_one));
                pickerSource.setOnSingleWheelListener(new OnSingleWheelListener() {
                    @Override
                    public void onWheeled(int index, String item) {

                    }
                });
                pickerSource.setOnItemPickListener(new OnItemPickListener<String>() {
                    @Override
                    public void onItemPicked(int index, String item) {
                        tvAddemployessPosition.setText(item);
                    }
                });
                pickerSource.show();
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
