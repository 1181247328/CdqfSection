package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_adapter.AccountDatilsContextAdapter;
import com.cdqf.cart_adapter.AccountDatilsImageAdapter;
import com.cdqf.cart_find.FillAddImageFind;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.cdqf.cart_view.ListViewForScrollView;
import com.cdqf.cart_view.MyGridView;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 审批详情
 */
public class AuditsDatilsActivity extends BaseActivity {

    private String TAG = AccountDatilsActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_datils_return)
    public RelativeLayout rlDatilsReturn = null;

    //工号
    @BindView(R.id.tv_datils_title)
    public TextView tvDatilsTitle = null;

    @BindView(R.id.sv_datils_view)
    public ScrollView svDatilsView = null;

    //时间
    @BindView(R.id.tv_datils_data)
    public TextView tvDatilsData = null;

    @BindView(R.id.lvfsv_datils_list)
    public ListViewForScrollView lvfsvDatilsList = null;

    private AccountDatilsContextAdapter accountDatilsContextAdapter = null;

    //总额
    @BindView(R.id.tv_datils_total)
    public TextView tvDatilsTotal = null;

    @BindView(R.id.mgv_datals_list)
    public MyGridView mgvDatalsList = null;

    private AccountDatilsImageAdapter accountDatilsImageAdapter = null;

    //状态
    @BindView(R.id.ll_datils_state)
    public LinearLayout llDatilsState = null;

    @BindView(R.id.tv_datils_state)
    public TextView tvDatilsState = null;

    /****待审批状态*****/
    @BindView(R.id.ll_datis_operation)
    public LinearLayout llDatisOperation = null;

    //取消
    @BindView(R.id.rcrl_datils_cencal)
    public RCRelativeLayout rcrlDatilsCencal = null;

    //同意
    @BindView(R.id.rcrl_datils_agreed)
    public RCRelativeLayout rcrlDatilsAgreed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_audistsdatils);

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
    }

    private void initView() {

    }

    private void initAdapter() {
        accountDatilsContextAdapter = new AccountDatilsContextAdapter(context);
        lvfsvDatilsList.setAdapter(accountDatilsContextAdapter);
        accountDatilsImageAdapter = new AccountDatilsImageAdapter(context);
        mgvDatalsList.setAdapter(accountDatilsImageAdapter);
    }

    private void initListener() {
        mgvDatalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initBack() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_datils_return, R.id.rcrl_datils_cencal, R.id.rcrl_datils_agreed})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_datils_return:
                finish();
                break;
            //取消
            case R.id.rcrl_datils_cencal:
                break;
            //同意
            case R.id.rcrl_datils_agreed:
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
     * @param s
     */
    @Subscribe
    public void onEventMainThread(FillAddImageFind s) {

    }

}
