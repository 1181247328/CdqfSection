package com.cdqf.cart_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.LossNewsAddFind;
import com.cdqf.cart_state.CartState;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 新损耗品(店长)
 */
public class LossNewsRightAdapter extends BaseAdapter {

    private String TAG = LossNewsRightAdapter.class.getSimpleName();

    private Context context = null;

    private CartState cartState = CartState.getCartState();

    private Map<Integer, Boolean> lightCheckMap = new HashMap<Integer, Boolean>();

    private Map<CompoundButton, Integer> lightConnectMap = new HashMap<CompoundButton, Integer>();

    private EventBus eventBus = EventBus.getDefault();

    private int type = 0;

    public LossNewsRightAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void setPosition(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cartState.getLossNewsList().get(type).getData().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_userright, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //商品名称
        viewHolder.tvUsersItemName.setText(cartState.getLossNewsList().get(type).getData().get(position).getName());
        //库存
        viewHolder.tvUsersItemNumber.setText("库存:" + cartState.getLossNewsList().get(type).getData().get(position).getNumber());
        //申请的数量有就显示，没有就隐藏
        boolean isSelete = cartState.getLossNewsList().get(type).getData().get(position).isSelete();
        int umberSelete = cartState.getLossNewsList().get(type).getData().get(position).getNumberSelete();
        if (isSelete) {
            viewHolder.tvUserItemAdd.setVisibility(View.VISIBLE);
            viewHolder.tvUserItemPrice.setVisibility(View.VISIBLE);
            viewHolder.llUserItemPrice.setVisibility(View.VISIBLE);
            viewHolder.tvUserItemPrice.setText(umberSelete + "");
        } else {
            viewHolder.tvUserItemAdd.setVisibility(View.GONE);
            viewHolder.tvUserItemPrice.setVisibility(View.GONE);
            viewHolder.llUserItemPrice.setVisibility(View.GONE);
            viewHolder.tvUserItemPrice.setText("0");
            if (umberSelete > 0) {
                viewHolder.tvUserItemAdd.setVisibility(View.VISIBLE);
                viewHolder.tvUserItemPrice.setVisibility(View.VISIBLE);
                viewHolder.llUserItemPrice.setVisibility(View.VISIBLE);
                viewHolder.tvUserItemPrice.setText(umberSelete + "");
            } else {
                viewHolder.tvUserItemAdd.setVisibility(View.GONE);
                viewHolder.tvUserItemPrice.setVisibility(View.GONE);
                viewHolder.llUserItemPrice.setVisibility(View.GONE);
            }
        }
        viewHolder.llUserItemPrice.setOnClickListener(new OnAddNumberListener(position));
        //是否选择
        lightConnectMap.put(viewHolder.cbUsersItemCheckbox, position);
        viewHolder.cbUsersItemCheckbox.setChecked(cartState.getLossNewsList().get(type).getData().get(position).isSelete());
        viewHolder.cbUsersItemCheckbox.setOnCheckedChangeListener(new OnCartCheckedChangeListener(position));
        return convertView;
    }

    class OnAddNumberListener implements View.OnClickListener {

        private int position;

        public OnAddNumberListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "---选择了申请数量---" + position);
            eventBus.post(new LossNewsAddFind(position));
        }
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
                cartState.getLossNewsList().get(type).getData().get(position).setSelete(true);
            } else {
                lightCheckMap.remove(lightConnectMap.get(buttonView));
                cartState.getLossNewsList().get(type).getData().get(position).setSelete(false);
                cartState.getLossNewsList().get(type).getData().get(position).setNumberSelete(0);
            }
            notifyDataSetChanged();
        }
    }

    class ViewHolder {

        //选择
        @BindView(R.id.cb_users_item_checkbox)
        public CheckBox cbUsersItemCheckbox = null;

        //名称
        @BindView(R.id.tv_users_item_name)
        public TextView tvUsersItemName = null;

        //库存
        @BindView(R.id.tv_users_item_number)
        public TextView tvUsersItemNumber = null;

        //显示的加强
        @BindView(R.id.tv_user_item_add)
        public TextView tvUserItemAdd = null;

        //价格
        @BindView(R.id.tv_user_item_price)
        public TextView tvUserItemPrice = null;

        @BindView(R.id.ll_user_item_price)
        public LinearLayout llUserItemPrice = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
