package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 营业状态
 */
public class BusinessAdapter extends BaseAdapter {

    private String TAG = BusinessAdapter.class.getSimpleName();

    private Context context = null;

    private List<String> testList = new CopyOnWriteArrayList<>();

    public BusinessAdapter(Context context) {
        this.context = context;
    }

    public void setTestList(List<String> testList) {
        this.testList = testList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return testList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_business, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvBusinessItemContext.setText(testList.get(position));
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_business_item_context)
        public TextView tvBusinessItemContext = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
