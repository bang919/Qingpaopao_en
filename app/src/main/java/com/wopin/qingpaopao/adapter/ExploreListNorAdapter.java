package com.wopin.qingpaopao.adapter;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExploreListNorAdapter extends BaseQuickAdapter<ExploreListRsp.PostsBean, BaseViewHolder> {

    private SimpleDateFormat mFormat;

    public ExploreListNorAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    @Override
    protected void convert(BaseViewHolder helper, ExploreListRsp.PostsBean postsBean) {
        GlideUtils.loadImage((ImageView) helper.getView(R.id.iv_explore_item), -1, postsBean.getFeatured_image(), new CenterCrop(), new RoundedCorners(10));
        GlideUtils.loadImage((ImageView) helper.getView(R.id.author_icon), -1, postsBean.getAuthor().getAvatar_URL(), new CenterCrop(), new CircleCrop());
        helper.setText(R.id.title_explore_item, postsBean.getTitle())
                .setText(R.id.time_explore_item, TimeFormatUtils.formatToTime(postsBean.getDate(), mFormat))
                .setText(R.id.tv_comment, String.valueOf(postsBean.getComments()))
                .setText(R.id.tv_like, String.valueOf(postsBean.getLikes()))
                .setText(R.id.tv_stars, String.valueOf(postsBean.getStars()))
                .setText(R.id.author_name, postsBean.getAuthor().getName());
    }
}
