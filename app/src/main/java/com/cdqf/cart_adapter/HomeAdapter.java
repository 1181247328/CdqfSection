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
 * 首页
 */
public class HomeAdapter extends BaseAdapter {

    private String TAG = HomeAdapter.class.getSimpleName();

    private Context context = null;

    private int[] image = {
            R.mipmap.home_loss,
            R.mipmap.home_service,
            R.mipmap.home_notices,
            R.mipmap.home_clock,
            R.mipmap.home_account
    };

    private String[] name = {
            "耗材管理",
            "服务",
            "通知",
            "考勤打卡",
            "报销",
    };

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return name.length;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivHomeItemLoss.setImageResource(image[position]);
        viewHolder.tvHomeItemName.setText(name[position]);
        return convertView;
    }

    class ViewHolder {

        //图片
        @BindView(R.id.iv_home_item_loss)
        public ImageView ivHomeItemLoss = null;

        //名称
        @BindView(R.id.tv_home_item_name)
        public TextView tvHomeItemName = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
