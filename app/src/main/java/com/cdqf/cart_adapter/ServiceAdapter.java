package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.ServiceOneFind;
import com.cdqf.cart_state.CartState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 服务
 */
public class ServiceAdapter extends BaseAdapter {

    private String TAG = ServiceAdapter.class.getSimpleName();

    private Context context = null;

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    public ServiceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getShopList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_service, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //订单号
        viewHolder.tvServiceItemId.setText("订单:" + cartState.getShopList().get(position).getOrdernum());
        //车牌号
        viewHolder.tvServiceItemPlate.setText(cartState.getShopList().get(position).getCarnum());
        //车型
        viewHolder.tvServiceItemType.setText(cartState.getShopList().get(position).getType());
        //时间
        viewHolder.tvServiceItemTimer.setText("时间:" + cartState.getShopList().get(position).getAddtime());
        //服务
        viewHolder.rcrlServiceItemWashed.setOnClickListener(new OnServiceListener(position));
        return convertView;
    }

    class ViewHolder {
        //订单号
        @BindView(R.id.tv_service_item_id)
        public TextView tvServiceItemId = null;

        //车牌号
        @BindView(R.id.tv_service_item_plate)
        public TextView tvServiceItemPlate = null;

        //我洗
        @BindView(R.id.rcrl_service_item_wash)
        public RCRelativeLayout rcrlServiceItemWash = null;

        //他洗
        @BindView(R.id.rcrl_service_item_washed)
        public RCRelativeLayout rcrlServiceItemWashed = null;

        //车型
        @BindView(R.id.tv_service_item_type)
        public TextView tvServiceItemType = null;

        //时间
        @BindView(R.id.tv_service_item_timer)
        public TextView tvServiceItemTimer = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 服务
     */
    class OnServiceListener implements View.OnClickListener {

        private int position;

        public OnServiceListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new ServiceOneFind(position));
        }
    }
}

