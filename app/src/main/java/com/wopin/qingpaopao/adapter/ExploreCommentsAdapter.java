package com.wopin.qingpaopao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.CommentRsp;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ExploreCommentsAdapter extends RecyclerView.Adapter<ExploreCommentsAdapter.CommentViewHolder> {

    private CommentRsp mCommentRsp;
    private SimpleDateFormat mFormat;
    private ExploreCommentsAdapterCallback mExploreCommentsAdapterCallback;

    public ExploreCommentsAdapter(ExploreCommentsAdapterCallback exploreCommentsAdapterCallback) {
        mExploreCommentsAdapterCallback = exploreCommentsAdapterCallback;
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }


    public void setCommentRsp(CommentRsp commentRsp) {
        mCommentRsp = commentRsp;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_explore_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        final CommentRsp.CommentBean commentBean = mCommentRsp.getComments().get(position);
        GlideUtils.loadImage(holder.mIconView, -1, commentBean.getAvatar_URL(), new CenterCrop(), new CircleCrop());
        holder.mNameTv.setText(commentBean.getAuthor_name());
        holder.mTimeTv.setText(TimeFormatUtils.formatToTime(commentBean.getDate(), mFormat));
        holder.mLikeBtn.setSelected(commentBean.isMyLike());
        holder.mCommentContentTv.setText(Html.fromHtml(commentBean.getContent()));

        holder.mFollowCommentLayout.removeAllViews();
        ArrayList<CommentRsp.CommentBean> followComments = commentBean.getFollowComment();
        if (followComments != null) {
            holder.mFollowCommentLayout.setVisibility(View.VISIBLE);
            for (CommentRsp.CommentBean followComment : followComments) {
                Context context = holder.itemView.getContext();
                TextView textView = new TextView(context);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                textView.setText(Html.fromHtml(context.getString(R.string.follow_comment_content, followComment.getAuthor_name(),
                        Html.fromHtml(followComment.getContent()).toString())));
                textView.setPadding(15, 5, 5, 15);
                holder.mFollowCommentLayout.addView(textView);
            }
        } else {
            holder.mFollowCommentLayout.setVisibility(View.GONE);
        }

        holder.mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExploreCommentsAdapterCallback.onReplyOtherBtnClick(commentBean);
            }
        });
        holder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExploreCommentsAdapterCallback.onCommentLikeBtnClick(holder.mLikeBtn, commentBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentRsp != null && mCommentRsp.getComments() != null ? mCommentRsp.getComments().size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView mIconView;
        TextView mNameTv;
        TextView mTimeTv;
        View mLikeBtn;
        View mCommentBtn;
        TextView mCommentContentTv;
        LinearLayout mFollowCommentLayout;

        public CommentViewHolder(View itemView) {
            super(itemView);
            mIconView = itemView.findViewById(R.id.author_icon);
            mNameTv = itemView.findViewById(R.id.author_name);
            mTimeTv = itemView.findViewById(R.id.tv_comment_time);
            mLikeBtn = itemView.findViewById(R.id.btn_like);
            mCommentBtn = itemView.findViewById(R.id.btn_comment);
            mCommentContentTv = itemView.findViewById(R.id.tv_comment_content);
            mFollowCommentLayout = itemView.findViewById(R.id.follow_comment_layout);
        }
    }

    public interface ExploreCommentsAdapterCallback {
        void onCommentLikeBtnClick(View likeBtn, CommentRsp.CommentBean commentBean);

        void onReplyOtherBtnClick(CommentRsp.CommentBean commentBean);
    }
}
