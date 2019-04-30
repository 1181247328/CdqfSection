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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_activity.LossActivity;
import com.cdqf.cart_activity.NoticeActivity;
import com.cdqf.cart_activity.ServiceActivity;
import com.cdqf.cart_adapter.HomeAdapter;
import com.cdqf.cart_find.AssistantShopFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_view.LineGridView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 首页
 */
public class HomeFragment extends Fragment {

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
    public LineGridView mgvHomeList = null;

    private HomeAdapter homeAdapter = null;

    private int s = 0;

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
                        initIntent(LossActivity.class);
                        break;
                    //服务
                    case 1:
                        initIntent(ServiceActivity.class);
                        break;
                    //通知
                    case 2:
                        initIntent(NoticeActivity.class);
                        break;
                }
            }
        });
    }

    private void initAdapter() {
        homeAdapter = new HomeAdapter(getContext());
        mgvHomeList.setAdapter(homeAdapter);
    }

    private void initBack() {
        tvHomeName.setText(cartState.getUser().getName() + ":");
        tvHomeContext.setText("今天是您加入脱狗车宝第" + cartState.getUser().getDay() + "天!");
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        initShopPull();
    }

    private void initShopPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(getContext());
        String shop = shop(cartState.getUser().getShopid());
        okHttpRequestWrap.post(shop, false, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse服务的粘性事件(店员)---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        eventBus.postSticky(new AssistantShopFind(data, true));
                        break;
                    default:
                        s++;
                        if (s == 3) {
                            s = 0;
                            eventBus.postSticky(new AssistantShopFind(msg, false));
                        } else {
                            initShopPull();
                        }
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
            }
        });
    }

    private String shop(String shopid) {
        String result = null;
        result = CartAddaress.ADDRESS + "/?s=order.staff&shopid=" + shopid;
        Log.e(TAG, "---店总---" + result);
        return result;
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
