package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.utils.GlideUtils;

import java.util.ArrayList;

public class ScoreMarketContentAdapter extends RecyclerView.Adapter<ScoreMarketContentAdapter.ScoreMarketContentViewHolder> {

    private ArrayList<ProductContent> mProductContents;

    public void setProductContents(ArrayList<ProductContent> productContents) {
        mProductContents = productContents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreMarketContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScoreMarketContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_welfare_scoremarket_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreMarketContentViewHolder holder, int position) {
        ProductContent productContent = mProductContents.get(position);
        String description = productContent.getDescription();
        int indexStart = description.indexOf("http");
        int indexEnd = description.indexOf(".jpg");
        if (indexStart != -1 && indexEnd != -1) {
            String image = description.substring(indexStart, indexEnd + 4);
            GlideUtils.loadImage(holder.mImageView, -1, image, new CenterCrop());
        }
        holder.mTitle.setText(productContent.getName());
        holder.mScoreTv.setText(String.format(holder.mScoreTv.getContext().getString(R.string.score_change), productContent.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mProductContents == null ? 0 : mProductContents.size();
    }

    class ScoreMarketContentViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTitle;
        private TextView mScoreTv;

        public ScoreMarketContentViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item_scoremarket_content);
            mTitle = itemView.findViewById(R.id.tv_item_scoremarket_content_title);
            mScoreTv = itemView.findViewById(R.id.tv_item_scoremarket_content_content);
        }
    }
}
