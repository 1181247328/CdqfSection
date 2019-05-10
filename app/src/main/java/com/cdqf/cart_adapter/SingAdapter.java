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

/**
 * 职位
 */
public class SingAdapter extends BaseAdapter {
    private String TAG = SingAdapter.class.getSimpleName();

    private Context context = null;

    private CartState cartState = CartState.getCartState();

    public SingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getPositionList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sign, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSignItemPosition.setText(cartState.getPositionList().get(position).getName());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_sign_item_position)
        public TextView tvSignItemPosition = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
