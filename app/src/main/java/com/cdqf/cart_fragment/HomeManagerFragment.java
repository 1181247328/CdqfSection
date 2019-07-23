package com.cdqf.cart_fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_activity.AccountActivity;
import com.cdqf.cart_activity.AuditActivity;
import com.cdqf.cart_activity.BusinessActivity;
import com.cdqf.cart_activity.ClockActivity;
import com.cdqf.cart_activity.EmployeesActivity;
import com.cdqf.cart_activity.LossNewsActivity;
import com.cdqf.cart_activity.MembersActivity;
import com.cdqf.cart_activity.NoticeManagerActivity;
import com.cdqf.cart_activity.OtherActivity;
import com.cdqf.cart_activity.ShopActivity;
import com.cdqf.cart_adapter.HomeManagerAdapter;
import com.cdqf.cart_class.Home;
import com.cdqf.cart_find.ScanFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_view.LineGridView;
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

/**
 * 首页(店长)
 */
public class HomeManagerFragment extends Fragment {

    private String TAG = HomeManagerFragment.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Gson gson = new Gson();

    private View view = null;

    @BindView(R.id.srl_home_pull)
    public SwipeRefreshLayout srlHomePull = null;

    //选择店
    @BindView(R.id.ll_homemanager_shop)
    public LinearLayout llHomemanagerShop = null;

    @BindView(R.id.tv_homemanager_shop)
    public TextView tvHomemanagerShop = null;

    @BindView(R.id.tv_homemanager_scan)
    public RelativeLayout tvHomemanagerScan = null;

    @BindView(R.id.rcrl_home_situation)
    public RelativeLayout rcrlHomeSituation = null;

    //集合
    @BindView(R.id.mgv_home_list)
    public LineGridView mgvHomeList = null;

    private HomeManagerAdapter homeManagerAdapter = null;

    //数量
    @BindView(R.id.tv_homemanager_number)
    public TextView tvHomemanagerNumber = null;

    //营业额
    @BindView(R.id.tv_homemanager_money)
    public TextView tvHomemanagerMoney = null;

    @BindView(R.id.rl_orders_bar)
    public RelativeLayout rlOrdersBar = null;

    //订单异常
    @BindView(R.id.tv_orders_abnormal)
    public TextView tvOrdersAbnormal = null;

    @BindView(R.id.pb_orders_bar)
    public ProgressBar pbOrdersBar = null;

