package com.cdqf.cart_activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_adapter.FillContextAdapter;
import com.cdqf.cart_adapter.FillImageAdapter;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.AuditPullFind;
import com.cdqf.cart_find.FillAddImageFind;
import com.cdqf.cart_find.FillContextCencelFind;
import com.cdqf.cart_find.FillContextFind;
import com.cdqf.cart_find.FillFind;
import com.cdqf.cart_find.FillPriceFind;
import com.cdqf.cart_find.ShopFillFind;
import com.cdqf.cart_find.TypeFillTypeFind;
import com.cdqf.cart_image.PagerActivity;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.MyGridView;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
 * 报销明细
 */
public class FillActivity extends BaseActivity {

    private String TAG = FillActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_fill_return)
    public RelativeLayout rlFillReturn = null;

    @BindView(R.id.sv_fill_view)
    public ScrollView svFillView = null;

    //时间
    @BindView(R.id.tv_fill_data)
    public TextView tvFillData = null;

    @BindView(R.id.lvfsv_fill_list)
    public ListViewForScrollView lvfsvFillList = null;

    private FillContextAdapter fillContextAdapter = null;

    //添加报销明细
    @BindView(R.id.ll_fill_add)
    public LinearLayout llFillAdd = null;

    //总额
    @BindView(R.id.tv_fill_total)
    public TextView tvFillTotal = null;

    @BindView(R.id.mgv_fill_list)
    public MyGridView mgvFillList = null;

    private FillImageAdapter fillImageAdapter = null;

    //提交审批
    @BindView(R.id.tv_fill_account)
    public TextView tvFillAccount = null;

    private Handler handler = null;

    private List<LocalMedia> selectList = new CopyOnWriteArrayList<>();

    //店铺id
    private int shopId;

    //金额
    private String price = "";

    //报销类别
    private int fillId;

    //费用描述
    private String describe = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_fill);

        StaturBar.setStatusBar(this, R.color.tab_main_text_icon);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        ButterKnife.bind(this);
        imageLoader = cartState.getImageLoader(context);
        handler = new Handler();
    }

    private void initView() {

    }

    private void initAdapter() {
        fillContextAdapter = new FillContextAdapter(context);
        lvfsvFillList.setAdapter(fillContextAdapter);
        fillImageAdapter = new FillImageAdapter(context);
        mgvFillList.setAdapter(fillImageAdapter);
    }

    private void initListener() {
        mgvFillList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initIntent(PagerActivity.class, position);
            }
        });
    }

    private void initBack() {
        //店铺
        fillContextAdapter.setShopName(cartState.getUser().getShopName());
        shopId = Integer.parseInt(cartState.getUser().getShopid()+"");
        //类别
        fillId = 1;
        fillContextAdapter.setType("耗材");
    }

    /**
     * 报销类别
     */
    private void initTypePull() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staff_id", cartState.getUser().getId());
        Log.e(TAG, "---类别---" + cartState.getUser().getId());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.FILL_TYPE, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---报销类别---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 204:
                    case 201:
                    case 200:
                        String data = resultJSON.getString("data");
                        cartState.initToast(context, msg, true, 0);
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

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        intent.putExtra("pageList", (Serializable) cartState.getImagePathsList());
        startActivity(intent);
    }

    private void create() {
        PictureSelector.create(FillActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(4)
                .minSelectNum(1)
                .imageSpanCount(4)
                .compress(true)
                .minimumCompressSize(100)
                .imageFormat(PictureMimeType.PNG)
                .selectionMedia(selectList)
                .isCamera(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    @OnClick({R.id.rl_fill_return, R.id.ll_fill_add, R.id.tv_fill_account})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_fill_return:
                finish();
                break;
            //添加报销明细
            case R.id.ll_fill_add:
                fillContextAdapter.setNumber(1);
                handler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        svFillView.fullScroll(ScrollView.FOCUS_DOWN);
                        lvfsvFillList.setSelection(fillContextAdapter.getCount() - 1);
                    }
                }, 300);
                break;
            //提交审批
            case R.id.tv_fill_account:
                if (TextUtils.equals(price, "")) {
                    cartState.initToast(context, "请输入金额", true, 0);
                    return;
                }

                if (TextUtils.equals(describe, "")) {
                    cartState.initToast(context, "请输入费用描述", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(18, "提示", "是否提交报销", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "提交报销");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    cartState.getImagePathsList().clear();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.e(TAG, "---1---" + media.getPath() + "---2---" + media.getCutPath() + "---3---" + media.getCompressPath());
                        if (media.isCompressed()) {
                            cartState.getImagePathsList().add(media.getCompressPath());
                        } else if (media.isCut()) {
                            cartState.getImagePathsList().add(media.getCutPath());
                        } else {
                            cartState.getImagePathsList().add(media.getPath());
                        }
                    }
                    fillImageAdapter.notifyDataSetChanged();
                    break;
            }
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
        cartState.getImagePathsList().clear();
        PictureFileUtils.deleteCacheDirFile(FillActivity.this);
    }

    /**
     * 添加
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(FillAddImageFind s) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(FillActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FillActivity.this, new String[]{Manifest.permission.CAMERA}, 8);
            } else {
                create();
            }
        } else {
            create();
        }

    }

    /**
     * 门店选择
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShopFillFind s) {
        if (cartState.getHomeList().size() <= 0) {
            return;
        }
        String[] homeName = new String[cartState.getHomeList().size()];
        for (int i = 0; i < cartState.getHomeList().size(); i++) {
            homeName[i] = cartState.getHomeList().get(i).getShop_new_name();
        }
        SinglePicker<String> pickerSource = new SinglePicker<>(FillActivity.this, homeName);
        LineConfig configSource = new LineConfig();
        configSource.setColor(ContextCompat.getColor(FillActivity.this, R.color.addstore_one));//线颜色
        configSource.setThick(ConvertUtils.toPx(FillActivity.this, 1));//线粗
        configSource.setItemHeight(20);
        pickerSource.setLineConfig(configSource);
        pickerSource.setCanLoop(false);//不禁用循环
        pickerSource.setLineVisible(true);
        pickerSource.setTopLineColor(Color.TRANSPARENT);
        pickerSource.setTextSize(14);
        pickerSource.setTitleText("所属店名");
        pickerSource.setSelectedIndex(0);
        pickerSource.setWheelModeEnable(true);
        pickerSource.setWeightEnable(true);
        pickerSource.setWeightWidth(1);
        pickerSource.setCancelTextColor(ContextCompat.getColor(FillActivity.this, R.color.house_eight));//顶部取消按钮文字颜色
        pickerSource.setCancelTextSize(14);
        pickerSource.setSubmitTextColor(ContextCompat.getColor(FillActivity.this, R.color.house_eight));//顶部确定按钮文字颜色
        pickerSource.setSubmitTextSize(14);
        pickerSource.setBackgroundColor(ContextCompat.getColor(FillActivity.this, R.color.white));//背景色
        pickerSource.setSelectedTextColor(ContextCompat.getColor(FillActivity.this, R.color.house_eight));//前四位值是透明度
        pickerSource.setUnSelectedTextColor(ContextCompat.getColor(FillActivity.this, R.color.addstore_one));
        pickerSource.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {

            }
        });
        pickerSource.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                shopId = cartState.getHomeList().get(index).getId();
                fillContextAdapter.setShopName(item);
            }
        });
        pickerSource.show();
    }

    /**
     * 报销类别
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(TypeFillTypeFind s) {
        SinglePicker<String> pickerSource = new SinglePicker<>(FillActivity.this, new String[]{
                "耗材", "硬件", "微信", "日常"
        });
        LineConfig configSource = new LineConfig();
        configSource.setColor(ContextCompat.getColor(FillActivity.this, R.color.addstore_one));//线颜色
        configSource.setThick(ConvertUtils.toPx(FillActivity.this, 1));//线粗
        configSource.setItemHeight(20);
        pickerSource.setLineConfig(configSource);
        pickerSource.setCanLoop(false);//不禁用循环
        pickerSource.setLineVisible(true);
        pickerSource.setTopLineColor(Color.TRANSPARENT);
        pickerSource.setTextSize(14);
        pickerSource.setTitleText("报销类别");
        pickerSource.setSelectedIndex(0);
        pickerSource.setWheelModeEnable(true);
        pickerSource.setWeightEnable(true);
        pickerSource.setWeightWidth(1);
        pickerSource.setCancelTextColor(ContextCompat.getColor(FillActivity.this, R.color.house_eight));//顶部取消按钮文字颜色
        pickerSource.setCancelTextSize(14);
        pickerSource.setSubmitTextColor(ContextCompat.getColor(FillActivity.this, R.color.house_eight));//顶部确定按钮文字颜色
        pickerSource.setSubmitTextSize(14);
        pickerSource.setBackgroundColor(ContextCompat.getColor(FillActivity.this, R.color.white));//背景色
        pickerSource.setSelectedTextColor(ContextCompat.getColor(FillActivity.this, R.color.house_eight));//前四位值是透明度
        pickerSource.setUnSelectedTextColor(ContextCompat.getColor(FillActivity.this, R.color.addstore_one));
        pickerSource.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {

            }
        });
        pickerSource.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                fillId = index + 1;
                fillContextAdapter.setType(item);
            }
        });
        pickerSource.show();
    }

    /**
     * 取消
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(FillContextCencelFind s) {
        WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
        whyDilogFragment.setInit(15, "提示", "是否删除", "否", "是", s.position);
        whyDilogFragment.show(getSupportFragmentManager(), "删除明细");
    }

    /**
     * 金额
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(FillPriceFind s) {
        Log.e(TAG, "---金额---" + s.price);
        price = s.price;
    }

    /**
     * 内容
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(FillContextFind s) {
        Log.e(TAG, "---内容---" + s.context);
        describe = s.context;
    }

    /**
     * 提交报销
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(FillFind s) {

        Map<String, Object> params = new HashMap<String, Object>();
        //员工id
        params.put("staff_id", cartState.getUser().getId());
        //店铺id
        params.put("shop_id", shopId);
        //耗材
        params.put("type", fillId);
        //报销价格
        params.put("examine_price", price);
        //备注说明
        params.put("describe", describe);
        //图片
//        params.put("img", cartState.getImagePathsList());
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        okHttpRequestWrap.postString(CartAddaress.FILL, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse---提交报销---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 201:
                    case 204:
                    case 200:
                        cartState.initToast(context, msg, true, 0);
                        eventBus.post(new AuditPullFind());
                        finish();
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
}
