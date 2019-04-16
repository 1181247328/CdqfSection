package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通知内容适配器
 */
public class NoticeAdapter extends BaseAdapter {

    private String TAG = NoticeAdapter.class.getSimpleName();

    private Context context = null;

    public NoticeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notice, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {

        //新
        @BindView(R.id.tv_notice_item_new)
        public RCRelativeLayout tvNoticeItemNew = null;

        //通知内容
        @BindView(R.id.tv_notice_item_context)
        public TextView tvNoticeItemContext = null;

        //时间
        @BindView(R.id.tv_notice_item_timer)
        public TextView tvNoticeItemTimer = null;

        //人物
        @BindView(R.id.tv_notice_item_name)
        public TextView tvNoticeItemName = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
