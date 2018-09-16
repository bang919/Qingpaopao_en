package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.utils.GlideUtils;

import java.util.ArrayList;

public class ScoreMarketContentDetailAdapter extends RecyclerView.Adapter<ScoreMarketContentDetailAdapter.WelfareViewHolder> {

    private ArrayList<ProductContent.ImagesBean> mImages;

    public void setScoreMarketContentDetails(ArrayList<ProductContent.ImagesBean> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WelfareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        return new WelfareViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull WelfareViewHolder holder, int position) {
        ProductContent.ImagesBean imagesBean = mImages.get(position);
        GlideUtils.loadImage(((ImageView) holder.itemView), -1, imagesBean.getSrc(), new CenterCrop());
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    class WelfareViewHolder extends RecyclerView.ViewHolder {

        public WelfareViewHolder(View itemView) {
            super(itemView);
        }
    }
}
