package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 店总
 */
public class ShopAdapter extends BaseAdapter {

    private String TAG = ServiceAdapter.class.getSimpleName();

    private Context context = null;

    public ShopAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        //订单号
        @BindView(R.id.tv_shop_item_id)
        public TextView tvShopItemId = null;

        //车牌号
        @BindView(R.id.tv_shop_item_plate)
        public TextView tvShopItemPlate = null;

        //他洗
        @BindView(R.id.rcrl_shop_item_washed)
        public RCRelativeLayout rcrlShopItemWashed = null;

        //车型
        @BindView(R.id.tv_shop_item_type)
        public TextView tvShopItemType = null;

        //时间
        @BindView(R.id.tv_shop_item_timer)
        public TextView tvShopItemTimer = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
