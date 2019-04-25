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
import com.cdqf.cart_find.ShopOneFind;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_state.CartState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 店总
 */
public class ShopAdapter extends BaseAdapter {

    private String TAG = ServiceAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public ShopAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //订单号
        viewHolder.tvShopItemId.setText("订单:" + cartState.getShopList().get(position).getOrdernum());
        //车牌号
        viewHolder.tvShopItemPlate.setText(cartState.getShopList().get(position).getCarnum());
        //车型
        viewHolder.tvShopItemType.setText(cartState.getShopList().get(position).getType());
        //时间
        viewHolder.tvShopItemTimer.setText("时间:" + cartState.getShopList().get(position).getAddtime());
        //服务
        viewHolder.rcrlShopItemClaim.setOnClickListener(new OnServiceListener(position));
        //完成
        viewHolder.rcrlShopItemWashed.setOnClickListener(new OnWashedListener(position));
        String shop = "("+cartState.getShopList().get(position).getService() + "人)";
        viewHolder.tvShopItemNumber.setText(shop);
        //完成状态
        int service = Integer.parseInt(cartState.getShopList().get(position).getService());
        Log.e(TAG, "---当前服务人数---" + service);
        if (service > 0) {
            viewHolder.rcrlShopItemWashed.setBackgroundColor(ContextCompat.getColor(context, R.color.loss_receive));
        } else {
            viewHolder.rcrlShopItemWashed.setBackgroundColor(ContextCompat.getColor(context, R.color.lossmanager_stock));
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

        //认领
        @BindView(R.id.rcrl_shop_item_claim)
        public RCRelativeLayout rcrlShopItemClaim = null;

        //点击完成
        @BindView(R.id.rcrl_shop_item_washed)
        public RCRelativeLayout rcrlShopItemWashed = null;

        //车型
        @BindView(R.id.tv_shop_item_type)
        public TextView tvShopItemType = null;

        //时间
        @BindView(R.id.tv_shop_item_timer)
        public TextView tvShopItemTimer = null;

        //服务人员
        @BindView(R.id.tv_shop_item_number)
        public TextView tvShopItemNumber = null;

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
            eventBus.post(new ShopServiceOneFind(position));
        }
    }

    /**
     * 完成
     */
    class OnWashedListener implements View.OnClickListener {

        private int position;

        public OnWashedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int service = Integer.parseInt(cartState.getShopList().get(position).getService());
            if (service > 0) {
                eventBus.post(new ShopOneFind(position));
            } else {
                cartState.initToast(context, "此车暂无服务人员", true, 0);
            }
        }
    }
}
