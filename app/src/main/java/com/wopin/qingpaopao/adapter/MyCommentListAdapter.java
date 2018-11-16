package com.wopin.qingpaopao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class MyCommentListAdapter extends RecyclerView.Adapter<MyCommentListAdapter.MyCommentViewHolder> {

    private SimpleDateFormat mFormat;
    private List<MyCommentsRsp.ResultBean.CommentsBean> mComments;
    private TreeMap<Integer, List<MyCommentsRsp.ResultBean.CommentsReplyMeBean>> replyMeCommentMap;
    private TreeMap<Integer, MyCommentsRsp.ResultBean.RelatedPostsBean> postMap;
    private MyCommentListCallback mMyCommentListCallback;

    public MyCommentListAdapter(MyCommentListCallback myCommentListCallback) {
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        mMyCommentListCallback = myCommentListCallback;
    }

    public void setData(MyCommentsRsp myCommentsRsp) {
        MyCommentsRsp.ResultBean result = myCommentsRsp.getResult();
        if (result != null) {
            mComments = result.getComments();

            //分类
            List<MyCommentsRsp.ResultBean.CommentsReplyMeBean> commentsReplyMe = result.getCommentsReplyMe();
            replyMeCommentMap = new TreeMap<>();
            for (MyCommentsRsp.ResultBean.CommentsReplyMeBean commentsReplyMeBean : commentsReplyMe) {
                List<MyCommentsRsp.ResultBean.CommentsReplyMeBean> commentsReplyMeBeans = replyMeCommentMap.get(commentsReplyMeBean.getParent());
                if (commentsReplyMeBeans == null) {
                    commentsReplyMeBeans = new ArrayList<>();
                }
                commentsReplyMeBeans.add(commentsReplyMeBean);
                replyMeCommentMap.put(commentsReplyMeBean.getParent(), commentsReplyMeBeans);
            }

            List<MyCommentsRsp.ResultBean.RelatedPostsBean> relatedPosts = result.getRelatedPosts();
            postMap = new TreeMap<>();
            for (MyCommentsRsp.ResultBean.RelatedPostsBean relatedPostsBean : relatedPosts) {
                postMap.put(relatedPostsBean.getId(), relatedPostsBean);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_my_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentViewHolder holder, int position) {
        //find datas
        Context context = holder.itemView.getContext();
        final MyCommentsRsp.ResultBean.CommentsBean commentBean = mComments.get(position);
        List<MyCommentsRsp.ResultBean.CommentsReplyMeBean> replyMeBeans = replyMeCommentMap.get(commentBean.getId());
        final MyCommentsRsp.ResultBean.RelatedPostsBean post = postMap.get(commentBean.getPost());
        //set datas
        GlideUtils.loadImage(holder.mIconView, -1, commentBean.getAvatar_URL(), new CenterCrop(), new CircleCrop());
        holder.mNameTv.setText(commentBean.getAuthor_name());
        holder.mTimeTv.setText(TimeFormatUtils.formatToTime(commentBean.getDate(), mFormat));
        holder.mContentTv.setText(Html.fromHtml(commentBean.getContent()));
        GlideUtils.loadImage(holder.mPostImage, -1, post.getFeatured_image(), new CenterCrop());
        holder.mPostContentTv.setText(Html.fromHtml(post.getContent()));
        holder.mReadCountTv.setText(String.valueOf(post.getRead()));
        holder.mCommentCountTv.setText(String.valueOf(post.getComments()));

        holder.mFollowCommentLayout.removeAllViews();
        if (replyMeBeans != null && replyMeBeans.size() > 0) {
            holder.mFollowCommentLayout.setVisibility(View.VISIBLE);
            for (MyCommentsRsp.ResultBean.CommentsReplyMeBean followComment : replyMeBeans) {
                TextView textView = new TextView(context);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                textView.setText(Html.fromHtml(context.getString(R.string.follow_comment_content, followComment.getAuthor_name(),
                        Html.fromHtml(followComment.getContent()).toString())));
                textView.setPadding(15, 5, 5, 5);
                holder.mFollowCommentLayout.addView(textView);
            }
        } else {
            holder.mFollowCommentLayout.setVisibility(View.GONE);
        }

        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyCommentListCallback.onDeleteCommentClick(commentBean);
            }
        });
        holder.mAddCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyCommentListCallback.onAddCommentClick(post.getId(), commentBean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments != null ? mComments.size() : 0;
    }

    class MyCommentViewHolder extends RecyclerView.ViewHolder {

        ImageView mIconView;
        TextView mNameTv;
        TextView mTimeTv;
        TextView mContentTv;
        ImageView mPostImage;
        TextView mPostContentTv;
        TextView mReadCountTv;
        TextView mCommentCountTv;
        LinearLayout mFollowCommentLayout;
        Button mDeleteBtn;
        Button mAddCommentBtn;

        public MyCommentViewHolder(View itemView) {
            super(itemView);
            mIconView = itemView.findViewById(R.id.author_icon);
            mNameTv = itemView.findViewById(R.id.author_name);
            mTimeTv = itemView.findViewById(R.id.tv_comment_time);
            mContentTv = itemView.findViewById(R.id.tv_comment_content);
            mPostImage = itemView.findViewById(R.id.post_image);
            mPostContentTv = itemView.findViewById(R.id.post_content);
            mReadCountTv = itemView.findViewById(R.id.read_value);
            mCommentCountTv = itemView.findViewById(R.id.comment_value);
            mDeleteBtn = itemView.findViewById(R.id.delete_btn);
            mAddCommentBtn = itemView.findViewById(R.id.additional_comment);
            mFollowCommentLayout = itemView.findViewById(R.id.follow_comment_layout);
        }
    }

    public interface MyCommentListCallback {
        void onAddCommentClick(int postId, int commentId);

        void onDeleteCommentClick(MyCommentsRsp.ResultBean.CommentsBean comment);
    }
}
