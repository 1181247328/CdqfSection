package com.cdqf.cart_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.LossManagerOneFind;
import com.cdqf.cart_state.CartState;
import com.gcssloop.widget.RCRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 损耗适配器(店长);
 */
public class LossManagerAdapter extends BaseAdapter {

    private String TAG = LossManagerAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    public LossManagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 7;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lossmanager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //输入数量
        viewHolder.rcrlLossmanagerItemInput.setOnClickListener(new OnInputListener(position));
        //点击领取
        viewHolder.rcrlLossmanagerItemLoss.setOnClickListener(new OnLossListener(position));
        return convertView;
    }

    class ViewHolder {

        //物品图片
        @BindView(R.id.iv_lossmanager_item_image)
        public ImageView ivLossmanagerItemImage = null;

        //商品名称
        @BindView(R.id.tv_lossmanager_item_name)
        public TextView tvLossmanagerItemName = null;

        //总量
        @BindView(R.id.tv_lossmanager_item_number)
        public TextView tvLossmanagerItemNumber = null;

        //输入数量
        @BindView(R.id.rcrl_lossmanager_item_input)
        public RCRelativeLayout rcrlLossmanagerItemInput = null;

        //领取
        @BindView(R.id.rcrl_lossmanager_item_loss)
        public RCRelativeLayout rcrlLossmanagerItemLoss = null;

        //状态
        public TextView tvLossmanagerItemAudit = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    class OnInputListener implements View.OnClickListener {

        private int position = 0;

        public OnInputListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "---输入数量---" + position);
            eventBus.post(new LossManagerOneFind(position));

        }
    }

    /**
     * 点击领取
     */
    class OnLossListener implements View.OnClickListener {

        private int position = 0;

        public OnLossListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "---添加库存---" + position);
            eventBus.post(new LossManagerOneFind(position));
        }
    }
}