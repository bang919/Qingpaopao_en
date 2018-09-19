package com.wopin.qingpaopao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public abstract class ScoreAndCrowdOrderListAdapter extends RecyclerView.Adapter<ScoreAndCrowdOrderListAdapter.ScoreAndCrowdOrderViewHolder> {

    private ArrayList<OrderResponse.OrderBean> mOrderBeans;

    public void setOrderBeans(ArrayList<OrderResponse.OrderBean> orderBeans) {
        mOrderBeans = orderBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreAndCrowdOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScoreAndCrowdOrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_and_crowd_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreAndCrowdOrderViewHolder holder, int position) {
        OrderResponse.OrderBean orderBean = mOrderBeans.get(position);
        Context context = holder.itemView.getContext();
        holder.mOrderId.setText(String.format(context.getString(R.string.order_id), orderBean.get_id()));
        holder.mOrderStatus.setText(orderBean.getOrderStatus());
        GlideUtils.loadImage(holder.mOrderImage, -1, orderBean.getImage(), new CenterCrop());
        holder.mOrderTitle.setText(orderBean.getTitle());
        holder.mOrderScore.setText(String.format(context.getString(getPriceFormat()), orderBean.getSinglePrice()));
        holder.mOrderNumber.setText(String.format(context.getString(R.string.number_x), orderBean.getNum()));
        holder.mOrderTime.setText(orderBean.getCreateDate());
    }

    public abstract int getPriceFormat();

    @Override
    public int getItemCount() {
        return mOrderBeans == null ? 0 : mOrderBeans.size();
    }

    class ScoreAndCrowdOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView mOrderId;
        private TextView mOrderStatus;
        private ImageView mOrderImage;
        private TextView mOrderTitle;
        private TextView mOrderScore;
        private TextView mOrderNumber;
        private TextView mOrderTime;
        private Button mHadReceiveBtn;

        public ScoreAndCrowdOrderViewHolder(View itemView) {
            super(itemView);
            mOrderId = itemView.findViewById(R.id.tv_order_id);
            mOrderStatus = itemView.findViewById(R.id.tv_order_status);
            mOrderImage = itemView.findViewById(R.id.iv_order_image);
            mOrderTitle = itemView.findViewById(R.id.tv_order_title);
            mOrderScore = itemView.findViewById(R.id.tv_order_score);
            mOrderNumber = itemView.findViewById(R.id.tv_order_number);
            mOrderTime = itemView.findViewById(R.id.tv_order_time);
            mHadReceiveBtn = itemView.findViewById(R.id.btn_had_receive);
        }
    }
}
