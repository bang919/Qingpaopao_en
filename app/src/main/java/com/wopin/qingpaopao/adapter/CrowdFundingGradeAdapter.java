package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.ProductContent;

import java.util.List;

public class CrowdFundingGradeAdapter extends RecyclerView.Adapter<CrowdFundingGradeAdapter.CrowdFundingGradeViewHolder> {

    private List<ProductContent.AttributeBean> mAttributes;
    private int selectItem = 0;
    private CrowdFundingGradeAdapterCallback mCrowdFundingGradeAdapterCallback;

    public CrowdFundingGradeAdapter(CrowdFundingGradeAdapterCallback crowdFundingGradeAdapterCallback) {
        mCrowdFundingGradeAdapterCallback = crowdFundingGradeAdapterCallback;
    }

    public void setAttributes(List<ProductContent.AttributeBean> attributes) {
        mAttributes = attributes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CrowdFundingGradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrowdFundingGradeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_crowd_funding_grade, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrowdFundingGradeViewHolder holder, final int position) {
        final ProductContent.AttributeBean attributeBean = mAttributes.get(position);
        holder.mTextView.setText(String.format(holder.itemView.getContext().getString(R.string.price_number), attributeBean.getName()));
        holder.itemView.setSelected(selectItem == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrowdFundingGradeAdapterCallback.onCrowdFundingGradeItemClick(attributeBean, position);
                selectItem = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAttributes == null ? 0 : mAttributes.size();
    }

    class CrowdFundingGradeViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public CrowdFundingGradeViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_item_crowd_funding_grade);
        }
    }

    public interface CrowdFundingGradeAdapterCallback {
        void onCrowdFundingGradeItemClick(ProductContent.AttributeBean attributeBean, int position);
    }
}
