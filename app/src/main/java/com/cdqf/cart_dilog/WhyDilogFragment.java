package com.cdqf.cart_dilog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.AccountExitFind;
import com.cdqf.cart_find.AddOrderFind;
import com.cdqf.cart_find.AllEmployeesFind;
import com.cdqf.cart_find.AuditsAgreedFind;
import com.cdqf.cart_find.AuditsDatilsCencalFind;
import com.cdqf.cart_find.DatilsPhoneFind;
import com.cdqf.cart_find.EmployeesIdFind;
import com.cdqf.cart_find.ExitFind;
import com.cdqf.cart_find.FillFind;
import com.cdqf.cart_find.LossNewsFind;
import com.cdqf.cart_find.NoteFind;
import com.cdqf.cart_find.PreferentialFind;
import com.cdqf.cart_find.RefusedTwoFind;
import com.cdqf.cart_find.ReleaseFind;
import com.cdqf.cart_find.ServiceTwoFind;
import com.cdqf.cart_find.ShopServiceTwoFind;
import com.cdqf.cart_find.ShopTwoFind;
import com.cdqf.cart_find.ThroughTwoFind;
import com.cdqf.cart_find.UserSumberFind;
import com.cdqf.cart_find.WithdrawalFind;
import com.cdqf.cart_state.CartState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 疑问对话框
 */
public class WhyDilogFragment extends DialogFragment {

    private String TAG = WhyDilogFragment.class.getSimpleName();

    private View view = null;

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private String title;

    private String context;

    private String cancel;

    private String determine;

    private int type = 0;

    private int position = 0;

    //标题
    @BindView(R.id.tv_why_dilog_title)
    public TextView tvWhyDilogTitle = null;

    //内容
    @BindView(R.id.tv_why_dilog_context)
    public TextView tvWhyDilogContext = null;

    //取消
    @BindView(R.id.tv_why_dilog_cancel)
    public TextView tvWhyDilogCancel = null;

    //确定
    @BindView(R.id.tv_why_dilog_determine)
    public TextView tvWhyDilogDetermine = null;

    public void setInit(int type, String title, String context, String cancel, String determine) {
        this.type = type;
        this.title = title;
        this.context = context;
        this.cancel = cancel;
        this.determine = determine;
    }

    public void setInit(int type, String title, String context, String cancel, String determine, int position) {
        this.type = type;
        this.title = title;
        this.context = context;
        this.cancel = cancel;
        this.determine = determine;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        view = inflater.inflate(R.layout.dilog_why, null);
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
        tvWhyDilogTitle.setText(title);
        tvWhyDilogContext.setText(context);
        tvWhyDilogCancel.setText(cancel);
        tvWhyDilogDetermine.setText(determine);
//        initTextColor();
    }

    private void initTextColor() {
        SpannableString styledText = new SpannableString(context);
        switch (type) {
            //ShopActivty
            case 0:
                styledText.setSpan(new TextAppearanceSpan(getContext(), R.style.style_two), 7, cartState.getShopList().get(position).getCarnum().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            //ShopActivty
            case 1:
                styledText.setSpan(new TextAppearanceSpan(getContext(), R.style.style_two), 7, cartState.getShopList().get(position).getCarnum().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
        }
        tvWhyDilogContext.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    @OnClick({R.id.tv_why_dilog_cancel, R.id.tv_why_dilog_determine})
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.tv_why_dilog_cancel:
                dismiss();
                break;
            case R.id.tv_why_dilog_determine:
                switch (type) {
                    //ShopActivty
                    case 0:
                        eventBus.post(new ShopTwoFind(position));
                        break;
                    //ShopActivty
                    case 1:
                        eventBus.post(new ShopServiceTwoFind(position));
                        break;
                    //ReleaseActivity
                    case 2:
                        eventBus.post(new ReleaseFind());
                        break;
                    //ReleaseActivity
                    case 3:
                        getActivity().finish();
                        break;
                    //ServiceActivity
                    case 4:
                        eventBus.post(new ServiceTwoFind(position));
                        break;
                    //LossManagerActivity
                    case 5:
//                        eventBus.post(new LossReceiveSubmitFind(position));
                        break;
                    //MainManagerActivity
                    case 6:
                        eventBus.post(new ExitFind());
                        break;
                    //DatilsActivity
                    case 7:
                        eventBus.post(new DatilsPhoneFind());
                        break;
                    //AuditActivity
                    case 8:
                        eventBus.post(new ThroughTwoFind(position));
                        break;
                    //AuditActivity
                    case 9:
                        eventBus.post(new RefusedTwoFind(position));
                        break;
                    //UserActivity
                    case 10:
                        eventBus.post(new UserSumberFind());
                        break;
                    //MyFragment
                    case 11:
                        eventBus.post(new AccountExitFind());
                        break;
                    //LossNewsActivity
                    case 12:
                        eventBus.postSticky(new LossNewsFind());
                        break;
                    //EmployeesAddActivity
                    case 13:
                        eventBus.post(new EmployeesIdFind());
                        break;
                    //PreferentialActivity
                    case 14:
                        eventBus.post(new PreferentialFind());
                        break;
                    //FillActivity
                    case 15:

                        break;
                    //AddOrderActivity添加订单
                    case 16:
                        eventBus.post(new AddOrderFind());
                        break;
                    //NoteActivity添加备注
                    case 17:
                        eventBus.post(new NoteFind());
                        break;
                    //FillActivity提交报销
                    case 18:
                        eventBus.post(new FillFind());
                        break;
                    //AuditsDatilsActivity取消报销
                    case 19:
                        eventBus.post(new AuditsDatilsCencalFind());
                        break;
                    //AuditsDatilsActivity同意报销
                    case 20:
                        eventBus.post(new AuditsAgreedFind());
                        break;
                    //WhyDilogFragment提交订单
                    case 21:
                        eventBus.post(new AllEmployeesFind());
                        break;
                    //OtherActivity立即提现
                    case 22:
                        eventBus.post(new WithdrawalFind());
                        break;
                }
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
