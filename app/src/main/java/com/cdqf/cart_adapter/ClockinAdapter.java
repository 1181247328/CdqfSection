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
 * 打卡记录适配器
 */
public class ClockinAdapter extends BaseAdapter {

    private String TAG = ClockinAdapter.class.getSimpleName();

    private Context context = null;

    private CartState cartState = CartState.getCartState();

    public ClockinAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getClockinList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clockin, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //入住时间
        String[] ckeckInTime = null;
        try {
            ckeckInTime = cartState.getClockinList().get(position).getAttendance_date().split(" ");
        } catch (NullPointerException e) {
        }
        viewHolder.tvClockinItemTimer.setText(ckeckInTime[0]);
        String state = "";
        switch (cartState.getClockinList().get(position).getStatus()) {
            case 1:
                state = "上班";
                break;
            default:
                state = "休假";
                break;
        }
        viewHolder.tvClockinItemState.setText(state);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_clockin_item_timer)
        public TextView tvClockinItemTimer = null;

        @BindView(R.id.tv_clockin_item_state)
        public TextView tvClockinItemState = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
