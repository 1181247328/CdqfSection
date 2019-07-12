package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cdqf.cart.R;
import com.cdqf.cart_find.RefusedFind;
import com.cdqf.cart_find.ThroughFind;
import com.cdqf.cart_state.CartState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 审核适配器
 */
public class AuditAdapter extends BaseAdapter {
    private String TAG = AuditAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    public AuditAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getAuditList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_audit, null);
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
//        viewHolder.rcrlAuditItemRefused.setOnClickListener(new OnRefusedListener(position));
//        //通过
//        viewHolder.rcrlAuditItemThrough.setOnClickListener(new OnThroughListener(position));
        return convertView;
    }

    class ViewHolder {

//        //图片
//        @BindView(R.id.iv_audit_item_image)
//        public ImageView ivAuditItemImage = null;
//
//        //物品名称
//        @BindView(R.id.tv_audit_item_name)
//        public TextView tvAuditItemName = null;
//
//        //数量
//        @BindView(R.id.tv_audit_item_number)
//        public TextView tvAuditItemNumber = null;
//
//        //人物
//        @BindView(R.id.tv_audit_item_figure)
//        public TextView tvAuditItemFigure = null;
//
//        //时间
//        @BindView(R.id.tv_audit_item_timer)
//        public TextView tvAuditItemTimer = null;
//
//        //拒绝
//        @BindView(R.id.rcrl_audit_item_refused)
//        public RCRelativeLayout rcrlAuditItemRefused = null;
//        //通过
//        @BindView(R.id.rcrl_audit_item_through)
//        public RCRelativeLayout rcrlAuditItemThrough = null;


        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 拒绝
     */
    class OnRefusedListener implements View.OnClickListener {

        private int position;

        public OnRefusedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new RefusedFind(position));
        }
    }

    /**
     * 同意
     */
    class OnThroughListener implements View.OnClickListener {

        private int position;

        public OnThroughListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new ThroughFind(position));
        }
    }
}
