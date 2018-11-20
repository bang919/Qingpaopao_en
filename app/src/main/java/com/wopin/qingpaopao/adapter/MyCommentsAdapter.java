package com.wopin.qingpaopao.adapter;

import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyCommentsAdapter extends BaseQuickAdapter<MyCommentsRsp.ResultBean.CommentsBean, BaseViewHolder> {

    private SimpleDateFormat mFormat;

    public MyCommentsAdapter(int layoutResId) {
        super(layoutResId);
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCommentsRsp.ResultBean.CommentsBean commentBean) {
        List<MyCommentsRsp.ResultBean.CommentsReplyMeBean> replyMeBeans = commentBean.getCommentReplys();
        MyCommentsRsp.ResultBean.RelatedPostsBean post = commentBean.getRelatedPostsBean();

        GlideUtils.loadImage((ImageView) helper.getView(R.id.author_icon), -1, commentBean.getAvatar_URL(), new CenterCrop(), new CircleCrop());
        GlideUtils.loadImage((ImageView) helper.getView(R.id.post_image), -1, post.getFeatured_image(), new CenterCrop());
        helper.setText(R.id.author_name, commentBean.getAuthor_name())
                .setText(R.id.tv_comment_time, TimeFormatUtils.formatToTime(commentBean.getDate(), mFormat))
                .setText(R.id.tv_comment_content, Html.fromHtml(commentBean.getContent()))
                .setText(R.id.post_content, Html.fromHtml(post.getContent()))
                .setText(R.id.read_value, String.valueOf(post.getRead()))
                .setText(R.id.comment_value, String.valueOf(post.getComments()))
                .addOnClickListener(R.id.delete_btn)
                .addOnClickListener(R.id.additional_comment);

        LinearLayout followCommentLayout = helper.getView(R.id.follow_comment_layout);
        followCommentLayout.removeAllViews();
        if (replyMeBeans != null && replyMeBeans.size() > 0) {
            followCommentLayout.setVisibility(View.VISIBLE);
            for (MyCommentsRsp.ResultBean.CommentsReplyMeBean followComment : replyMeBeans) {
                TextView textView = new TextView(followCommentLayout.getContext());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                textView.setText(Html.fromHtml(followCommentLayout.getContext().getString(R.string.follow_comment_content, followComment.getAuthor_name(),
                        Html.fromHtml(followComment.getContent()).toString())));
                textView.setPadding(15, 5, 5, 5);
                followCommentLayout.addView(textView);
            }
        } else {
            followCommentLayout.setVisibility(View.GONE);
        }
    }
}
