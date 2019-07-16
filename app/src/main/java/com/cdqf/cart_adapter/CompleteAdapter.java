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
import com.cdqf.cart_find.CompleteKeyFind;
import com.cdqf.cart_find.ShopServiceOneFind;
import com.cdqf.cart_state.CartState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 已完成
 */
public class CompleteAdapter extends BaseAdapter {

    private String TAG = CompleteAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    public CompleteAdapter(Context context) {
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
        Log.e(TAG, "----数量---" + cartState.getCompleteList().size());
        return cartState.getCompleteList().size() + 1;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_complete, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //支付
            String pay = "";
            switch (cartState.getCompleteList().get(position - 1).getPay_type()) {
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
            viewHolder.tvShopItemName.setText(cartState.getCompleteList().get(position - 1).getGoods_names());
            //价格
            viewHolder.tvShopItemPrice.setText("￥" + cartState.getCompleteList().get(position - 1).getAmount());
            //车牌号
            viewHolder.tvShopItemPlate.setText(cartState.getCompleteList().get(position - 1).getCarnum());
            //车型
            viewHolder.tvShopItemType.setText(cartState.getCompleteList().get(position - 1).getCar_type_name());
            //服务方式
            String claim = "";
            switch (cartState.getCompleteList().get(position - 1).getType()) {
                case 1:
                    claim = "待付款";
                    break;
                case 2:
                    claim = "服务";
                    break;
                case 3:
                    claim = "已服务";
                    break;
                default:
                    //TODO
                    break;
            }
            viewHolder.tvShopItemClaim.setText(claim);
            //时间
            viewHolder.tvShopItemTimer.setText(cartState.getCompleteList().get(position - 1).getAddtime());
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

        //认领
        @BindView(R.id.rcrl_shop_item_claim)
        public RCRelativeLayout rcrlShopItemClaim = null;
        @BindView(R.id.tv_shop_item_claim)
        public TextView tvShopItemClaim = null;

        //车型
        @BindView(R.id.tv_shop_item_type)
        public TextView tvShopItemType = null;

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
                eventBus.post(new CompleteKeyFind(item, false));
            } else if (cartState.licensePlate(item)) {
                eventBus.post(new CompleteKeyFind(item, false));
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
            eventBus.post(new ShopServiceOneFind(position));
        }
    }
}
