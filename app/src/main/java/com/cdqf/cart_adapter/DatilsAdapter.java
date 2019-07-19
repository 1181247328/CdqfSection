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
 * 下单明细适配器
 */
public class DatilsAdapter extends BaseAdapter {

    private String TAG = DatilsAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public DatilsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getNumberList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_datils, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvDatilsItemNumber.setText(cartState.getNumberList().get(position).getCarnum());
        String pay = "";
        switch (cartState.getNumberList().get(position).getPay_type()) {
            case 1:
                pay = "平台";
                break;
            case 2:
                pay = "现金";
                break;
            case 3:
                pay = "农商";
                break;
        }
        viewHolder.tvDatilsItemPay.setText(pay);
        return convertView;
    }


    class ViewHolder {

        //车牌号
        @BindView(R.id.tv_datils_item_number)
        public TextView tvDatilsItemNumber = null;

        //支付方式
        @BindView(R.id.tv_datils_item_pay)
        public TextView tvDatilsItemPay = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
