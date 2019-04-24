package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        return 4;
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
//        imageLoader.displayImage(cartState.getAuditList().get(position).getImage(), viewHolder.ivAuditItemImage, cartState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
//        //物品
//        viewHolder.tvAuditItemName.setText(cartState.getAuditList().get(position).getGoods_name());
//        //数量
//        viewHolder.tvAuditItemNumber.setText("数量:" + cartState.getAuditList().get(position).getNumber());
//        //人物
//        viewHolder.tvAuditItemFigure.setText("申请人:" + cartState.getAuditList().get(position).getName());
//        //时间
//        viewHolder.tvAuditItemTimer.setText(cartState.getAuditList().get(position).getAdd_time());

        return convertView;
    }

    class ViewHolder {

        //图片
        @BindView(R.id.iv_record_item_image)
        public ImageView ivRecordItemImage = null;

        //物品名称
        @BindView(R.id.tv_record_item_name)
        public TextView tvRecordItemName = null;

        //数量
        @BindView(R.id.tv_record_item_number)
        public TextView tvRecordItemNumber = null;

        //人物
        @BindView(R.id.tv_record_item_figure)
        public TextView tvRecordItemFigure = null;

        //时间
        @BindView(R.id.tv_record_item_timer)
        public TextView tvRecordItemTimer = null;

        //状态
        @BindView(R.id.tv_record_item_state)
        public TextView tvRecordItemState = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
