package com.cdqf.cart_dilog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.LossReceiveFind;
import com.cdqf.cart_state.CartState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 疑问对话框
 */
public class LossDilogFragment extends DialogFragment {

    private String TAG = LossDilogFragment.class.getSimpleName();

    private View view = null;

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    //物品名称
    @BindView(R.id.tv_loss_dilog_title)
    public TextView tvLossDilogTitle = null;

    //库存
    @BindView(R.id.tv_loss_dilog_number)
    public TextView tvLossDilogNumber = null;

    //领取数量
    @BindView(R.id.xet_loss_dilog_context)
    public EditText xetLossDilogContext = null;

    //取消
    @BindView(R.id.tv_loss_dilog_cancel)
    public TextView tvLossDilogCancel = null;

    //确定
    @BindView(R.id.tv_loss_dilog_determine)
    public TextView tvLossDilogDetermine = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        view = inflater.inflate(R.layout.dilog_loss, null);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //初始化前
        initAgo();

        //初始化控件
        initView();

        //适配器
        initAdapter();

        //注册监听器
        initListener();

        //初始化后
        initBack();
        return view;
    }

    /**
     * 初始化前
     */
    private void initAgo() {
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化控件
     */
    private void initView() {
    }

    private void initAdapter() {

    }

    /**
     * 注册监听器
     */
    private void initListener() {

    }

    /**
     * 初始化后
     */
    private void initBack() {
        getDialog().setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.tv_loss_dilog_cancel, R.id.tv_loss_dilog_determine})
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.tv_loss_dilog_cancel:
                break;
            case R.id.tv_loss_dilog_determine:
                eventBus.post(new LossReceiveFind(1));
                break;
        }
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels / 2 + 100, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
