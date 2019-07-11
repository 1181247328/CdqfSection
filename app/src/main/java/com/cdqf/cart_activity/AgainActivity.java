package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.cdqf.cart.R;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 重新定位
 */
public class AgainActivity extends BaseActivity {
    private String TAG = AgainActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_again_return)
    public RelativeLayout rlAgainReturn = null;

    //地图
    @BindView(R.id.mv_again_map)
    public MapView mvAgainMap = null;

    //重新定位
    @BindView(R.id.rcrl_again_position)
    public RCRelativeLayout rcrlAgainPosition = null;

    //我的位置
    @BindView(R.id.tv_again_current)
    public TextView tvAgainCurrent = null;

    //状态
    @BindView(R.id.tv_again_state)
    public TextView tvAgainState = null;

    //地址
    @BindView(R.id.tv_again_address)
    public TextView tvAgainAddress = null;

    //相机
    @BindView(R.id.rcrl_again_raphic)
    public RCRelativeLayout rcrlAgainRaphic = null;

    //选项
    @BindView(R.id.et_again_fill)
    public EditText etAgainFill = null;

    //打卡
    @BindView(R.id.rcrl_again_clock)
    public RCRelativeLayout rcrlAgainClock = null;

    private AMap aMap = null;

    private MyLocationStyle myLocationStyle = null;

    private Circle cirCle = null;

    private LatLng latLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_again);

        StaturBar.setStatusBar(this, R.color.white);

        cartState.changeStatusBarTextImgColor(this, true);

        initAgo();

        mvAgainMap.onCreate(savedInstanceState);

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        ButterKnife.bind(this);
        imageLoader = cartState.getImageLoader(context);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {
        if (aMap == null) {
            aMap = mvAgainMap.getMap();
        }
    }

    private void initAdapter() {

    }

    private void initListener() {

    }

    private void initBack() {
        initLocationStyle();
    }

    //定位
    private void initLocationStyle() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));//地图缩放
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null) {
                    //维度
                    double latitude = location.getLatitude();
                    //经度
                    double longitude = location.getLongitude();
                    Log.e(TAG, "---纬度---" + latitude + "---经度---" + longitude);
                    //代表定位当前
                    latLng = new LatLng(latitude, longitude);
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
            }
        });

        //打卡的位置
        LatLng latLngTwo = new LatLng(30.672024, 104.085349);
        cirCle = aMap.addCircle(new CircleOptions().
                center(latLngTwo).
                radius(300).
                fillColor(Color.BLUE).
                strokeColor(Color.TRANSPARENT));
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLngTwo).title("成都启锋科技有限公司"));
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_again_return, R.id.rcrl_again_position, R.id.rcrl_again_clock})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_again_return:
                finish();
                break;
            //重新定位
            case R.id.rcrl_again_position:
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                break;
            case R.id.rcrl_again_clock:
                LatLng latLng = new LatLng(30.673788, 104.084919);
                boolean isCard = cirCle.contains(latLng);
                if (isCard) {
                    Toast.makeText(context, "下班了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "请到指定位置打卡", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "---启动---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "---恢复---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
        mvAgainMap.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
        mvAgainMap.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        mvAgainMap.onDestroy();
        eventBus.unregister(this);
    }

    /**
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughFind t) {

    }

}