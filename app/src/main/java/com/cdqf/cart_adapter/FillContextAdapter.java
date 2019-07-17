package com.cdqf.cart_adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_find.FillContextCencelFind;
import com.cdqf.cart_find.FillContextFind;
import com.cdqf.cart_find.FillPriceFind;
import com.cdqf.cart_find.ShopFillFind;
import com.cdqf.cart_find.TypeFillTypeFind;
import com.cdqf.cart_state.CashierInputFilter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 报销明细适配器
 */
public class FillContextAdapter extends BaseAdapter {

    private String TAG = FillContextAdapter.class.getSimpleName();

    private Context context = null;

    private EventBus eventBus = EventBus.getDefault();

    private int number = 1;

    private String shopName = "";

    private String type = "";

    public FillContextAdapter(Context context) {
        this.context = context;
    }

    public void setNumber(int n) {
        number += n;
        notifyDataSetChanged();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
        notifyDataSetChanged();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return number;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fill_context, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvFillItemShop.setText(shopName);
        viewHolder.tvFillItemType.setText(type);

        viewHolder.etFillItemPrice.setTag(position);
        viewHolder.etFillItemContext.setTag(position);

        InputFilter[] filters = {new CashierInputFilter()};
        viewHolder.etFillItemPrice.setFilters(filters);
        viewHolder.etFillItemPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        viewHolder.tvFillItemId.setText("报销明细(" + (position + 1) + ")");
        viewHolder.tvFillItemCencel.setOnClickListener(new OnCencelListener(position));
        viewHolder.llFillItemShop.setOnClickListener(new OnShopListener(position));
        viewHolder.llFillItemType.setOnClickListener(new OnTypeListener(position));
        viewHolder.etFillItemPrice.addTextChangedListener(new OnPriteListener(position, viewHolder));
        viewHolder.etFillItemContext.addTextChangedListener(new OnContextListener(position, viewHolder));
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.tv_fill_item_id)
        public TextView tvFillItemId = null;

        //取消
        @BindView(R.id.tv_fill_item_cencel)
        public TextView tvFillItemCencel = null;

        //所属门店
        @BindView(R.id.ll_fill_item_shop)
        public LinearLayout llFillItemShop = null;

        //名称
        @BindView(R.id.tv_fill_item_shop)
        public TextView tvFillItemShop = null;

        //金额
        @BindView(R.id.et_fill_item_price)
        public EditText etFillItemPrice = null;

        //所属类别
        @BindView(R.id.ll_fill_item_type)
        public LinearLayout llFillItemType = null;

        //类型
        @BindView(R.id.tv_fill_item_type)
        public TextView tvFillItemType = null;

        //名称
        @BindView(R.id.et_fill_item_context)
        public EditText etFillItemContext = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 取消
     */
    class OnCencelListener implements View.OnClickListener {

        private int position = 0;

        public OnCencelListener(int position) {

            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new FillContextCencelFind(position));
        }
    }

    /**
     * 所属门店
     */
    class OnShopListener implements View.OnClickListener {
        private int position;

        public OnShopListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new ShopFillFind(position));
        }
    }

    /**
     * 选择报销类别
     */
    class OnTypeListener implements View.OnClickListener {

        private int position;

        public OnTypeListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new TypeFillTypeFind(position));
        }
    }

    //报销费用
    class OnPriteListener implements TextWatcher {

        private ViewHolder viewHolder;
        private int position;

        public OnPriteListener(int position, ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int position = (int) viewHolder.etFillItemPrice.getTag();
            if (position == this.position) {
                String price = viewHolder.etFillItemPrice.getText().toString();
                eventBus.post(new FillPriceFind(price));
            }
        }
    }

    //费用描述
    class OnContextListener implements TextWatcher {

        private ViewHolder viewHolder;
        private int position;

        public OnContextListener(int position, ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int position = (int) viewHolder.etFillItemContext.getTag();
            if (position == this.position) {
                String context = viewHolder.etFillItemContext.getText().toString();
                eventBus.post(new FillContextFind(context));
            }
        }
    }
}
