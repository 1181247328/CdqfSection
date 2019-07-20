package com.cdqf.cart_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_activity.AddOrderActivity;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_state.CartState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MembersDatilsAdapter extends BaseAdapter {

    private String TAG = NoticeAdapter.class.getSimpleName();

    private EventBus eventBus = EventBus.getDefault();

    private CartState cartState = CartState.getCartState();

    private Context context = null;

    public MembersDatilsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getMembersDatils().getService().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mebersdatils, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //支付
        String pay = "";
        switch (cartState.getMembersDatils().getService().get(position).getPay_type()) {
            case 1:
                pay = "余额";
                break;
            case 2:
                pay = "微信";
                break;
            case 3:
                pay = "现金";
                break;
            case 4:
                pay = "农商";
                break;
            default:
                pay = "现金";
                break;
        }
        viewHolder.tvDetailsWay.setText(pay);

        //金钱
        viewHolder.tvDetailsPrice.setText("￥" + cartState.getMembersDatils().getService().get(position).getZongprice());
        //车牌号
        viewHolder.tvDetailsPlate.setText(cartState.getMembersDatils().getService().get(position).getCarnum());
        //车型
        String cart = "";
        switch (cartState.getMembersDatils().getService().get(position).getPay_type()) {
            case 1:
                cart = "小轿车";
                break;
            case 2:
                cart = "SUV";
                break;
            default:
                cart = "未知";
                break;
        }
        viewHolder.tvDetailsType.setText(cart);
        //日期
        viewHolder.tvDetailsTimer.setText(cartState.getMembersDatils().getService().get(position).getAddtime());

        //加订单
        viewHolder.rcrlDetailsAddorder.setOnClickListener(new OnAddOrderListener(position));

        //服务
        viewHolder.rcrlDetailsClaim.setOnClickListener(new OnServiceListener(position));
        return convertView;
    }

    class ViewHolder {
        //平台
        @BindView(R.id.tv_details_way)
        public TextView tvDetailsWay = null;

        //价格
        @BindView(R.id.tv_details_price)
        public TextView tvDetailsPrice = null;

        //车牌
        @BindView(R.id.tv_details_plate)
        public TextView tvDetailsPlate = null;

        //车型
        @BindView(R.id.tv_details_type)
        public TextView tvDetailsType = null;

        //服务
        @BindView(R.id.rcrl_details_claim)
        public RCRelativeLayout rcrlDetailsClaim = null;

        //加订单
        @BindView(R.id.rcrl_details_addorder)
        public RCRelativeLayout rcrlDetailsAddorder = null;

        //下单时间
        @BindView(R.id.tv_details_timer)
        public TextView tvDetailsTimer = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 加订单
     */
    class OnAddOrderListener implements View.OnClickListener {

        private int position;

        public OnAddOrderListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            initIntent(AddOrderActivity.class);
        }
    }

    /**
     * 服务
     */
    class OnServiceListener implements View.OnClickListener {

        private int position;

        public OnServiceListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new ShopServiceOneFind(position));
        }
    }


    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}
