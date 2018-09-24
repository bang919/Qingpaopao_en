package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.MyFollowListRsp;
import com.wopin.qingpaopao.utils.GlideUtils;

import java.util.ArrayList;

public class MyFollowListAdapter extends RecyclerView.Adapter<MyFollowListAdapter.MyFollowViewHolder> {

    private ArrayList<MyFollowListRsp.MyFollowBean> mMyFollowBeanArrayList;

    public void setMyFollows(ArrayList<MyFollowListRsp.MyFollowBean> myFollows) {
        mMyFollowBeanArrayList = myFollows;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyFollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFollowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_my_follow_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyFollowViewHolder holder, int position) {
        MyFollowListRsp.MyFollowBean myFollowBean = mMyFollowBeanArrayList.get(position);
        if (myFollowBean.getIcon() != null) {
            GlideUtils.loadImage(holder.mImageView, -1, myFollowBean.getIcon(), new CenterCrop(), new CircleCrop());
        }
        holder.mNameTv.setText(myFollowBean.getUserName());
    }

    @Override
    public int getItemCount() {
        return mMyFollowBeanArrayList == null ? 0 : mMyFollowBeanArrayList.size();
    }

    class MyFollowViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mNameTv;

        public MyFollowViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.author_icon);
            mNameTv = itemView.findViewById(R.id.author_name);
        }
    }
}
