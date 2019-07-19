package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.cart.R;
import com.cdqf.cart_state.CartState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeManagerAdapter extends BaseAdapter {

    private String TAG = HomeManagerAdapter.class.getSimpleName();

    private Context context = null;

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private int[] image = {
            R.mipmap.home_loss,
            R.mipmap.home_service,
            R.mipmap.home_notices,
            R.mipmap.home_user,
            R.mipmap.home_members,
            R.mipmap.home_task,
            R.mipmap.home_shop,
            R.mipmap.home_clock,
            R.mipmap.home_account
    };

    private String[] name = {
            "耗材管理",
            "服务",
            "通知",
            "审核",
            "会员",
            "任务",
            "店员管理",
            "考勤打卡",
            "报销"
    };

    public HomeManagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartState.getUser().getMenu().size();
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
        imageLoader.displayImage(cartState.getUser().getMenu().get(position).getIcon(), viewHolder.ivHomeItemLoss, cartState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
//        viewHolder.ivHomeItemLoss.setImageResource(image[position]);
//        if (position == 1) {
//            String shop = "店总" + "(10)";
//            SpannableString styledText = new SpannableString(shop);
//            styledText.setSpan(new TextAppearanceSpan(context, R.style.style_one), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            styledText.setSpan(new TextAppearanceSpan(context, R.style.style_two), 2, shop.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            viewHolder.tvHomeItemName.setText(styledText, TextView.BufferType.SPANNABLE);
//        } else {
        viewHolder.tvHomeItemName.setText(cartState.getUser().getMenu().get(position).getTitle());
//        }
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
