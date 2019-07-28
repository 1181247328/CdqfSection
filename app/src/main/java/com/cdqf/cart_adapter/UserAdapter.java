package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.UserAddFind;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.DoubleOperationUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * 用户适配器
 */

public class UserAdapter extends BaseAdapter {

    private String TAG = UserAdapter.class.getSimpleName();

    private Context context = null;

    private CartState cartState = CartState.getCartState();

    private Map<Integer, Boolean> lightCheckMap = new HashMap<Integer, Boolean>();

    private Map<CompoundButton, Integer> lightConnectMap = new HashMap<CompoundButton, Integer>();

    private EventBus eventBus = EventBus.getDefault();

    private static int position = 0;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cartState.getUserGoodsList().get(this.position).getChildren().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvUserItemName.setText(cartState.getUserGoodsList().get(this.position).getChildren().get(position).getGoodsname());
        int cartType = cartState.getDatils().getCartype();
        double price = 0;
        switch (cartType) {
            case 1:
                price = Double.parseDouble(cartState.getUserGoodsList().get(this.position).getChildren().get(position).getPrice());
                break;
            case 2:
                double prices = Double.parseDouble(cartState.getUserGoodsList().get(this.position).getChildren().get(position).getPrice());
                double activePrice = Double.parseDouble(cartState.getUserGoodsList().get(this.position).getChildren().get(position).getActive_price());
                price = DoubleOperationUtil.add(prices, activePrice);
                break;
            default:
                //TODO 价格
                break;
        }
        viewHolder.tvUserItemPrice.setText("￥" + price);
        lightConnectMap.put(viewHolder.cbUserItemCheckbox, position);
//        viewHolder.cbUserItemCheckbox.setChecked(lightCheckMap.get(position) == null ? false : true);
        viewHolder.cbUserItemCheckbox.setChecked(cartState.getUserGoodsList().get(this.position).getChildren().get(position).isSelect());
        viewHolder.cbUserItemCheckbox.setOnCheckedChangeListener(new OnCartCheckedChangeListener(position));
        if (this.position == cartState.getUserGoodsList().size() - 1) {
            viewHolder.cbUserItemCheckbox.setVisibility(View.GONE);
        } else {
            viewHolder.cbUserItemCheckbox.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class OnCartCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        private int position = 0;

        public OnCartCheckedChangeListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed()) {
                return;
            }
            if (isChecked) {
                lightCheckMap.put(lightConnectMap.get(buttonView), isChecked);
                cartState.getUserGoodsList().get(UserAdapter.position).getChildren().get(position).setSelect(true);
                eventBus.post(new UserAddFind(position));
            } else {
                lightCheckMap.remove(lightConnectMap.get(buttonView));
                cartState.getUserGoodsList().get(UserAdapter.position).getChildren().get(position).setSelect(false);
                eventBus.post(new UserAddFind(position));
            }
        }
    }


    class ViewHolder {

        //选择
        @BindView(R.id.cb_user_item_checkbox)
        public CheckBox cbUserItemCheckbox = null;

        //名称
        @BindView(R.id.tv_user_item_name)
        public TextView tvUserItemName = null;

        //价格
        @BindView(R.id.tv_user_item_price)
        public TextView tvUserItemPrice = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
