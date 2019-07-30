package com.cdqf.cart_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.ServcieKeyFind;
import com.cdqf.cart_find.ServiceYesOneFind;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_state.CartState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 店总
 */
public class ShopAdapter extends BaseAdapter {

    private String TAG = ServiceAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public ShopAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getCount() {
        return cartState.getServiceLis().size() + 1;
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
        int type = getItemViewType(position);
        if (type == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_serach, null);
            TextView tvShopItemCancel = convertView.findViewById(R.id.tv_shop_item_cancel);
            EditText etShopItemInput = convertView.findViewById(R.id.et_shop_item_input);
            tvShopItemCancel.setOnClickListener(new OnDetermineListener(etShopItemInput));
        } else if (type == 1) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //支付
            String pay = "";
            switch (cartState.getServiceLis().get(position - 1).getPay_type()) {
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
            viewHolder.tvShopItemWay.setText(pay);
            //项目名称
            viewHolder.tvShopItemName.setText(cartState.getServiceLis().get(position - 1).getGoods_names());
            //价格
            viewHolder.tvShopItemPrice.setText("￥" + cartState.getServiceLis().get(position - 1).getAmount());
            //车牌号
            viewHolder.tvShopItemPlate.setText(cartState.getServiceLis().get(position - 1).getCarnum());
            //车型
            viewHolder.tvShopItemType.setText(cartState.getServiceLis().get(position - 1).getCar_type_name());
            //服务方式
            String claim = "";
            switch (cartState.getServiceLis().get(position - 1).getType()) {
                case 1:
                    claim = "待付款";
                    viewHolder.rcrlShopItemYes.setVisibility(View.GONE);
                    viewHolder.tvShopItemClaim.setText(claim);
                    break;
                case 2:
                    claim = "服务";
                    viewHolder.rcrlShopItemYes.setVisibility(View.VISIBLE);
                    viewHolder.tvShopItemClaim.setText(claim + "(" + cartState.getServiceLis().get(position - 1).getStaff() + ")");
                    break;
                case 3:
                    claim = "已服务";
                    viewHolder.rcrlShopItemYes.setVisibility(View.GONE);
                    viewHolder.tvShopItemClaim.setText(claim);
                    break;
                default:
                    //TODO
                    viewHolder.rcrlShopItemYes.setVisibility(View.GONE);
                    viewHolder.tvShopItemClaim.setText("未知");
                    break;
            }

            //权限显示

            viewHolder.rcrlShopItemClaim.setOnClickListener(new OnServiceListener(position));
            //时间
            viewHolder.tvShopItemTimer.setText(cartState.getServiceLis().get(position - 1).getAddtime());
            //完成
            viewHolder.rcrlShopItemYes.setOnClickListener(new OnYesListener(position));
        }
        return convertView;
    }

    class ViewHolder {

        //平台
        @BindView(R.id.tv_shop_item_way)
        public TextView tvShopItemWay = null;

        //项目名称
        @BindView(R.id.tv_shop_item_name)
        public TextView tvShopItemName = null;

        //价格
        @BindView(R.id.tv_shop_item_price)
        public TextView tvShopItemPrice = null;

        //车牌号
        @BindView(R.id.tv_shop_item_plate)
        public TextView tvShopItemPlate = null;

        //车型
        @BindView(R.id.tv_shop_item_type)
        public TextView tvShopItemType = null;

        //服务
        @BindView(R.id.rcrl_shop_item_claim)
        public RCRelativeLayout rcrlShopItemClaim = null;
        @BindView(R.id.tv_shop_item_claim)
        public TextView tvShopItemClaim = null;

        //完成
        @BindView(R.id.rcrl_shop_item_yes)
        public RCRelativeLayout rcrlShopItemYes = null;

        //时间
        @BindView(R.id.tv_shop_item_timer)
        public TextView tvShopItemTimer = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 确定
     */
    class OnDetermineListener implements View.OnClickListener {

        private EditText etShopItemInput = null;

        public OnDetermineListener(EditText etShopItemInput) {
            this.etShopItemInput = etShopItemInput;
        }

        @Override
        public void onClick(View v) {
            String item = etShopItemInput.getText().toString();
            Log.e(TAG, "---要搜索的关键字---" + item);
            if (item.length() <= 0) {
                cartState.initToast(context, "车牌或手机号不能为空", true, 0);
                return;
            }
            //判断是不是手机
            if (cartState.isMobile(item)) {
                eventBus.post(new ServcieKeyFind(item, false));
            } else if (cartState.licensePlate(item)) {
                eventBus.post(new ServcieKeyFind(item, false));
            } else {
                cartState.initToast(context, "请输入正确的车牌和手机", true, 0);
            }
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
            if (cartState.getServiceLis().get(position - 1).getType() == 2) {
                eventBus.post(new ShopServiceOneFind(position));
            }
        }
    }

    /**
     * 完成
     */
    class OnYesListener implements View.OnClickListener {

        private int position;

        public OnYesListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (cartState.getServiceLis().get(position - 1).getStaff() <= 0) {
                cartState.initToast(context, "服务人数必须大于一人", true, 0);
                return;
            }
            eventBus.post(new ServiceYesOneFind(position));
        }
    }
}
