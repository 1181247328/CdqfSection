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
import de.greenrobot.event.EventBus;

public class AccountDatilsContextAdapter extends BaseAdapter {

    private String TAG = AccountDatilsContextAdapter.class.getSimpleName();

    private Context context = null;

    private EventBus eventBus = EventBus.getDefault();

    private String shopName = "";
    private String price = "";
    private String type = "";
    private String describe = "";

    public AccountDatilsContextAdapter(Context context) {
        this.context = context;
    }

    public void setContext(String shopName, String price, String type, String describe) {
        this.shopName = shopName;
        this.price = price;
        this.type = type;
        this.describe = describe;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 1;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_acdatils_context, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFillItemShop.setText(shopName);
        viewHolder.etFillItemPrice.setText(price);
        viewHolder.tvFillItemType.setText(type);
        viewHolder.etFillItemContext.setText(describe);
        return convertView;
    }

    class ViewHolder {

        //名称
        @BindView(R.id.tv_fill_item_shop)
        public TextView tvFillItemShop = null;

        //金额
        @BindView(R.id.tv_fill_item_price)
        public TextView etFillItemPrice = null;

        //类型
        @BindView(R.id.tv_fill_item_type)
        public TextView tvFillItemType = null;

        //描述
        @BindView(R.id.tv_fill_item_context)
        public TextView etFillItemContext = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}

