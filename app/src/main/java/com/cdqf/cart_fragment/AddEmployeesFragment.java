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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_class.PositionEmployees;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.AddEmployessPullFind;
import com.cdqf.cart_find.AllEmployeesFind;
import com.cdqf.cart_find.AllEmployeesPullFind;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_find.EmployeesPullFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @BindView(R.id.ll_addemployees_one)
    public LinearLayout llAddemployeesOne = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    //员工id
    private int shopId = 0;

    //员工姓名
    private String name = "";

    //职位
    private String position = "";

    private int positionId = 0;

    //手机号
    private String phone;

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
        tvAllemployeesItemShopname.setText(cartState.getUser().getShopName());
        shopId = cartState.getUser().getShopid();
        initPull(false);
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        okHttpRequestWrap.get(CartAddaress.ROLL, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---获得职位---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        rlOrdersBar.setVisibility(View.GONE);
                        llAddemployeesOne.setVisibility(View.VISIBLE);
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        String datas = resultJSON.getString("data");
                        cartState.getPositionEmployeesList().clear();
                        cartState.initToast(getContext(), msg, true, 0);
                        eventBus.post(new EmployeesPullFind(false, true));
                        List<PositionEmployees> positionEmployeesList = gson.fromJson(datas, new TypeToken<List<PositionEmployees>>() {
                        }.getType());
                        cartState.setPositionEmployeesList(positionEmployeesList);
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        llAddemployeesOne.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        cartState.initToast(getContext(), msg, true, 0);
                        eventBus.post(new EmployeesPullFind(false, true));
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                rlOrdersBar.setVisibility(View.GONE);
                llAddemployeesOne.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                eventBus.post(new EmployeesPullFind(false, false));
            }
        });
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
                if (cartState.getHomeList().size() <= 0) {
                    return;
                }
                String[] homeName = new String[cartState.getHomeList().size()];
                for (int i = 0; i < cartState.getHomeList().size(); i++) {
                    homeName[i] = cartState.getHomeList().get(i).getShop_new_name();
                }
                SinglePicker<String> pickerSources = new SinglePicker<>(getActivity(), homeName);
                LineConfig configSources = new LineConfig();
                configSources.setColor(ContextCompat.getColor(getContext(), R.color.addstore_one));//线颜色
                configSources.setThick(ConvertUtils.toPx(getActivity(), 1));//线粗
                configSources.setItemHeight(20);
                pickerSources.setLineConfig(configSources);
                pickerSources.setCanLoop(false);//不禁用循环
                pickerSources.setLineVisible(true);
                pickerSources.setTopLineColor(Color.TRANSPARENT);
                pickerSources.setTextSize(14);
                pickerSources.setTitleText("店名");
                pickerSources.setSelectedIndex(0);
                pickerSources.setWheelModeEnable(true);
                pickerSources.setWeightEnable(true);
                pickerSources.setWeightWidth(1);
                pickerSources.setCancelTextColor(ContextCompat.getColor(getContext(), R.color.house_eight));//顶部取消按钮文字颜色
                pickerSources.setCancelTextSize(14);
                pickerSources.setSubmitTextColor(ContextCompat.getColor(getContext(), R.color.house_eight));//顶部确定按钮文字颜色
                pickerSources.setSubmitTextSize(14);
                pickerSources.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));//背景色
                pickerSources.setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.house_eight));//前四位值是透明度
                pickerSources.setUnSelectedTextColor(ContextCompat.getColor(getContext(), R.color.addstore_one));
                pickerSources.setOnSingleWheelListener(new OnSingleWheelListener() {
                    @Override
                    public void onWheeled(int index, String item) {

                    }
                });
                pickerSources.setOnItemPickListener(new OnItemPickListener<String>() {
                    @Override
                    public void onItemPicked(int index, String item) {
                        tvAllemployeesItemShopname.setText(item);
                        shopId = cartState.getHomeList().get(index).getId();
                    }
                });
                pickerSources.show();
                break;
            //选择职位
            case R.id.ll_addemployess_position:
                if (cartState.getPositionEmployeesList().size() <= 0) {
                    cartState.initToast(getContext(), "职位选择异常,请刷新", true, 0);
                    return;
                }
                String[] homePosition = new String[cartState.getPositionEmployeesList().size()];
                for (int i = 0; i < cartState.getPositionEmployeesList().size(); i++) {
                    homePosition[i] = cartState.getPositionEmployeesList().get(i).getName();
                }
                SinglePicker<String> pickerSource = new SinglePicker<>(getActivity(), homePosition);
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
                        positionId = cartState.getPositionEmployeesList().get(index).getId();
                    }
                });
                pickerSource.show();
                break;
            //提交
            case R.id.tv_allemployees_submit:
                name = etAllemployeesName.getText().toString();
                if (name.length() <= 0) {
                    cartState.initToast(getContext(), "请输入姓名", true, 0);
                    return;
                }
                position = tvAddemployessPosition.getText().toString();
                if (position.length() <= 0) {
                    cartState.initToast(getContext(), "请输入职位", true, 0);
                    return;
                }
                phone = etAllemployeesPhone.getText().toString();
                if (phone.length() <= 0) {
                    cartState.initToast(getContext(), "请输入手机号码", true, 0);
                    return;
                }
                if (!cartState.isMobile(phone)) {
                    cartState.initToast(getContext(), "请输入正确的手机号码", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(21, "提示", "是否添加员工", "否", "是");
                whyDilogFragment.show(getFragmentManager(), "提交订单");
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
    public void onEventMainThread(AddEmployessPullFind a) {
        initPull(false);
    }

    /**
     * 提交订单
     *
     * @param a
     */
    @Subscribe
    public void onEventMainThread(AllEmployeesFind a) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        params.put("shop_id", shopId);
        params.put("name", name);
        params.put("role_id", positionId);
        params.put("phone", phone);
        okHttpRequestWrap.postString(CartAddaress.ADD_ROLL, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---添加员工---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        eventBus.post(new AllEmployeesPullFind());
                        eventBus.post(new DatilsPullFind(0));
                        cartState.initToast(getContext(), msg, true, 0);
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
}
