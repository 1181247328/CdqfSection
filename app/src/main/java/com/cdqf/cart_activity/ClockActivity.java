package com.cdqf.cart_activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cdqf.cart.R;
import com.cdqf.cart_class.Clock;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartCalender;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.BitmapCallback;
import com.zxy.tiny.callback.FileCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 考勤打卡
 */
public class ClockActivity extends BaseActivity {
    private String TAG = ClockActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    @BindView(R.id.srl_clock_pull)
    public SwipeRefreshLayout srlClockPull = null;

    @BindView(R.id.sv_clock_sc)
    public ScrollView svClockSc = null;

    //返回
    @BindView(R.id.rl_clock_return)
    public RelativeLayout rlClockReturn = null;

    //头像
    @BindView(R.id.iv_clock_hear)
    public ImageView ivClockHear = null;

    //姓名
    @BindView(R.id.tv_clock_name)
    public TextView tvClockName = null;

    //所属店
    @BindView(R.id.tv_clock_address)
    public TextView tvClockAddress = null;

    //时间
    @BindView(R.id.tv_clock_timer)
    public TextView tvClockTimer = null;

    @BindView(R.id.rcrl_clock_state)
    public RCRelativeLayout rcrlClockState = null;

    //状态
    @BindView(R.id.tv_clock_state)
    public TextView tvClockState = null;

    //状态时间
    @BindView(R.id.tv_clock_statetimer)
    public TextView tvClockStateTimer = null;

    //范围
    @BindView(R.id.tv_clock_scope)
    public TextView tvClockScope = null;

    //重新定位
    @BindView(R.id.tv_clock_positioning)
    public TextView tvClockPositioning = null;

    //固定上班时间
    @BindView(R.id.tv_clock_fixed)
    public TextView tvClockFixed = null;

    @BindView(R.id.ll_clock_on_timer)
    public LinearLayout llClockOnTimer = null;

    //上班打卡时间
    @BindView(R.id.tv_clock_workon)
    public TextView tvClockWorkon = null;

    //上班打卡地点
    @BindView(R.id.tv_clock_addresson)
    public TextView tvClockAddresson = null;

    //上班打卡图片
    @BindView(R.id.rcrl_clock_imageon)
    public RCRelativeLayout rcrlClockImageon = null;
    @BindView(R.id.iv_clock_imageon)
    public ImageView ivClockImageon = null;

    //下班
    @BindView(R.id.ll_clock_under)
    public LinearLayout llClockUnder = null;

    //固定下班时间
    @BindView(R.id.tv_clock_after)
    public TextView tvClockAfter = null;

    @BindView(R.id.ll_clock_two)
    public LinearLayout llClockTwo = null;

    //下班打卡时间
    @BindView(R.id.tv_clock_fixedafterafter)
    public TextView tvClockFixedafterafter = null;

    //下班打卡地点
    @BindView(R.id.tv_clock_addressafter)
    public TextView tvClockAddressafter = null;

    //下班打卡图片
    @BindView(R.id.rcrl_clock_imageafter)
    public RCRelativeLayout rcrlClockImageafter = null;
    @BindView(R.id.iv_clock_imageafter)
    public ImageView ivClockImageafter = null;

    //上班的拍摄图片
    @BindView(R.id.tv_clock_image_one)
    public TextView tvClockImageOne = null;

    @BindView(R.id.tv_clock_two)
    public TextView tvClockTwo = null;

    @BindView(R.id.tv_clock_id)
    public TextView tvClockId = null;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String image;

    private Circle cirCle = null;

    private LatLng latLng = null;

    private boolean isWork = true;

    //是否已经定位
    private boolean isClock = false;

    //是否到了打卡时间
    private boolean isCart = false;

    //是不是拍了上班门店照片
    private boolean isImageOne = false;

    //上班门店照片Uri
    private Uri photoUriOne = null;

    private String photoUriOn = "";

    private boolean isCard = false;

    private String address = "";

    //是不是可以点击拍摄照片
    private boolean isImagesone = true;

    //是不是拍摄了下班打卡的门店照片
    private boolean isImageTwo = false;

