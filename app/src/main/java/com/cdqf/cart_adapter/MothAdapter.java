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

public class MothAdapter extends BaseAdapter {

    private String TAG = MothAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public MothAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getMothList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_daily, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //日期
        viewHolder.tvDailyItemTimer.setText(cartState.getMothList().get(position).getStart_time());
        //下单次数
        viewHolder.tvDailyItemPlace.setText(cartState.getMothList().get(position).getService_number() + "");
        //服务次数
        viewHolder.tvDailyItemService.setText(cartState.getMothList().get(position).getStaff_service_number()+"");
        //实收/成本
        viewHolder.tvDailyItemProportion.setText(cartState.getMothList().get(position).getTurnover() + "/" + cartState.getMothList().get(position).getCost_price());
        //比率
        viewHolder.tvDailyItemRate.setText(cartState.getDailyList().get(position).getTrend() == 1 ?
                "+" + cartState.getMothList().get(position).getCompare_val() + "%" :
                "-" + cartState.getMothList().get(position).getCompare_val() + "%");
        //服务金额
        viewHolder.tvDailyItemPrice.setText(cartState.getMothList().get(position).getTurnover());
        //提成
        viewHolder.tvDailyItemCommission.setText(cartState.getMothList().get(position).getGet_money());
        return convertView;
    }


    class ViewHolder {

        //时间
        @BindView(R.id.tv_daily_item_timer)
        public TextView tvDailyItemTimer = null;

        //下单次数
        @BindView(R.id.tv_daily_item_place)
        public TextView tvDailyItemPlace = null;

        //服务次数
        @BindView(R.id.tv_daily_item_service)
        public TextView tvDailyItemService = null;

        //比例
        @BindView(R.id.tv_daily_item_proportion)
        public TextView tvDailyItemProportion = null;

        //比例率
        @BindView(R.id.tv_daily_item_rate)
        public TextView tvDailyItemRate = null;

        //服务金额
        @BindView(R.id.tv_daily_item_price)
        public TextView tvDailyItemPrice = null;

        //提成
        @BindView(R.id.tv_daily_item_commission)
        public TextView tvDailyItemCommission = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
