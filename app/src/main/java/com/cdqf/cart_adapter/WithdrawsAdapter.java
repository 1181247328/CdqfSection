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
 * 已撤回
 */
public class WithdrawsAdapter extends BaseAdapter {
    private String TAG = WithdrawsAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    public WithdrawsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_aduits, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        //何人提交
        @BindView(R.id.tv_audits_item_name)
        public TextView tvAuditsItemName = null;

        //时间
        @BindView(R.id.tv_audits_item_timer)
        public TextView tvAuditsItemTimer = null;

        //钱
        @BindView(R.id.tv_audits_item_price)
        public TextView tvAuditsItemPrice = null;

        //耗材
        @BindView(R.id.tv_audits_item_material)
        public TextView tvAuditsItemMaterial = null;

        //明细
        @BindView(R.id.tv_audits_item_context)
        public TextView tvAuditsItemContext = null;

        //状态
        @BindView(R.id.tv_audits_item_state)
        public TextView tvAuditsItemState = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

}