    private int work = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_clock);

        StaturBar.setStatusBar(this, R.color.white);

        cartState.changeStatusBarTextImgColor(this, true);

        initAgo();

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

    }

    private void initAdapter() {

    }

    private void initListener() {
        srlClockPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPull(false);
            }
        });
    }

    private void initBack() {
        svClockSc.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                srlClockPull.setEnabled(svClockSc.getScrollY() == 0);
            }
        });

        initPull(true);

    }

    private void initOption() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        double latiude = aMapLocation.getLatitude();//获取纬度
                        double longitude = aMapLocation.getLongitude();//获取经度
                        GeocodeSearch geocoderSearch = new GeocodeSearch(ClockActivity.this);
                        LatLonPoint latLonPoint = new LatLonPoint(latiude, longitude);
                        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 300, GeocodeSearch.AMAP);
                        geocoderSearch.getFromLocationAsyn(query);
                        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                            @Override
                            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                                if (i == 1000) {
                                    address = regeocodeResult.getRegeocodeAddress().getProvince() +
                                            regeocodeResult.getRegeocodeAddress().getCity() +
                                            regeocodeResult.getRegeocodeAddress().getDistrict() +
                                            regeocodeResult.getRegeocodeAddress().getTownship();
                                }
                            }

                            @Override
                            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                            }
                        });
                        address = "";
                        Log.e(TAG, "---纬度---" + latiude + "---经度---" + longitude);
                        latLng = new LatLng(latiude, longitude);
                        isClock = true;
                        //获取系统时间
                        //时
                        int hour = CartCalender.getHour();
                        String hours = hour + "";
                        if (hour <= 9) {
                            hours = "0" + hour;
                        }
                        //分
                        int minute = CartCalender.getMinute();
                        String minutes = minute + "";
                        if (minute <= 9) {
                            minutes = "0" + minute;
                        }
                        //秒
                        int second = CartCalender.getSecond();
                        String seconds = second + "";
                        if (second <= 9) {
                            seconds = "0" + second;
                        }
                        tvClockStateTimer.setVisibility(View.VISIBLE);
                        tvClockStateTimer.setText(hours + ":" + minutes + ":" + seconds);
                        //状态
                        //打卡范围到了
                        isCard = cirCle.contains(latLng);
                        if (isCard) {
                            tvClockScope.setText("已进入打卡范围");
                        } else {
                            tvClockScope.setText("未进入打卡范围");
                        }
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e(TAG, "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    private void initPull(boolean isToast) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staff_id", cartState.getUser().getId());
        params.put("shop_id", cartState.getUser().getShopid());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.CLOCK, isToast, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---打卡---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        Clock clock = gson.fromJson(data, Clock.class);
                        cartState.setClock(clock);
                        //姓名
                        tvClockName.setText(cartState.getUser().getUsername());
                        //店名
                        tvClockAddress.setText(clock.getShop().getName());
                        //时间
                        String timer = getFetureDate(clock.getDisplay_time());
                        tvClockTimer.setText(timer);
                        //打卡状态
                        tvClockState.setText(clock.getTips());
                        //打卡的位置
                        double latitude = Double.parseDouble(clock.getShop().getLatitude());
                        double longitude = Double.parseDouble(clock.getShop().getLongitude());
                        LatLng latLngTwo = new LatLng(latitude, longitude);
                        AMap aMap = new MapView(ClockActivity.this).getMap();
                        cirCle = aMap.addCircle(new CircleOptions().
                                center(latLngTwo).
                                radius(300).
                                fillColor(Color.BLUE).
                                strokeColor(Color.TRANSPARENT));
                        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLngTwo).title("成都启锋科技有限公司"));

                        if (clock.getGo_work().size() <= 0) {
                            isWork = true;
                        } else {
                            isWork = false;
                        }

                        //是否上班
                        if (clock.getGo_work().size() <= 0) {
                            llClockOnTimer.setVisibility(View.GONE);
                            tvClockImageOne.setVisibility(View.VISIBLE);
                            isImagesone = true;
                            isImageOne = false;
                            llClockUnder.setVisibility(View.GONE);
                        } else {
                            work = 1;
                            isImagesone = false;
                            isImageOne = true;
                            llClockUnder.setVisibility(View.VISIBLE);
                            llClockTwo.setVisibility(View.GONE);
                            llClockOnTimer.setVisibility(View.VISIBLE);
                            tvClockImageOne.setVisibility(View.GONE);
                            tvClockWorkon.setText(clock.getGo_work().get(0).getCreated_at());
                            tvClockAddresson.setText(clock.getShop().getName());
                            imageLoader.displayImage(clock.getGo_work().get(0).getImage(), ivClockImageon);
                        }

                        //是否下班
                        if (clock.getLeave_work().size() <= 0) {
                            llClockTwo.setVisibility(View.GONE);
                            tvClockTwo.setVisibility(View.VISIBLE);
                            isImagesone = true;
                        } else {
                            work = 2;
                            llClockTwo.setVisibility(View.VISIBLE);
                            tvClockTwo.setVisibility(View.GONE);
                            isImagesone = false;
                            tvClockFixedafterafter.setText(clock.getLeave_work().get(0).getCreated_at());
                            tvClockAddressafter.setText(clock.getShop().getName());
                            imageLoader.displayImage(clock.getLeave_work().get(0).getImage(), ivClockImageafter);
                        }

                        initOption();
                        break;
                    default:
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
                cartState.initToast(context, error, true, 0);
            }
        });
    }

    /**
     * 上班相机
     */
    private void camera(int REQUEST_CODE_TAKE_PICTURE) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        photoUriOne = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUriOne);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        //转换来的base64码需要加前缀，必须是NO_WRAP参数，表示没有空格。
        return "data:image/png;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP);
        //转换来的base64码不需要需要加前缀，必须是NO_WRAP参数，表示没有空格。
        //return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * 将base64的头去掉
     *
     * @param base64
     * @return
     */
    public static String base64ToNoHeaderBase64(String base64) {
        return base64.replace("data:image/png;base64,", "");
    }

    private String getTimeOne(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getFetureDate(long expire) {
        //PHP和Java时间戳存在三位位差，用000补齐
        if (String.valueOf(expire).length() == 10) {
            expire = expire * 1000;
        }
        Date date = new Date(expire);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(date);
        if (result.startsWith("0")) {
            result = result.substring(1);
        }
        return result;
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_clock_return, R.id.rcrl_clock_state, R.id.tv_clock_positioning, R.id.rcrl_clock_imageon, R.id.tv_clock_id, R.id.rcrl_clock_imageafter})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_clock_return:
                finish();
                break;
            case R.id.rcrl_clock_state:
                if(work == 2){
                    return;
                }
                //判断是不是定位了
                if (!isClock) {
                    cartState.initToast(context, "努力定位中", true, 0);
                    return;
                }
