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
 * 上班中适配器
 */
public class WorkAdapter extends BaseAdapter {

    private String TAG = WorkAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public WorkAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_allemployees, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {

        //车牌号
        @BindView(R.id.tv_allemployees_item_shopname)
        public TextView tvAllemployeesItemShopname = null;

        //姓名
        @BindView(R.id.tv_allemployees_item_name)
        public TextView tvAllemployeesItemName = null;

        //职位
        @BindView(R.id.tv_allemployees_item_position)
        public TextView tvAllemployeesItemPosition = null;

        //工号
        @BindView(R.id.tv_allemployees_item_id)
        public TextView tvAllemployeesItemId = null;

        //手机号
        @BindView(R.id.tv_allemployees_item_phone)
        public TextView tvAllemployeesItemPhone = null;

        //紧急联系人
        @BindView(R.id.tv_allemployees_item_urgentname)
        public TextView tvAllemployeesItemUrgentname = null;

        //状态
        @BindView(R.id.tv_allemployees_item_state)
        public TextView tvAllemployeesItemState = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

}
