package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_state.CartState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 记录适配器
 */
public class RecordAdapter extends BaseAdapter {

    private String TAG = AuditAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    public RecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getRecordList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //状态
        String state = "";
        switch (cartState.getRecordList().get(position).getStatus()) {
            case 1:
                state = "入库";
                break;
            case 2:
                state = "出库";
                break;
            default:
                state = "未知";
                //TODO 入库和出库
                break;
        }
        viewHolder.tvRecordItemState.setText(state);
        //物品名称
        viewHolder.tvRecordItemName.setText(cartState.getRecordList().get(position).getConsumables().getName());
        //数量
        viewHolder.tvRecordItemNumber.setText(cartState.getRecordList().get(position).getNum()+"");
        //时间
        viewHolder.tvRecordItemTimer.setText(cartState.getRecordList().get(position).getCreated_at());
        return convertView;
    }

    class ViewHolder {

        //状态
        @BindView(R.id.tv_record_item_state)
        public TextView tvRecordItemState = null;

        //物品名称
        @BindView(R.id.tv_record_item_name)
        public TextView tvRecordItemName = null;

        //数量
        @BindView(R.id.tv_record_item_number)
        public TextView tvRecordItemNumber = null;

        //时间
        @BindView(R.id.tv_record_item_timer)
        public TextView tvRecordItemTimer = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
