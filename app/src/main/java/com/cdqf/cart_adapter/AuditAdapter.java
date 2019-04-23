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
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 审核适配器
 */
public class AuditAdapter extends BaseAdapter {
    private String TAG = AuditAdapter.class.getSimpleName();

    private Context context = null;

    private CartState cartState = CartState.getCartState();

    public AuditAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_audit, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {

        //图片
        @BindView(R.id.iv_audit_item_image)
        public ImageView ivAuditItemImage = null;

        //物品名称
        @BindView(R.id.tv_audit_item_name)
        public TextView tvAuditItemName = null;

        //数量
        @BindView(R.id.tv_audit_item_number)
        public TextView tvAuditItemNumber = null;

        //人物
        @BindView(R.id.tv_audit_item_figure)
        public TextView tvAuditItemFigure = null;

        //时间
        @BindView(R.id.tv_audit_item_timer)
        public TextView tvAuditItemTimer = null;

        //审核通过
        @BindView(R.id.rcrl_audit_item_through)
        public RCRelativeLayout rcrlAuditItemThrough = null;


        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
