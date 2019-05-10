package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.cdqf.cart.R;
import com.cdqf.cart_class.Position;
import com.cdqf.cart_dilog.EmployeesDilogFragment;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.EmployeesIdFind;
import com.cdqf.cart_hear.FileUtil;
import com.cdqf.cart_hear.ShelvesImageFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.ACache;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.IDCardValidate;
import com.cdqf.cart_state.StatusBarCompat;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 添加店员
 */
public class EmployeesAddActivity extends BaseActivity {

    private String TAG = EmployeesAddActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    private ACache aCache = null;

    //返回
    @BindView(R.id.rl_addemployees_return)
    public RelativeLayout rlAddemployeesReturn = null;

    //员工姓名
    @BindView(R.id.xet_addemployees_name)
    public XEditText xetAddemployeesName = null;
    private String name = "";

    //职位
    @BindView(R.id.ns_addemployees_position)
    public NiceSpinner nsAddemployeesPosition = null;
    private String positions = "";
    private String position_id = "";

    //手机号
    @BindView(R.id.xet_addemployees_phone)
    public XEditText xetAddemployeesPhone = null;
    private String phone = "";

    //紧急联系人
    @BindView(R.id.xet_addemployees_contact)
    public XEditText xetAddemployeesContact = null;
    private String contact = "";

    //紧急联系人手机号码
    @BindView(R.id.xet_addemployees_number)
    public XEditText xetAddemployeesNumber = null;
    private String contactPhone = "";

    //身份证号码
    @BindView(R.id.xet_addemployees_certificate)
    public XEditText xetAddemployeesCertificate = null;
    private String certificate = "";

    //身份证正面
    @BindView(R.id.rcrl_addemployees_positive)
    public RCRelativeLayout rcrAddemployeesPositive = null;
    @BindView(R.id.iv_addemployees_positive)
    public ImageView ivAddemployeesPositive = null;
    private String positive = "";

    //身份证反面
    @BindView(R.id.rcrl_addemployees_reverse)
    public RCRelativeLayout rcrlAddemployeesReverse = null;
    @BindView(R.id.iv_addemployees_reverse)
    public ImageView ivAddemployeesReverse = null;
    private String reverse = "";

    private String[] image = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //加载布局
        setContentView(R.layout.activity_addemployees);

