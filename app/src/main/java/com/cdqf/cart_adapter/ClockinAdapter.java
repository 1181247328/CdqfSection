package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 打卡记录适配器
 */
public class ClockinAdapter extends BaseAdapter {

    private String TAG = ClockinAdapter.class.getSimpleName();

    private Context context = null;

    public ClockinAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
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