    //滑动
    @BindView(R.id.nsv_home_scroll)
    public NestedScrollView nsvHomeScroll = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "---创建---");

        view = inflater.inflate(R.layout.fragment_homemanager, container, false);

        initAgo();

        initListener();

        initAdapter();

        initBack();

        return view;
    }

    private void initAgo() {
        imageLoader = cartState.getImageLoader(getContext());
        ButterKnife.bind(this, view);
    }

    private void initListener() {
        srlHomePull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initShopPull();
            }
        });
        mgvHomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = cartState.getUser().getMenu().get(position).getTitle();
                switch (name) {
                    //损耗品
                    case "耗材管理":
                        initIntent(LossNewsActivity.class);
                        break;
                    //服务
                    case "服务":
                        initIntent(ShopActivity.class);
                        break;
                    //通知
                    case "通知":
                        initIntent(NoticeManagerActivity.class);
                        break;
                    //审核
                    case "审核":
                        initIntent(AuditActivity.class);
                        break;
                    //会员
                    case "会员":
                        initIntent(MembersActivity.class);
                        break;
                    //任务
                    case "任务":
                        initIntent(OtherActivity.class);
                        break;
                    //店员管理
                    case "店员管理":
                        initIntent(EmployeesActivity.class);
                        break;
                    //考勤打卡
                    case "考勤打卡":
                        initIntent(ClockActivity.class);
                        break;
                    //报销
                    case "报销":
                        initIntent(AccountActivity.class);
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
        srlHomePull.setEnabled(false);
        initShopPull();
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }

    private void initShopPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staff_id", cartState.getUser().getId());
        Log.e(TAG, "---主页---" + cartState.getUser().getId());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        okHttpRequestWrap.postString(CartAddaress.HOME, false, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---主页---(店员)---" + response);
                if (srlHomePull != null) {
                    srlHomePull.setEnabled(true);
                    srlHomePull.setRefreshing(false);
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
                        tvOrdersAbnormal.setVisibility(View.GONE);
                        nsvHomeScroll.setVisibility(View.VISIBLE);
                        String data = resultJSON.getString("data");
                        cartState.initToast(getContext(), msg, true, 0);
                        cartState.getHomeList().clear();
                        List<Home> homeList = gson.fromJson(data, new TypeToken<List<Home>>() {
                        }.getType());
                        cartState.setHomeList(homeList);

                        //判断是不是有默认的
                        boolean isHome = false;
                        for (Home h : homeList) {
                            if (h.getStatus() == 1) {
                                isHome = true;
                                tvHomemanagerShop.setText(h.getShop_new_name());
                                cartState.getUser().setShopid(h.getId());
                                cartState.getUser().setShopName(h.getShop_new_name());
                                break;
                            }
                        }
                        if (!isHome) {
                            tvHomemanagerShop.setText(homeList.get(0).getShop_new_name());
                            cartState.getUser().setShopid(homeList.get(0).getId());
                            cartState.getUser().setShopName(homeList.get(0).getShop_new_name());
                        }
                        break;
                    default:
                        rlOrdersBar.setVisibility(View.GONE);
                        tvOrdersAbnormal.setVisibility(View.VISIBLE);
                        nsvHomeScroll.setVisibility(View.GONE);
                        cartState.initToast(getContext(), msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                if (srlHomePull != null) {
                    srlHomePull.setEnabled(true);
                    srlHomePull.setRefreshing(false);
                }
                rlOrdersBar.setVisibility(View.GONE);
                tvOrdersAbnormal.setVisibility(View.VISIBLE);
                nsvHomeScroll.setVisibility(View.GONE);
                cartState.initToast(getContext(), error, true, 0);
            }
        });
    }

    /**
     * 修改绑定门店id
     *
     * @param
     */
    private void initShopId() {
        Map<String, Object> params = new HashMap<String, Object>();
        //门店id
        params.put("shop_id", cartState.getUser().getShopid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        okHttpRequestWrap.postString(CartAddaress.CART_SHOP_ID + cartState.getUser().getId(), true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---修改绑定门店---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        String data = resultJSON.getString("data");
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
                cartState.initToast(getContext(), error, true, 0);
            }
        });
    }

    @OnClick({R.id.ll_homemanager_shop, R.id.tv_homemanager_scan, R.id.rcrl_home_situation})
    public void onClick(View v) {
        switch (v.getId()) {
            //选择店铺
            case R.id.ll_homemanager_shop:
                if (cartState.getHomeList().size() <= 0) {
                    initShopPull();
                    return;
                }
                String[] homeName = new String[cartState.getHomeList().size()];
                for (int i = 0; i < cartState.getHomeList().size(); i++) {
                    homeName[i] = cartState.getHomeList().get(i).getShop_new_name();
                }
                SinglePicker<String> pickerSource = new SinglePicker<>(getActivity(), homeName);
                LineConfig configSource = new LineConfig();
                configSource.setColor(ContextCompat.getColor(getContext(), R.color.addstore_one));//线颜色
                configSource.setThick(ConvertUtils.toPx(getActivity(), 1));//线粗
                configSource.setItemHeight(20);
                pickerSource.setLineConfig(configSource);
                pickerSource.setCanLoop(false);//不禁用循环
                pickerSource.setLineVisible(true);
                pickerSource.setTopLineColor(Color.TRANSPARENT);
                pickerSource.setTextSize(14);
                pickerSource.setTitleText("店名");
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
                        tvHomemanagerShop.setText(cartState.getHomeList().get(index).getShop_new_name());
                        cartState.getUser().setShopid(cartState.getHomeList().get(index).getId());
                        cartState.getUser().setShopName(cartState.getHomeList().get(index).getShop_new_name());
                        initShopId();
                    }
                });
                pickerSource.show();
                break;
            //扫一扫
            case R.id.tv_homemanager_scan:
                eventBus.post(new ScanFind());
                break;
            //
            case R.id.rcrl_home_situation:
                initIntent(BusinessActivity.class);
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