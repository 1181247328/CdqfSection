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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cdqf.cart.R;
import com.cdqf.cart_find.DatilsPhoneFind;
import com.cdqf.cart_hear.HearDilogFragment;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StatusBarCompat;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xw.repo.XEditText;

import org.angmarch.views.NiceSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

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

    //返回
    @BindView(R.id.rl_addemployees_return)
    public RelativeLayout rlAddemployeesReturn = null;

    //员工姓名
    @BindView(R.id.xet_addemployees_name)
    public XEditText xetAddemployeesName = null;

    //职位
    @BindView(R.id.ns_addemployees_position)
    public NiceSpinner nsAddemployeesPosition = null;

    //手机号
    @BindView(R.id.xet_addemployees_phone)
    public XEditText xetAddemployeesPhone = null;

    //紧急联系人
    @BindView(R.id.xet_addemployees_contact)
    public XEditText xetAddemployeesContact = null;

    //紧急联系人手机号码
    @BindView(R.id.xet_addemployees_number)
    public XEditText xetAddemployeesNumber = null;

    //身份证号码
    @BindView(R.id.xet_addemployees_certificate)
    public XEditText xetAddemployeesCertificate = null;

    //身份证正面
    @BindView(R.id.rcrl_addemployees_positive)
    public RCRelativeLayout rcrAddemployeesPositive = null;
    @BindView(R.id.iv_addemployees_positive)
    public ImageView ivAddemployeesPositive = null;

    //身份证反面
    @BindView(R.id.rcrl_addemployees_reverse)
    public RCRelativeLayout rcrlAddemployeesReverse = null;
    @BindView(R.id.iv_addemployees_reverse)
    public ImageView ivAddemployeesReverse = null;

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

    }

    private void initBack() {

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

    @OnClick({R.id.rl_addemployees_return, R.id.rcrl_addemployees_positive, R.id.tv_addemployees_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_addemployees_return:
                finish();
                break;
            //正面
            case R.id.rcrl_addemployees_positive:
                HearDilogFragment hearDilogFragment = new HearDilogFragment();
                hearDilogFragment.show(getSupportFragmentManager(), "身份证正面");
                break;
            //反面
            case R.id.rcrl_addemployees_reverse:
                HearDilogFragment hearReverseDilogFragment = new HearDilogFragment();
                hearReverseDilogFragment.show(getSupportFragmentManager(), "身份证反面");
                break;
            //提交
            case R.id.tv_addemployees_submit:
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


    /**
     * 输入领取数量
     *
     * @param r
     */
    @Subscribe
    public void onEventMainThread(DatilsPhoneFind r) {
    }
}
