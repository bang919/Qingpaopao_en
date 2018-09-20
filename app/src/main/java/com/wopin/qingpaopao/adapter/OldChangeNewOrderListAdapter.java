package com.wopin.qingpaopao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.OrderResponse;
import com.wopin.qingpaopao.utils.GlideUtils;

import java.util.ArrayList;

public class OldChangeNewOrderListAdapter extends RecyclerView.Adapter<OldChangeNewOrderListAdapter.OldChangeNewOrderViewHolder> {


    private ArrayList<OrderResponse.OrderBean> mOrderBeans;
    private OldChangeNewOrderListAdapterCalblack mOldChangeNewOrderListAdapterCalblack;

    public void setOldChangeNewOrderListAdapterCalblack(OldChangeNewOrderListAdapterCalblack oldChangeNewOrderListAdapterCalblack) {
        mOldChangeNewOrderListAdapterCalblack = oldChangeNewOrderListAdapterCalblack;
    }

    public void setOrderBeans(ArrayList<OrderResponse.OrderBean> orderBeans) {
        mOrderBeans = orderBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OldChangeNewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OldChangeNewOrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_old_change_new_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OldChangeNewOrderViewHolder holder, int position) {
        final OrderResponse.OrderBean orderBean = mOrderBeans.get(position);
        Context context = holder.itemView.getContext();
        holder.mOrderTime.setText(String.format(context.getString(R.string.order_time), orderBean.getCreateDate()));
        holder.mOrderStatus.setText(orderBean.getOrderStatus());
        GlideUtils.loadImage(holder.mOrderImage, -1, orderBean.getImage(), new CenterCrop());
        holder.mOrderTitle.setText(orderBean.getTitle());
        holder.mRecyclePrice.setText(Html.fromHtml(context.getString(R.string.recycle_price, orderBean.getOfferPrice())));
        int needPay = Integer.valueOf(orderBean.getSinglePrice()) - Integer.valueOf(orderBean.getOfferPrice());
        holder.mNeedPayPrice.setText(Html.fromHtml(context.getString(R.string.need_pay_price, String.valueOf(needPay))));
        holder.mOrderNumber.setText(String.format(context.getString(R.string.number_x), orderBean.getNum()));
        holder.mInputOrderMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOldChangeNewOrderListAdapterCalblack != null) {
                    mOldChangeNewOrderListAdapterCalblack.onSetTrackingNumberBtnClick(orderBean);
                }
            }
        });
        holder.mCheckOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOldChangeNewOrderListAdapterCalblack != null) {
                    mOldChangeNewOrderListAdapterCalblack.onOrderDetailBtnClick(orderBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderBeans == null ? 0 : mOrderBeans.size();
    }

    class OldChangeNewOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView mOrderTime;
        private TextView mOrderStatus;
        private ImageView mOrderImage;
        private TextView mOrderTitle;
        private TextView mRecyclePrice;
        private TextView mNeedPayPrice;
        private TextView mOrderNumber;
        private Button mCheckOrderBtn;
        private Button mInputOrderMessageBtn;

        public OldChangeNewOrderViewHolder(View itemView) {
            super(itemView);
            mOrderTime = itemView.findViewById(R.id.tv_order_time);
            mOrderStatus = itemView.findViewById(R.id.tv_order_status);
            mOrderImage = itemView.findViewById(R.id.iv_order_image);
            mOrderTitle = itemView.findViewById(R.id.tv_order_title);
            mRecyclePrice = itemView.findViewById(R.id.recycle_price);
            mNeedPayPrice = itemView.findViewById(R.id.need_pay_price);
            mOrderNumber = itemView.findViewById(R.id.tv_order_number);
            mCheckOrderBtn = itemView.findViewById(R.id.btn_check_order);
            mInputOrderMessageBtn = itemView.findViewById(R.id.btn_input_order_message);
        }
    }

    public interface OldChangeNewOrderListAdapterCalblack {
        void onSetTrackingNumberBtnClick(OrderResponse.OrderBean orderBean);

        void onOrderDetailBtnClick(OrderResponse.OrderBean orderBean);
    }
}
