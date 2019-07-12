package com.cdqf.cart_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cdqf.cart.R;
import com.cdqf.cart_find.FillAddImageFind;
import com.cdqf.cart_state.CartState;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 报销凭证
 */
public class FillImageAdapter extends BaseAdapter {

    private String TAG = FillImageAdapter.class.getSimpleName();

    private CartState cartState = CartState.getCartState();

    private EventBus eventBus = EventBus.getDefault();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Context context = null;

    public FillImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return cartState.getImagePathsList().size() + 1;
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
        ViewHolder viewHolder = null;
        if (type == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_fill_image, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            imageLoader.displayImage("file://" + cartState.getImagePathsList().get(position),
                    viewHolder.ivFillItemImage,
                    cartState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
            viewHolder.rlFillItemReduction.setOnClickListener(new OnReductionListener(position));
        } else if (type == 1) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fill_add, null);
            RelativeLayout rlFillItemadd = convertView.findViewById(R.id.rl_fill_item_add);
            rlFillItemadd.setOnClickListener(new OnAddListener(position));
        }
        return convertView;
    }

    class ViewHolder {

        //图片显示
        @BindView(R.id.iv_fill_item_image)
        public ImageView ivFillItemImage = null;

        //去除
        @BindView(R.id.rl_fill_item_reduction)
        public RelativeLayout rlFillItemReduction = null;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 添加
     */
    class OnAddListener implements View.OnClickListener {

        private int position;

        public OnAddListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            eventBus.post(new FillAddImageFind());
        }
    }

    /**
     * 减
     */
    class OnReductionListener implements View.OnClickListener {
        private int position;

        public OnReductionListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            cartState.getImagePathsList().remove(position);
            notifyDataSetChanged();
        }
    }
}