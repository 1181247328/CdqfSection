package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_state.CartState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 会员下单适配器
 */
public class MembershipAdapter extends BaseAdapter {

    private String TAG = MembershipAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public MembershipAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getMemebersshipList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_membership, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvMembershipItemName.setText(cartState.getMemebersshipList().get(position).getUser_car().getGet_car().getCarnum());
        viewHolder.tvMembershipItemPhone.setText(cartState.getMemebersshipList().get(position).getUser_car().getPhone());
        viewHolder.tvMembershipItemTimer.setText(cartState.getMemebersshipList().get(position).getUser_car().getAddtime());
        viewHolder.tvMembershipItemPrice.setText(cartState.getMemebersshipList().get(position).getBalance());
        return convertView;
    }

    class ViewHolder {

        //车牌
        @BindView(R.id.tv_membership_item_name)
        public TextView tvMembershipItemName = null;

        //手机
        @BindView(R.id.tv_membership_item_phone)
        public TextView tvMembershipItemPhone = null;

        //时间
        @BindView(R.id.tv_membership_item_timer)
        public TextView tvMembershipItemTimer = null;

        //余额
        @BindView(R.id.tv_membership_item_price)
        public TextView tvMembershipItemPrice = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}