package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.cart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的
 */
public class MyAdapter extends BaseAdapter {

    private String TAG = MyAdapter.class.getSimpleName();

    private Context context = null;

    private int[] image = {
            R.mipmap.my_name,
            R.mipmap.my_number,
            R.mipmap.my_state,
            R.mipmap.my_position,
            R.mipmap.my_phone,
            R.mipmap.my_month,
    };

    private String[] name = {
            "姓名",
            "工号",
            "状态",
            "职位",
            "紧急联系电话",
            "当月服务总数"
    };

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.length;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivMyItemImage.setImageResource(image[position]);
        viewHolder.tvMyItemNick.setText(name[position]);
        return convertView;
    }

    class ViewHolder {

        //图片
        @BindView(R.id.iv_my_item_image)
        public ImageView ivMyItemImage = null;

        //昵称
        @BindView(R.id.tv_my_item_nick)
        public TextView tvMyItemNick = null;

        //名称
        @BindView(R.id.tv_my_item_name)
        public TextView tvMyItemName = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
