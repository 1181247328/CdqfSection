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
import com.cdqf.cart_state.CartState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 其它服务
 */
public class OtherDilogFragment extends DialogFragment {

    private String TAG = OtherDilogFragment.class.getSimpleName();

    private View view = null;

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private int position = 0;

    private int type = 0;

    //取消
    @BindView(R.id.tv_other_dilog_cancel)
    public TextView tvOtherDilogCancel = null;

    //确定
    @BindView(R.id.tv_other_dilog_sumit)
    public TextView tvOtherDilogSumit = null;

    //服务名称
    @BindView(R.id.et_other_dilog_name)
    public EditText etOtherDilogName = null;

    //服务项目
    @BindView(R.id.et_other_dilog_project)
    public EditText etOtherDilogProject = null;

    //耗材成本
    @BindView(R.id.et_other_dilog_cost)
    public EditText etOtherDilogCost = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        view = inflater.inflate(R.layout.dilog_other, null);
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

    @OnClick({R.id.tv_other_dilog_cancel, R.id.tv_other_dilog_sumit})
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.tv_other_dilog_cancel:
                dismiss();
                break;
            //确定
            case R.id.tv_other_dilog_sumit:
                dismiss();
                break;
        }
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
