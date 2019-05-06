package com.cdqf.cart_adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * 新损耗品适配器(店长)
 */
public class LossNewsLeftAdapter extends BaseAdapter {

    private String TAG = LossNewsLeftAdapter.class.getSimpleName();

    private Context context = null;

    private CartState cartState = CartState.getCartState();

    private int type = 0;

    public LossNewsLeftAdapter(Context context) {
        this.context = context;
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public int getType() {
        return type;
    }

    @Override
    public int getCount() {
        return cartState.getLossNewsList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_name, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvNameItemContext.setText(cartState.getLossNewsList().get(position).getName());
        if (type == position) {
            viewHolder.tvNameItemContext.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_icon));
            viewHolder.tvNameItemContext.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
            viewHolder.tvNameItemContext.setTextColor(ContextCompat.getColor(context, R.color.name_text));
            viewHolder.tvNameItemContext.setBackgroundColor(ContextCompat.getColor(context, R.color.service_bak));
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_name_item_context)
        public TextView tvNameItemContext = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

