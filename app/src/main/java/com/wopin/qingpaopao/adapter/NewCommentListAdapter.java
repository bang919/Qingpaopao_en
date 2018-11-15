package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NewCommentListAdapter extends RecyclerView.Adapter<NewCommentListAdapter.NewCommentViewHolder> {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newComments;
    private NewCommentClickListener mNewCommentClickListener;

    public NewCommentListAdapter(NewCommentClickListener newCommentClickListener) {
        mNewCommentClickListener = newCommentClickListener;
    }

    public void setNewComments(ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newComments) {
        this.newComments = newComments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewCommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_new_comment_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewCommentViewHolder holder, int position) {
        final NewCommentRsp.ResultBean.NewCommentBean newComment = newComments.get(position);

        GlideUtils.loadImage(holder.mImageView, -1, newComment.getPostThumbnail(), new CenterCrop());
        GlideUtils.loadImage(holder.mHead, -1, newComment.getAvatar_URL(), new CenterCrop(), new CircleCrop());
        holder.mName.setText(newComment.getAuthor_name());
        holder.mContent.setText(Html.fromHtml(newComment.getContent()));
        holder.mTime.setText(TimeFormatUtils.formatToTime(newComment.getDate(), format));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewCommentClickListener.onNewCommentClick(newComment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newComments != null ? newComments.size() : 0;
    }

    class NewCommentViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        ImageView mHead;
        TextView mName;
        TextView mContent;
        TextView mTime;

        public NewCommentViewHolder(View itemView) {
            super(itemView);
            mHead = itemView.findViewById(R.id.new_comment_head);
            mImageView = itemView.findViewById(R.id.new_comment_iv);
            mName = itemView.findViewById(R.id.new_comment_name);
            mContent = itemView.findViewById(R.id.new_comment_content);
            mTime = itemView.findViewById(R.id.new_comment_time);
        }
    }

    public interface NewCommentClickListener {
        void onNewCommentClick(NewCommentRsp.ResultBean.NewCommentBean newComment);
    }
}
