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
 * 会员备注
 */
public class DatilsRemarksAdapter extends BaseAdapter {

    private String TAG = DatilsRemarksAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public DatilsRemarksAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getDatils().getUser_remarks().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_daremarks, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvDatilsItemNote.setText(cartState.getDatils().getUser_remarks().get(position).getContent());
        return convertView;
    }

    class ViewHolder {

        //车牌号
        @BindView(R.id.tv_datils_item_note)
        public TextView tvDatilsItemNote = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
