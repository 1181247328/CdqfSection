package com.cdqf.cart_adapter;

import android.content.Context;
import android.text.TextUtils;
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
import de.greenrobot.event.EventBus;

/**
 * 我的
 */
public class MyAdapter extends BaseAdapter {

    private String TAG = MyAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private EventBus eventBus = EventBus.getDefault();

    private Context context = null;

    private int[] image = {
            R.mipmap.my_name,
            R.mipmap.my_number,
//            R.mipmap.my_state,
            R.mipmap.my_position,
            R.mipmap.my_phone,
            R.mipmap.my_month,
    };

    private String[] name = {
            "姓名",
            "工号",
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
        switch (position) {
            //姓名
            case 0:
                viewHolder.tvMyItemName.setText(cartState.getMyUser().getName());
                break;
            //工号
            case 1:
                viewHolder.tvMyItemName.setText(cartState.getMyUser().getLogin_account());
                break;
            //职位
            case 2:
                //审核通过
                if (TextUtils.equals(cartState.getMyUser().getType(), "1")) {
                    //员工
                    viewHolder.tvMyItemName.setText("店员(" + cartState.getMyUser().getPosition_id() + ")");
                } else if (TextUtils.equals(cartState.getMyUser().getType(), "2")) {
                    //店长
                    viewHolder.tvMyItemName.setText("店长(" + cartState.getMyUser().getPosition_id() + ")");
                } else {
                    //TODO
                }
                break;
            //紧急电话
            case 3:
                viewHolder.tvMyItemName.setText(cartState.getMyUser().getPhone());
                break;
            //服务总数
            case 4:
                viewHolder.tvMyItemName.setText(cartState.getMyUser().getCount() + "");
                break;
        }
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
