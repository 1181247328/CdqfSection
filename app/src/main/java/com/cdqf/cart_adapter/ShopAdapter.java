package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cdqf.cart.R;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getCount() {
        return 20;
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
        int type = getItemViewType(position);
        if (type == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_serach, null);
            TextView tvShopItemCancel = convertView.findViewById(R.id.tv_shop_item_cancel);
            EditText etShopItemInput = convertView.findViewById(R.id.et_shop_item_input);
        } else if (type == 1) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
//        //订单号
//        viewHolder.tvShopItemId.setText("订单:" + cartState.getShopList().get(position).getOrdernum());
//        //车牌号
//        if (TextUtils.equals(cartState.getShopList().get(position).getCarnum(), "")) {
//            viewHolder.tvShopItemPlate.setText("此订单不可无须服务");
//            viewHolder.rcrlShopItemClaim.setVisibility(View.GONE);
//            //完成
//            viewHolder.rcrlShopItemWashed.setVisibility(View.GONE);
//        } else {
//            viewHolder.tvShopItemPlate.setText(cartState.getShopList().get(position).getCarnum());
//            viewHolder.rcrlShopItemClaim.setVisibility(View.VISIBLE);
//            //完成
//            viewHolder.rcrlShopItemWashed.setVisibility(View.VISIBLE);
//        }
//        //车型
//        viewHolder.tvShopItemType.setText(cartState.getShopList().get(position).getType());
//        //时间
//        viewHolder.tvShopItemTimer.setText("时间:" + cartState.getShopList().get(position).getAddtime());
//        //服务
//        viewHolder.rcrlShopItemClaim.setOnClickListener(new OnServiceListener(position));
//        //完成
//        viewHolder.rcrlShopItemWashed.setOnClickListener(new OnWashedListener(position));
//        String shop = "(" + cartState.getShopList().get(position).getService() + "人)";
//        viewHolder.tvShopItemNumber.setText(shop);
//        //完成状态
//        int service = 0;
//        try {
//            service = Integer.parseInt(cartState.getShopList().get(position).getService().trim());
//        } catch (Exception e) {
//            e.printStackTrace();
//            service = 0;
//        }
//        Log.e(TAG, "---当前服务人数---" + service);
//        if (service > 0) {
//            viewHolder.rcrlShopItemWashed.setBackgroundColor(ContextCompat.getColor(context, R.color.loss_receive));
//        } else {
//            viewHolder.rcrlShopItemWashed.setBackgroundColor(ContextCompat.getColor(context, R.color.lossmanager_stock));
//        }
        return convertView;
    }

    class ViewHolder {

        //车牌号
        @BindView(R.id.tv_shop_item_plate)
        public TextView tvShopItemPlate = null;

        //认领
        @BindView(R.id.rcrl_shop_item_claim)
        public RCRelativeLayout rcrlShopItemClaim = null;

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
}
