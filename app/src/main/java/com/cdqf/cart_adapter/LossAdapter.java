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
 * 损耗品适配器
 */
public class LossAdapter extends BaseAdapter {

    private String TAG = LossAdapter.class.getSimpleName();

    private Context context = null;

    public LossAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_loss, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvLossItemState.setOnClickListener(new OnStateListener(position));
        return convertView;
    }

    class ViewHolder {

        //物品
        @BindView(R.id.tv_loss_item_items)
        public TextView tvLossItemItems = null;

        //数量
        @BindView(R.id.tv_loss_item_number)
        public TextView tvLossItemNumber = null;

        //状态
        @BindView(R.id.tv_loss_item_state)
        public TextView tvLossItemState = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    class OnStateListener implements View.OnClickListener {

        private int postion = 0;

        public OnStateListener(int postion) {
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
