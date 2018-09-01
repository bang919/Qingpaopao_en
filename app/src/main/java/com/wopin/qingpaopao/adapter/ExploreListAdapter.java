package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ExploreListAdapter extends RecyclerView.Adapter<ExploreListAdapter.ExploreHolder> {

    private ArrayList<ExploreListRsp.PostsBean> mPosts;
    private ExploreListItemClick mExploreListItemClick;

    public ExploreListAdapter(ExploreListItemClick exploreListItemClick) {
        mExploreListItemClick = exploreListItemClick;
    }

    public void setDatas(ArrayList<ExploreListRsp.PostsBean> posts) {
        mPosts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExploreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExploreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreHolder holder, final int position) {
        final ExploreListRsp.PostsBean postsBean = mPosts.get(position);
        GlideUtils.loadImage(holder.mImageView, -1, postsBean.getFeatured_image(), new CenterCrop(), new RoundedCorners(10));
        holder.mTitleTv.setText(postsBean.getTitle());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        holder.mTimeTv.setText(TimeFormatUtils.formatToTime(postsBean.getDate(), format));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExploreListItemClick.onExploreItemClick(postsBean, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts == null ? 0 : mPosts.size();
    }

    class ExploreHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTitleTv;
        private TextView mTimeTv;

        public ExploreHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_explore_item);
            mTitleTv = itemView.findViewById(R.id.title_explore_item);
            mTimeTv = itemView.findViewById(R.id.time_explore_item);
        }
    }

    public interface ExploreListItemClick {
        void onExploreItemClick(ExploreListRsp.PostsBean postsBean, int position);
    }
}
