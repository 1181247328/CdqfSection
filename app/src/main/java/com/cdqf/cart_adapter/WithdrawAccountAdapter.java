package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_state.CartState;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 已撤回
 */
public class WithdrawAccountAdapter extends BaseAdapter {

    private String TAG = WithdrawAccountAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public WithdrawAccountAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getWithdrawList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_audit, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAuditItemNumber.setText("工号：" + cartState.getWithdrawList().get(position).getStaff_id());
        viewHolder.tvAuditItemPrice.setText("报销金额：￥" + cartState.getWithdrawList().get(position).getExamine_price());
        viewHolder.tvAuditItemTimer.setText("报销时间：" + cartState.getWithdrawList().get(position).getCreated_at());
        viewHolder.tvAuditItemTimer.setText("已撤回");
        return convertView;
    }

    class ViewHolder {

        //车牌号
        @BindView(R.id.tv_audit_item_number)
        public TextView tvAuditItemNumber = null;

        //金额
        @BindView(R.id.tv_audit_item_price)
        public TextView tvAuditItemPrice = null;

        //时间
        @BindView(R.id.tv_audit_item_timer)
        public TextView tvAuditItemTimer = null;

        //状态
        @BindView(R.id.tv_audit_item_state)
        public TextView tvAuditItemState = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

}