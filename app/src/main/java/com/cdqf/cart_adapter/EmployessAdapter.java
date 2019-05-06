package com.cdqf.cart_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.List;

/**
 * 员工管理适配器
 */
public class EmployessAdapter extends PanelAdapter {

    private List<List<String>> data = null;

    private Context context = null;

    public EmployessAdapter(List<List<String>> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return data.get(0).size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        String title = data.get(row).get(column);
        ViewHolder titleViewHolder = (ViewHolder) holder;
        titleViewHolder.tvEmployessItemContext.setText(title);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employees, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEmployessItemContext = null;

        public ViewHolder(View v) {
            super(v);
            this.tvEmployessItemContext = (TextView) v.findViewById(R.id.tv_employess_item_context);
        }
    }
}