//                //判断是不是到了打卡时间
//                if (!isCart) {
//                    cartState.initToast(context, "未到打卡时间", true, 0);
//                    return;
//                }
                if (isWork) {
                    //判断是不是拍了上班打卡门店照片
                    if (!isImageOne) {
                        cartState.initToast(context, "请拍摄上班门店照片", true, 0);
                        return;
                    }
                } else {
                    if (!isImageTwo) {
                        cartState.initToast(context, "请拍摄下班门店照片", true, 0);
                        return;
                    }
                }
                //判断是不是打过卡了

                //判断是不是正常上班打卡
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("staff_id", cartState.getUser().getId());
                params.put("shop_id", cartState.getUser().getShopid());
                params.put("amap_lng", latLng.longitude);
                params.put("amap_lat", latLng.latitude);
                if (isCard) {
                    params.put("position_status", 1);
                } else {
                    params.put("position_status", 2);
                }
                params.put("image", image);
                params.put("address", address);
                OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
                okHttpRequestWrap.postString(CartAddaress.CLOCK_IN, true, "请稍候", params, new OnHttpRequest() {
                    @Override
                    public void onOkHttpResponse(String response, int id) {
                        Log.e(TAG, "---onOkHttpResponse---打卡---" + response);
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("code");
                        String msg = resultJSON.getString("message");
                        switch (error_code) {
                            //获取成功
                            case 204:
                            case 201:
                            case 200:
                                cartState.initToast(context, msg, true, 0);
                                initPull(true);
                                break;
                            default:
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }

                    @Override
                    public void onOkHttpError(String error) {
                        Log.e(TAG, "---onOkHttpError---" + error);
                        cartState.initToast(context, error, true, 0);
                    }
                });

                break;
            //重新定位
            case R.id.tv_clock_positioning:
                initIntent(AgainActivity.class);
                break;
            //上班打卡图片
            case R.id.rcrl_clock_imageafter:
            case R.id.rcrl_clock_imageon:
                if (!isImagesone) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ClockActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ClockActivity.this, new String[]{Manifest.permission.CAMERA}, 8);
                    } else {
                        camera(11);
                    }
                } else {
                    camera(11);
                }
                break;
            case R.id.tv_clock_id:
                initIntent(ClockinActivity.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //上班拍照
            case 11:
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    Log.e(TAG, "---Uri不为空---");
                    uri = data.getData();
                } else {
                    Log.e(TAG, "---Uri为空---");
                    uri = photoUriOne;
                }
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(uri).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        Log.e(TAG, "---员工使用扫一扫---" + outfile);
                        isImageOne = true;
                        isImageTwo = true;
                        photoUriOn = outfile;
                        if (isWork) {
                            imageLoader.displayImage("file:/" + outfile, ivClockImageon);
                        } else {
                            imageLoader.displayImage("file:/" + outfile, ivClockImageafter);
                        }
                    }
                });

                Tiny.getInstance().source(uri).asBitmap().withOptions(options).compress(new BitmapCallback() {
                    @Override
                    public void callback(boolean isSuccess, Bitmap bitmap, Throwable t) {
                        Log.e(TAG, "---门店图片---");
                        isImageOne = true;
                        isImageTwo = true;
                        image = bitmapToBase64(bitmap);
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
        eventBus.unregister(this);
        mlocationClient.onDestroy();
    }

    /**
     * @param t
     */
    @Subscribe
    public void onEventMainThread(ThroughFind t) {

    }

}