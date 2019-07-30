package com.cdqf.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdqf.cart.R;
import com.cdqf.cart_dilog.WhyDilogFragment;
import com.cdqf.cart_find.DatilsPullFind;
import com.cdqf.cart_find.NoteFind;
import com.cdqf.cart_okhttp.OKHttpRequestWrap;
import com.cdqf.cart_okhttp.OnHttpRequest;
import com.cdqf.cart_state.BaseActivity;
import com.cdqf.cart_state.CartAddaress;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.StaturBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 添加备注
 */
public class NoteActivity extends BaseActivity {
    private String TAG = ClockActivity.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_note_return)
    public RelativeLayout rlNoteReturn = null;

    //本单
    @BindView(R.id.tv_note_single)
    public TextView tvNoteSingle = null;

    //本会员
    @BindView(R.id.tv_note_members)
    public TextView tvNoteMembers = null;

    //添加备注
    @BindView(R.id.et_note_context)
    public EditText etNoteContext = null;

    //提交
    @BindView(R.id.tv_note_submit)
    public TextView tvNoteSubmit = null;

    private String note = "";

    //1 = 本单，2 = 会员
    private int noteNumber = 1;

    //是否能对会员进行评价
    private boolean isNote = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //加载布局
        setContentView(R.layout.activity_note);

        StaturBar.setStatusBar(this, R.color.tab_main_text_icon);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (cartState.getDatils().getUserid() == 0) {
            isNote = false;
        }
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

    @OnClick({R.id.rl_note_return, R.id.tv_note_submit, R.id.tv_note_single, R.id.tv_note_members})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_note_return:
                finish();
                break;
            //本单
            case R.id.tv_note_single:
                noteNumber = 1;
                tvNoteSingle.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                tvNoteSingle.setBackgroundColor(ContextCompat.getColor(context, R.color.tab_main_text_icon));

                tvNoteMembers.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                tvNoteMembers.setBackgroundColor(ContextCompat.getColor(context, R.color.shop_one));
                break;
            //会员
            case R.id.tv_note_members:
                if (!isNote) {
                    cartState.initToast(context, "只能对订单进行评价", true, 0);
                    return;
                }
                noteNumber = 2;
                tvNoteSingle.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                tvNoteSingle.setBackgroundColor(ContextCompat.getColor(context, R.color.shop_one));

                tvNoteMembers.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
                tvNoteMembers.setBackgroundColor(ContextCompat.getColor(context, R.color.tab_main_text_icon));
                break;
            //提交
            case R.id.tv_note_submit:
                note = etNoteContext.getText().toString();
                if (note.length() <= 0) {
                    cartState.initToast(context, "备注不能为空", true, 0);
                    return;
                }
                WhyDilogFragment whyDilogFragment = new WhyDilogFragment();
                whyDilogFragment.setInit(17, "提示", "是否添加备注", "否", "是");
                whyDilogFragment.show(getSupportFragmentManager(), "添加备注");
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
     * @param n
     */
    @Subscribe
    public void onEventMainThread(NoteFind n) {
        Map<String, Object> params = new HashMap<String, Object>();
        OKHttpRequestWrap okHttpRequestWrap = new OKHttpRequestWrap(context);
        String address;
        if (noteNumber == 1) {
            //添加订单备注信息
            address = CartAddaress.ORDER_NOET;
            params.put("order_id", cartState.getDatils().getId());
            params.put("remarks", note);
        } else {
            //添加会员备注信息
            address = CartAddaress.USER_NOET;
            params.put("user_id", cartState.getDatils().getUserid());
            params.put("content", note);
            params.put("staff_id", cartState.getUser().getId());
        }
        okHttpRequestWrap.postString(address, true, "添加中", params, new OnHttpRequest() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                Log.e(TAG, "---onOkHttpResponse添加备注---" + response);
                JSONObject resultJSON = JSON.parseObject(response);
                int error_code = resultJSON.getInteger("code");
                String msg = resultJSON.getString("message");
                switch (error_code) {
                    //获取成功
                    case 200:
                        cartState.initToast(context, msg, true, 0);
                        eventBus.post(new DatilsPullFind());
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
            }
        });
    }

}