        //API>=20以上用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉侵
            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.black));
        }

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        aCache = ACache.get(context);
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
        nsAddemployeesPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positions = cartState.getPositionList().get(position).getName();
                position_id = cartState.getPositionList().get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initBack() {
        if (aCache.getAsString("position") != null) {
            String data = aCache.getAsString("position");
            dataPosition(data);
        }
        initPull();
    }

    private void dataPosition(String data) {
        cartState.getPositionList().clear();
        List<Position> positionList = gson.fromJson(data, new TypeToken<List<Position>>() {
        }.getType());
        cartState.setPositionList(positionList);
        List<String> posiList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < positionList.size(); i++) {
            posiList.add(positionList.get(i).getName());
        }
        positions = cartState.getPositionList().get(0).getName();
        position_id = cartState.getPositionList().get(0).getId();
        nsAddemployeesPosition.attachDataSource(posiList);
    }

    private void initPull() {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String position = CartAddaress.SHOP_POSITION;
        okHttpRequestWrap.post(position, true, "请稍候", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse获取员工职位---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("ret");
                String msg = resultJSON.getString("msg");
                switch (error_code) {
                    //获取成功
                    case 200:
                        String data = resultJSON.getString("data");
                        dataPosition(data);
                        aCache.put("position", data);
                        break;
                    default:
                        cartState.initToast(context, msg, true, 0);
                        break;
                }
            }

            @Override
            public void onOkHttpError(String error) {
                Log.e(TAG, "---onOkHttpError---" + error);
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
        startActivity(intent);
    }

    @OnClick({R.id.rl_addemployees_return, R.id.rcrl_addemployees_positive, R.id.tv_addemployees_submit, R.id.rcrl_addemployees_reverse})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_addemployees_return:
                finish();
                break;
            //正面
            case R.id.rcrl_addemployees_positive:
                EmployeesDilogFragment hearDilogFragment = new EmployeesDilogFragment();
                hearDilogFragment.setType(1);
                hearDilogFragment.show(getSupportFragmentManager(), "身份证正面");
                break;
            //反面
            case R.id.rcrl_addemployees_reverse:
                EmployeesDilogFragment hearReverseDilogFragment = new EmployeesDilogFragment();
                hearReverseDilogFragment.setType(2);
                hearReverseDilogFragment.show(getSupportFragmentManager(), "身份证反面");
                break;
            //提交
            case R.id.tv_addemployees_submit:
                name = xetAddemployeesName.getText().toString();
                if (name.length() <= 0) {
                    cartState.initToast(context, "请输入员工姓名", true, 0);
                    return;
                }
                phone = xetAddemployeesPhone.getText().toString();
                if (phone.length() <= 0) {
                    cartState.initToast(context, "请输入员工手机号", true, 0);
                    return;
                }
                if (!cartState.isMobile(phone)) {
                    cartState.initToast(context, "请输入正确的手机号", true, 0);
                    return;
                }
                contact = xetAddemployeesContact.getText().toString();
                if (contact.length() <= 0) {
                    cartState.initToast(context, "请输入紧急联系人", true, 0);
                    return;
                }
                contactPhone = xetAddemployeesNumber.getText().toString();
                if (contactPhone.length() <= 0) {
                    cartState.initToast(context, "请输入紧急联系人手机号", true, 0);
                    return;
                }
                if (!cartState.isMobile(contactPhone)) {
                    cartState.initToast(context, "请输入正确的紧急联系人手机号", true, 0);
                    return;
                }
                certificate = xetAddemployeesCertificate.getText().toString();
                if (certificate.length() <= 0) {
                    cartState.initToast(context, "请输入员工身份证号", true, 0);
                    return;
                }
                if (!IDCardValidate.validate_effective(certificate)) {
                    cartState.initToast(context, "请输入正确的员工身份证号", true, 0);
                    return;
                }
                if (image[0].length() <= 0) {
                    cartState.initToast(context, "请添加员工身份证正面", true, 0);
                    return;
                }
                if (image[1].length() <= 0) {
                    cartState.initToast(context, "请添加员工身份证反面", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(13, "提示", "是否提交新员工" + name + "的申请", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "提交员工添加");
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
    }

    private void image() {
        OkHttpUtils
                .post()
                .url(CartAddaress.SHOP_IDIMAGE)
                .addFile("file", "reverse.png", new File(image[1]))
                .build()
                .execute(new StringCallback() {
                    XProgressDialog xProgressDialog = null;

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Log.e(TAG, "---最后2---");
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        Log.e(TAG, "---开始2---");
                        xProgressDialog = new XProgressDialog(context, "提交中", 1);
                        xProgressDialog.show();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "---onError2---" + e.getMessage());
                        if (xProgressDialog != null) {
                            xProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "---onResponse2---" + response);
                        if (xProgressDialog != null) {
                            xProgressDialog.dismiss();
                        }
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("ret");
                        String msg = resultJSON.getString("msg");
                        switch (error_code) {
                            //获取成功
                            case 200:
                                JSONObject data = resultJSON.getJSONObject("data");
                                reverse = data.getString("url");
                                idCard();
                                break;
                            default:
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }
                });
    }

    private void idCard() {
        String shopowner =
                CartAddaress.ADDRESS +
                        "/?s=Shopowner.add_staff&" +
                        "name=" + name +
                        "&phone=" + phone +
                        "&shop_id=" + position_id +
                        "&position_id=" + cartState.getUser().getShopid() +
                        "&urgent_phone=" + contactPhone +
                        "&urgent_name=" + contact +
                        "&id_card=" + certificate +
                        "&idcard_file_just=" + positive +
                        "&idcard_file_back=" + reverse;
        Log.e(TAG, "---提交员工信息---" + shopowner);
        OkHttpUtils.post()
                .url(shopowner)
//                .addParams("name", name)
//                .addParams("phone", phone)
//                .addParams("position_id", position_id)
//                .addParams("shop_id", cartState.getUser().getShopid())
//                .addParams("urgent_phone", contactPhone)
//                .addParams("urgent_name", contact)
//                .addParams("id_card", certificate)
//                .addFile("idcard_file_just", "positive", new File(positive))
//                .addFile("idcard_file_back", "reverse", new File(reverse))
                .build()
                .execute(new StringCallback() {
                    XProgressDialog xProgressDialog = null;

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Log.e(TAG, "---最后---");
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        Log.e(TAG, "---开始---");
                        xProgressDialog = new XProgressDialog(context, "提交中", 1);
                        xProgressDialog.show();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "---onError---" + e.getMessage());
                        if (xProgressDialog != null) {
                            xProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "---onResponse---" + response);
                        if (xProgressDialog != null) {
                            xProgressDialog.dismiss();
                        }

                    }
                });
    }

    /**
     * 身份证
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(ShelvesImageFind s) {
        if (s.type == 1) {
            ivAddemployeesPositive.setImageBitmap(s.bitmap);
            ivAddemployeesPositive.setScaleType(ImageView.ScaleType.FIT_XY);
//            positive = FileUtil.POSITIVE;
            image[0] = FileUtil.POSITIVE;
        } else if (s.type == 2) {
            ivAddemployeesReverse.setImageBitmap(s.bitmap);
            ivAddemployeesReverse.setScaleType(ImageView.ScaleType.FIT_XY);
//            reverse = FileUtil.REVERSE;
            image[1] = FileUtil.POSITIVE;
        } else {
            //TODO
        }
        Log.e(TAG, "---身份证正面---" + positive + "\n" + "---身份证反面---" + reverse);
    }

    /**
     * 提交
     *
     * @param s
     */
    @Subscribe
    public void onEventMainThread(EmployeesIdFind s) {

        OkHttpUtils
                .post()
                .url(CartAddaress.SHOP_IDIMAGE)
                .addFile("file", "positive.png", new File(image[0]))
                .build()
                .execute(new StringCallback() {
                    XProgressDialog xProgressDialog = null;

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Log.e(TAG, "---最后1---");
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        Log.e(TAG, "---开始1---");
                        xProgressDialog = new XProgressDialog(context, "提交中", 1);
                        xProgressDialog.show();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "---onError1---" + e.getMessage());
                        if (xProgressDialog != null) {
                            xProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "---onResponse1---" + response);
                        if (xProgressDialog != null) {
                            xProgressDialog.dismiss();
                        }
                        JSONObject resultJSON = JSON.parseObject(response);
                        int error_code = resultJSON.getInteger("ret");
                        String msg = resultJSON.getString("msg");
                        switch (error_code) {
                            //获取成功
                            case 200:
                                JSONObject data = resultJSON.getJSONObject("data");
                                positive = data.getString("url");
                                image();
                                break;
                            default:
                                cartState.initToast(context, msg, true, 0);
                                break;
                        }
                    }
                });
    }
}
