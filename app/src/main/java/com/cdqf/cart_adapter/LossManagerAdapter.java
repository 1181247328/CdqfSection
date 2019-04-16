package com.cdqf.cart_adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 损耗适配器(店长);
 */
public class LossManagerAdapter extends BaseAdapter {

    private String TAG = LossManagerAdapter.class.getSimpleName();

    private Context context = null;

    public LossManagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 7;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lossmanager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 3) {
            viewHolder.tvLossItemAdd.setVisibility(View.VISIBLE);
            viewHolder.tvLossItemNumber.setText("0 (缺货)");
            viewHolder.tvLossItemNumber.setTextColor(ContextCompat.getColor(context, R.color.lossmanager_stock));
            viewHolder.tvLossItemAdd.setOnClickListener(new OnAddListener(position));
        } else {
            viewHolder.tvLossItemAdd.setVisibility(View.GONE);
            viewHolder.tvLossItemNumber.setText("100");
            viewHolder.tvLossItemNumber.setTextColor(ContextCompat.getColor(context, R.color.tab_main_text_default));
        }
        return convertView;
    }

    class ViewHolder {

        //物品
        @BindView(R.id.tv_loss_item_items)
        public TextView tvLossItemItems = null;

        //数量
        @BindView(R.id.tv_loss_item_number)
        public TextView tvLossItemNumber = null;

        //添加库存
        @BindView(R.id.tv_loss_item_add)
        public TextView tvLossItemAdd = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    class OnAddListener implements View.OnClickListener {

        private int position = 0;

        public OnAddListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "---添加库存---" + position);
        }
    }
}