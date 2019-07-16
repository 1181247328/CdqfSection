package com.cdqf.cart_dilogadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_state.CartState;
import com.cdqf.cart_state.DoubleOperationUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 服务选项适配器
 */
public class ServiceDilogAdapter extends BaseAdapter {
    private String TAG = ServiceDilogAdapter.class.getSimpleName();

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    public ServiceDilogAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getServiceOrderList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dilog_service, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvserviceItemName.setText(cartState.getServiceOrderList().get(position).getGoods_name());
        if (cartState.getModel() == 1) {
            //小轿车
            viewHolder.tvServiceItemprice.setText("￥" + cartState.getServiceOrderList().get(position).getPrice());
        } else {
            //SUV
            double price = Double.parseDouble(cartState.getServiceOrderList().get(position).getPrice());
            double addPrice = Double.parseDouble(cartState.getServiceOrderList().get(position).getAddprice());
            double sum = DoubleOperationUtil.add(price, addPrice);
            viewHolder.tvServiceItemprice.setText("￥" + sum);
        }
        return convertView;
    }

    class ViewHolder {


        //名称
        @BindView(R.id.tv_service_item_name)
        public TextView tvserviceItemName = null;

        //价格
        @BindView(R.id.tv_service_item_price)
        public TextView tvServiceItemprice = null;


        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

}